package com.djeffing.spring_ai.dtos.emailGenerator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailGeneratorRequest {
    private String nom;
    private String destinataire;
    private String objet;
    private String context;
    private String objectif;
    private  String langue;
    private  String taille;
    private String style;
    private String ton;
    private String humer;
    private  String emoji;
}
