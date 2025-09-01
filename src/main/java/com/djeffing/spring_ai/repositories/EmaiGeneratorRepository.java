package com.djeffing.spring_ai.repositories;

import com.djeffing.spring_ai.models.EmailGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface EmaiGeneratorRepository extends JpaRepository<EmailGenerator, Long> {

    // Trouve par user_id (votre méthode actuelle)
    List<EmailGenerator> findByUserId(long user_id);

    // Trouve par user_id avec pagination
    Page<EmailGenerator> findByUserId(long user_id, Pageable pageable);

    // Trouve par user_id ordonné par date de création
    List<EmailGenerator> findByUserIdOrderByCreatedAtDesc(long user_id);

    // Compte le nombre d'emails pour un utilisateur
    long countByUserId(long user_id);

    // Trouve les plus récents pour un utilisateur
    List<EmailGenerator> findTop10ByUserIdOrderByCreatedAtDesc(long user_id);
}
