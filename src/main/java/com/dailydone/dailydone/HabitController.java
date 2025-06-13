package com.dailydone.dailydone;

import org.springframework.web.bind.annotation.*;

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
    public Habit createHabit(@RequestBody Habit habit) {
        return habitRepository.save(habit);
    }

    @PutMapping("/{id}")
    public Habit updateHabit(@PathVariable Long id, @RequestBody Habit habitDetails) {
        Optional<Habit> optionalHabit = habitRepository.findById(id);
        if (optionalHabit.isPresent()) {
            Habit habit = optionalHabit.get();
            habit.setName(habitDetails.getName());
            habit.setDescription(habitDetails.getDescription());
            habit.setCategory(habitDetails.getCategory());
            return habitRepository.save(habit);
        }
        throw new RuntimeException("Habit nicht gefunden mit ID: " + id);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id) {
        habitRepository.deleteById(id);
    }

    @PostMapping("/{id}/check")
    public HabitCheck checkHabit(@PathVariable Long id) {
        LocalDate today = LocalDate.now();

        // Prüfen ob heute bereits abgehakt
        Optional<HabitCheck> existingCheck = habitCheckRepository
                .findByHabitIdAndDate(id, today);

        if (existingCheck.isPresent()) {
            throw new RuntimeException("Habit bereits heute abgehakt");
        }

        // Neuen Check erstellen
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit nicht gefunden"));

        HabitCheck check = new HabitCheck();
        check.setHabit(habit);
        check.setDate(today);

        return habitCheckRepository.save(check);
    }

    @GetMapping("/checks/month/{yearMonth}")
    public List<HabitCheck> getChecksForMonth(@PathVariable String yearMonth) {
        return habitCheckRepository.findAll();
    }

    // NEUE STREAK-ENDPOINTS
    @GetMapping("/{id}/streaks")
    public Map<String, Object> getHabitStreaks(@PathVariable Long id) {
        Map<String, Object> streaks = new HashMap<>();

        int currentStreak = calculateCurrentStreak(id);
        int bestStreak = calculateBestStreak(id);

        streaks.put("currentStreak", currentStreak);
        streaks.put("bestStreak", bestStreak);
        streaks.put("isCheckedToday", isCheckedToday(id));

        return streaks;
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

    // STREAK-BERECHNUNGS-METHODEN
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

            // Aufeinanderfolgende Tage?
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