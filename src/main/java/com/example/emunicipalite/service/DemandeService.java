package com.example.emunicipalite.service;

import com.example.emunicipalite.dto.AdminDashboardDTO;
import com.example.emunicipalite.entites.Demande;
import com.example.emunicipalite.entites.ResultatDemande;
import com.example.emunicipalite.repository.DemandeRepository;
import com.example.emunicipalite.repository.ResultatDemandeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DemandeService implements IDemandeService {

    private final DemandeRepository demandeRepository;
    private final ResultatDemandeRepository resultatDemandeRepository;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    @Override
    public void save(Demande demande, MultipartFile image, MultipartFile documentPdf) {

        if (demande.getId() != null) {
            Demande existingDemande = demandeRepository.findById(demande.getId()).orElse(null);
            if (existingDemande != null) {
                if (image == null || image.isEmpty()) {
                    demande.setImage(existingDemande.getImage());
                }
                if (documentPdf == null || documentPdf.isEmpty()) {
                    demande.setDocumentPdf(existingDemande.getDocumentPdf());
                }
            }
        }
        if (image != null && !image.isEmpty()) {
            demande.setImage(uploadFile(image));
        }
        if (documentPdf != null && !documentPdf.isEmpty()) {
            demande.setDocumentPdf(uploadFile(documentPdf));
        }

        demandeRepository.save(demande);
    }
    private String uploadFile(MultipartFile file) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload : " + file.getOriginalFilename());
        }
    }

    @Override
    public Page<Demande> getDemandesByCitoyen(Long id, String keyword, Pageable pageable) {
        return demandeRepository.findByCitoyenId(id, keyword, pageable);
    }

    @Override
    public void delete(Long id) {
        demandeRepository.deleteById(id);
    }

    @Override
    public Demande findById(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
    }


    @Override
    public AdminDashboardDTO getAdminDashboardStats() {
        List<Demande> allDemandes = demandeRepository.findAll();
        long total = allDemandes.size();
        long enAttente = allDemandes.stream()
                .filter(d -> d.getStatut() != null && d.getStatut().equals("En attente"))
                .count();

        long acceptees = allDemandes.stream()
                .filter(d -> d.getStatut() != null && d.getStatut().equals("Acceptée"))
                .count();

        double totalFrais = allDemandes.stream()
                .filter(d -> d.getService() != null)
                .mapToDouble(d -> d.getService().getFrais())
                .sum();

        Map<String, Long> parService = allDemandes.stream()
                .filter(d -> d.getService() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getService().getNom(),
                        Collectors.counting()
                ));

        return new AdminDashboardDTO(total, enAttente, acceptees, totalFrais, parService);
    }

    @Override
    public List<Demande> getLastFiveDemandes() {
        return demandeRepository.findAll().stream()
                .sorted((d1, d2) -> d2.getDateCreation().compareTo(d1.getDateCreation()))
                .limit(5)
                .collect(Collectors.toList());
    }
    @Override
    public Page<Demande> getAllDemandes(String keyword, Pageable pageable) {
        if (keyword != null) return demandeRepository.findByCitoyenNomContaining(keyword, pageable);
        return demandeRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void updateStatut(Long id, String statut) {
        Demande d = demandeRepository.findById(id).orElseThrow();
        d.setStatut(statut);
        demandeRepository.save(d);
    }

    @Override
    @Transactional
    public void saveResultat(Long demandeId, ResultatDemande res, MultipartFile file) {
        Demande d = demandeRepository.findById(demandeId).orElseThrow();
        if (file != null && !file.isEmpty()) {
            String fileName = uploadFile(file);
            res.setFichierPdf(fileName);
        }
        res.setDateEmission(LocalDateTime.now());
        ResultatDemande savedRes = resultatDemandeRepository.save(res);
        d.setResultatDemande(savedRes);
        d.setStatut("Acceptée");
        demandeRepository.save(d);
    }

    @Override
    public List<ResultatDemande> getResultatsParCitoyen(Long citoyenId) {
        return resultatDemandeRepository.findByByCitoyenId(citoyenId);
    }


}
