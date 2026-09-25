package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.AttendanceDispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceDisputeRepository extends JpaRepository<AttendanceDispute, Long> {
}
