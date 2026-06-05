package com.example.emunicipalite.repository;

import com.example.emunicipalite.entites.Citoyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitoyenRepository extends JpaRepository<Citoyen, Long> {
    Optional<Citoyen> findByEmail(String email);
    boolean existsByEmail(String email);
}
