# 📌 Spring-AI

Spring-AI est une API construite avec **Spring Boot** qui intègre **GPT-4o-mini** afin de faciliter l’utilisation de l’IA dans vos projets.

Ce projet a été développé dans un cadre **personnel et éducatif** afin d’apprendre à :
 
- Connecter une API OpenAI  
- Utiliser GPT-4 dans un projet Spring Boot  
- Tester une API avec Postman  

---

## ✨ Fonctionnalités principales
- **Génération de Roadmap personnalisée** selon différents paramètres (compétences, nombre de mois, etc.)  
- **Génération d’e-mails** avec des paramètres tels que : objet, destinataire, langue, contexte, objectif, ton, signature  
- **Chat interactif** similaire à ChatGPT  

---

## 🛠️ Technologies utilisées

### Backend
- Java  
- Spring Boot (3.5.4)  
- Maven  
- OpenAI (API)  
- GPT-4o-mini  

### Bibliothèques importantes
- **OpenAI** → Communication avec le modèle GPT  

---

## 🚀 Installation et démarrage

Cloner le dépôt :  

```bash
git clone https://github.com/djeffing-dev/spring-ai.git
````

Se déplacer dans le projet :

```bash
cd spring-ai
```

Configurer le fichier `application.properties` :

```bash
cp src/main/resources/application.example.properties src/main/resources/application.properties
```

👉 Ajouter votre clé d’API OpenAI dans le fichier.

Lancer l’application :

```bash
mvn spring-boot:run
```

---

## 📡 Utilisation de l’API

### Endpoints disponibles

| Méthode | Endpoint              | Description        |
| ------- | --------------------- | ------------------ |
| `POST`  | `/jpt/getRoadmap`     | Génère une Roadmap |
| `POST`  | `/jpt/emailGenerator` | Génère un e-mail   |
| `POST`  | `/jpt/books/prompt`   | Discute avec GPT-4 |

---

## 📌 Exemple de requêtes

Des exemples de requêtes sont disponibles dans le fichier :

```
test-endpoint.http
```

à la racine du projet.

