package tech.chillo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import tech.chillo.entite.Avis;

public interface AvisRepository extends CrudRepository<Avis, Long> {
}
