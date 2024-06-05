package tech.chillo.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.chillo.entite.Jwt;
import tech.chillo.entite.RefreshToken;
import tech.chillo.entite.Utilisateur;
import tech.chillo.repository.JwtRepository;
import tech.chillo.service.UtilisateurService;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {
    public static final String REFRESH = "refresh";
    public static final String TOKEN_INVALIDE = "Token invalide";
    private final String ENCRIPTION_KEY = "23be894a770c0744677d896639bcca14c7adc2cfba82efdef4568aca91fea36c";
    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;

    public Jwt tokenByValue(String value) {
       return this.jwtRepository.findByValeurAndDesactiveAndExpire(value, false,false)
               .orElseThrow(() -> new RuntimeException("Token invalide ou inconnu"));
    }

    public Map<String, String> generate(String username){
        Utilisateur utilisateur = this.utilisateurService.loadUserByUsername(username);
       final Map<String, String> jwtMap = new java.util.HashMap<>(this.generateJwt(utilisateur));
        RefreshToken refreshToken = RefreshToken.builder()
                .valeur(UUID.randomUUID().toString())
                .expire(false)
                .creation(Instant.now())
                .expiration(Instant.now().plusMillis(30 * 60 * 1000))
                .build();
        final Jwt jwt = Jwt
                .builder()
                .valeur(jwtMap.get("bearer"))
                .desactive(false)
                .expire(false)
                .utilisateur(utilisateur)
                .refreshToken(refreshToken)
                .build();
        this.jwtRepository.save(jwt);
        jwtMap.put(REFRESH,  refreshToken.getValeur());
        return jwtMap;
    }

    private void disableTokens(Utilisateur utilisateur){
        final List<Jwt> jwtList = this.jwtRepository.findUtilisateur(utilisateur.getEmail()).peek(
                jwt -> {
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());

        this.jwtRepository.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);;
        return expirationDate.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> function){
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 60 * 1000;

        Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, utilisateur.getEmail()
        );


       final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return Map.of("bearer", bearer);
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }


    public void deconnexion() {
       Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Jwt jwt = this.jwtRepository.findUtilisateurValidToken(
               utilisateur.getEmail(),
               false,
               false
       ).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));
       jwt.setExpire(true);
       jwt.setDesactive(true);
       this.jwtRepository.save(jwt);
    }

   // @Scheduled(cron = "@daily")
   //@Scheduled(cron = "0 */1 * * * *")
   @Scheduled(cron = "0 * * * *")
    public void removeUselessJwt() {
        log.info("Suppression des token à {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactive(true, true);
    }

    public Map<String, String> refreshToken(Map<String, String> refreshTokenRequest) {
        final Jwt jwt = this.jwtRepository.findByRefreshToken(refreshTokenRequest.get(REFRESH)).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));
        if(jwt.getRefreshToken().isExpire() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())) {
            throw new RuntimeException(TOKEN_INVALIDE);
        }
        this.disableTokens(jwt.getUtilisateur());
        return this.generate(jwt.getUtilisateur().getEmail());
    }
}
