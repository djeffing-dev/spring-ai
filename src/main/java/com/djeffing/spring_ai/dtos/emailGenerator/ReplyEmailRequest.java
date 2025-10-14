package com.djeffing.spring_ai.dtos.emailGenerator;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReplyEmailRequest {
    private String name;
    private String destinataire;
    private String receivedEmail;
    private String replayGoal;
    private String langue;
    private String taille;
    private String style;
    private String ton;
    private String humer;
    private String emoji;
}
