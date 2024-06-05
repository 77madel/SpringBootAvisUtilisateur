package tech.chillo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.chillo.entite.Avis;
import tech.chillo.entite.Utilisateur;
import tech.chillo.repository.AvisRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class AvisService {

    private AvisRepository avisRepository;

    public void creer(Avis avis) {
        //Pour savoir quel utilisateur est connecter
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUtilisateur(utilisateur);
        this.avisRepository.save(avis);
    }

    public List<Avis> liste() {
        return (List<Avis>) this.avisRepository.findAll();
    }
}
