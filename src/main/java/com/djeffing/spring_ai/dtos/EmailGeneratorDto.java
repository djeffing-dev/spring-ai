package com.djeffing.spring_ai.dtos;

public record EmailGeneratorDto(
        String objet,
        String destinataire,
        String langue,
        String contexte,
        String objetif,
        String ton,
        String signiature

) {
}
