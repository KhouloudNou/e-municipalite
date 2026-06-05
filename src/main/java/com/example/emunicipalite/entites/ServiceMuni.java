package com.example.emunicipalite.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ServiceMuni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private Double frais;
    private String documentsRequis;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<Demande> demandes;

    @ManyToMany(mappedBy = "servicesAbonnes")
    private List<Citoyen> abonneCitoyens ;
}