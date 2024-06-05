package tech.chillo.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.chillo.entite.Utilisateur;
import tech.chillo.entite.Validation;
import tech.chillo.repository.ValidationRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Transactional
@Slf4j
@AllArgsConstructor
@Service
public class ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;
    public void enregistrer(Utilisateur utilisateur) {
        Validation validation =new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);
        validation.setExpiration(expiration);
        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }

    public Validation lireEnFonctionDuCode(String code) {
     return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code est invalide"));
     }

     @Scheduled(cron = "*/10 * * * * *")
     public void nettoyerTable(){
         Instant instant = Instant.now();
         log.info("Supprission des token {}", instant);
        this.validationRepository.deleteAllByExpirationBefore(instant);
     }
}
