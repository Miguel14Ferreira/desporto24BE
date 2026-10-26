package com.example.desporto24.controllers;

import com.example.desporto24.service.AdminPlatformOverviewService;
import com.example.desporto24.service.AdminGrowthTrendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/overview")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminPlatformOverviewController {
    private final AdminPlatformOverviewService service;
    private final AdminGrowthTrendsService trendsService;

    @GetMapping("/venue-quality")
    public AdminPlatformOverviewService.VenueQualityCoverage venueQuality() {
        return service.venueQuality();
    }

    @GetMapping("/trends")
    public AdminGrowthTrendsService.GrowthTrends trends() {
        return trendsService.trends();
    }

    @GetMapping
    public AdminPlatformOverviewService.PlatformOverview overview() {
        return service.snapshot();
    }
}
