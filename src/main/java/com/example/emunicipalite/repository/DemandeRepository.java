package com.example.emunicipalite.repository;

import com.example.emunicipalite.entites.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    @Query("SELECT d FROM Demande d WHERE d.citoyen.id = :id AND (d.statut LIKE %:kw% )")
    Page<Demande> findByCitoyenId(@Param("id") Long id, @Param("kw") String Keyword, Pageable pageable);

    Page<Demande> findByCitoyenNomContaining(String nom, Pageable pageable);
}
