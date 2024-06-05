package tech.chillo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.chillo.entite.Avis;
import tech.chillo.service.AvisService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "avis")
public class AvisController {

    private final AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis) {
       this.avisService.creer(avis);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATEUR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Avis> liste() {
       return this.avisService.liste();
    }
}
