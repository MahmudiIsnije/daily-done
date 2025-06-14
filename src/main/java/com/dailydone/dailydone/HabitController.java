package com.dailydone.dailydone;

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
@CrossOrigin(origins = {"http://localhost:3000", "https://daily-done-frontend.onrender.com"})
public class HabitController {

    private final HabitRepository habitRepository;
    private final HabitCheckRepository habitCheckRepository;

    public HabitController(HabitRepository habitRepository, HabitCheckRepository habitCheckRepository) {
        this.habitRepository = habitRepository;
        this.habitCheckRepository = habitCheckRepository;
    }

    @GetMapping
    public List<Habit> getHabits() {
        return habitRepository.findAll();
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

        Habit savedHabit = habitRepository.save(habit);
        return ResponseEntity.ok(savedHabit);
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

        Optional<Habit> optionalHabit = habitRepository.findById(id);
        if (optionalHabit.isPresent()) {
            Habit habit = optionalHabit.get();
            habit.setName(habitDetails.getName());
            habit.setDescription(habitDetails.getDescription());
            habit.setCategory(habitDetails.getCategory());
            return ResponseEntity.ok(habitRepository.save(habit));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Habit nicht gefunden mit ID: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHabit(@PathVariable Long id) {
        if (habitRepository.existsById(id)) {
            habitRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Habit erfolgreich gelöscht"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Habit nicht gefunden mit ID: " + id));
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<?> checkHabit(@PathVariable Long id) {
        LocalDate today = LocalDate.now();

        // Prüfen ob heute bereits abgehakt
        Optional<HabitCheck> existingCheck = habitCheckRepository
                .findByHabitIdAndDate(id, today);

        if (existingCheck.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Habit bereits heute abgehakt"));
        }

        // Neuen Check erstellen
        Optional<Habit> optionalHabit = habitRepository.findById(id);
        if (optionalHabit.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Habit nicht gefunden"));
        }

        HabitCheck check = new HabitCheck();
        check.setHabit(optionalHabit.get());
        check.setDate(today);

        return ResponseEntity.ok(habitCheckRepository.save(check));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleHabitStatus(@PathVariable Long id) {
        LocalDate today = LocalDate.now();

        // Prüfen ob Habit existiert
        Optional<Habit> optionalHabit = habitRepository.findById(id);
        if (optionalHabit.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Habit nicht gefunden mit ID: " + id));
        }

        // Prüfen ob heute bereits abgehakt
        Optional<HabitCheck> existingCheck = habitCheckRepository
                .findByHabitIdAndDate(id, today);

        if (existingCheck.isPresent()) {
            // Uncheck: Lösche den heutigen Check
            habitCheckRepository.delete(existingCheck.get());
            return ResponseEntity.ok(Map.of(
                    "message", "Habit erfolgreich als unerledigt markiert",
                    "status", "unchecked",
                    "date", today.toString()
            ));
        } else {
            // Check: Erstelle neuen Check
            HabitCheck check = new HabitCheck();
            check.setHabit(optionalHabit.get());
            check.setDate(today);
            HabitCheck savedCheck = habitCheckRepository.save(check);

            return ResponseEntity.ok(Map.of(
                    "message", "Habit erfolgreich als erledigt markiert",
                    "status", "checked",
                    "date", today.toString(),
                    "check", savedCheck
            ));
        }
    }

    @GetMapping("/checks/month/{yearMonth}")
    public List<HabitCheck> getChecksForMonth(@PathVariable String yearMonth) {
        return habitCheckRepository.findAll();
    }

    @GetMapping("/{id}/streaks")
    public ResponseEntity<?> getHabitStreaks(@PathVariable Long id) {
        if (!habitRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Habit nicht gefunden"));
        }

        Map<String, Object> streaks = new HashMap<>();
        streaks.put("currentStreak", calculateCurrentStreak(id));
        streaks.put("bestStreak", calculateBestStreak(id));
        streaks.put("isCheckedToday", isCheckedToday(id));

        return ResponseEntity.ok(streaks);
    }

    @GetMapping("/streaks/all")
    public Map<Long, Map<String, Object>> getAllHabitStreaks() {
        List<Habit> habits = habitRepository.findAll();
        Map<Long, Map<String, Object>> allStreaks = new HashMap<>();

        for (Habit habit : habits) {
            Map<String, Object> streaks = new HashMap<>();
            streaks.put("currentStreak", calculateCurrentStreak(habit.getId()));
            streaks.put("bestStreak", calculateBestStreak(habit.getId()));
            streaks.put("isCheckedToday", isCheckedToday(habit.getId()));
            allStreaks.put(habit.getId(), streaks);
        }

        return allStreaks;
    }

    // Streak-Berechnungen
    private int calculateCurrentStreak(Long habitId) {
        LocalDate today = LocalDate.now();
        LocalDate checkDate = today;
        int streak = 0;

        // Wenn heute noch nicht abgehakt, starte gestern
        if (!isCheckedToday(habitId)) {
            checkDate = today.minusDays(1);
        }

        // Zähle rückwärts bis zu einem Tag ohne Check
        while (habitCheckRepository.findByHabitIdAndDate(habitId, checkDate).isPresent()) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }

        return streak;
    }

    private int calculateBestStreak(Long habitId) {
        List<HabitCheck> checks = habitCheckRepository.findByHabitIdOrderByDateAsc(habitId);

        if (checks.isEmpty()) {
            return 0;
        }

        int maxStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < checks.size(); i++) {
            LocalDate previousDate = checks.get(i - 1).getDate();
            LocalDate currentDate = checks.get(i).getDate();

            // Aufeinanderfolgende Tage
            if (ChronoUnit.DAYS.between(previousDate, currentDate) == 1) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }

        return maxStreak;
    }

    private boolean isCheckedToday(Long habitId) {
        LocalDate today = LocalDate.now();
        return habitCheckRepository.findByHabitIdAndDate(habitId, today).isPresent();
    }
}