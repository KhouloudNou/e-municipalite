package com.example.emunicipalite.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResultatDemande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refrencePdf;
    private String fichierPdf;
    private LocalDateTime dateEmission;
    private String commentaireAdmin;

    @OneToOne(mappedBy = "resultatDemande")
    private Demande demande;
}