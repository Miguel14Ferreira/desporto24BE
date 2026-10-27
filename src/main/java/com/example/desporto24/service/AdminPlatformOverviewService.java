package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.moderation.UserReportRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.net.URI;
import java.net.URISyntaxException;
import com.example.desporto24.model.venue.Venue;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Comparator;
import org.springframework.data.domain.PageRequest;

/** Aggregated, database-backed operational counters. No personal data or full-table entity reads. */
@Service
@RequiredArgsConstructor
public class AdminPlatformOverviewService {
    private final ProfileRepository profiles;
    private final SessionRepository sessions;
    private final CommunityRepository communities;
    private final CommunityTournamentRepository tournaments;
    private final VenueRepository venues;
    private final UserReportRepository reports;
    private final OnlinePresenceService onlinePresence;

    @Transactional(readOnly = true)
    public PlatformOverview snapshot() {
        Instant now = Instant.now();
        Date current = Date.from(now);
        Date inSevenDays = Date.from(now.plus(7, ChronoUnit.DAYS));
        return new PlatformOverview(
                profiles.countRegisteredProfiles(),
                profiles.countEnabledRegisteredProfiles(),
                profiles.countRegistrationsSince(Date.from(now.minus(7, ChronoUnit.DAYS))),
                profiles.countRecentLoginsSince(Date.from(now.minus(15, ChronoUnit.MINUTES))),
                onlinePresence.countOnlineAt(now),
                sessions.count(),
                sessions.countUpcomingSessions(current),
                sessions.countUpcomingSessionsBetween(current, inSevenDays),
                sessions.countByStatus(SessionStatus.COMPLETED),
                sessions.countByStatus(SessionStatus.CANCELLED),
                communities.count(),
                tournaments.count(),
                tournaments.countByStatus(TournamentStatus.IN_PROGRESS),
                venues.count(),
                reports.countByStatus(UserReportStatus.OPEN),
                now
        );
    }

    /** Missing postal address is not the same as missing usable location (GPS may exist). */
    @Transactional(readOnly = true)
    public VenueQualityCoverage venueQuality() {
        List<VenueDistrictCoverage> districts = venues.qualityByDistrict().stream()
                .map(row -> new VenueDistrictCoverage(
                        row[0] == null || row[0].toString().isBlank()
                                ? "Sem distrito" : row[0].toString(),
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).longValue(),
                        ((Number) row[3]).longValue(),
                        ((Number) row[4]).longValue(),
                        ((Number) row[5]).longValue()))
                .sorted(Comparator.comparingLong(VenueDistrictCoverage::withoutSport).reversed()
                        .thenComparing(VenueDistrictCoverage::district))
                .toList();
        return new VenueQualityCoverage(
                districts.stream().mapToLong(VenueDistrictCoverage::venues).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutSport).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutCoordinates).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutAddress).sum(),
                districts.stream().mapToLong(VenueDistrictCoverage::withoutAddressAndCoordinates).sum(),
                districts, Instant.now());
    }

    /** Existing paginated review; no changes for callers that omit the focus flag. */
    @Transactional(readOnly = true)
    public VenueGpsReviewPage missingGpsReview(String district, int page) {
        return missingGpsReview(district, page, false);
    }

    /**
     * Focus mode reads only explicitly researched venue IDs, never a national
     * catalogue scan. Re-check saved identity and current GPS status every time:
     * stale candidates are excluded, not treated as valid evidence.
     *
     * The bounded fixed set is intentionally code-reviewed before expansion.
     */
    @Transactional(readOnly = true)
    public VenueGpsReviewPage missingGpsReview(String district, int page, boolean onlyWithEvidence) {
        if (page < 0 || page > 1000) throw new IllegalArgumentException("Página de revisão GPS inválida");
        String filter = district == null ? "" : district.trim();
        if (!filter.isEmpty() && (filter.length() > 120 || filter.equals("Sem distrito"))) {
            throw new IllegalArgumentException("Filtro de distrito inválido");
        }
        if (onlyWithEvidence) {
            var ids = java.util.stream.Stream.of(
                    GPS_RESEARCH_LEADS.keySet().stream(),
                    GPS_FACILITY_CONTEXT.keySet().stream(),
                    java.util.stream.Stream.of(280L, 284L))
                    .flatMap(java.util.function.Function.identity())
                    .distinct().sorted().toList();
            var items = new java.util.ArrayList<VenueGpsReviewItem>();
            for (Venue venue : venues.findAllById(ids)) {
                if (venue.getLatitude() != null && venue.getLongitude() != null) continue;
                if (!filter.isEmpty() && !filter.equals(venue.getDistrict())) continue;
                var item = toGpsReviewItem(venue);
                if (item.researchCandidate() != null || item.addressReview() != null
                        || item.possibleDuplicateReview() != null
                        || item.facilityContextReview() != null) {
                    items.add(item);
                }
            }
            items.sort(java.util.Comparator.comparing(VenueGpsReviewItem::id));
            int start = page * 20;
            if (start >= items.size()) return new VenueGpsReviewPage(List.of(), page, false);
            int end = Math.min(start + 20, items.size());
            return new VenueGpsReviewPage(List.copyOf(items.subList(start, end)),
                    page, end < items.size());
        }
        var slice = venues.missingGpsReview(filter, PageRequest.of(page, 20));
        return new VenueGpsReviewPage(slice.getContent().stream().map(this::toGpsReviewItem)
                .toList(), page, slice.hasNext());
    }

    private VenueGpsReviewItem toGpsReviewItem(Venue venue) {
        return new VenueGpsReviewItem(
                venue.getId(), venue.getName(), venue.getCity(), venue.getDistrict(),
                venue.getAddress(), venue.getSourceReference(),
                venue.getInformationSource() == null ? null : venue.getInformationSource().name(),
                venue.getInformationSource() == Venue.InformationSource.OFFICIAL
                        ? safeOfficialSourceWebsite(venue.getWebsite()) : null,
                researchCandidate(venue), addressReview(venue), possibleDuplicateReview(venue),
                facilityContextReview(venue));
    }

    /** Source links are research clues only; never imply that the facility GPS was verified. */
    static String safeOfficialSourceWebsite(String raw) {
        if (raw == null || raw.isBlank() || raw.length() > 255) return null;
        String value = raw.trim();
        try {
            URI uri = new URI(value);
            String host = uri.getHost();
            if (!"https".equalsIgnoreCase(uri.getScheme()) || host == null
                    || !host.contains(".") || uri.getRawUserInfo() != null
                    || uri.getPort() != -1 || uri.getRawFragment() != null
                    || host.equalsIgnoreCase("localhost") || host.toLowerCase(java.util.Locale.ROOT).endsWith(".local")
                    || host.matches("[0-9.]+")) return null;
            return value;
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    /**
     * Curated and immutable, not an import pipeline. Each lead must be documented
     * independently against a specific saved OFFICIAL venue identity.
     *
     * 273 (Évora): explicitly inspected municipal Contacts & Location
     * "ver no mapa" link contains 38.573234,-7.928876 as site coordinates.
     * The underlying municipal link is HTTP Google Maps and is NOT exposed
     * as a trusted HTTPS source or evidence of entrance/portão GPS.
     * This is official site evidence only. The municipal official HTTPS
     * Contacts & Location page remains the audit reference.
     * Previously: municipal site map 38.573234,-7.928876, regional Turismo
     * do Alentejo 38.573425,-7.928949, ~22m apart.
     * 276 (Santarém): municipal tourism directions 39.2250178,-8.6886809
     * and a matching OSM building way 1172748137, both site-level only.
     * 283 (Porto): the named OSM stadium polygon 1298753441 is independently
     * documented against the municipal Campo Municipal do Outeiro and operator.
     * Its Mapcarta coordinates (41.17589,-8.59259) are rounded, site-level only;
     * Ágora and club describe different access streets. Never infer an entrance.
     * None of these proves a particular pedestrian entrance.
     */
    private static final java.util.Map<Long, CuratedSiteEvidence> GPS_RESEARCH_LEADS =
            java.util.Map.of(
                    273L, new CuratedSiteEvidence(
                            "Piscinas Municipais de Évora", "Évora", "Évora",
                            "Arantes de Oliveira",
                            "https://www.cm-evora.pt/visitante/lazer-visitar/piscinas-municipais/",
                            38.573234, -7.928876,
                            "https://www.cm-evora.pt/municipe/equipamentos/equipamentos-desportivos/"
                                    + "piscinas-municipais/contactos-e-localizacao-das-piscinas-municipais/",
                            "https://www.outdooractive.pt/pt/poi/evora/piscina-municipal/802361541/"),
                    276L, new CuratedSiteEvidence(
                            "Piscinas Municipais do Sacapeito", "Santarém", "Santarém",
                            "Cidade de Badajoz",
                            "https://viversantarem.pt/instalacoes-desportivas/",
                            39.2250178, -8.6886809,
                            "https://www.visitesantarem.pt/lista/piscinas-municipais-do-sacapeito/",
                            "https://www.openstreetmap.org/way/1172748137"),
                    281L, new CuratedSiteEvidence(
                            "Campo Municipal dos Choupos", "Porto", "Porto",
                            "Rua dos Plátanos",
                            "https://www.agoraporto.pt/noticias/desporto/desporto/rmid",
                            41.17345, -8.65816,
                            "https://www.agoraporto.pt/grandes-campos-municipais/polidesportivo-dos-choupos",
                            "https://www.openstreetmap.org/way/449975768"),
                    283L, new CuratedSiteEvidence(
                            "Campo Municipal do Outeiro", "Porto", "Porto",
                            "Rua D. Afonso Henriques, Paranhos",
                            "https://www.agoraporto.pt/noticias/desporto/desporto/rmid",
                            41.17589, -8.59259,
                            "https://www.agoraporto.pt/grandes-campos-municipais/campo-municipal-do-outeiro",
                            "https://mapcarta.com/pt/W1298753441"));

    /**
     * Previously documented facilities whose identities are supported by primary
     * municipal sources, but with NO point precise enough for a GPS proposal.
     * These are strictly contextual review notes and never candidate coordinates.
     */
    private static final java.util.Map<Long, CuratedFacilityContext> GPS_FACILITY_CONTEXT =
            java.util.Map.of(
                    285L, new CuratedFacilityContext(
                            "Polidesportivo Municipal do Paço", "Maia", "Porto",
                            "Rua do Paço, Pedrouços, 4425-678 Maia",
                            "https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/"
                                    + "instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco",
                            "DISTINCT_MUNICIPAL_SPORTS_VENUES_NO_POINT",
                            "https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/"
                                    + "instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco",
                            "https://www.cm-maia.pt/desporto/instalacoes-desportivas?pois_list_14_page=4"),
                    286L, new CuratedFacilityContext(
                            "Campo de Padel do Fojo", "Mira", "Coimbra",
                            "Fojo, Seixo de Mira, Mira",
                            "https://www.cm-mira.pt/node/4084",
                            "SHARED_COMPLEX_DIFFERENT_SPORTS_NO_POINT",
                            "https://www.cm-mira.pt/node/4084",
                            "https://www.cm-mira.pt/node/3308"),
                    288L, new CuratedFacilityContext(
                            "Campo de Futebol N.º1 do Complexo Desportivo da Rodovia",
                            "Braga", "Braga",
                            "Complexo Desportivo da Rodovia, 4710-294 Braga",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6223",
                            "SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6223",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6224"),
                    289L, new CuratedFacilityContext(
                            "Campo de Futebol N.º3 do Complexo Desportivo da Rodovia",
                            "Braga", "Braga",
                            "Complexo Desportivo da Rodovia, 4710-294 Braga",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6224",
                            "SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6224",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6223"),
                    290L, new CuratedFacilityContext(
                            "Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia", "Braga", "Braga",
                            "Complexo Desportivo da Rodovia, 4710-294 Braga",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6225",
                            "SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6225",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6226"),
                    291L, new CuratedFacilityContext(
                            "Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia", "Braga", "Braga",
                            "Complexo Desportivo da Rodovia, 4710-294 Braga",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6226",
                            "SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6226",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6225"),
                    292L, new CuratedFacilityContext(
                            "Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia", "Braga", "Braga",
                            "Complexo Desportivo da Rodovia, 4710-294 Braga",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6229",
                            "SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6229",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6230"),
                    293L, new CuratedFacilityContext(
                            "Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia", "Braga", "Braga",
                            "Complexo Desportivo da Rodovia, 4710-294 Braga",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6230",
                            "SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6230",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6229"),
                    294L, new CuratedFacilityContext(
                            "Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia", "Braga", "Braga",
                            "Complexo Desportivo da Rodovia, 4710-294 Braga",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6231",
                            "GROUPED_BASKETBALL_COURTS_NO_POINT",
                            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/"
                                    + "item/item-1-6231",
                            "https://www.cm-braga.pt/pt/1201/viver/ambiente/espacos-verdes/"
                                    + "item/item-1-722"));

    private record CuratedFacilityContext(
            String name, String city, String district, String address, String website,
            String issueCode, String officialSourceUrl, String relatedSourceUrl) {}

    /** No GPS point. Identifies separate facilities that must not share coordinates. */
    static VenueFacilityContextReview facilityContextReview(Venue venue) {
        if (venue == null || venue.getId() == null
                || venue.getLatitude() != null || venue.getLongitude() != null
                || venue.getInformationSource() != Venue.InformationSource.OFFICIAL) return null;
        var lead = GPS_FACILITY_CONTEXT.get(venue.getId());
        if (lead == null || !lead.name().equals(venue.getName())
                || !lead.city().equals(venue.getCity())
                || !lead.district().equals(venue.getDistrict())
                || !lead.address().equals(venue.getAddress())
                || !lead.website().equals(safeOfficialSourceWebsite(venue.getWebsite()))) {
            return null;
        }
        return new VenueFacilityContextReview(
                lead.issueCode(), lead.officialSourceUrl(), lead.relatedSourceUrl(),
                venue.getId() >= 288L && venue.getId() <= 294L
                        ? RODOVIA_CARTOGRAPHY_REVIEW : null,
                venue.getId() >= 288L && venue.getId() <= 294L
                        ? rodoviaNumberingPlanReview(venue.getId()) : null);
    }

    /**
     * Municipality's 2020 guide separates the adjacent Parque Desportivo and
     * Complexo Desportivo; the OSM objects are a park-area geometry and sports
     * building footprint, NOT numbered playing surfaces. The 2025
     * municipality report with coordinates 41.55296,-8.40345 is about
     * a poplar tree next to sporting facilities, NOT a sports field.
     * The municipal GeoPortal may support further investigation but its
     * public landing page does not prove individual numbered pitches.
     * Review ONLY; do not expose those tree coordinates as a candidate.
     */
    private static final RodoviaCartographyReview RODOVIA_CARTOGRAPHY_REVIEW =
            new RodoviaCartographyReview(
                    "ADJACENT_PARK_AND_SPORTS_BUILDING_NOT_NUMBERED_PITCH",
                    "https://juventude.cm-braga.pt/wp-content/uploads/2023/06/"
                            + "Roteiro-Desportivo-e-Lazer-2020.pdf",
                    "https://mapcarta.com/pt/W106454979",
                    "https://mapcarta.com/pt/W884706818",
                    "https://www.cm-braga.pt/archive/doc/Parque_Desportivo_da_Rodovia_0.pdf",
                    "https://sigportal.cm-braga.pt/WebSIGBraga/");

    public record RodoviaCartographyReview(
            String issueCode, String municipalGuideUrl, String parkAreaUrl,
            String sportsBuildingUrl, String nonPitchTreeReportUrl,
            String officialGeoportalUrl) {}

    /**
     * A visitor-supplied photograph shows a numbered on-site overview sign.
     * Neither its date nor a geospatial transformation is verified. The
     * regional federation's 2025/26 schedule independently identifies ONLY
     * field number 1 (105 x 68 m), not the geolocation of any surface.
     */
    private static RodoviaNumberingPlanReview rodoviaNumberingPlanReview(long venueId) {
        return new RodoviaNumberingPlanReview(
                "NUMBERED_SIGN_SCHEMATIC_WITHOUT_GEOREFERENCE",
                "https://fr.tripadvisor.ch/Attraction_Review-g189171-d19701352-Reviews-"
                        + "Parque_Desportivo_da_Rodovia-Braga_Braga_District_Northern_Portugal.html",
                venueId == 288L
                        ? "https://afbraga.fpf.pt/DesktopModules/InstitutionalDocuments/"
                                + "DownloadFiles.ashx?IdDoc=184789" : null);
    }

    public record RodoviaNumberingPlanReview(
            String issueCode, String signageGalleryUrl, String fieldOneScheduleUrl) {}

    public record VenueFacilityContextReview(
            String issueCode, String officialSourceUrl, String relatedSourceUrl,
            RodoviaCartographyReview cartographyReview,
            RodoviaNumberingPlanReview numberingPlanReview) {}

    private record CuratedSiteEvidence(String name, String city, String district,
                                       String addressPart, String storedWebsite,
                                       double latitude, double longitude,
                                       String officialSourceUrl, String secondarySourceUrl) {}

    /** Administrator-only research clue. Never performs a venue write or GPS approval. */
    static VenueGpsResearchCandidate researchCandidate(Venue venue) {
        if (venue == null || venue.getId() == null
                || venue.getLatitude() != null || venue.getLongitude() != null
                || venue.getInformationSource() != Venue.InformationSource.OFFICIAL) return null;
        CuratedSiteEvidence lead = GPS_RESEARCH_LEADS.get(venue.getId());
        if (lead == null || !lead.name().equals(venue.getName())
                || !lead.city().equals(venue.getCity())
                || !lead.district().equals(venue.getDistrict())
                || venue.getAddress() == null
                || !venue.getAddress().contains(lead.addressPart())
                || !lead.storedWebsite().equals(safeOfficialSourceWebsite(venue.getWebsite()))) {
            return null;
        }
        return new VenueGpsResearchCandidate(
                lead.latitude(), lead.longitude(), lead.officialSourceUrl(),
                lead.secondarySourceUrl(),
                Long.valueOf(273L).equals(venue.getId())
                        ? "MUNICIPAL_MAP_LINK_SITE_NOT_VERIFIED_ENTRANCE"
                        : "SITE_REFERENCE_NOT_VERIFIED_ENTRANCE");
    }

    /**
     * A distinct address-quality warning for the EXISTING ID 284 only.
     *
     * The venue page places the court in Joaquim Campos at Mercado do Forno
     * do Tijolo, which the parish locates at Rua Maria da Fonte. "Rua do Saco
     * 1A" comes from the same page's common footer for Polo da Pena (office),
     * not from the sports equipment's address. Do not imply an exact entrance.
     * No venue edit, reverse geocoding, or geographic coordinates are generated.
     */
    static VenueAddressReview addressReview(Venue venue) {
        if (venue == null || !Long.valueOf(284L).equals(venue.getId())
                || venue.getLatitude() != null || venue.getLongitude() != null
                || venue.getInformationSource() != Venue.InformationSource.OFFICIAL
                || !"Polidesportivo de Arroios — Joaquim Campos".equals(venue.getName())
                || !"Lisboa".equals(venue.getCity()) || !"Lisboa".equals(venue.getDistrict())
                || !"Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa".equals(venue.getAddress())
                || !"https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/"
                        .equals(safeOfficialSourceWebsite(venue.getWebsite()))) {
            return null;
        }
        return new VenueAddressReview(
                "ADMIN_OFFICE_ADDRESS_VS_VENUE_LOCATION_REVIEW",
                "Mercado do Forno do Tijolo, Rua Maria da Fonte, Lisboa — entrada por confirmar",
                "https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/",
                "https://jfarroios.pt/equipamentos/mercados/mercado-do-forno-do-tijolo/");
    }

    /** Read-only context, never a validated new postal address or edit proposal. */
    public record VenueAddressReview(String issueCode, String venueLocationToCheck,
                                      String equipmentSourceUrl, String marketSourceUrl) {}

    /**
     * Viso is an alias-level potential duplicate, NOT an approved merge.
     * Unlike the Choupos site lead, the already populated OSM venue 2623 may
     * correspond to the municipal venue 280. Only report the relationship when
     * both the exact saved municipal record and the live OSM record are intact.
     * Never copy its coordinates or remove a venue.
     */
    static boolean visoDuplicateReviewEligible(Venue venue) {
        return venue != null && Long.valueOf(280L).equals(venue.getId())
                && venue.getLatitude() == null && venue.getLongitude() == null
                && venue.getInformationSource() == Venue.InformationSource.OFFICIAL
                && "Campo Municipal do Viso".equals(venue.getName())
                && "Porto".equals(venue.getCity()) && "Porto".equals(venue.getDistrict())
                && "Rua Direita do Viso, 4250 Porto".equals(venue.getAddress())
                && "https://www.agoraporto.pt/noticias/desporto/desporto/rmid"
                        .equals(safeOfficialSourceWebsite(venue.getWebsite()));
    }

    /** One bounded, read-only DB lookup only for the existing ID 280. */
    private VenuePossibleDuplicateReview possibleDuplicateReview(Venue venue) {
        if (!visoDuplicateReviewEligible(venue)) return null;
        return venues.findById(2623L)
                .filter(other -> "Campo Sintético do Viso".equals(other.getName())
                        && "Porto".equals(other.getCity()) && "Porto".equals(other.getDistrict())
                        && other.getInformationSource() == Venue.InformationSource.OPENSTREETMAP
                        && other.getLatitude() != null && other.getLongitude() != null
                        && Math.abs(other.getLatitude() - 41.1819461) < 0.0003
                        && Math.abs(other.getLongitude() - (-8.6450944)) < 0.0003)
                .map(other -> new VenuePossibleDuplicateReview(
                        "POTENTIAL_SAME_FACILITY_REVIEW", other.getId(), other.getName(),
                        "https://www.agoraporto.pt/grandes-campos-municipais/campo-sintetico-do-viso",
                        "https://www.openstreetmap.org/way/334849388"))
                .orElse(null);
    }

    /** Identity warning only, not an automatic merge, deletion, or GPS update. */
    public record VenuePossibleDuplicateReview(String issueCode, long relatedVenueId,
                                                String relatedVenueName, String officialSourceUrl,
                                                String mapSourceUrl) {}

    /** Named as a research candidate to avoid implying approval or entrance precision. */
    public record VenueGpsResearchCandidate(double latitude, double longitude,
                                             String officialSourceUrl, String secondarySourceUrl,
                                             String precision) {}
    public record VenueGpsReviewItem(long id, String name, String city, String district,
                                     String address, String sourceReference, String informationSource,
                                     String sourceWebsite, VenueGpsResearchCandidate researchCandidate,
                                     VenueAddressReview addressReview,
                                     VenuePossibleDuplicateReview possibleDuplicateReview,
                                     VenueFacilityContextReview facilityContextReview) {}
    public record VenueGpsReviewPage(List<VenueGpsReviewItem> venues, int page, boolean hasMore) {}

    public record VenueDistrictCoverage(
            String district, long venues, long withoutSport, long withoutCoordinates,
            long withoutAddress, long withoutAddressAndCoordinates) {}

    public record VenueQualityCoverage(
            long venues, long withoutSport, long withoutCoordinates,
            long withoutAddress, long withoutAddressAndCoordinates,
            List<VenueDistrictCoverage> districts, Instant generatedAt) {}

    public record PlatformOverview(
            long registeredUsers, long enabledUsers, long registrationsLast7Days,
            long loginsLast15Minutes, Long onlineUsers,
            long totalSessions, long upcomingSessions, long sessionsNext7Days,
            long completedSessions, long cancelledSessions,
            long communities, long tournaments, long activeTournaments,
            long venues, long openUserReports, Instant generatedAt) {}
}
