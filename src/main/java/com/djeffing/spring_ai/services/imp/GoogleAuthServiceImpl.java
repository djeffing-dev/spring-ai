package com.djeffing.spring_ai.services.imp;

import com.djeffing.spring_ai.configs.securities.JwtUtils;
import com.djeffing.spring_ai.dtos.jwt.JwtResponse;
import com.djeffing.spring_ai.dtos.oauth.google.UrlDto;
import com.djeffing.spring_ai.models.Role;
import com.djeffing.spring_ai.models.Tool;
import com.djeffing.spring_ai.models.User;
import com.djeffing.spring_ai.repositories.RoleRepository;
import com.djeffing.spring_ai.repositories.ToolRespository;
import com.djeffing.spring_ai.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoogleAuthServiceImpl {
    private final String clientId;
    private final String clientSecret;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final ToolRespository toolRespository;

    // L'injection se fait directement dans les paramètres du constructeur
    public GoogleAuthServiceImpl(
            @Value("${spring.security.oauth2.resourceserver.opaque-token.clientId}") String clientId,
            @Value("${spring.security.oauth2.resourceserver.opaque-token.clientSecret}") String clientSecret,
            UserRepository userRepository,
            JwtUtils jwtUtils,
            RoleRepository roleRepository,
            ToolRespository toolRespository) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.toolRespository = toolRespository;
    }

    private final NetHttpTransport transport = new NetHttpTransport();
    private final GsonFactory jsonFactory = new GsonFactory();


    // Génération de l'url d'authentification via Google
    public ResponseEntity<UrlDto> createAuthUrl(){
        System.out.println("--------------------> clientId : "+clientId);
        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                "http://localhost:4200/login",
                Arrays.asList("email", "profile", "openid")  // ✅ "openid" en minuscules
        ).build();
        return ResponseEntity.ok(new UrlDto(url));
    }

    // Récupération du token d'authentification
    public ResponseEntity<?> callBack(String code){
        try {
            // ✅ Utiliser les instances de classe
            var tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    transport,      // ✅ Réutiliser l'instance
                    jsonFactory,    // ✅ Réutiliser l'instance
                    clientId,
                    clientSecret,
                    code,
                    "http://localhost:4200/login"
            ).execute();

            String idTokenString = tokenResponse.getIdToken();
            GoogleIdToken idToken = GoogleIdToken.parse(jsonFactory, idTokenString);

            // Vérification de la validité du token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            // ✅ Gérer le cas où la vérification échoue
            if(!verifier.verify(idToken)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid ID token"));
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Extraire les informations utilisateurs
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String profilUrl = (String) payload.get("picture");

            // Trouver ou créer l'utilisateur
            User user = findOrCreateUser(email, name, profilUrl);

            //Enregister le token de connexion à google
            String accesToken = tokenResponse.getAccessToken();
            saveOrUpdateGoogleToken(user.getId(), accesToken);

            // Génération d'un token JWT pour l'utilisateur
            String jwtToken = jwtUtils.generateJwtTokenByUser(user);
            JwtResponse jwtResponse = JwtResponse.builder()
                    .token(jwtToken)
                    .type("Bearer ")
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .profilUrl(user.getProfilUrl())
                    .roles(getRoleNames(user.getRoles()))
                    .build();

            return ResponseEntity.ok(jwtResponse);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Failed to authenticate with Google: " + e.getMessage()));
        } catch (GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token verification failed: " + e.getMessage()));
        }
    }

    // ✅ Éviter la double requête à la base de données
    private User findOrCreateUser(String email, String username, String profilUrl){
        Optional<User> existingUser = userRepository.findByEmail(email);

        if(existingUser.isPresent()){
            return existingUser.get();
        }

        User user = new User();
        user.setActive(true);
        user.setUsername(username);
        user.setProfilUrl(profilUrl);
        user.setEmail(email);

        // ✅ Récupérer le rôle USER depuis la base de données
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rôle USER non trouvé"));

        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
    }

    private void saveOrUpdateGoogleToken (long userId, String assesToken){
        String name  = "Google";
        Optional<Tool> toolOptional = toolRespository.findByUserIdAndName(userId, name);
        if(toolOptional.isPresent()){
            Tool tool = toolOptional.get();
            tool.setAssessToken(assesToken);
            toolRespository.save(tool);
            return;
        }

        Tool tool = Tool.build("Google",userId, assesToken);
        toolRespository.save(tool);
    }

    public List<String> getRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName) // On transforme chaque objet Role en String (son nom)
                .toList();          // On convertit le tout en Liste
    }
}
