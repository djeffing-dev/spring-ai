package com.djeffing.spring_ai.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "codes")
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(nullable = false, unique = true)
    private String value;
    @ManyToOne
    @JoinColumn(nullable = false, name="user_id")
    private User user;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    @Column(nullable = false)
    private String type; // Ex: "EMAIL_VERIFICATION", "PASSWORD_RESET"
    @Column(nullable = false)
    private boolean isUsed = false; // Valeur par défaut à false

    // --- Méthode utilitaire pour vérifier l'expiration ---
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;
}
