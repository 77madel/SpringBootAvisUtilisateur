package tech.chillo.repository;

import org.springframework.data.repository.CrudRepository;
import tech.chillo.entite.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
}
