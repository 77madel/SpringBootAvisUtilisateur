package tech.chillo.securite;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.chillo.service.UtilisateurService;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableMethodSecurity
@EnableAutoConfiguration
public class ConfigurationSecuriteApplication {

    private final JwtFilter jwtFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public ConfigurationSecuriteApplication(JwtFilter jwtFilter, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtFilter = jwtFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    //creer de la bean une chaine de securite
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSe, HttpSecurity httpSecurity) throws Exception {
      return
        httpSecurity
                //pour desactiver .csrf
                .csrf(AbstractHttpConfigurer::disable)
                //une autorisation sur les requetes pour inscription
                .authorizeRequests(
                        authorize ->
                                authorize
                                        //Authoriser les routes !!!
                                        .requestMatchers(POST,"/inscription").permitAll()
                                        .requestMatchers(POST,"/activation").permitAll()
                                        .requestMatchers(POST,"/connexion").permitAll()
                                        .requestMatchers(POST,"/refresh-token").permitAll()
                                        .requestMatchers(POST,"/modifier-mot-de-passe").permitAll()
                                        .requestMatchers(POST,"/nouveau-mot-de-passe").permitAll()

                                        .requestMatchers(GET, "/avis/").hasAnyAuthority("ROLE_MANAGER","ROLE_ADMINISTRATEUR")
                                        //Sinon pour les autres requete on doit etre authentifier
                                        .anyRequest().authenticated()

                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    //Fonction qui permet d'authentifier un utilisateur
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}
