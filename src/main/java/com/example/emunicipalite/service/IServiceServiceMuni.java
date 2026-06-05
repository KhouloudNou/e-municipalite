package com.example.emunicipalite.service;

import com.example.emunicipalite.entites.ServiceMuni;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IServiceServiceMuni {


    List<ServiceMuni> getAllServices();
    ServiceMuni findById(Long id);
    ServiceMuni save(ServiceMuni service);
    void deleteById(Long id);
    Page<ServiceMuni> searchServices(String keyword, int page, int size);
    Page<ServiceMuni> getAvailableServicesForCitoyen(Long citoyenId, String keyword, int page, int size);
}