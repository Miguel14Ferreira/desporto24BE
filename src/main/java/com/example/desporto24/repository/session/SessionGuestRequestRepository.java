package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionGuestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SessionGuestRequestRepository extends JpaRepository<SessionGuestRequest,Long> {
    Optional<SessionGuestRequest> findBySessionIdAndNormalizedName(Long sessionId,String normalizedName);
    Optional<SessionGuestRequest> findBySessionIdAndReceiptHash(Long sessionId,String receiptHash);
    List<SessionGuestRequest> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
    List<SessionGuestRequest> findBySessionIdAndStatusOrderByCreatedAtAsc(
        Long sessionId,SessionGuestRequest.State status);
    long countBySessionIdAndStatus(Long sessionId,SessionGuestRequest.State status);
}
