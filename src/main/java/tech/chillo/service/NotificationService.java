package tech.chillo.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tech.chillo.entite.Validation;

@AllArgsConstructor
@Service
public class NotificationService {
    JavaMailSender javaMailSender;
    public void envoyer(Validation validation){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@gmail.com");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Votre code d'activation");

       String text = String.format("Bonjour %s, <br /> Votre code d'action est %s; A bientot", validation.getUtilisateur().getNom(),validation.getCode());
       message.setText(text);
       javaMailSender.send(message);
    }
}
