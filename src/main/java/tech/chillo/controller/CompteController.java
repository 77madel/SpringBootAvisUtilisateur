package tech.chillo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.chillo.dto.AuthenticationDTO;
import tech.chillo.entite.Utilisateur;
import tech.chillo.securite.JwtService;
import tech.chillo.service.UtilisateurService;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class CompteController {

    private final UtilisateurService utilisateurService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @PostMapping(path = "/inscription")
    public void inscription(@RequestBody Utilisateur utilisateur){
        log.info("inscription");
        this.utilisateurService.inscription(utilisateur);
    }

    @PostMapping(path = "/activation")
    public void activation(@RequestBody Map<String, String> activation){
        this.utilisateurService.activation(activation);
    }

    @PostMapping(path = "modifier-mot-de-passe")
    public void modifierMotDePasse(@RequestBody Map<String, String> activation) {
        this.utilisateurService.modifierMotDePasse(activation);
    }

    @PostMapping(path = "nouveau-mot-de-passe")
    public void nouveauMotDePasse(@RequestBody Map<String, String> activation) {
        this.utilisateurService.nouveauMotDePasse(activation);
    }

    @PostMapping(path = "refresh-token")
    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> refreshTokenRequest) {
        return this.jwtService.refreshToken(refreshTokenRequest);
    }

    @PostMapping(path = "deconnexion")
    public void deconnexion(){
        this.jwtService.deconnexion();
    }

    @PostMapping(path ="connexion")
    public Map<String, String> connexion(@RequestBody AuthenticationDTO authenticationDTO){
       final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password())
        );
       if (authenticate.isAuthenticated()){
           return this.jwtService.generate(authenticationDTO.username());
       }
       return null;
    }
}
