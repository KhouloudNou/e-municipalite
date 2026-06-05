package com.example.emunicipalite.repository;

import com.example.emunicipalite.entites.ResultatDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResultatDemandeRepository extends JpaRepository<ResultatDemande, Integer> {
    @Query("SELECT r FROM ResultatDemande r WHERE r.demande.citoyen.id = :citoyenId")
    List<ResultatDemande> findByByCitoyenId(@Param("citoyenId") Long citoyenId);
}
