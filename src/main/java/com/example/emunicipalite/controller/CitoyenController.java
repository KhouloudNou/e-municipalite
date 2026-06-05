package com.example.emunicipalite.controller;

import com.example.emunicipalite.dto.CitoyenDashboardDTo;
import com.example.emunicipalite.entites.Admin;
import com.example.emunicipalite.entites.Citoyen;
import com.example.emunicipalite.entites.ServiceMuni;
import com.example.emunicipalite.service.IAdminService;
import com.example.emunicipalite.service.IServiceCitoyen;
import com.example.emunicipalite.service.IServiceServiceMuni;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class CitoyenController {
    private final IServiceCitoyen iServiceCitoyen;
    private final IServiceServiceMuni iServiceServiceMuni;
    private IAdminService iAdminService;

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("citoyen", new Citoyen());
        return "citoyen/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Citoyen citoyen, HttpSession session, Model model) {
        if (iServiceCitoyen.existsByEmail(citoyen.getEmail())) {
            model.addAttribute("error", "Email déjà utilisé !");
            return "citoyen/register";
        }
        Citoyen savedCitoyen = iServiceCitoyen.save(citoyen);
        session.setAttribute("citoyenConnecte", savedCitoyen);
        return "redirect:/onboarding";
    }

    @GetMapping("/onboarding")
    public String showOnboarding(HttpSession session, Model model) {
        Citoyen sessionCitoyen = (Citoyen) session.getAttribute("citoyenConnecte");
        if (sessionCitoyen == null) return "redirect:/login";

        List<ServiceMuni> allServices = iServiceServiceMuni.getAllServices();
        model.addAttribute("allServices", allServices);
        return "citoyen/onboarding";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam(required = false) List<Long> serviceIds, HttpSession session) {
        Citoyen sessionCitoyen = (Citoyen) session.getAttribute("citoyenConnecte");
        if (sessionCitoyen == null) return "redirect:/login";

        if (serviceIds != null && !serviceIds.isEmpty()) {
            iServiceCitoyen.ajouterAbonnements(sessionCitoyen.getId(), serviceIds);
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "citoyen/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Optional<Admin> admin = iAdminService.findByEmail(email);

        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            session.setAttribute("adminConnecte", admin.get());
            return "redirect:/admin/dashboard";
        }

        try {
            Citoyen citoyen = iServiceCitoyen.findByEmail(email);
            if (citoyen.getPassword().equals(password)) {
                session.setAttribute("citoyenConnecte", citoyen);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Mot de passe incorrect !");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Email introuvable !");
        }

        return "citoyen/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Citoyen sessionCitoyen = (Citoyen) session.getAttribute("citoyenConnecte");
        if (sessionCitoyen == null) return "redirect:/login";

        CitoyenDashboardDTo dto = iServiceCitoyen.getDashboardStats(sessionCitoyen.getEmail());

        model.addAttribute("citoyen", dto);
        model.addAttribute("nbDemandes", dto.getNbDemandes());
        model.addAttribute("nbAbonnements", dto.getNbAbonnements());
        model.addAttribute("nbDocsPrets", dto.getNbDocsPrets());

        return "citoyen/dashboard";
    }

    @GetMapping("/services")
    public String showAvailableServices(
            HttpSession session,
            Model model,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {

        Citoyen sessionCitoyen = (Citoyen) session.getAttribute("citoyenConnecte");
        if (sessionCitoyen == null) return "redirect:/login";

        Page<ServiceMuni> pageServices = iServiceServiceMuni.getAvailableServicesForCitoyen(
                sessionCitoyen.getId(), keyword, page, size
        );

        model.addAttribute("availableServices", pageServices.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", pageServices.getTotalPages());
        model.addAttribute("pages", new int[pageServices.getTotalPages()]);

        return "citoyen/services-list";
    }

    @PostMapping("/services/subscribe-more")
    public String subscribeMore(@RequestParam(required = false) List<Long> serviceIds, HttpSession session) {
        Citoyen sessionCitoyen = (Citoyen) session.getAttribute("citoyenConnecte");
        if (sessionCitoyen == null) return "redirect:/login";

        if (serviceIds != null && !serviceIds.isEmpty()) {
            iServiceCitoyen.ajouterAbonnements(sessionCitoyen.getId(), serviceIds);
        }

        return "redirect:/dashboard";
    }
}