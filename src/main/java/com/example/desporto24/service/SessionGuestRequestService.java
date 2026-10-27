package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityPlayer;
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
    private final CommunityPlayerRepository players;
    private final SessionGuestRequestRepository requests;
    private final SessionParticipantRepository participants;

    public record GuestPlayer(Long id,String name) {}
    public record GuestRoster(Long sessionId,Long communityId,List<GuestPlayer> players) {}
    public record GuestRequestInput(String linkToken,Long playerId,String newName,String note) {}
    public record ReceiptInput(String receipt) {}
    public record GuestReceipt(String receipt,String status) {}
    public record GuestView(Long id,String name,String status,String note,String teamCode) {}

    @Transactional
    public String issueLink(Long sessionId,String username) {
        Session session=locked(sessionId);
        requireCreator(session,username);
        requirePublicEligible(session);
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
        return new GuestRoster(sessionId,session.getCommunity().getId(),players.findByCommunityIdOrderByDisplayNameAsc(
                session.getCommunity().getId()).stream()
                .filter(p->p.getProfile()==null)
                .limit(150)
                .map(p->new GuestPlayer(p.getId(),p.getDisplayName()))
                .toList());
    }

    @Transactional
    public GuestReceipt request(Long sessionId,GuestRequestInput input) {
        Session session=locked(sessionId);
        verifyToken(session,input==null?null:input.linkToken());
        requirePublicEligible(session);
        if(!SessionRegistrationOpening.isOpen(session,Instant.now())) {
            throw conflict("A convocatória ainda não abriu");
        }
        boolean picked=input.playerId()!=null;
        boolean typed=input.newName()!=null&&!input.newName().isBlank();
        if(picked==typed)throw invalid("Escolhe um jogador ou indica um nome novo");
        CommunityPlayer player=null;
        String name;
        if(picked){
            player=players.findById(input.playerId()).orElseThrow(()->invalid("Jogador não encontrado"));
            if(player.getProfile()!=null||!session.getCommunity().getId().equals(player.getCommunity().getId()))
                throw invalid("Este nome não está disponível para convidados");
            name=player.getDisplayName();
        } else {
            name=clean(input.newName(),80);
            String normalized=normalized(name);
            boolean exists=players.findByCommunityIdOrderByDisplayNameAsc(session.getCommunity().getId())
                    .stream().anyMatch(p->normalized(p.getDisplayName()).equals(normalized));
            if(exists)throw invalid("Este nome já existe. Seleciona-o na lista");
        }
        String normalized=normalized(name);
        if(requests.findBySessionIdAndNormalizedName(sessionId,normalized).isPresent())
            throw conflict("Este jogador já tem um pedido nesta sessão");
        if(requests.countBySessionIdAndStatus(sessionId,SessionGuestRequest.State.PENDING)>=40)
            throw conflict("Limite de pedidos pendentes atingido");
        String note=input.note()==null?"":clean(input.note(),160);
        byte[] bytes=new byte[32];RANDOM.nextBytes(bytes);
        String receipt=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        SessionGuestRequest request=new SessionGuestRequest();
        request.setSession(session);
        request.setCommunityPlayer(player);
        request.setDisplayName(name);
        request.setNormalizedName(normalized);
        request.setReceiptHash(hash(receipt));
        request.setNote(note);
        request.setStatus(SessionGuestRequest.State.PENDING);
        request.setCreatedAt(Instant.now());
        requests.saveAndFlush(request);
        return new GuestReceipt(receipt,request.getStatus().name());
    }

    @Transactional(readOnly=true)
    public GuestReceipt status(Long sessionId,ReceiptInput input) {
        String receipt=input==null?null:input.receipt();
        if(receipt==null||!receipt.matches("[A-Za-z0-9_-]{43}"))
            throw invalid("Comprovativo inválido");
        SessionGuestRequest request=requests.findBySessionIdAndReceiptHash(sessionId,hash(receipt))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado"));
        return new GuestReceipt(receipt,request.getStatus().name());
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
            requirePublicEligible(session);
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

    private Session validLink(Long sessionId,String token) {
        Session session=sessions.findById(sessionId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Sessão não encontrada"));
        verifyToken(session,token);
        requirePublicEligible(session);
        return session;
    }

    private void requirePublicEligible(Session session){
        if(session.getCommunity()==null || session.getAudience()!=SessionAudience.PUBLIC
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
