package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.VenueSportClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueSportClassificationRepository extends JpaRepository<VenueSportClassification, Long> {

    List<VenueSportClassification> findAllByOrderByPriorityAscIdAsc();
}
