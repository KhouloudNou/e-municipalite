package com.example.emunicipalite.service;

import com.example.emunicipalite.entites.Admin;

import java.util.Optional;

public interface IAdminService {
    public Optional<Admin> findByEmail(String email);
}
