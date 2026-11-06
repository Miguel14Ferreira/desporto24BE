package com.example.desporto24.repository;

import com.example.desporto24.model.MobilePushDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface MobilePushDeviceRepository extends JpaRepository<MobilePushDevice,String> {
    Optional<MobilePushDevice> findByPushToken(String pushToken);
    List<MobilePushDevice> findTop20ByUsernameOrderByUpdatedAtDesc(String username);
    void deleteByPushToken(String pushToken);
}
