package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorRequest;
import com.djeffing.spring_ai.services.interfaces.EmaiGeneratorService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/emailGenrator")
@Validated
@Tag(name = "Email Generator", description = "API pour la génération et gestion d'emails automatisés")
public class EmailGeneratorController {

    final private EmaiGeneratorService emaiGeneratorService;

    public EmailGeneratorController(EmaiGeneratorService emaiGeneratorService) {
        this.emaiGeneratorService = emaiGeneratorService;
    }

    @Operation(
            summary = "Générer un nouvel email",
            description = "Génère un email personnalisé basé sur les paramètres fournis (contexte, objectif, style, etc.). " +
                    "L'email est automatiquement sauvegardé et associé à l'utilisateur connecté.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Email généré avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé - Token JWT manquant ou invalide"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Données d'entrée invalides"
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String create(@RequestBody EmailGeneratorRequest emailGeneratorRequest){
        return emaiGeneratorService.create(emailGeneratorRequest);
    }

    @Operation(
            summary = "Lister tous les emails (Admin uniquement)",
            description = "Récupère la liste complète de tous les emails générés par tous les utilisateurs. " +
                    "Accès restreint aux administrateurs uniquement.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Liste des emails récupérée avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé - Token JWT manquant ou invalide"
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Hidden // Masqué dans la documentation publique
    public List<EmailGeneratorDto> findAll(){
        return emaiGeneratorService.findAll();
    }

    @Operation(
            summary = "Modifier un email existant",
            description = "Met à jour un email existant avec de nouveaux paramètres et regénère le contenu. " +
                    "L'utilisateur ne peut modifier que ses propres emails (sauf admin).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Email modifié et regénéré avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé - Token JWT manquant ou invalide"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Email non trouvé ou accès refusé"
                    )
            }
    )
    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String update( @RequestParam long id, @RequestBody EmailGeneratorRequest emailGeneratorRequest){
        return emaiGeneratorService.update(id, emailGeneratorRequest);
    }

    @Operation(
            summary = "Lister les emails d'un utilisateur spécifique (Admin)",
            description = "Récupère tous les emails générés par un utilisateur spécifique via son ID. " +
                    "Accès restreint aux administrateurs pour des fins de modération/support.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Liste des emails de l'utilisateur récupérée"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé - Token JWT manquant ou invalide"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Accès refusé - Privilèges administrateur requis"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Utilisateur non trouvé"
                    )
            }
    )
    @GetMapping("/findByUserId")
    @PreAuthorize("hasRole('ADMIN')")
    @Hidden
    public List<EmailGeneratorDto> findByUserId(  @RequestParam Long id){
        return emaiGeneratorService.findByUserId(id);
    }

    @Operation(
            summary = "Lister mes emails (utilisateur connecté)",
            description = "Récupère tous les emails générés par l'utilisateur actuellement connecté. " +
                    "L'identification se fait automatiquement via le token JWT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Liste de vos emails récupérée avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé - Token JWT manquant ou invalide"
                    )
            }
    )
    @GetMapping("/findByUserToken")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<EmailGeneratorDto> findByUserToken(){
        return emaiGeneratorService.findByUserToken();
    }

    @Operation(
            summary = "Récupérer un email par son ID",
            description = "Récupère les détails complets d'un email spécifique via son ID unique. " +
                    "Les utilisateurs ne peuvent accéder qu'à leurs propres emails (sauf admin).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Email trouvé et récupéré avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé - Token JWT manquant ou invalide"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Email non trouvé ou accès refusé"
                    )
            }
    )
    @GetMapping("/findById")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public EmailGeneratorDto findById(@RequestParam long id
    ){
        return emaiGeneratorService.findById(id);
    }

    @Operation(
            summary = "Supprimer un email",
            description = "Supprime définitivement un email de la base de données. " +
                    "Les utilisateurs ne peuvent supprimer que leurs propres emails (sauf admin). " +
                    "Cette action est irréversible.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Email supprimé avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé - Token JWT manquant ou invalide"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Email non trouvé ou accès refusé"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Impossible de supprimer - Conflits de dépendances"
                    )
            }
    )
    @DeleteMapping("/deleteById")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(@RequestParam Long id
    ){
        return emaiGeneratorService.deleteById(id);
    }
}