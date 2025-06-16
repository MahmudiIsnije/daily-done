# Daily-Done - Habit Tracking App
Eine moderne Full-Stack-Webanwendung zur Verwaltung und Verfolgung von täglichen Gewohnheiten 
mit Fortschrittstracking und Streak-Berechnung.

## Live Demo
Deployed App: https://daily-done-qztv.onrender.com

----

## Projektbeschreibung
Daily-Done ist eine intuitive Habit-Tracking-App, die Nutzern dabei hilft, 
positive Gewohnheiten zu entwickeln und zu verfolgen. Die App bietet:

- Habit Management: Erstellen, bearbeiten und löschen von täglichen Gewohnheiten
- Kategorisierung: Organisierung von Habits in verschiedene Kategorien (Gesundheit, Produktivität, etc.)
- Tägliches Tracking: Einfaches Abhaken erledigter Habits
- Streak-System: Verfolgung aktueller und bester Streak-Reihen
- Fortschrittsverfolgung: Visualisierung des Habit-Fortschritts
- Responsive Design: Optimiert für Desktop und Mobile
- Kalender-Integration: Monatliche Übersicht der erledigten Habits

---

## Technologie-Stack
Backend

- Spring Boot 3.x - REST API Framework
- Java 21 - Programmiersprache
- PostgreSQL - Produktionsdatenbank
- H2 Database - Test- und Entwicklungsdatenbank
- Spring Data JPA - Datenzugriff und ORM
- Bean Validation - Eingabevalidierung
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
- CORS - Cross-Origin Resource Sharing

--- 

## Installation & Setup
Voraussetzungen

- Java 21+
- Node.js 16+
- Maven 3.6+
- PostgreSQL (für Produktion)
- Git

### 1. Repository klonen 
git clone : https://github.com/MahmudiIsnije/daily-done.git

### 2. Backend Setup 
Dependencies installieren: mvn clean install
Anwendung starten: mvn spring-boot:run
Die Anwendung läuft auf: http://localhost:8080

### 3. Frontend Setup
npm install
npm run serve
Frontend läuft auf: http://localhost:3000

--- 

## Testing

### Backend Tests ausführen
- Alle Tests: mvn test

### Frontend Tests ausführen
- Alle Tests: npm run test

---

# API Endpoints
GET /api/habits -- Beschreibung: Alle Gewohnheiten abrufen - Response: Liste aller Habits mit Kategorien


## Habits

- GET /api/habits - Alle Gewohnheiten abrufen 
- POST /api/habits - Neue Gewohnheit erstellen 
- GET /api/habits/{id} - Bestimmte Gewohnheit abrufen 
- PUT /api/habits/{id} - Gewohnheit aktualisieren 
- DELETE /api/habits/{id} - Gewohnheit löschen
- POST /api/habits/{id}/check - Habit für heute abhaken
- PATCH /api/habits/{id}/toggle - Habit-Status umschalten (check/uncheck)
- GET /api/habits/{id}/streaks - Streak-Daten für ein Habit abrufen
- GET /api/habits/streaks/all - Alle Streak-Daten abrufen
- GET /api/habits/checks/month/{yearMonth} - Monatsübersicht der Checks

## Categories

- GET /api/categories - Alle Kategorien abrufen 
- POST /api/categories - Neue Kategorie erstellen
- GET /api/categories/{id} - Bestimmte Kategorie abrufen 
- PUT /api/categories/{id} - Kategorie aktualisieren 
- DELETE /api/categories/{id} - Kategorie löschen

---

# Verwendung

1. App öffnen und zur Habit-Übersicht navigieren
2. Neue Gewohnheit erstellen mit Name, Beschreibung und Kategorie
3. Täglich abhaken durch Klick auf das Habit
4. Fortschritt verfolgen in der Streak-Übersicht
5. Kategorien verwalten für bessere Organisation

## Teammitglieder

- Isnije Mahmudi

---

## ⭐ Falls dir dieses Projekt gefällt, gib ihm einen Stern auf GitHub!