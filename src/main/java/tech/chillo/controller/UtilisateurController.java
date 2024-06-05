package tech.chillo.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.chillo.entite.Utilisateur;
import tech.chillo.service.UtilisateurService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "utilisateur")
public class UtilisateurController {

    private UtilisateurService utilisateurService;
    //Liste Utilisateur
    @PreAuthorize("hasAnyAuthority('ADMINISTRATEUR_READ', 'MANAGER_READ')")
    @GetMapping
    public List<Utilisateur> liste() {
        return this.utilisateurService.liste();
    }
}
