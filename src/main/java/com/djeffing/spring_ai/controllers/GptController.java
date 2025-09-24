package com.djeffing.spring_ai.controllers;

import com.djeffing.spring_ai.configs.rateLimit.RateLimit;
import com.djeffing.spring_ai.dtos.RoadMapDto;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorRequest;
import com.djeffing.spring_ai.mappers.emailGenerator.EmailGeneratorMapper;
import com.djeffing.spring_ai.services.interfaces.GptService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.List;

@RestController
@RequestMapping("api/gpt/")
@Tag(
        name = "GPT Generator",
        description = "API publique permettant de générer du contenu avec GPT (emails, roadmaps, prompts). " +
                "Chaque utilisateur (même non inscrit) est limité à 4 requêtes par jour pour éviter les abus."
)
public class GptController {
    private final GptService gptService;
    private final EmailGeneratorMapper emailGeneratorMapper;

    public GptController(GptService gptService, EmailGeneratorMapper emailGeneratorMapper) {
        this.gptService = gptService;
        this.emailGeneratorMapper = emailGeneratorMapper;
    }

    @PostMapping("/getRoadmap")
    @Hidden
    public Flux<String> getRoadmap(@RequestBody RoadMapDto roadMapDto){
        return gptService.getRoadmap(roadMapDto);
    }

    @Operation(
            summary = "Générer un email personnalisé (4/jour, gratuit, sans compte)",
            description = "Permet de générer un email basé sur des paramètres fournis (contexte, objectif, style, etc.). " +
                    "L'email est généré automatiquement sans connexion. " +
                    "Chaque utilisateur a droit à **4 emails par jour**.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Email généré avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Données d'entrée invalides"
                    ),
                    @ApiResponse(
                            responseCode = "429",
                            description = "Quota de requêtes dépassé (limite de 4/jour atteinte)"
                    )
            }
    )
    @PostMapping("/emailGenerator")
    @RateLimit(limit = 4, duration = 86400) // 4 requêtes / 24h
    public String emailGenerator(@RequestBody EmailGeneratorRequest emailGeneratorRequest){
        EmailGeneratorDto emailGeneratorDto = emailGeneratorMapper
                .emailGeneratorRequestToEmailGeneratorDto(emailGeneratorRequest);
        return gptService.emailGenerator(emailGeneratorDto);
    }


    @PostMapping("/emailGenerator-admin-free")
   // @RateLimit(limit = 4, duration = 86400) // 4 requêtes / 24h
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Hidden
    public String emailGeneratorForFreeAdmin(@RequestBody EmailGeneratorRequest emailGeneratorRequest){
        EmailGeneratorDto emailGeneratorDto = emailGeneratorMapper
                .emailGeneratorRequestToEmailGeneratorDto(emailGeneratorRequest);
        return gptService.emailGenerator(emailGeneratorDto);
    }

    @Operation(
            summary = "Envoyer un prompt personnalisé à GPT (4/jour, gratuit, sans compte)",
            description = "Permet d'envoyer une liste de messages (prompt) directement à GPT et de récupérer la réponse. " +
                    "Idéal pour tester librement l'API sans avoir besoin de créer un compte. " +
                    "Chaque utilisateur a droit à **4 requêtes par jour**.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Réponse générée avec succès"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Données d'entrée invalides"
                    ),
                    @ApiResponse(
                            responseCode = "429",
                            description = "Quota de requêtes dépassé (limite de 4/jour atteinte)"
                    )
            }
    )
    @PostMapping("/prompt")
    @RateLimit(limit = 4, duration = 86400) // 4 requêtes / 24h
    public String prompt(@RequestBody List<String> messages){
        return gptService.prompt(messages);
    }
}