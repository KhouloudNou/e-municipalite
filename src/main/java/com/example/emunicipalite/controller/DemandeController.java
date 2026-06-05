package com.example.emunicipalite.controller;

import com.example.emunicipalite.entites.Citoyen;
import com.example.emunicipalite.entites.Demande;
import com.example.emunicipalite.entites.ResultatDemande;
import com.example.emunicipalite.entites.ServiceMuni;
import com.example.emunicipalite.service.IDemandeService;
import com.example.emunicipalite.service.IServiceCitoyen;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class DemandeController {

    private final IDemandeService demandeService;
    private final IServiceCitoyen citoyenService;


    @GetMapping("/nouvelleDemande")
    public String showForm(HttpSession session, Model model) {
        Citoyen citoyen = getCitoyenFromSession(session);
        if (citoyen == null) return "redirect:/login";

        Demande demande = new Demande();
        demande.setService(new ServiceMuni());

        model.addAttribute("demande", demande);
        model.addAttribute("mesServices", getServicesAbonnes(citoyen));

        return "citoyen/form-demande";
    }


    @PostMapping("/saveDemande")
    public String save(
            @Valid @ModelAttribute("demande") Demande demande,
            BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("pdfFile")   MultipartFile pdfFile,
            HttpSession session,
            Model model
    ) {
        Citoyen citoyen = getCitoyenFromSession(session);
        if (citoyen == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("mesServices", getServicesAbonnes(citoyen));
            return "citoyen/form-demande";
        }

        demande.setCitoyen(citoyen);

        if (demande.getId() == null) {
            demande.setDateCreation(LocalDateTime.now());
            demande.setStatut("En attente");
        }

        demandeService.save(demande, imageFile, pdfFile);
        return "redirect:/listeDemandes";
    }



    @GetMapping("/listeDemandes")
    public String listeDemandes(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Citoyen citoyen = getCitoyenFromSession(session);
        if (citoyen == null) return "redirect:/login";

        Page<Demande> pageDemandes = demandeService.getDemandesByCitoyen(
                citoyen.getId(), keyword, PageRequest.of(page, size)
        );

        model.addAttribute("demandes",    pageDemandes.getContent());
        model.addAttribute("totalPages",  pageDemandes.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword",     keyword);

        return "citoyen/Liste-demandes";
    }


    @GetMapping("/deleteDemande/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (getCitoyenFromSession(session) == null) return "redirect:/login";
        demandeService.delete(id);
        return "redirect:/listeDemandes";
    }

    @GetMapping("/editDemande/{id}")
    public String editDemande(@PathVariable Long id, HttpSession session, Model model) {
        Citoyen citoyen = getCitoyenFromSession(session);
        if (citoyen == null) return "redirect:/login";

        Demande demande = demandeService.findById(id);


        if (!demande.getCitoyen().getId().equals(citoyen.getId())) {
            return "redirect:/listeDemandes";
        }

        model.addAttribute("demande", demande);
        model.addAttribute("mesServices", getServicesAbonnes(citoyen));

        return "citoyen/form-demande";
    }

    @GetMapping("/mesResultats")
    public String mesResultats(HttpSession session, Model model) {
        Citoyen citoyen = getCitoyenFromSession(session);
        if (citoyen == null) return "redirect:/login";

        List<ResultatDemande> lesResultats = demandeService.getResultatsParCitoyen(citoyen.getId());

        model.addAttribute("resultats", lesResultats);
        return "citoyen/liste-resultats";
    }


    private Citoyen getCitoyenFromSession(HttpSession session) {
        return (Citoyen) session.getAttribute("citoyenConnecte");
    }

    private List<ServiceMuni> getServicesAbonnes(Citoyen citoyen) {
        return citoyenService.findById(citoyen.getId()).getServicesAbonnes();
    }
}