package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.reference.ReferenceCatalogItemResponse;
import com.example.desporto24.service.ReferenceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/catalog")
public class ReferenceCatalogController {

    private final ReferenceCatalogService referenceCatalogService;

    @GetMapping("/sports")
    public List<ReferenceCatalogItemResponse> sports() {
        return referenceCatalogService.list(ReferenceCatalogService.SPORT);
    }

    @GetMapping("/genders")
    public List<ReferenceCatalogItemResponse> genders() {
        return referenceCatalogService.list(ReferenceCatalogService.GENDER);
    }

    @GetMapping("/skill-levels")
    public List<ReferenceCatalogItemResponse> skillLevels() {
        return referenceCatalogService.list(ReferenceCatalogService.SKILL_LEVEL);
    }
}
