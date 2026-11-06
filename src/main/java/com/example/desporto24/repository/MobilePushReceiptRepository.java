package com.example.desporto24.repository;

import com.example.desporto24.model.MobilePushReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface MobilePushReceiptRepository extends JpaRepository<MobilePushReceipt,String> {
    List<MobilePushReceipt> findTop100ByNextCheckAtBeforeOrderByNextCheckAtAsc(Instant moment);
}
