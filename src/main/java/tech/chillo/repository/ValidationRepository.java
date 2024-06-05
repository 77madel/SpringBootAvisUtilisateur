package tech.chillo.repository;

import org.springframework.data.repository.CrudRepository;
import tech.chillo.entite.Validation;

import java.time.Instant;
import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Long> {

    Optional<Validation> findByCode(String code);
    void deleteAllByExpirationBefore(Instant now);
}
