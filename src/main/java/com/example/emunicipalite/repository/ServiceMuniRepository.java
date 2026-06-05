package com.example.emunicipalite.repository;

import com.example.emunicipalite.entites.ServiceMuni;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceMuniRepository extends JpaRepository<ServiceMuni, Long> {

    @Query("SELECT s FROM ServiceMuni s WHERE " + "(LOWER(s.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND s NOT IN (SELECT sub FROM Citoyen c JOIN c.servicesAbonnes sub WHERE c.id = :citoyenId)")
    Page<ServiceMuni> searchAvailableServices(@Param("citoyenId") Long citoyenId, @Param("keyword") String keyword, Pageable pageable
    );



    @Query("SELECT s FROM ServiceMuni s WHERE " +
            "LOWER(s.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ServiceMuni> searchServices(@Param("keyword") String keyword, Pageable pageable);
    List<ServiceMuni> findByNomContainingIgnoreCase(String nom);
}