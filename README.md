<!--
  Projet réalisé dans le cadre du module de technologies web avancées.
  Responsable pédagogique et encadreur : M. Richard Grin, Professeur à l’Université Côte d’Azur.
-->

# TP2 – Application JSF intégrée à Gemini

## 1. Présentation générale

Ce dépôt correspond au livrable du **TP2 de l’étudiant Walid Benazzouz**, réalisé sous la supervision académique de **M. Richard Grin (Université Côte d’Azur)**. L’objectif est de concevoir une application web Java basée sur Jakarta EE et JSF, offrant une interface de conversation avec un modèle de langage Gemini via la bibliothèque LangChain4j. Le projet met l’accent sur la structuration d’une application d’entreprise (couches présentation / logique / intégration), la gestion d’un état de conversation et l’utilisation de composants PrimeFaces pour l’expérience utilisateur.

## 2. Contexte pédagogique et objectifs

- **Cadre universitaire** : module de développement web avancé (TP noté).
- **Encadrement** : M. Richard Grin, professeur à l’Université Côte d’Azur.
- **Compétences visées** :
  - Compréhension d’une pile Jakarta EE moderne (JSF 3, CDI, Servlets, Maven).
  - Intégration d’une API externe basée sur l’IA générative (Google Gemini) via LangChain4j.
  - Gestion du cycle de vie d’un bean à portée de vue (`@ViewScoped`) et des interactions utilisateur.
  - Mise en œuvre d’une interface riche avec PrimeFaces et JavaScript personnalisé.
- **Livrables attendus** : application opérationnelle, code documenté et réflexion sur les limites / perspectives.

## 3. Architecture logicielle

L’application suit une architecture en couches, schématisée ci-dessous :

```
[Navigateur] ⇄ JSF/PrimeFaces (index.xhtml)
    ↓ ↑  CDI (@Named bb)
Managed Bean (`Bb`) ⇄ Service d’intégration LLM (`LlmClient`)
    ↓        ⇄ LangChain4j ⇄ API Google Gemini (modèle 2.5 Flash)
Mémoire de conversation (MessageWindowChatMemory)
```

- **Couche présentation** : page `index.xhtml` (JSF) + ressources statiques (`mycsslayout.css`, `script.js`).
- **Logique métier** : bean `Bb`, responsable des interactions, des messages Faces et de la construction de l’historique de conversation.
- **Accès IA** : classe `LlmClient`, qui encapsule la configuration LangChain4j/Gemini et gère la mémoire glissante de 10 messages.
- **Infrastructure** : fichier `web.xml` pour la déclaration de la FacesServlet, filtre `CharsetFilter` garantissant l’UTF-8, et fichiers CDI (`beans.xml`).

## 4. Fonctionnalités clés

- **Sélection du rôle système** : liste déroulante de rôles prédéfinis (assistant, traducteur, guide touristique) avec possibilité d’édition manuelle avant le premier appel.
- **Conversation persistante sur la vue** : mémorisation des échanges utilisateur/assistant pendant la session grâce à `StringBuilder` et à la mémoire du LLM.
- **Gestion des notifications** : messages d’erreur JSF en cas de question vide ou de problème d’appel au modèle.
- **Outils de productivité** : boutons JavaScript pour copier question, réponse ou conversation et réinitialiser les zones de texte.
- **Mode debug (en construction)** : interrupteur prévu pour afficher les requêtes/réponses JSON du LLM (`bb.toggleDebug()`), à compléter pour l’exposer dans la vue.

## 5. Dépendances et environnement

- **JDK** : version 11 (définie dans le `pom.xml`).
- **Serveur d’applications** : tout conteneur Jakarta EE 10+ compatible JSF/PrimeFaces (ex. Payara Server 6, WildFly 30, TomEE 9). Les APIs Jakarta sont déclarées en portée `provided`.
- **Bibliothèques majeures** :
  - Jakarta EE (`jakarta.jakartaee-api` 9.1) pour JSF/CDI.
  - PrimeFaces 15.0.5 (classifier `jakarta`).
  - LangChain4j 1.8.0 et connecteur `langchain4j-google-ai-gemini` 1.8.0.
- **Configuration externe** : variable d’environnement `GEMINI_KEY` contenant une clé API Google AI Studio valide. L’instanciation de `LlmClient` échoue explicitement si la clé est absente.

## 6. Mise en route

1. **Cloner le dépôt** puis se positionner dans le répertoire :
   ```bash
   git clone <URL>
   cd TP2-web-walid-benazzouz
   ```
2. **Configurer la clé API** :
   ```bash
   export GEMINI_KEY="votre-cle-google"
   ```
3. **Construire l’archive WAR** :
   ```bash
   mvn clean package
   ```
4. **Déployer** le fichier `target/TP2-web-walid-benazzouz.war` sur votre serveur Jakarta EE (ex. via l’outil d’administration Payara).
5. **Accéder à l’application** : `http://localhost:8080/TP2-web-walid-benazzouz/` (URL à adapter selon le serveur). La page d’accueil `index.xhtml` s’ouvre automatiquement.

## 7. Structure du projet

- `src/main/java/ma/emsi/.../jsf/Bb.java` : bean managé JSF, point d’entrée de la logique UI.
- `src/main/java/ma/emsi/.../llm/LlmClient.java` : encapsulation de LangChain4j, mémoire de chat et rôle système.
- `src/main/java/ma/emsi/.../llm/Assistant.java` : interface pour générer le proxy LangChain4j.
- `src/main/java/ma/emsi/.../jsf/CharsetFilter.java` : filtre enforce UTF-8.
- `src/main/webapp/index.xhtml` : vue principale avec PrimeFaces.
- `src/main/webapp/resources/css/mycsslayout.css` : charte graphique.
- `src/main/webapp/resources/js/script.js` : fonctionnalités JavaScript auxiliaires.
- `src/main/resources/META-INF/beans.xml` & `persistence.xml` : configuration CDI/JPA (anticipation d’évolutions futures).

## 8. Parcours utilisateur détaillé

1. L’utilisateur sélectionne ou saisit un rôle système décrivant le comportement attendu du modèle.
2. Il formule une question ; le bouton « Envoyer » déclenche `bb.envoyer()`.
3. Le bean 
   - valide la présence de la question,
   - envoie le rôle système choisi (lors du premier appel) à `LlmClient.setSystemRole()`,
   - soumet la requête via `LlmClient.ask()`.
4. La réponse est affichée, puis la paire question/réponse est ajoutée au bloc « Conversation ».
5. L’utilisateur peut copier les contenus et relancer un échange ou réinitialiser la session via « Nouveau chat ».

## 9. Qualité, tests et limites

- **Tests automatiques** : non implémentés à ce stade (absence de classes de test). Peuvent être introduits avec JUnit 5 pour vérifier la logique métier (ex. tests unitaires du bean via mocks de `LlmClient`).
- **Couverture fonctionnelle** : le mode debug prévu dans la vue (`bb.texteRequeteJson`, `bb.texteReponseJson`) n’est pas encore alimenté dans `Bb`. Il constitue une piste d’amélioration pour exposer les payloads LangChain4j et faciliter les diagnostics.
- **Robustesse** : gestion d’erreurs basique (message Faces en cas d’exception). Une amélioration recommandée consiste à journaliser les erreurs côté serveur et à différencier les types d’échec (clé manquante, quota dépassé, etc.).
- **Sécurité** : la clé `GEMINI_KEY` ne doit jamais être versionnée. Prévoir un stockage sécurisé (vault, secret manager) en production.

## 10. Perspectives d’évolution

- Ajout d’une persistance des conversations (JPA) afin de capitaliser sur le squelette `persistence.xml` déjà présent.
- Enrichissement du catalogue de rôles système et internationalisation de l’interface.
- Intégration d’un véritable volet « Mode Debug » affichant le JSON des requêtes/réponses, voire un log chronologique.
- Mise en place de tests automatisés et d’une CI pour garantir la non-régression.
- Optimisation de l’expérience utilisateur (progress bar pendant l’appel LLM, historique filtrable, export PDF des conversations).

## 11. Références et bibliographie

- [Documentation Jakarta Faces 4.0](https://jakarta.ee/specifications/faces/4.0/)
- [PrimeFaces 15 – User Guide](https://primefaces.github.io/primefaces/15_0_0/#/)
- [LangChain4j – Getting Started](https://langchain4j.dev/)
- [Google AI Studio – Gemini API](https://ai.google.dev/)

---

_Projet réalisé et présenté par Walid Benazzouz, sous l’encadrement de M. Richard Grin (Université Côte d’Azur)._ 
