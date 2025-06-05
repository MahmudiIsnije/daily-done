package com.dailydone.dailydone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habits")
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

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id) {
        habitRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Habit updateHabit(@PathVariable Long id, @RequestBody Habit updated) {
        return habitRepository.findById(id).map(habit -> {
            habit.setName(updated.getName());
            habit.setDescription(updated.getDescription());
            return habitRepository.save(habit);
        }).orElseThrow(() -> new RuntimeException("Habit not found"));
    }


    @PostMapping("/{id}/check")
    public ResponseEntity<HabitCheck> checkHabit(@PathVariable Long id) {
        Habit habit = habitRepository.findById(id).orElse(null);
        if (habit == null) {
            return ResponseEntity.notFound().build();
        }

        LocalDate today = LocalDate.now();

        boolean alreadyChecked = !habitCheckRepository.findByHabitIdAndDate(id, today).isEmpty();
        if (alreadyChecked) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }

        HabitCheck check = new HabitCheck(habit, today);
        habitCheckRepository.save(check);

        return ResponseEntity.ok(check);
}
}
