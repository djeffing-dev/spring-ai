package com.djeffing.spring_ai.dtos.emailGenerator;

import com.djeffing.spring_ai.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailGeneratorDto {
    private Long id;
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
    private  String content;
    @JsonIgnoreProperties({"password","roles","createdAt","updatedAt","active"})
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "America/Toronto")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "America/Toronto")
    private LocalDateTime updatedAt;
}
