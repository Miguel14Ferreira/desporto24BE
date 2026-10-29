package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.dto.session.SessionPublicPreviewResponse;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.repository.community.CommunityHistoricalPlayerLinkRepository;
import com.example.desporto24.model.session.SessionGuestRequest;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.session.SessionGuestRequestRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SessionGuestRequestService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final SessionRepository sessions;
    private final SessionCommunityShareRepository sharedCommunities;
    private final CommunityPlayerRepository players;
    private final CommunityHistoricalPlayerLinkRepository historicalLinkRepository;
    private final SessionGuestRequestRepository requests;
    private final SessionParticipantRepository participants;
    private final ProfileRepository profiles;
    private final CommunityHistoricalPlayerLinkService historicalLinks;
    private final WaitlistPromotionService waitlistPromotionService;

    public record GuestPlayer(Long id,String name) {}
    public record GuestRoster(Long sessionId,Long communityId,List<GuestPlayer> players) {}
    public record GuestRequestInput(String linkToken,Long playerId,String newName,String note) {}
    public record ReceiptInput(String receipt) {}
    public record GuestReceipt(String receipt,String status,String playerName,String meetingPointInstructions) {
        public GuestReceipt(String receipt,String status){ this(receipt,status,null,null); }
        public GuestReceipt(String receipt,String status,String playerName){ this(receipt,status,playerName,null); }
    }
    /** The signed link is a bearer capability; never expose these identities on a public GET. */
    public record ConfirmedPlayer(String key,String name,boolean hasAccount) {}

    /**
     * A signed invitation alone is not proof of attendance. Reveal private arrival
     * instructions only to the holder of that guest's approved receipt, while
     * the game remains active. A newly fetched receipt always reflects edits.
     */
    private String confirmedMeetingPoint(Session session,SessionGuestRequest.State state) {
        if(session==null || (state!=SessionGuestRequest.State.APPROVED
                && state!=SessionGuestRequest.State.LINKED)
                || (session.getStatus()!=SessionStatus.OPEN
                    && session.getStatus()!=SessionStatus.FULL)) return null;
        return session.getMeetingPointInstructions();
    }
    public record LinkedReceipt(String status,String username) {}
    public record GuestView(Long id,String name,String status,String note,String teamCode) {}

    /**
     * An invitation link and a typed player name are never chat credentials.
     * Only a private 256-bit RSVP receipt tied to an APPROVED seat authorizes chat.
     * Re-evaluate this database row on every read or send to revoke immediately
     * on cancellation, organizer removal or account linking.
     */
    @Transactional(readOnly=true)
    public SessionGuestRequest requireApprovedChatGuest(Long sessionId,String receipt) {
        if (receipt == null || !receipt.matches("[A-Za-z0-9_-]{43}")) {
            throw new AccessDeniedException("Comprovativo de presença obrigatório");
        }
        SessionGuestRequest guest = requests.findBySessionIdAndReceiptHash(sessionId,hash(receipt))
                .orElseThrow(() -> new AccessDeniedException("Presença não encontrada"));
        if (guest.getStatus() != SessionGuestRequest.State.APPROVED
                || guest.getLinkedProfile() != null) {
            throw new AccessDeniedException("Presença não confirmada");
        }
        return guest;
    }

    /**
     * A single crosspost retains the selected community historical roster.
     * For crossposts to several groups we deliberately do not combine private
     * historical player lists; invitees can still enter their own name.
     */
    private Community historicalCommunity(Session session) {
        if (session.getCommunity() != null) return session.getCommunity();
        var associations=sharedCommunities.findBySession_IdOrderByIdAsc(session.getId());
        return associations.size()==1 ? associations.getFirst().getCommunity() : null;
    }

    @Transactional
    public String issueLink(Long sessionId,String username) {
        Session session=locked(sessionId);
        requireCreator(session,username);
        requireGuestEligible(session);
        byte[] bytes=new byte[32];
        RANDOM.nextBytes(bytes);
        String token=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        session.setGuestLinkHash(hash(token));
        sessions.save(session);
        return token;
    }

    @Transactional(readOnly=true)
    public GuestRoster roster(Long sessionId,String token) {
        Session session=validLink(sessionId,token);
        Community group=historicalCommunity(session);
        if (group == null) return new GuestRoster(sessionId, null, List.of());
        var claimed = historicalLinkRepository.findByCommunityIdAndStatus(group.getId(),"ACCEPTED")
                .stream().map(link->link.getPlayer().getId()).collect(java.util.stream.Collectors.toSet());
        // Previously submitted names cannot be reserved a second time.
        // Keep them visible in confirmedPlayers(), not in the identity picker.
        var alreadyUsedNames = requests.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .filter(r -> r.getStatus()!=SessionGuestRequest.State.CANCELLED
                        && r.getStatus()!=SessionGuestRequest.State.REJECTED)
                .map(SessionGuestRequest::getNormalizedName)
                .collect(java.util.stream.Collectors.toSet());
        return new GuestRoster(sessionId,group.getId(),players.findByCommunityIdOrderByDisplayNameAsc(
                group.getId()).stream()
                .filter(p->p.getProfile()==null && !claimed.contains(p.getId())
                        && !alreadyUsedNames.contains(normalized(p.getDisplayName())))
                .limit(150)
                .map(p->new GuestPlayer(p.getId(),p.getDisplayName()))
                .toList());
    }

    /** Participant-only display identities. Never expose guest notes, receipts or contacts. */
    public record ConfirmedGuest(Long id,String name,String teamCode) {}

    @Transactional(readOnly=true)
    public List<ConfirmedGuest> confirmedGuestsForMember(Long sessionId,String username) {
        Session session=sessions.findById(sessionId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Sessão não encontrada"));
        if(username==null||username.isBlank())
            throw new AccessDeniedException("Acesso não autorizado");
        boolean creator=session.getProfile()!=null
                && username.equalsIgnoreCase(session.getProfile().getUsername());
        if(!creator){
            Profile viewer=profiles.findUserByUsernameIgnoreCase(username)
                    .orElseThrow(()->new AccessDeniedException("Acesso não autorizado"));
            if(!participants.existsBySessionIdAndProfileIdAndState(
                    sessionId,viewer.getId(),ParticipantState.ACTIVE))
                throw new AccessDeniedException("Apenas participantes confirmados podem consultar os convidados");
        }
        return requests.findBySessionIdAndStatusOrderByCreatedAtAsc(
                sessionId,SessionGuestRequest.State.APPROVED).stream()
                .map(guest->new ConfirmedGuest(guest.getId(),guest.getDisplayName(),guest.getTeamCode()))
                .toList();
    }

    /**
     * Session-only confirmed roster, available exclusively to people possessing
     * the organiser-issued signed link. No email, phone, IDs or private
     * community ranking data are returned.
     */
    @Transactional(readOnly=true)
    public List<ConfirmedPlayer> confirmedPlayers(Long sessionId,String token) {
        validLink(sessionId,token);
        List<ConfirmedPlayer> result=new java.util.ArrayList<>();
        for(SessionParticipant member:participants.findBySessionIdAndStateOrderByJoinedAtAsc(
                sessionId,ParticipantState.ACTIVE)) {
            if(member.getProfile()!=null && member.getProfile().getUsername()!=null) {
                result.add(new ConfirmedPlayer("account:"+member.getId(),
                        member.getProfile().getUsername(),true));
            }
        }
        for(SessionGuestRequest guest:requests.findBySessionIdAndStatusOrderByCreatedAtAsc(
                sessionId,SessionGuestRequest.State.APPROVED)) {
            result.add(new ConfirmedPlayer("guest:"+guest.getId(),guest.getDisplayName(),false));
        }
        return List.copyOf(result);
    }

    @Transactional
    public GuestReceipt request(Long sessionId,GuestRequestInput input) {
        Session session=locked(sessionId);
        verifyToken(session,input==null?null:input.linkToken());
        requireGuestEligible(session);
        if(!SessionRegistrationOpening.isOpen(session,Instant.now())) {
            throw conflict("A convocatória ainda não abriu");
        }
        // A valid, organizer-issued link is consent to reserve a guest spot
        // immediately. Lock the session row so simultaneous requests cannot
        // overbook a full game.
        long active=participants.countBySessionIdAndState(sessionId,ParticipantState.ACTIVE);
        if(SessionCapacity.remainingPlaces(session,active)<=0)
            throw conflict("Este jogo já não tem vagas disponíveis");
        boolean picked=input.playerId()!=null;
        boolean typed=input.newName()!=null&&!input.newName().isBlank();
        if(picked==typed)throw invalid("Escolhe um jogador ou indica um nome novo");
        CommunityPlayer player=null;
        Community group=historicalCommunity(session);
        String name;
        if(picked){
            if(group==null)throw invalid("Este jogo não tem uma lista histórica de jogadores");
            player=players.findById(input.playerId()).orElseThrow(()->invalid("Jogador não encontrado"));
            if(player.getProfile()!=null||!group.getId().equals(player.getCommunity().getId())
                    || historicalLinkRepository.existsByCommunityIdAndPlayerIdAndStatus(
                        group.getId(),player.getId(),"ACCEPTED"))
                throw invalid("Este nome já está associado a uma conta");
            name=player.getDisplayName();
        } else {
            name=clean(input.newName(),80);
            String normalized=normalized(name);
            if(group!=null){
                boolean exists=players.findByCommunityIdOrderByDisplayNameAsc(group.getId())
                        .stream().anyMatch(p->normalized(p.getDisplayName()).equals(normalized));
                if(exists)throw invalid("Este nome já existe. Seleciona-o na lista");
            }
        }
        String normalized=normalized(name);
        // A cancelled person may return later; reuse the unique reservation
        // row with a brand-new secret receipt. Existing active/linked seats
        // remain protected against duplicates.
        SessionGuestRequest previous=requests.findBySessionIdAndNormalizedName(sessionId,normalized)
                .orElse(null);
        if(previous!=null && previous.getStatus()!=SessionGuestRequest.State.CANCELLED)
            throw conflict("Este jogador já tem um pedido nesta sessão");

        String note=input.note()==null||input.note().isBlank()?"":clean(input.note(),160);
        byte[] bytes=new byte[32];RANDOM.nextBytes(bytes);
        String receipt=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        SessionGuestRequest request=previous==null?new SessionGuestRequest():previous;
        request.setSession(session);
        request.setCommunityPlayer(player);
        request.setLinkedProfile(null);
        request.setTeamCode(null);
        request.setDisplayName(name);
        request.setNormalizedName(normalized);
        request.setReceiptHash(hash(receipt));
        request.setNote(note);
        Instant now=Instant.now();
        request.setStatus(SessionGuestRequest.State.APPROVED);
        request.setCreatedAt(now);
        request.setReviewedAt(now);
        requests.saveAndFlush(request);
        int external=session.getExternalConfirmedParticipants()==null?0:
                session.getExternalConfirmedParticipants();
        session.setExternalConfirmedParticipants(external+1);
        session.setStatus(SessionCapacity.remainingPlaces(session,active)==0
                ? SessionStatus.FULL : SessionStatus.OPEN);
        session.setUpdatedAt(now);
        sessions.save(session);
        return new GuestReceipt(receipt,request.getStatus().name(),name,
                confirmedMeetingPoint(session,request.getStatus()));
    }

    @Transactional
    public GuestReceipt status(Long sessionId,ReceiptInput input) {
        String receipt=input==null?null:input.receipt();
        if(receipt==null||!receipt.matches("[A-Za-z0-9_-]{43}"))
            throw invalid("Comprovativo inválido");
        String digest=hash(receipt);
        SessionGuestRequest request=requests.findBySessionIdAndReceiptHash(sessionId,digest)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
        // Bring pre-release pending receipts into the new instant-confirmation flow,
        // but never overbook or override explicit cancellation/rejection.
        if(request.getStatus()==SessionGuestRequest.State.PENDING){
            Session session=locked(sessionId);
            request=requests.findBySessionIdAndReceiptHash(sessionId,digest)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
            Instant now=Instant.now();
            if(request.getStatus()==SessionGuestRequest.State.PENDING
                    && session.getGameDate()!=null && session.getGameDate().toInstant().isAfter(now)
                    && session.getStatus()!=SessionStatus.CANCELLED
                    && session.getStatus()!=SessionStatus.COMPLETED
                    && (session.getStatus()==SessionStatus.OPEN || session.getStatus()==SessionStatus.FULL)
                    && SessionRegistrationOpening.isOpen(session,now)){
                long active=participants.countBySessionIdAndState(sessionId,ParticipantState.ACTIVE);
                if(SessionCapacity.remainingPlaces(session,active)>0){
                    int external=session.getExternalConfirmedParticipants()==null?0:
                            session.getExternalConfirmedParticipants();
                    request.setStatus(SessionGuestRequest.State.APPROVED);
                    request.setReviewedAt(now);
                    requests.save(request);
                    session.setExternalConfirmedParticipants(external+1);
                    session.setUpdatedAt(now);
                    session.setStatus(SessionCapacity.remainingPlaces(session,active)==0
                            ?SessionStatus.FULL:SessionStatus.OPEN);
                    sessions.save(session);
                }
            }
        }
        return new GuestReceipt(receipt,request.getStatus().name(),request.getDisplayName(),
                confirmedMeetingPoint(request.getSession(),request.getStatus()));
    }

    /**
     * Explicit user consent, organizer approval and possession of an unguessable
     * receipt are ALL required. Transfers the already reserved guest seat to the
     * real registered profile instead of counting the same person twice.
     * Imported statistics remain untouched; the existing claim service enforces
     * historical identity collisions and overlapping-round integrity.
     */
    @Transactional
    public LinkedReceipt linkAccount(Long sessionId,ReceiptInput input,String username) {
        Session session=locked(sessionId);
        String receipt=input==null?null:input.receipt();
        if(receipt==null||!receipt.matches("[A-Za-z0-9_-]{43}"))
            throw invalid("Comprovativo inválido");
        SessionGuestRequest guest=requests.findBySessionIdAndReceiptHash(sessionId,hash(receipt))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
        Profile owner=profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(()->new AccessDeniedException("Conta não encontrada"));
        if(guest.getStatus()==SessionGuestRequest.State.LINKED &&
                guest.getLinkedProfile()!=null && owner.getId().equals(guest.getLinkedProfile().getId()))
            return new LinkedReceipt("LINKED",owner.getUsername());
        if(guest.getStatus()!=SessionGuestRequest.State.APPROVED)
            throw conflict("A presença ainda não foi aprovada");
        if(participants.findBySessionIdAndProfileId(sessionId,owner.getId()).isPresent())
            throw conflict("Esta conta já tem uma inscrição nesta sessão");
        int external=session.getExternalConfirmedParticipants()==null?0:
                session.getExternalConfirmedParticipants();
        if(external<=0)throw conflict("A reserva de convidado deixou de existir");
        if(guest.getCommunityPlayer()!=null){
            historicalLinks.claimOrganizerApprovedGuest(guest.getCommunityPlayer().getCommunity().getId(),
                    owner.getUsername(),guest.getCommunityPlayer().getId());
        }
        SessionParticipant member=new SessionParticipant();
        member.setSession(session);
        member.setProfile(owner);
        member.setState(ParticipantState.ACTIVE);
        member.setAttendanceStatus(AttendanceStatus.PENDING);
        member.setJoinedAt(Instant.now());
        member.setTeamCode(guest.getTeamCode());
        participants.saveAndFlush(member);
        session.setExternalConfirmedParticipants(external-1);
        session.setUpdatedAt(Instant.now());
        sessions.save(session);
        guest.setLinkedProfile(owner);
        guest.setStatus(SessionGuestRequest.State.LINKED);
        requests.save(guest);
        return new LinkedReceipt("LINKED",owner.getUsername());
    }

    @Transactional(readOnly=true)
    public List<GuestView> list(Long sessionId,String username) {
        Session session=sessions.findById(sessionId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Sessão não encontrada"));
        requireCreator(session,username);
        return requests.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(g->new GuestView(g.getId(),g.getDisplayName(),g.getStatus().name(),
                        g.getNote(),g.getTeamCode())).toList();
    }

    @Transactional
    public GuestView review(Long sessionId,Long guestId,boolean approve,String username) {
        Session session=locked(sessionId);
        requireCreator(session,username);
        SessionGuestRequest request=requests.findById(guestId)
                .filter(r->r.getSession().getId().equals(sessionId))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
        if(request.getStatus()!=SessionGuestRequest.State.PENDING)
            throw conflict("Pedido já decidido");
        if(approve){
            requireGuestEligible(session);
            if(!SessionRegistrationOpening.isOpen(session,Instant.now()))
                throw conflict("A convocatória ainda não abriu");
            long active=participants.countBySessionIdAndState(sessionId,ParticipantState.ACTIVE);
            if(SessionCapacity.remainingPlaces(session,active)<=0)
                throw conflict("Não há vagas para confirmar esta presença");
            int external=session.getExternalConfirmedParticipants()==null
                    ? 0:session.getExternalConfirmedParticipants();
            session.setExternalConfirmedParticipants(external+1);
            session.setStatus(SessionCapacity.remainingPlaces(session,active)==0
                    ? SessionStatus.FULL:SessionStatus.OPEN);
            session.setUpdatedAt(Instant.now());
            sessions.save(session);
            request.setStatus(SessionGuestRequest.State.APPROVED);
        } else {
            request.setStatus(SessionGuestRequest.State.REJECTED);
        }
        request.setReviewedAt(Instant.now());
        requests.save(request);
        return new GuestView(request.getId(),request.getDisplayName(),request.getStatus().name(),
                request.getNote(),request.getTeamCode());
    }

    /**
     * Cancel only an approved external reservation, before kickoff.
     * The existing session row lock serializes the seat release with joining,
     * approvals and account linking. A linked account must use normal
     * registered-participant removal, not decrement external capacity again.
     */
    @Transactional
    public GuestView cancel(Long sessionId,Long guestId,String username) {
        Session session=locked(sessionId);
        requireCreator(session,username);
        if(session.getStatus()==SessionStatus.CANCELLED
                || session.getStatus()==SessionStatus.COMPLETED
                || session.getGameDate()==null
                || !session.getGameDate().toInstant().isAfter(Instant.now()))
            throw conflict("Não é possível cancelar presenças após o início ou fecho do jogo");
        SessionGuestRequest guest=requests.findById(guestId)
                .filter(r->r.getSession().getId().equals(sessionId))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
        cancelApprovedLocked(sessionId,session,guest);
        return new GuestView(guest.getId(),guest.getDisplayName(),guest.getStatus().name(),
                guest.getNote(),guest.getTeamCode());
    }

    public record ChangeGuestIdentityInput(String receipt,Long playerId,String newName) {}

    /**
     * Fixes the identity of a confirmed guest without moving, releasing or
     * re-reserving the seat. Atomic under the same session row lock as joining.
     * The private receipt is the sole proof of ownership, never the shared link.
     */
    @Transactional
    public GuestReceipt changeGuestIdentity(Long sessionId,ChangeGuestIdentityInput input) {
        String receipt=input==null?null:input.receipt();
        if(receipt==null||!receipt.matches("[A-Za-z0-9_-]{43}"))
            throw invalid("Comprovativo inválido");
        Session session=locked(sessionId);
        SessionGuestRequest guest=requests.findBySessionIdAndReceiptHash(sessionId,hash(receipt))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
        if(guest.getStatus()!=SessionGuestRequest.State.APPROVED)
            throw conflict("Apenas uma presença confirmada sem conta pode ser alterada");
        if(session.getStatus()==SessionStatus.CANCELLED || session.getStatus()==SessionStatus.COMPLETED
                || session.getGameDate()==null
                || !session.getGameDate().toInstant().isAfter(Instant.now()))
            throw conflict("Já não é possível alterar o jogador depois do início");

        boolean selected=input.playerId()!=null;
        boolean typed=input.newName()!=null && !input.newName().isBlank();
        if(selected==typed)throw invalid("Escolhe um jogador ou indica um nome novo");
        Community group=historicalCommunity(session);
        CommunityPlayer historical=null;
        String name;
        if(selected){
            if(group==null)throw invalid("Esta sessão não tem jogadores históricos");
            historical=players.findById(input.playerId())
                    .orElseThrow(()->invalid("Jogador não encontrado"));
            if(historical.getProfile()!=null || historical.getCommunity()==null
                    || !group.getId().equals(historical.getCommunity().getId())
                    || historicalLinkRepository.existsByCommunityIdAndPlayerIdAndStatus(
                        group.getId(),historical.getId(),"ACCEPTED"))
                throw invalid("Este jogador já está associado a uma conta");
            name=historical.getDisplayName();
        } else {
            name=clean(input.newName(),80);
            if(group!=null){
                String wanted=normalized(name);
                boolean historicalName=players.findByCommunityIdOrderByDisplayNameAsc(group.getId())
                        .stream().anyMatch(p->normalized(p.getDisplayName()).equals(wanted));
                if(historicalName)throw invalid("Este nome existe na comunidade. Seleciona-o na lista");
            }
        }

        String normalizedName=normalized(name);
        if(normalizedName.equals(guest.getNormalizedName())
                && java.util.Objects.equals(historical==null?null:historical.getId(),
                    guest.getCommunityPlayer()==null?null:guest.getCommunityPlayer().getId()))
            return new GuestReceipt(receipt,"APPROVED",guest.getDisplayName(),
                    confirmedMeetingPoint(session,guest.getStatus()));
        // Unique (session_id, normalized_name) prevents concurrent identity collisions.
        // A previously cancelled identity is still reserved as an audit row;
        // it cannot be stolen or overwritten by another guest.
        SessionGuestRequest other=requests.findBySessionIdAndNormalizedName(sessionId,normalizedName)
                .orElse(null);
        if(other!=null && !java.util.Objects.equals(other.getId(),guest.getId()))
            throw conflict("Este jogador já está associado a outra inscrição nesta sessão");

        guest.setCommunityPlayer(historical);
        guest.setDisplayName(name);
        guest.setNormalizedName(normalizedName);
        // Team balance is no longer valid after replacing a participant identity.
        if(guest.getTeamCode()!=null){
            guest.setTeamCode(null);
            List<SessionParticipant> registered=participants
                    .findBySessionIdAndStateOrderByJoinedAtAsc(sessionId,ParticipantState.ACTIVE);
            boolean changed=false;
            for(SessionParticipant member:registered){
                if(member.getTeamCode()!=null){member.setTeamCode(null);changed=true;}
            }
            if(changed)participants.saveAll(registered);
            List<SessionGuestRequest> approved=requests
                    .findBySessionIdAndStatusOrderByCreatedAtAsc(
                        sessionId,SessionGuestRequest.State.APPROVED);
            boolean guestChanged=false;
            for(SessionGuestRequest otherGuest:approved){
                if(otherGuest.getTeamCode()!=null){
                    otherGuest.setTeamCode(null);guestChanged=true;
                }
            }
            if(guestChanged)requests.saveAll(approved);
        }
        requests.saveAndFlush(guest);
        // No capacity change; same request ID, same receipt, same confirmed slot.
        return new GuestReceipt(receipt,"APPROVED",name,confirmedMeetingPoint(session,guest.getStatus()));
    }

    /**
     * A guest may cancel ONLY the reservation proven by its private receipt.
     * A shared invite token and a display name alone never authorize deletion.
     */
    @Transactional
    public GuestReceipt cancelByReceipt(Long sessionId,ReceiptInput input) {
        String receipt=input==null?null:input.receipt();
        if(receipt==null||!receipt.matches("[A-Za-z0-9_-]{43}"))
            throw invalid("Comprovativo inválido");
        Session session=locked(sessionId);
        SessionGuestRequest guest=requests.findBySessionIdAndReceiptHash(sessionId,hash(receipt))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
        if(guest.getStatus()==SessionGuestRequest.State.CANCELLED)
            return new GuestReceipt(receipt,"CANCELLED",guest.getDisplayName(),null);
        if(guest.getStatus()!=SessionGuestRequest.State.APPROVED)
            throw conflict("A presença já não pode ser anulada pelo convite");
        if(session.getStatus()==SessionStatus.CANCELLED||session.getStatus()==SessionStatus.COMPLETED
                || session.getGameDate()==null
                || !session.getGameDate().toInstant().isAfter(Instant.now()))
            throw conflict("Já não é possível cancelar esta presença");
        cancelApprovedLocked(sessionId,session,guest);
        return new GuestReceipt(receipt,"CANCELLED",guest.getDisplayName(),null);
    }

    private void cancelApprovedLocked(Long sessionId,Session session,SessionGuestRequest guest) {
        if(guest.getStatus()!=SessionGuestRequest.State.APPROVED)
            throw conflict("Apenas uma presença de convidado confirmada pode ser cancelada");
        int external=session.getExternalConfirmedParticipants()==null?0:
                session.getExternalConfirmedParticipants();
        if(external<=0)throw conflict("A reserva do convidado já não existe");
        Instant now=Instant.now();
        guest.setStatus(SessionGuestRequest.State.CANCELLED);
        guest.setTeamCode(null);
        requests.save(guest);
        session.setExternalConfirmedParticipants(external-1);
        long active=participants.countBySessionIdAndState(sessionId,ParticipantState.ACTIVE);
        session.setStatus(SessionCapacity.remainingPlaces(session,active)==0
                ? SessionStatus.FULL:SessionStatus.OPEN);
        session.setUpdatedAt(now);
        sessions.save(session);

        // An old team assignment is no longer valid after changing the roster.
        List<SessionParticipant> registered=participants
                .findBySessionIdAndStateOrderByJoinedAtAsc(sessionId,ParticipantState.ACTIVE);
        boolean changed=false;
        for(SessionParticipant member:registered){
            if(member.getTeamCode()!=null){member.setTeamCode(null);changed=true;}
        }
        if(changed)participants.saveAll(registered);
        List<SessionGuestRequest> stillApproved=requests
                .findBySessionIdAndStatusOrderByCreatedAtAsc(sessionId,SessionGuestRequest.State.APPROVED);
        boolean guestsChanged=false;
        for(SessionGuestRequest other:stillApproved){
            if(other.getTeamCode()!=null){other.setTeamCode(null);guestsChanged=true;}
        }
        if(guestsChanged)requests.saveAll(stillApproved);

        // Never hand a freed place to a new guest ahead of the waitlist.
        while(SessionCapacity.remainingPlaces(session,
                participants.countBySessionIdAndState(sessionId,ParticipantState.ACTIVE))>0) {
            if(waitlistPromotionService.promoteNext(session,now).isEmpty())break;
        }
        active=participants.countBySessionIdAndState(sessionId,ParticipantState.ACTIVE);
        session.setStatus(SessionCapacity.remainingPlaces(session,active)==0
                ? SessionStatus.FULL:SessionStatus.OPEN);
        session.setUpdatedAt(now);
        sessions.save(session);
    }

    /** Safe preview visible only with an organizer-issued secret for members-only games. */
    @Transactional(readOnly=true)
    public SessionPublicPreviewResponse preview(Long sessionId,String token) {
        Session session=validLink(sessionId,token);
        return new SessionPublicPreviewResponse(
                session.getId(),session.getSport(),session.getCity(),
                session.getGameDate().toInstant(),session.getMaxParticipants(),
                SessionCapacity.totalConfirmed(session,
                    participants.countBySessionIdAndState(sessionId,ParticipantState.ACTIVE)),
                session.getPriceAmount(),session.getStatus(),session.getGamePhoto(),
                session.getVenue()==null?null:session.getVenue().getName(),
                session.getAddress()!=null && !session.getAddress().isBlank()
                  ?session.getAddress()
                  :session.getVenue()==null?null:session.getVenue().getAddress(),
                invitationLatitude(session),invitationLongitude(session));
    }

    /**
     * The session's confirmed GPS pin is authoritative, not reverse-geocoded
     * street text. A valid organiser-signed invitation already reveals its
     * exact address; never expose coordinates from the ordinary public GET.
     */
    private static Double invitationLatitude(Session s) {
        Double lat = s.getLatitude(), lon = s.getLongitude();
        if (!validPin(lat,lon) && s.getVenue()!=null) {
            lat=s.getVenue().getLatitude();
            lon=s.getVenue().getLongitude();
        }
        return validPin(lat,lon) ? lat : null;
    }

    private static Double invitationLongitude(Session s) {
        Double lat = s.getLatitude(), lon = s.getLongitude();
        if (!validPin(lat,lon) && s.getVenue()!=null) {
            lat=s.getVenue().getLatitude();
            lon=s.getVenue().getLongitude();
        }
        return validPin(lat,lon) ? lon : null;
    }

    private static boolean validPin(Double lat,Double lon) {
        return lat!=null && lon!=null && Double.isFinite(lat) && Double.isFinite(lon)
                && Math.abs(lat)<=90 && Math.abs(lon)<=180
                && !(lat==0d && lon==0d);
    }

    private Session validLink(Long sessionId,String token) {
        Session session=sessions.findById(sessionId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Sessão não encontrada"));
        verifyToken(session,token);
        requireGuestEligible(session);
        return session;
    }

    private void requireGuestEligible(Session session){
        if((session.getAudience()!=SessionAudience.PUBLIC
                && session.getAudience()!=SessionAudience.COMMUNITY_ONLY)
                || Boolean.TRUE.equals(session.getPrivateGame())
                || Boolean.FALSE.equals(session.getEnabled())
                || session.getGameDate()==null
                || !session.getGameDate().toInstant().isAfter(Instant.now())
                || session.getStatus()==SessionStatus.CANCELLED
                || session.getStatus()==SessionStatus.COMPLETED)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ligação indisponível");
    }
    private void verifyToken(Session session,String token){
        if(token==null||!token.matches("[A-Za-z0-9_-]{43}")||session.getGuestLinkHash()==null
                || !MessageDigest.isEqual(session.getGuestLinkHash().getBytes(StandardCharsets.UTF_8),
                    hash(token).getBytes(StandardCharsets.UTF_8)))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ligação indisponível");
    }
    private void requireCreator(Session session,String username){
        if(session.getProfile()==null||username==null
                ||!session.getProfile().getUsername().equalsIgnoreCase(username))
            throw new AccessDeniedException("Apenas o organizador pode gerir presenças");
    }
    private Session locked(Long id){
        return sessions.findByIdForUpdate(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Sessão não encontrada"));
    }
    private static String clean(String value,int max){
        if(value==null)throw invalid("Texto obrigatório");
        String name=value.strip().replaceAll("[\\p{Cntrl}]+"," ");
        if(name.length()<2||name.length()>max)throw invalid("Comprimento inválido");
        return name;
    }
    private static String normalized(String value){
        return Normalizer.normalize(value.strip(),Normalizer.Form.NFD)
                .replaceAll("\\p{M}","").toLowerCase(Locale.ROOT).replaceAll("\\s+"," ");
    }
    private static String hash(String input){
        try{
            byte[] digest=MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        }catch(java.security.NoSuchAlgorithmException ex){throw new IllegalStateException(ex);}
    }
    private static ResponseStatusException invalid(String s){return new ResponseStatusException(HttpStatus.BAD_REQUEST,s);}
    private static ResponseStatusException conflict(String s){return new ResponseStatusException(HttpStatus.CONFLICT,s);}
}
