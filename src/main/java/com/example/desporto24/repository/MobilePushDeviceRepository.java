package com.example.desporto24.repository;

import com.example.desporto24.model.MobilePushDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MobilePushDeviceRepository extends JpaRepository<MobilePushDevice,String> {
    Optional<MobilePushDevice> findByPushToken(String pushToken);
}
