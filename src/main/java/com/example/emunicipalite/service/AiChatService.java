package com.example.emunicipalite.service;

import com.example.emunicipalite.entites.ServiceMuni;
import com.example.emunicipalite.repository.ServiceMuniRepository;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AiChatService {

    private final ChatLanguageModel chatModel;
    private final ServiceMuniRepository serviceMuniRepository;


    public AiChatService(ChatLanguageModel chatModel, ServiceMuniRepository serviceMuniRepository) {
        this.chatModel = chatModel;
        this.serviceMuniRepository = serviceMuniRepository;
    }

    public String repondreAuCitoyen(String questionCitoyen) {
        try {

            List<ServiceMuni> services = serviceMuniRepository.findAll();


            StringBuilder contextBaladiya = new StringBuilder("Voici les services disponibles dans notre municipalité :\n");
            for (ServiceMuni s : services) {
                contextBaladiya.append("- Service: ").append(s.getNom())
                        .append(" | Description: ").append(s.getDescription())
                        .append(" | Frais: ").append(s.getFrais()).append(" DT")
                        .append(" | Documents requis: ").append(s.getDocumentsRequis()).append("\n");
            }


            String promptFinal = "Tu es l'assistant de e-Baladiya en Tunisie. Réponds poliment à cette question: "
                    + questionCitoyen
                    + "\nEn te basant uniquement sur ces données réelles de notre base :\n"
                    + contextBaladiya.toString();


            return chatModel.generate(promptFinal);

        } catch (Exception e) {

            return "Note : [Mode Local] L'API OpenAI est indisponible. "
                    + "Veuillez consulter nos services officiels ci-dessous.";
    }
}
}