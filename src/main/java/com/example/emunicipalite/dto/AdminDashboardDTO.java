package com.example.emunicipalite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data @AllArgsConstructor
public class AdminDashboardDTO {
    private long totalDemandes;
    private long demandesEnAttente;
    private long demandesAcceptees;
    private double totalFraisCollectes;
    private Map<String, Long> demandesParService;
}