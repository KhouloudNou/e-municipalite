package com.example.emunicipalite.service;

import com.example.emunicipalite.entites.Citoyen;
import com.example.emunicipalite.entites.ServiceMuni;
import com.example.emunicipalite.repository.ServiceMuniRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceMuniService implements IServiceServiceMuni {

    private final ServiceMuniRepository serviceRepository;

    @Override
    public List<ServiceMuni> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public ServiceMuni findById(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found " + id));
    }


    @Override
    public Page<ServiceMuni> getAvailableServicesForCitoyen(Long citoyenId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.searchAvailableServices(citoyenId, keyword != null ? keyword : "", pageable);
    }

    @Override

    @Transactional
    public ServiceMuni save(ServiceMuni service) {
        if (service.getId() != null) {
            ServiceMuni existingService = serviceRepository.findById(service.getId()).orElse(null);
            if (existingService != null) {
                service.setDemandes(existingService.getDemandes());
                service.setAbonneCitoyens(existingService.getAbonneCitoyens());
            }
        }


        return serviceRepository.save(service);
    }



    @Override
    @Transactional
    public void deleteById(Long id) {
        ServiceMuni service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service introuvable"));
        if (service.getAbonneCitoyens() != null) {
            for (Citoyen citoyen : service.getAbonneCitoyens()) {
                citoyen.getServicesAbonnes().remove(service);
            }
        }
        serviceRepository.delete(service);
    }

    @Override
    public Page<ServiceMuni> searchServices(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.searchServices(keyword != null ? keyword : "", pageable);
    }
}