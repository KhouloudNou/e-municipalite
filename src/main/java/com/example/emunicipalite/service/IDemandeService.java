package com.example.emunicipalite.service;

import com.example.emunicipalite.dto.AdminDashboardDTO;
import com.example.emunicipalite.entites.Demande;
import com.example.emunicipalite.entites.ResultatDemande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDemandeService {
    void save(Demande demande, MultipartFile image, MultipartFile documentPdf);
    Page<Demande> getDemandesByCitoyen(Long id, String keyword, Pageable pageable);
    void delete(Long id);
    Demande findById(Long id);
    AdminDashboardDTO getAdminDashboardStats();
    List<Demande> getLastFiveDemandes();
    Page<Demande> getAllDemandes(String keyword, Pageable pageable);
    void updateStatut(Long id, String statut);
    void saveResultat(Long demandeId, ResultatDemande res, MultipartFile file);
    List<ResultatDemande> getResultatsParCitoyen(Long citoyenId);

}
