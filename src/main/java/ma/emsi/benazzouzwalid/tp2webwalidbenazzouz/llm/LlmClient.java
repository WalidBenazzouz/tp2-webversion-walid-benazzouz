package ma.emsi.benazzouzwalid.tp2webwalidbenazzouz.llm;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service d'accès centralisé au modèle de langage Gemini via LangChain4j.
 * Gère la mémoire de chat et le rôle système pour maintenir le contexte.
 */
@ApplicationScoped
public class LlmClient {

    // === Champs principaux ===
    private ChatMemory chatMemory;
    private Assistant assistant;
    private String systemRole;

    // === Constructeur ===

    /**
     * Initialise le client Gemini et configure le modèle conversationnel.
     */
    public LlmClient() {

        // Lecture de la clé API depuis les variables d'environnement
        String apiKey = System.getenv("GEMINI_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Erreur : variable d'environnement GEMINI_KEY absente ou vide."
            );
        }

        // Création du modèle Gemini avec paramètres par défaut
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .build();

        // Mise en place d’une mémoire de conversation glissante (10 messages)
        chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        // Construction du service Assistant à partir du modèle et de la mémoire
        assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
    }

    // === Méthodes publiques ===

    /**
     * Définit un rôle système pour le modèle et réinitialise la mémoire du chat.
     *
     * @param role description du comportement attendu du modèle.
     */
    public void setSystemRole(String role) {
        this.systemRole = role;
        chatMemory.clear();

        if (role != null && !role.trim().isEmpty()) {
            chatMemory.add(SystemMessage.from(role));
        }
    }

    /**
     * Envoie un message au modèle et récupère la réponse générée.
     *
     * @param prompt texte de la question ou instruction.
     * @return réponse textuelle produite par le modèle.
     */
    public String ask(String prompt) {
        return assistant.chat(prompt);
    }

    // === Getters ===

    public String getSystemRole() {
        return systemRole;
    }
}
