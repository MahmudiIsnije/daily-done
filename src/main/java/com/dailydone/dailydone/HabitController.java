package com.dailydone.dailydone;

import com.dailydone.dailydone.service.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})

public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public ResponseEntity<List<Habit>> getHabits() {
        try {
            List<Habit> habits = habitService.getAllHabits();
            return ResponseEntity.ok(habits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHabitById(@PathVariable Long id) {
        try {
            return habitService.getHabitById(id)
                    .map(habit -> ResponseEntity.ok().body(habit))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body((Habit) Map.of("error", "Habit nicht gefunden mit ID: " + id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Laden des Habits"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createHabit(@Valid @RequestBody Habit habit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Habit savedHabit = habitService.createHabit(habit);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedHabit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Erstellen des Habits"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabit(@PathVariable Long id, @Valid @RequestBody Habit habitDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Habit updatedHabit = habitService.updateHabit(id, habitDetails);
            return ResponseEntity.ok(updatedHabit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Aktualisieren des Habits"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHabit(@PathVariable Long id) {
        try {
            habitService.deleteHabit(id);
            return ResponseEntity.ok(Map.of("message", "Habit erfolgreich gelöscht"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Löschen des Habits"));
        }
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<?> checkHabitToday(@PathVariable Long id) {
        try {
            HabitCheck check = habitService.checkHabitToday(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(check);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Abhaken des Habits"));
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleHabitStatus(@PathVariable Long id) {
        try {
            Map<String, Object> result = habitService.toggleHabitStatus(id);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Umschalten des Habit-Status"));
        }
    }

    @GetMapping("/checks/month/{yearMonth}")
    public ResponseEntity<?> getChecksForMonth(@PathVariable String yearMonth) {
        try {
            List<HabitCheck> checks = habitService.getChecksForMonth(yearMonth);
            return ResponseEntity.ok(checks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Laden der Kalender-Daten"));
        }
    }

    @GetMapping("/{id}/streaks")
    public ResponseEntity<?> getHabitStreaks(@PathVariable Long id) {
        try {
            Map<String, Object> streaks = habitService.getHabitStreaks(id);
            return ResponseEntity.ok(streaks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Laden der Streak-Daten"));
        }
    }

    @GetMapping("/streaks/all")
    public ResponseEntity<?> getAllHabitStreaks() {
        try {
            Map<Long, Map<String, Object>> allStreaks = habitService.getAllHabitStreaks();
            return ResponseEntity.ok(allStreaks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Laden aller Streak-Daten"));
        }
    }
}