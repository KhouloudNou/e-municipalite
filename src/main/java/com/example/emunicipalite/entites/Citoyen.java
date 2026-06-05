package com.example.emunicipalite.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Citoyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @Column(unique = true)
    private String email;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "citoyen_services",
            joinColumns = @JoinColumn(name = "citoyen_id"),
            inverseJoinColumns = @JoinColumn(name = "service_muni_id")
    )
    private List<ServiceMuni> servicesAbonnes;

    @OneToMany(mappedBy = "citoyen" , cascade = CascadeType.ALL)
    private List<Demande> demandes;


}
