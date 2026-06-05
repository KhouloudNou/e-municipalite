package com.example.emunicipalite.dto;

import com.example.emunicipalite.entites.Demande;
import com.example.emunicipalite.entites.ServiceMuni;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CitoyenDashboardDTo {
    private String nom;
    private int nbAbonnements;
    private int nbDemandes;
    private long nbDocsPrets;
    private List<Demande> demandes;
    private List<ServiceMuni> servicesAbonnes;
}