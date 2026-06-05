package com.example.emunicipalite.controller;

import com.example.emunicipalite.entites.Demande;
import com.example.emunicipalite.entites.ResultatDemande;
import com.example.emunicipalite.service.IDemandeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/demandes")
public class AdminDemandeController {

    private final IDemandeService demandeService;

    @GetMapping("/liste")
    public String liste(Model model,
                        @RequestParam(name="page", defaultValue="0") int p,
                        @RequestParam(name="keyword", defaultValue="") String kw) {
        Page<Demande> pageDemandes = demandeService.getAllDemandes(kw, PageRequest.of(p, 5));
        model.addAttribute("demandes", pageDemandes.getContent());
        model.addAttribute("pages", new int[pageDemandes.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("keyword", kw);
        return "admin/liste-demandes";
    }

    @GetMapping("/traiter/{id}")
    public String passerEnCours(@PathVariable Long id) {
        demandeService.updateStatut(id, "En Cours");
        return "redirect:/admin/demandes/liste";
    }

    @GetMapping("/form-resultat/{id}")
    public String formResultat(@PathVariable Long id, Model model) {
        Demande d = demandeService.findById(id);
        model.addAttribute("demande", d);
        model.addAttribute("resultat", new ResultatDemande());
        return "admin/form-resultat";
    }

    @PostMapping("/save-resultat")
    public String saveResultat(@RequestParam Long demandeId,
                               @ModelAttribute ResultatDemande resultat,
                               @RequestParam("file") MultipartFile file) {

        demandeService.saveResultat(demandeId, resultat, file);
        return "redirect:/admin/demandes/liste";
    }
}