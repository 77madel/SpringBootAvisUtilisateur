package tech.chillo.controller.advice;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

//Pour gerer les exceptions
@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody
    ProblemDetail badCredentialsException(final BadCredentialsException exception){
        ApplicationControllerAdvice.log.error(exception.getMessage(), exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(UNAUTHORIZED, "Identifiant Incorret");
        problemDetail.setProperty("erreur", "Nous avons pas pu vous identifier");
        return  problemDetail;
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public @ResponseBody
    ProblemDetail badCredentialsException(final AuthorizationDeniedException exception){
        ApplicationControllerAdvice.log.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(FORBIDDEN, "Vos droits ne vous permettent pas d'effectuer cette action");
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = {RuntimeException.class, ExpiredJwtException.class})
    public @ResponseBody
    ProblemDetail badCredentialsException(final Exception exception){
        ApplicationControllerAdvice.log.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(UNAUTHORIZED, "Token expirer ou Invalid");
//        problemDetail.setProperty("erreur", "Nous avons pas pu vous identifier");
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = Exception.class)
    public Map<String, String> exceptionHandler(){
        return Map.of("erreur", "description");
    }

}
