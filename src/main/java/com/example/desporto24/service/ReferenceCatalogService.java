package com.example.desporto24.service;

import com.example.desporto24.model.dto.reference.ReferenceCatalogItemResponse;
import com.example.desporto24.repository.reference.ReferenceCatalogItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceCatalogService {

    public static final String SPORT = "SPORT";
    public static final String GENDER = "GENDER";
    public static final String SKILL_LEVEL = "SKILL_LEVEL";

    private final ReferenceCatalogItemRepository repository;

    @Transactional(readOnly = true)
    public boolean isAllowed(String category, String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return repository.existsByCategoryIgnoreCaseAndCodeIgnoreCaseAndActiveTrue(
                category,
                value.trim()
        );
    }

    @Transactional(readOnly = true)
    public boolean isAllowedOrBlank(String category, String value) {
        return value == null || value.isBlank() || isAllowed(category, value);
    }

    @Transactional(readOnly = true)
    public boolean areAllowedCsv(String category, String values) {
        if (values == null || values.isBlank()) {
            return true;
        }

        List<String> selected = Arrays.stream(values.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .toList();

        return !selected.isEmpty()
                && selected.stream().allMatch(value -> isAllowed(category, value));
    }

    @Transactional(readOnly = true)
    public List<ReferenceCatalogItemResponse> list(String category) {
        return repository.findByCategoryIgnoreCaseAndActiveTrueOrderBySortOrderAscLabelAsc(category)
                .stream()
                .map(item -> new ReferenceCatalogItemResponse(item.getCode(), item.getLabel()))
                .toList();
    }
}
