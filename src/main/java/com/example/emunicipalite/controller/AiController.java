package com.example.emunicipalite.controller;

import com.example.emunicipalite.service.AiChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bot")
@CrossOrigin("*")
public class AiController {

    private final AiChatService aiChatService;

    public AiController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }


    @GetMapping("/page")
    public String afficherChatPage(Model model) {
        model.addAttribute("userQuestion", "");
        model.addAttribute("aiResponse", "Bienvenue sur e-Baladiya ! Je suis votre assistant virtuel. Posez-moi une question.");
        return "citoyen/chatbot";
    }


    @PostMapping("/chat")
    public String chatAvecBaladiya(@RequestParam("question") String question, Model model) {
        try {
            String reponseIA = aiChatService.repondreAuCitoyen(question);
            model.addAttribute("userQuestion", question);
            model.addAttribute("aiResponse", reponseIA);
        } catch (Exception e) {
            model.addAttribute("userQuestion", question);
            model.addAttribute("aiResponse", "Erreur lors de l'appel à l'IA : " + e.getMessage());
        }
        return "citoyen/chatbot";
    }
}