package com.djeffing.spring_ai.services.imp;

import com.djeffing.spring_ai.configs.exceptions.ErrorException;
import com.djeffing.spring_ai.configs.securities.userDetails.UserDetailsImpl;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorRequest;
import com.djeffing.spring_ai.mappers.emailGenerator.EmailGeneratorMapper;
import com.djeffing.spring_ai.models.EmailGenerator;
import com.djeffing.spring_ai.models.User;
import com.djeffing.spring_ai.repositories.EmaiGeneratorRepository;
import com.djeffing.spring_ai.services.interfaces.EmaiGeneratorService;
import com.djeffing.spring_ai.services.interfaces.GptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmaiGeneratorServiceImp implements EmaiGeneratorService {
    final private EmaiGeneratorRepository emaiGeneratorRepository;
    final  private EmailGeneratorMapper emailGeneratorMapper;
    final  private GptService gptService;



    public EmaiGeneratorServiceImp(EmaiGeneratorRepository emaiGeneratorRepository,
                                   EmailGeneratorMapper emailGeneratorMapper,
                                   GptService gptService
    ) {
        this.emaiGeneratorRepository = emaiGeneratorRepository;
        this.emailGeneratorMapper = emailGeneratorMapper;
        this.gptService = gptService;
    }

    private  EmailGeneratorDto save(EmailGeneratorDto emailGeneratorDto){
        EmailGenerator emailGenerator = emailGeneratorMapper.toEmailGenerator(emailGeneratorDto);
        EmailGenerator emailGeneratorSaved = emaiGeneratorRepository.save(emailGenerator);
        return emailGeneratorMapper.toEmailGeneratorDto(emailGeneratorSaved);
    }

    // Fonction pour obtenir les informations sur l'utilisateur authentifier
    private UserDetailsImpl auhtenticateUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return  (UserDetailsImpl) auth.getPrincipal();
    }



    // Modifiez la méthode generateEmailAndSave
    private String generateEmailAndSave(EmailGeneratorDto emailGeneratorDto){
        StringBuilder contentBuilder = new StringBuilder();

        // Étape 1 : Bloque la génération de l'email et récupère le contenu complet
        String fullContent = gptService.emailGenerator(emailGeneratorDto);

        // Étape 2 : Sauvegarde l'email complet de manière synchrone
        emailGeneratorDto.setContent(fullContent);
        try {
            save(emailGeneratorDto);
        } catch (Exception e) {
            System.err.println("❌ Erreur de sauvegarde bloquante -> " + e.getMessage());
            return null; // Renvoie une erreur si la sauvegarde échoue
        }

        // Étape 3 : Une fois la sauvegarde réussie, renvoie le contenu sous forme de Flux
        return fullContent;
    }


    @Override
    public String create(EmailGeneratorRequest emailGeneratorRequest) {
        UserDetailsImpl userDetails  = auhtenticateUser();

        EmailGeneratorDto emailGeneratorDto = emailGeneratorMapper
                .emailGeneratorRequestToEmailGeneratorDto(emailGeneratorRequest);

        // ✅ Récupération de l'ID utilisateur depuis le token
        emailGeneratorDto.setUser(new User(userDetails.getId()));
        return generateEmailAndSave(emailGeneratorDto);
    }

    @Override
    public  String freeCreate(EmailGeneratorRequest emailGeneratorRequest){
        EmailGeneratorDto emailGeneratorDto = emailGeneratorMapper
                .emailGeneratorRequestToEmailGeneratorDto(emailGeneratorRequest);
        return gptService.emailGenerator(emailGeneratorDto);
    }


    @Override
    public List<EmailGeneratorDto> findAll() {
        return emaiGeneratorRepository.findAll().stream()
                .map(emailGeneratorMapper::toEmailGeneratorDto)
                .collect(Collectors.toList());
    }

    // UPDATE EMAIL
    @Override
    public String update(long emailGenerator_id, EmailGeneratorRequest emailGeneratorRequest) {
        EmailGeneratorDto emailGeneratorDto = emailGeneratorMapper
                .emailGeneratorRequestToEmailGeneratorDto(emailGeneratorRequest);

        EmailGeneratorDto existingDto = findById(emailGenerator_id);
        UserDetailsImpl userDetails  = auhtenticateUser();

        if(!Objects.equals(userDetails.getId(), existingDto.getUser().getId())){
            System.err.println("Vous n'etes pas autorisez a modifier cette donnée");
            throw new ErrorException("Vous n'etes pas autorisez a modifier cette donnée");
        }

        // ✅ La bonne façon de faire un "patch" :
        // Copie seulement les propriétés non-nulles soumises dans la requête
        if (emailGeneratorDto.getLangue() != null) {
            existingDto.setLangue(emailGeneratorDto.getLangue());
        }
        if (emailGeneratorDto.getTaille() != null) {
            existingDto.setTaille(emailGeneratorDto.getTaille());
        }
        if (emailGeneratorDto.getStyle() != null) {
            existingDto.setStyle(emailGeneratorDto.getStyle());
        }
        if (emailGeneratorDto.getHumer() != null) {
            existingDto.setHumer(emailGeneratorDto.getHumer());
        }
        if (emailGeneratorDto.getTon() != null) {
            existingDto.setTon(emailGeneratorDto.getTon());
        }
        if (emailGeneratorDto.getEmoji() != null) {
            existingDto.setEmoji(emailGeneratorDto.getEmoji());
        }
        if (emailGeneratorDto.getNom() != null) {
            existingDto.setNom(emailGeneratorDto.getNom());
        }
        if (emailGeneratorDto.getDestinataire() != null) {
            existingDto.setDestinataire(emailGeneratorDto.getDestinataire());
        }
        if (emailGeneratorDto.getObjet() != null) {
            existingDto.setObjet(emailGeneratorDto.getObjet());
        }
        if (emailGeneratorDto.getContent() != null) {
            existingDto.setContent(emailGeneratorDto.getContent());
        }
        if (emailGeneratorDto.getObjectif() != null) {
            existingDto.setObjectif(emailGeneratorDto.getObjectif());
        }
        if (emailGeneratorDto.getContext() != null) {
            existingDto.setContext(emailGeneratorDto.getContext());
        }

        return generateEmailAndSave(existingDto);
    }

    @Override
    public EmailGeneratorDto findById(long emailGenerator_id){
        return emaiGeneratorRepository.findById(emailGenerator_id)
                .map(emailGeneratorMapper::toEmailGeneratorDto)
                .orElseThrow(() -> new ErrorException("L'email avec l'ID " + emailGenerator_id + " est introuvable"));
    }

    @Override
    public List<EmailGeneratorDto> findByUserId(Long user_id) {
        return emaiGeneratorRepository.findByUserIdOrderByCreatedAtDesc(user_id)
                .stream().map(emailGeneratorMapper::toEmailGeneratorDto)
                .collect(Collectors.toList());
    }

    // Retoune la liste des emails d'un utilisateur authentifier
    @Override
    public List<EmailGeneratorDto> findByUserToken() {
        UserDetailsImpl userDetails = auhtenticateUser();
        return findByUserId(userDetails.getId());
    }

    @Override
    public ResponseEntity<?> deleteById(Long emailGenerator_id) {
        EmailGeneratorDto existingDto = findById(emailGenerator_id);

        UserDetailsImpl userDetails  = auhtenticateUser();
        if(!Objects.equals(userDetails.getId(), existingDto.getUser().getId())){
            System.err.println("Vous n'etes pas autorisez a modifier cette donnée");
            throw new ErrorException("Vous n'etes pas autorisez a modifier cette donnée");
        }

        emaiGeneratorRepository.deleteById(existingDto.getId());
        return ResponseEntity.ok(Map.of("message","Votre mail à été supprimer avec success"));
    }
}
