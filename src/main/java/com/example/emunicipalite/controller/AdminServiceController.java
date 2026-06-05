package com.example.emunicipalite.controller;

import com.example.emunicipalite.entites.ServiceMuni;
import com.example.emunicipalite.service.IServiceServiceMuni;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@AllArgsConstructor
@RequestMapping("/admin/services")
public class AdminServiceController {

    private final IServiceServiceMuni serviceMuniService;


    @GetMapping("/liste")
    public String liste(Model model,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size) {

        Page<ServiceMuni> servicePage = serviceMuniService.searchServices(keyword, page, size);

        model.addAttribute("services", servicePage.getContent());
        model.addAttribute("servicePage", servicePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", servicePage.getTotalPages());

        return "admin/services-liste";
    }

    @GetMapping("/nouveau")
    public String formNouveau(Model model) {
        model.addAttribute("serviceMuni", new ServiceMuni());
        return "admin/form-service";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("serviceMuni") ServiceMuni service,
                       BindingResult br, Model model) {
        if (br.hasErrors()) {
            return "admin/form-service";
        }
        serviceMuniService.save(service);
        return "redirect:/admin/services/liste";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("serviceMuni", serviceMuniService.findById(id));
        return "admin/form-service";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        serviceMuniService.deleteById(id);
        return "redirect:/admin/services/liste";
    }
}