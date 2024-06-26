package tech.chillo.entite;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh-token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean expire;
    private String valeur;
    private Instant creation;
    private Instant expiration;

}
