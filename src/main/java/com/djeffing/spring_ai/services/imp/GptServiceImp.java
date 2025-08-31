package com.djeffing.spring_ai.services.imp;

import com.djeffing.spring_ai.dtos.RoadMapDto;
import com.djeffing.spring_ai.dtos.emailGenerator.EmailGeneratorDto;
import com.djeffing.spring_ai.services.interfaces.GptService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class GptServiceImp implements GptService {

    final  private ChatClient chatClient;

    public GptServiceImp(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }
    @Override
    public Flux<String> getRoadmap(RoadMapDto roadMapDto) {
        String systemPrompt = """
                    Tu es un expert en pédagogie et en planification d’apprentissage, spécialisé dans la création de roadmaps personnalisées pour acquérir des compétences techniques et non techniques.           \s
                    Objectif : À partir d’une liste de compétences et d’un nombre de mois donné, crée une roadmap détaillée pour permettre à un étudiant ou professionnel de maîtriser ces compétences dans le temps imparti.\s
                    Contraintes et format attendu :
                    - Plan global : Présente un aperçu du parcours d’apprentissage.
                    - Découpage en milestones mensuels : Chaque mois doit avoir un objectif clair et mesurable.
                    - Contenus d’apprentissage : Pour chaque milestone, inclure des ressources concrètes :
                      * Livres (avec titre, auteur, et pourquoi il est utile)
                      * Cours en ligne (avec plateforme et lien si possible)
                      * Tutoriels ou articles pratiques
                      * Exercices ou projets à réaliser
                    - Ordre logique : Commencer par les bases, puis progresser vers des concepts avancés.
                    - Approche équilibrée : Prévoir du temps pour théorie, pratique et révisions.
                    - Évaluation : À la fin de chaque milestone, proposer une manière d’auto-évaluer la progression (quiz, projet, démonstration).
                     Format de sortie (Titre du plan, Tableau ou liste des milestones par mois, Détails des ressources et projets pour chaque étape, Conseils de motivation et bonnes pratiques)
                    Génère la roadmap de manière claire, inspirante et orientée vers l’action.
                """;

        String userPrompt = """
            Paramètres fournis : :
            1. Compétences à acquérir : %s
            2. Durée disponible : %s
            """.formatted(roadMapDto.skill(), roadMapDto.nbMonth());


        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .stream()
                .content();
    }

    @Override
    public Flux<String> prompt(List<String> messages) {
        List<Map<String, String>> conversations = buildConversation(messages);
        return evenStream(conversations);
    }

    @Override
    public String emailGenerator(EmailGeneratorDto emailGeneratorDto) {
        String systemPrompt = """
                Tu es un expert en communication écrite et en rédaction professionnelle.
                Ta mission est de rédiger un e-mail clair, structuré et percutant, basé sur les informations fournies par l'utilisateur.
                - Rédige un e-mail complet dans la langue indiquée
                - Le style doit correspondre exactement au ton demandé
                - L’e-mail doit contenir (Une accroche ou introduction claire, Le contexte résumé en 2-3 phrases, Les arguments principaux présentés de façon logique, Une conclusion incitant le destinataire à agir)
                - Utiliser une formulation professionnelle, fluide et naturelle, sans fautes.
                - Ne pas inventer d’informations non fournies
                - Formater avec des paragraphes aérés pour une lecture facile
                """;

        String userPrompt = """
            Paramètres fournis :
            1. Objet du mail : %s
            2. Destinataire: %s
            3. Langue : %s
            4. Context : %s
            5. Ce que je veux obtenir : %s
            6. Ton du mail : %s
            7. Humer : %s
            8. Style d'email: %s
            9. taille: %s
            10. Ajouter des emojies: %s
            11. Signature (nom) : %s
            """.formatted(emailGeneratorDto.getObjet(),
                emailGeneratorDto.getDestinataire(),
                emailGeneratorDto.getLangue(),
                emailGeneratorDto.getContent(),
                emailGeneratorDto.getObjectif(),
                emailGeneratorDto.getTon(),
                emailGeneratorDto.getHumer(),
                emailGeneratorDto.getStyle(),
                emailGeneratorDto.getTaille(),
                (emailGeneratorDto.getEmoji()) ? "Oui" : "Non",
                emailGeneratorDto.getNom());

        var response = Map.of("content",
                Objects.requireNonNull(chatClient.prompt().system(systemPrompt)
                        .user(userPrompt).call().content()));

        /*return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .stream()
                .content();*/
        return response.get("content");
    }

    //Transforme une liste de messages en un format de dictionnaire adapté pour l'API GPT (avec rôles 'user' et 'assistant')
    private List<Map<String, String>> buildConversation(List<String> messages){
        List<Map<String, String>> conversation = new ArrayList<>();
        for (int i=0; i<messages.size(); i++){
            Map<String, String> msg = new HashMap<>();
            msg.put("role",(i%2 == 0) ? "user" : "assistant");
            msg.put("content", messages.get(i));
            conversation.add(msg);
        }
        return conversation;
    }

    //Gère la génération de la réponse en streaming avec l'API GPT, renvoyant chaque morceau de texte au fur et à mesure
    private Flux<String> evenStream(List<Map<String, String>> conversation){
        // Conversion de votre liste de Map en une liste de Message de Spring AI
        List<Message> messages = conversation.stream()
                .map(msgMap -> {
                    String role = msgMap.get("role");
                    String content = msgMap.get("content");
                    if ("user".equals(role)) {
                        return new UserMessage(content);
                    } else if ("assistant".equals(role)) {
                        return new AssistantMessage(content);
                    }
                    // Vous pouvez ajouter d'autres rôles comme 'system' si nécessaire
                    // ...
                    return new UserMessage(content); // Fallback par défaut
                })
                .collect(Collectors.toList());

        return chatClient.prompt(new Prompt(messages))
                .stream()
                .content();
    }
}
