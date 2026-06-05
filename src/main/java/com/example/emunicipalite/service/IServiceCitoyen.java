package com.example.emunicipalite.service;

import com.example.emunicipalite.dto.CitoyenDashboardDTo;
import com.example.emunicipalite.entites.Citoyen;

import java.util.List;

public interface IServiceCitoyen {
    Citoyen save(Citoyen citoyen);
    Citoyen findByEmail(String email);
    boolean existsByEmail(String email);
    void ajouterAbonnements(Long citoyenId, List<Long> serviceIds);
    Citoyen findById(Long id);
    CitoyenDashboardDTo getDashboardStats(String email);
}
