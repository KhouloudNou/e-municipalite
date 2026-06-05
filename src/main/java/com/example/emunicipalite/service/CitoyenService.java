package com.example.emunicipalite.service;

import com.example.emunicipalite.dto.CitoyenDashboardDTo;
import com.example.emunicipalite.entites.Citoyen;
import com.example.emunicipalite.entites.ServiceMuni; // Import s7i7
import com.example.emunicipalite.repository.CitoyenRepository;
import com.example.emunicipalite.repository.ServiceMuniRepository; // Lezem n'injectiouh houni
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import el Transactional

import java.util.List;

@Service
@AllArgsConstructor
public class CitoyenService implements IServiceCitoyen {

    private final CitoyenRepository citoyenRepository;
    private final ServiceMuniRepository serviceMuniRepository;

    @Override
    public Citoyen save(Citoyen citoyen) {
        return citoyenRepository.save(citoyen);
    }

    @Override
    public Citoyen findByEmail(String email) {
        return citoyenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Citoyen not found with email: " + email));
    }


    @Override
    public Citoyen findById(Long id) {
        return citoyenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Citoyen not found with id: " + id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return citoyenRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void ajouterAbonnements(Long citoyenId, List<Long> serviceIds) {

        Citoyen citoyen = findById(citoyenId);
        List<ServiceMuni> nouveauxServices = serviceMuniRepository.findAllById(serviceIds);
        for (ServiceMuni s : nouveauxServices) {
            if (!citoyen.getServicesAbonnes().contains(s)) {
                citoyen.getServicesAbonnes().add(s);
            }
        }
        citoyenRepository.save(citoyen);
    }
    @Override
    @Transactional(readOnly = true)
    public CitoyenDashboardDTo getDashboardStats(String email) {
        Citoyen citoyen = findByEmail(email);
        long nbDocsPrets = citoyen.getDemandes().stream()
                .filter(d -> d.getResultatDemande() != null)
                .count();

        return CitoyenDashboardDTo.builder()
                .nom(citoyen.getNom())
                .nbAbonnements(citoyen.getServicesAbonnes().size())
                .nbDemandes(citoyen.getDemandes().size())
                .nbDocsPrets(nbDocsPrets)
                .demandes(citoyen.getDemandes())
                .servicesAbonnes(citoyen.getServicesAbonnes())
                .build();
    }

}