package tech.chillo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.chillo.enums.TypeDeRole;
import tech.chillo.entite.Role;
import tech.chillo.entite.Utilisateur;
import tech.chillo.entite.Validation;
import tech.chillo.repository.UtilisateurRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {
    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;
    public void inscription(Utilisateur utilisateur){
        if (!utilisateur.getEmail().contains("@")){
            throw new RuntimeException("Votre email est incorrect.");
        }

        if (!utilisateur.getEmail().contains(".")){
            throw new RuntimeException("Votre email est incorrect.");
        }
        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (utilisateurOptional.isPresent()){
            throw new RuntimeException("Votre email est deja utiliser.");
        }

        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.UTILISATEUR);
        if (utilisateur.getRole() != null && utilisateur.getRole().getLibelle().equals(TypeDeRole.ADMINISTRATEUR)){
            roleUtilisateur.setLibelle(TypeDeRole.ADMINISTRATEUR);
            utilisateur.setActif(true);
        }
        utilisateur.setRole(roleUtilisateur);

        String mpdCrypte = this.passwordEncoder.encode(utilisateur.getMdp());
        utilisateur.setMdp(mpdCrypte);
        utilisateur = this.utilisateurRepository.save(utilisateur);
        if (utilisateur.getRole() != null && utilisateur.getRole().getLibelle().equals(TypeDeRole.ADMINISTRATEUR)){
            roleUtilisateur.setLibelle(TypeDeRole.ADMINISTRATEUR);
        }
        if (roleUtilisateur.getLibelle().equals(TypeDeRole.ADMINISTRATEUR)){
            this.validationService.enregistrer(utilisateur);
        }

    }

    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("Votre code est expirer.");
        }
        Utilisateur utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(() -> new RuntimeException("Utilisateur inconnue."));
        utilisateurActiver.setActif(true);
        this.utilisateurRepository.save(utilisateurActiver);
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun Utilisateur ne correspond a cet identifiant."));
    }

    public void modifierMotDePasse(Map<String, String> parametres) {
        Utilisateur utilisateur = this.loadUserByUsername(parametres.get("email"));
        this.validationService.enregistrer(utilisateur);
    }

    public void nouveauMotDePasse(Map<String, String> parametres) {
        Utilisateur utilisateur = this.loadUserByUsername(parametres.get("email"));
        final Validation validation = validationService.lireEnFonctionDuCode(parametres.get("code"));
        if(validation.getUtilisateur().getEmail().equals(utilisateur.getEmail())) {
            String mdpCrypte = this.passwordEncoder.encode(parametres.get("password"));
            utilisateur.setMdp(mdpCrypte);
            this.utilisateurRepository.save(utilisateur);
        }
    }

    public List<Utilisateur> liste() {
        final Iterable<Utilisateur> utilisateurIterable = this.utilisateurRepository.findAll();
        List utilisateurs = new ArrayList();
        for (Utilisateur utilisateur: utilisateurIterable){
            utilisateurs.add(utilisateur);
        }
        return utilisateurs;
    }
}
