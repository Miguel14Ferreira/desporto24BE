package com.example.desporto24.repository;
import com.example.desporto24.model.PrivateGroupMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.*;
public interface PrivateGroupMessageRepository extends JpaRepository<PrivateGroupMessage,Long> {
    List<PrivateGroupMessage> findByGroupIdAndCreatedAtGreaterThanEqualOrderByIdDesc(
        Long groupId,Instant since,Pageable pageable);
    List<PrivateGroupMessage> findByGroupIdAndCreatedAtGreaterThanEqualAndIdLessThanOrderByIdDesc(
        Long groupId,Instant since,Long beforeId,Pageable pageable);
}
