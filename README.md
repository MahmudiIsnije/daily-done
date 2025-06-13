# Daily-Done

## Teammitglieder

- Isnije Mahmudi

----

## Projektbeschreibung
DailyDone ist eine benutzerfreundliche Habit-Tracking-App, die entwickelt wurde,
um Menschen dabei zu helfen, positive Gewohnheiten aufzubauen und beizubehalten.
Mit einem modernen Pastell-Design und motivierenden Features macht es Spaß, täglich Fortschritte zu verfolgen.
Die App bietet ein umfassendes System zum Verwalten von Gewohnheiten mit Kategorien,
Streak-Tracking und visueller Fortschrittsverfolgung. Nutzer können ihre täglichen Routinen wie
„Wasser trinken", „Joggen gehen" oder „Buch lesen" organisiert verfolgen und durch das Streak-System zusätzlich motiviert werden.

---

## Use Cases
### Habit-Management
- Neue Gewohnheiten erstellen
- Gewohnheiten bearbeiten (inline editing)
- Gewohnheiten löschen
- Alle Gewohnheiten anzeigen

### Kategorie-System 
- 6 vordefinierte Kategorien mit farbigen Icons
- Filter nach Kategorien
- Habit-Counter pro Kategorie
- Zwischen "Alle Habits" und Kategorien wechseln

### Streak-System
- Aktuelle Streak-Anzeige (Tage hintereinander)
- Rekord-Streak tracking und Anzeige
- Motivierende Badges (🔥 aktuell, 🏆 Rekord)

### Progress-Tracking
- Tägliches Abhaken (einmal pro Tag)
- Progress-Bars für visuellen Fortschritt
- "Heute erledigt" Status anzeigen
- Verhinderung von Mehrfach-Checks

### Kalender-Integration
- Monats-Kalender mit Aktivitäts-Punkten
- Farbcodierte Darstellung nach Kategorien
- Hover-Tooltips mit Habit-Namen
- "Heute erledigt" Übersicht im Kalender-Bereich

### UI/UX Features
- Responsive Design (Desktop + Mobile)
- Smooth Animationen und Hover-Effekte
- Modernes Pastell-Design mit Glassmorphism


## Technologie-Stack
- Backend

Spring Boot (Java)
JPA/Hibernate für Datenbankzugriffe
PostgreSQL (Production) / H2 (Development)
REST API mit CORS-Unterstützung

- Frontend

Vue.js 3 mit Composition API
V-Calendar für Kalender-Funktionalität
Custom CSS mit modernem Pastell-Design
Responsive Grid Layout

## 📊 API-Endpoints
### Habits

- GET /api/habits - Alle Habits abrufen
- POST /api/habits - Neues Habit erstellen
- PUT /api/habits/{id} - Habit bearbeiten
- DELETE /api/habits/{id} - Habit löschen
- POST /api/habits/{id}/check - Habit heute abhaken

### Kategorien

- GET /api/categories - Alle Kategorien abrufen
- POST /api/categories - Neue Kategorie erstellen

### Streaks

- GET /api/habits/{id}/streaks - Streak-Daten für einzelnes Habit
- GET /api/habits/streaks/all - Streak-Daten für alle Habits

### Kalender

- GET /api/habits/checks/month/{yearMonth} - Monatliche Aktivitätsdaten