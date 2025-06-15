# Daily-Done - Habit Tracking App
Eine moderne Webanwendung zur Verwaltung und Verfolgung von täglichen Gewohnheiten.

## Live Demo
Deployed App: https://daily-done-qztv.onrender.com

----

## Projektbeschreibung
Daily Done ist eine Full-Stack-Webanwendung, die es Benutzern ermöglicht:

- Tägliche Gewohnheiten zu erstellen und zu verwalten
- Gewohnheiten verschiedenen Kategorien zuzuordnen
- Fortschritte zu verfolgen und zu visualisieren
- Eine intuitive Benutzeroberfläche zu nutzen

---

## Technologie-Stack
Backend

- Spring Boot 3.x - REST API Framework
- Java 17 - Programmiersprache
- PostgreSQL - Produktionsdatenbank
- H2 - Test-Datenbank
- Maven - Build Management
- JUnit 5 - Testing Framework

Frontend

- Vue.js 3 - JavaScript Framework
- Vue Router - Client-side Routing
- Axios - HTTP Client
- CSS3 - Styling
- Jest - Testing Framework

Deployment & DevOps

- Render - Cloud Hosting Platform
- GitHub Actions - CI/CD Pipeline
- Git - Versionskontrolle

## Installation & Setup
Voraussetzungen

- Java 17 oder höher
- Node.js 16 oder höher
- Maven 3.6+
- PostgreSQL (für Produktion)

--- 

# API Endpoints

## Habits

- GET /api/habits - Alle Gewohnheiten abrufen 
- POST /api/habits - Neue Gewohnheit erstellen 
- GET /api/habits/{id} - Bestimmte Gewohnheit abrufen 
- PUT /api/habits/{id} - Gewohnheit aktualisieren 
- DELETE /api/habits/{id} - Gewohnheit löschen

## Categories

- GET /api/categories - Alle Kategorien abrufen 
- POST /api/categories - Neue Kategorie erstellen
- GET /api/categories/{id} - Bestimmte Kategorie abrufen 
- PUT /api/categories/{id} - Kategorie aktualisieren 
- DELETE /api/categories/{id} - Kategorie löschen

---

## Teammitglieder

- Isnije Mahmudi