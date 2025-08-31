package com.djeffing.spring_ai.configs.securities;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.djeffing.spring_ai.configs.securities.userDetails.UserDetailsImpl;
import com.djeffing.spring_ai.configs.exceptions.InvalidTokenException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Base64;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String secretKey;

    /*@PostConstruct
    protected  void init(){ this.secretKey= Base64.getEncoder().encodeToString(secretKey.getBytes());}*/



    public String generateJwtToken(Authentication authentication){
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000); // 1h

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return JWT.create()
                .withIssuer("spring-ai")
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("id", userPrincipal.getId())
                .withClaim("username", userPrincipal.getUsername())
                .withClaim("email", userPrincipal.getEmail())
                .sign(Algorithm.HMAC256(this.secretKey));

    }

    public String getEmailFromJwtToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("email").asString();
    }

    public boolean validateJwtToken(String auhToken){
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(auhToken);
            return  true;
        } catch (JWTVerificationException e){
            throw  new InvalidTokenException("Invalid JWT -> "+e.getMessage());
        }
    }
}
