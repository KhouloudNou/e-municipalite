package com.example.emunicipalite.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String adresse;
    private LocalDate dateNaissance;
    @NotBlank(message = "La description est obligatoire")
    private String description;
    private String image;
    private String documentPdf;

    private LocalDateTime dateCreation;
    private String statut;


    @ManyToOne
    @JoinColumn(name = "citoyen_id")
    private Citoyen citoyen;

    @ManyToOne
    @JoinColumn(name = "service")
    private ServiceMuni service;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private ResultatDemande resultatDemande;
}