package com.example.emunicipalite.controller;

import com.example.emunicipalite.dto.AdminDashboardDTO;
import com.example.emunicipalite.entites.Admin;
import com.example.emunicipalite.service.IDemandeService;
import com.example.emunicipalite.service.IServiceServiceMuni;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final IDemandeService demandeService;


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminConnecte");
        if (admin == null) return "redirect:/login";

        AdminDashboardDTO stats = demandeService.getAdminDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("lastDemandes", demandeService.getLastFiveDemandes());

        return "admin/dashboard";
    }
}