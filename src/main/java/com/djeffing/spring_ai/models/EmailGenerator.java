package com.djeffing.spring_ai.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Email_generator")
public class EmailGenerator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

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
    @Column(columnDefinition = "TEXT") // ✅ Utilise TEXT pour du contenu long
    private  String content;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;
}
