package tech.chillo.securite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tech.chillo.entite.Jwt;
import tech.chillo.service.UtilisateurService;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService,HandlerExceptionResolver handlerExceptionResolver) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Jwt tokenDansLaBDD = null;
        String username = null;
        boolean isTokenExpired = true;
        try {
            //Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im1hZG91QGdtYWlsLmNvbSIsIm5vbSI6Ik1hZG91IEtPTkUifQ.pr9Vp6yPseKKHcceZuckzulfwPjeU9GYa_nWYJXNw9w
           final String authorization = request.getHeader("Authorization");
           if (authorization != null && authorization.startsWith("Bearer ")) {
               token = authorization.substring(7);
               tokenDansLaBDD = this.jwtService.tokenByValue(token);
               isTokenExpired = jwtService.isTokenExpired(token);
               username = jwtService.extractUsername(token);
           }
            if (
                    !isTokenExpired
                            && tokenDansLaBDD.getUtilisateur().getEmail().equals(username)
                            && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = utilisateurService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
