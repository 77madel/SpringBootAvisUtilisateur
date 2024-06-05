package tech.chillo;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.chillo.entite.Role;
import tech.chillo.entite.Utilisateur;
import tech.chillo.enums.TypeDeRole;
import tech.chillo.repository.UtilisateurRepository;

@AllArgsConstructor
@SpringBootApplication
public class AvisUtilisateursApplication implements CommandLineRunner {

    UtilisateurRepository utilisateurRepository;
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AvisUtilisateursApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Utilisateur admin = Utilisateur.builder()
                .actif(true)
                .nom("admin")
                .mdp(passwordEncoder.encode("admin"))
                .email("admin@admin.com")
                .role(
                        Role.builder()
                                .libelle(TypeDeRole.ADMINISTRATEUR)
                                .build()
                )
                .build();
        admin =  this.utilisateurRepository.findByEmail("admin@admin.com").orElse(admin);
        this.utilisateurRepository.save(admin);

        Utilisateur manager = Utilisateur.builder()
                .actif(true)
                .nom("manager")
                .mdp(passwordEncoder.encode("manager"))
                .email("manager@manager.com")
                .role(
                        Role.builder()
                                .libelle(TypeDeRole.MANAGER)
                                .build()
                )
                .build();
      manager =  this.utilisateurRepository.findByEmail("manager@manager.com").orElse(manager);
        this.utilisateurRepository.save(manager);
    }
}
