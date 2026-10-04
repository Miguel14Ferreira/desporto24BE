package com.example.desporto24.repository.reference;

import com.example.desporto24.model.reference.ReferenceCatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferenceCatalogItemRepository extends JpaRepository<ReferenceCatalogItem, Long> {

    boolean existsByCategoryIgnoreCaseAndCodeIgnoreCaseAndActiveTrue(String category, String code);

    List<ReferenceCatalogItem> findByCategoryIgnoreCaseAndActiveTrueOrderBySortOrderAscLabelAsc(String category);
}
