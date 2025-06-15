package com.dailydone.dailydone.service;

import com.dailydone.dailydone.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitCheckRepository habitCheckRepository;
    private final CategoryRepository categoryRepository;

    public HabitService(HabitRepository habitRepository,
                        HabitCheckRepository habitCheckRepository,
                        CategoryRepository categoryRepository) {
        this.habitRepository = habitRepository;
        this.habitCheckRepository = habitCheckRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    public Optional<Habit> getHabitById(Long id) {
        return habitRepository.findById(id);
    }

    public Habit createHabit(Habit habit) {
        // Validierung
        validateHabit(habit);

        // Kategorie prüfen
        if (habit.getCategory() != null && habit.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(habit.getCategory().getId());
            if (category.isEmpty()) {
                throw new IllegalArgumentException("Kategorie nicht gefunden");
            }
            habit.setCategory(category.get());
        }

        return habitRepository.save(habit);
    }

    public Habit updateHabit(Long id, Habit habitDetails) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habit nicht gefunden mit ID: " + id));

        validateHabit(habitDetails);

        habit.setName(habitDetails.getName());
        habit.setDescription(habitDetails.getDescription());

        if (habitDetails.getCategory() != null && habitDetails.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(habitDetails.getCategory().getId());
            if (category.isEmpty()) {
                throw new IllegalArgumentException("Kategorie nicht gefunden");
            }
            habit.setCategory(category.get());
        }

        return habitRepository.save(habit);
    }

    public void deleteHabit(Long id) {
        if (!habitRepository.existsById(id)) {
            throw new IllegalArgumentException("Habit nicht gefunden mit ID: " + id);
        }

        // Erst alle HabitChecks löschen
        List<HabitCheck> checks = habitCheckRepository.findByHabitIdOrderByDateAsc(id);
        habitCheckRepository.deleteAll(checks);

        // Dann das Habit löschen
        habitRepository.deleteById(id);
    }

    public HabitCheck checkHabitToday(Long habitId) {
        LocalDate today = LocalDate.now();

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit nicht gefunden"));

        // Prüfen ob heute bereits abgehakt
        Optional<HabitCheck> existingCheck = habitCheckRepository
                .findByHabitIdAndDate(habitId, today);

        if (existingCheck.isPresent()) {
            throw new IllegalStateException("Habit bereits heute abgehakt");
        }

        HabitCheck check = new HabitCheck(habit, today);
        return habitCheckRepository.save(check);
    }

    public Map<String, Object> toggleHabitStatus(Long habitId) {
        LocalDate today = LocalDate.now();

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit nicht gefunden mit ID: " + habitId));

        Optional<HabitCheck> existingCheck = habitCheckRepository
                .findByHabitIdAndDate(habitId, today);

        Map<String, Object> result = new HashMap<>();

        if (existingCheck.isPresent()) {
            // Uncheck: Lösche den heutigen Check
            habitCheckRepository.delete(existingCheck.get());
            result.put("message", "Habit erfolgreich als unerledigt markiert");
            result.put("status", "unchecked");
        } else {
            // Check: Erstelle neuen Check
            HabitCheck check = new HabitCheck(habit, today);
            HabitCheck savedCheck = habitCheckRepository.save(check);
            result.put("message", "Habit erfolgreich als erledigt markiert");
            result.put("status", "checked");
            result.put("check", savedCheck);
        }

        result.put("date", today.toString());
        return result;
    }

    public Map<String, Object> getHabitStreaks(Long habitId) {
        if (!habitRepository.existsById(habitId)) {
            throw new IllegalArgumentException("Habit nicht gefunden");
        }

        Map<String, Object> streaks = new HashMap<>();
        streaks.put("currentStreak", calculateCurrentStreak(habitId));
        streaks.put("bestStreak", calculateBestStreak(habitId));
        streaks.put("isCheckedToday", isCheckedToday(habitId));

        return streaks;
    }

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

    public List<HabitCheck> getChecksForMonth(String yearMonth) {
        // Hier könnte man später nach Monat filtern
        return habitCheckRepository.findAll();
    }

    // Private Hilfsmethoden
    private void validateHabit(Habit habit) {
        if (habit.getName() == null || habit.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Habit-Name darf nicht leer sein");
        }

        if (habit.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Habit-Name muss mindestens 2 Zeichen haben");
        }

        if (habit.getName().trim().length() > 100) {
            throw new IllegalArgumentException("Habit-Name darf maximal 100 Zeichen haben");
        }

        if (habit.getDescription() == null || habit.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Beschreibung darf nicht leer sein");
        }

        if (habit.getDescription().trim().length() < 5) {
            throw new IllegalArgumentException("Beschreibung muss mindestens 5 Zeichen haben");
        }

        if (habit.getDescription().trim().length() > 500) {
            throw new IllegalArgumentException("Beschreibung darf maximal 500 Zeichen haben");
        }
    }

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