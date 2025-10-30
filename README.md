# 🎓 TP2 – Application JSF intégrée à Gemini

> **Projet académique** réalisé dans le cadre du module de technologies web avancées  
> **Université Côte d'Azur** • Encadrement : Prof. Richard Grin

<div align="center">

![Java](https://img.shields.io/badge/Java-11-orange?style=flat-square&logo=java)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-9.1-blue?style=flat-square)
![JSF](https://img.shields.io/badge/JSF-3.0-green?style=flat-square)
![PrimeFaces](https://img.shields.io/badge/PrimeFaces-15.0.5-yellow?style=flat-square)
![LangChain4j](https://img.shields.io/badge/LangChain4j-1.8.0-purple?style=flat-square)

</div>

---

## 📋 Table des matières

- [Présentation](#-présentation)
- [Architecture](#-architecture)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies](#-technologies)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Structure du projet](#-structure-du-projet)
- [Évolutions futures](#-évolutions-futures)
- [Auteur](#-auteur)

---

## 🎯 Présentation

### Contexte académique

Ce projet constitue le **livrable du TP2** de l'étudiant **Walid Benazzouz**, réalisé sous la supervision de **M. Richard Grin**, professeur à l'Université Côte d'Azur.

### Objectif

Développer une application web Java basée sur **Jakarta EE** et **JSF**, offrant une interface de conversation interactive avec le modèle **Google Gemini** via la bibliothèque **LangChain4j**.

### Compétences développées

- ✅ Maîtrise de la pile Jakarta EE (JSF 3, CDI, Servlets, Maven)
- ✅ Intégration d'une API d'IA générative (Google Gemini)
- ✅ Gestion du cycle de vie des beans JSF (`@ViewScoped`)
- ✅ Interface utilisateur riche avec PrimeFaces
- ✅ Architecture en couches et bonnes pratiques

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    NAVIGATEUR                           │
│              JSF/PrimeFaces (index.xhtml)               │
└─────────────────────┬───────────────────────────────────┘
                      │ CDI (@Named)
┌─────────────────────▼───────────────────────────────────┐
│              MANAGED BEAN (Bb)                          │
│         • Gestion interactions utilisateur              │
│         • Construction historique conversation          │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│          SERVICE D'INTÉGRATION (LlmClient)              │
│         • Configuration LangChain4j                     │
│         • Mémoire glissante (10 messages)               │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                 API GOOGLE GEMINI                       │
│              (Modèle Gemini 2.5 Flash)                  │
└─────────────────────────────────────────────────────────┘
```

### Couches de l'application

| Couche | Composants | Responsabilité |
|--------|-----------|----------------|
| **Présentation** | `index.xhtml`, CSS, JS | Interface utilisateur et interactions |
| **Logique métier** | `Bb.java` | Orchestration des flux et gestion de l'état |
| **Intégration IA** | `LlmClient.java`, `Assistant.java` | Communication avec Gemini via LangChain4j |
| **Infrastructure** | `web.xml`, `CharsetFilter.java` | Configuration serveur et filtres |

---

## ✨ Fonctionnalités

### 🎭 Sélection de rôle système
- Liste déroulante avec rôles prédéfinis (assistant, traducteur, guide touristique)
- Édition manuelle possible avant le premier échange
- Personnalisation du comportement de l'IA

### 💬 Conversation persistante
- Mémorisation des échanges sur toute la session
- Historique affiché en temps réel
- Contexte conservé entre les messages

### 🔔 Gestion des notifications
- Messages d'erreur JSF pour les champs vides
- Alertes en cas de problème d'appel au modèle
- Retours visuels clairs à l'utilisateur

### 🛠️ Outils de productivité
- **Copier** : question, réponse ou conversation complète
- **Réinitialiser** : nouveau chat avec effacement de l'historique
- **Mode debug** : affichage JSON (en développement)

---

## 🔧 Technologies

### Backend
- **Java 11**
- **Jakarta EE 9.1** (JSF, CDI, Servlets)
- **LangChain4j 1.8.0** + connecteur Google AI Gemini
- **Maven** pour la gestion des dépendances

### Frontend
- **JSF 3** (Jakarta Faces)
- **PrimeFaces 15.0.5** (composants UI riches)
- **JavaScript** personnalisé
- **CSS3** pour le style

### Environnement
- **Serveur d'applications** : Jakarta EE 10+ (Payara 6, WildFly 30, TomEE 9)
- **Build** : Maven 3+
- **API externe** : Google AI Studio (Gemini)

---

## 🚀 Installation

### Prérequis

- JDK 11 ou supérieur
- Maven 3.6+
- Serveur Jakarta EE compatible
- Clé API Google AI Studio

### Étapes d'installation

1️⃣ **Cloner le dépôt**
```bash
git clone <URL_DU_DEPOT>
cd TP2-web-walid-benazzouz
```

2️⃣ **Configurer la clé API**
```bash
export GEMINI_KEY="votre-cle-google-ai-studio"
```

3️⃣ **Compiler le projet**
```bash
mvn clean package
```

4️⃣ **Déployer l'application**
- Déployer le fichier `target/TP2-web-walid-benazzouz.war` sur votre serveur
- Via console d'administration ou auto-deploy

5️⃣ **Accéder à l'application**
```
http://localhost:8080/TP2-web-walid-benazzouz/
```

---

## 📖 Utilisation

### Parcours utilisateur

1. **Choisir un rôle** : sélectionner ou saisir le rôle système souhaité
2. **Poser une question** : entrer votre message dans la zone de texte
3. **Envoyer** : cliquer sur le bouton pour soumettre
4. **Consulter la réponse** : la réponse de Gemini s'affiche instantanément
5. **Poursuivre** : continuer la conversation ou démarrer un nouveau chat

### Exemple d'utilisation

```
Rôle : Tu es un guide touristique expert de la ville de Casablanca
Question : Quels sont les 3 sites incontournables à visiter ?
Réponse : [Gemini fournit une réponse détaillée et contextuelle]
```

---

## 📁 Structure du projet

```
TP2-web-walid-benazzouz/
│
├── src/main/
│   ├── java/ma/emsi/.../
│   │   ├── jsf/
│   │   │   ├── Bb.java                  # Bean managé JSF
│   │   │   └── CharsetFilter.java       # Filtre UTF-8
│   │   └── llm/
│   │       ├── LlmClient.java           # Client LangChain4j
│   │       └── Assistant.java           # Interface proxy
│   │
│   ├── webapp/
│   │   ├── index.xhtml                  # Vue principale
│   │   ├── resources/
│   │   │   ├── css/mycsslayout.css      # Styles
│   │   │   └── js/script.js             # Scripts JS
│   │   └── WEB-INF/
│   │       └── web.xml                  # Configuration webapp
│   │
│   └── resources/META-INF/
│       ├── beans.xml                    # Configuration CDI
│       └── persistence.xml              # Configuration JPA
│
└── pom.xml                              # Dépendances Maven
```

---

## 🔮 Évolutions futures

### Court terme
- [ ] Compléter le mode debug avec affichage JSON
- [ ] Ajouter des tests unitaires (JUnit 5)
- [ ] Améliorer la gestion d'erreurs

### Moyen terme
- [ ] Persistance des conversations (base de données)
- [ ] Export PDF des historiques
- [ ] Internationalisation (i18n)
- [ ] Catalogue enrichi de rôles système

### Long terme
- [ ] Progress bar pendant les appels LLM
- [ ] Historique filtrable et recherchable
- [ ] Pipeline CI/CD
- [ ] Optimisation des performances

---

## ⚠️ Limitations connues

- ❌ Tests automatiques non implémentés
- ❌ Mode debug partiellement fonctionnel
- ❌ Pas de persistance en base de données
- ⚠️ Gestion d'erreurs basique
- ⚠️ Clé API stockée en variable d'environnement (prévoir vault en production)

---

## 📚 Références

- [Jakarta Faces 4.0 Documentation](https://jakarta.ee/specifications/faces/4.0/)
- [PrimeFaces 15 User Guide](https://primefaces.github.io/primefaces/15_0_0/#/)
- [LangChain4j Getting Started](https://langchain4j.dev/)
- [Google AI Studio – Gemini API](https://ai.google.dev/)

---

## 👨‍💻 Auteur

**Walid Benazzouz**  
Étudiant en Technologies Web Avancées  
Université Côte d'Azur

**Encadrement académique**  
Prof. Richard Grin  
Université Côte d'Azur

---

<div align="center">

**TP2 – Technologies Web Avancées** • 2024-2025

</div>
