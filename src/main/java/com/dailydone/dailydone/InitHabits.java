package com.dailydone.dailydone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitHabits implements CommandLineRunner {

    private final HabitRepository habitRepository;

    public InitHabits(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Override
    public void run(String... args) {
        if (habitRepository.count() == 0) {
            habitRepository.save(new Habit("Wasser trinken", "Trinke 2 Liter Wasser"));
            habitRepository.save(new Habit("Lesen", "Lese 10 Seiten eines Buches"));
            habitRepository.save(new Habit("Laufen", "Gehe Joggen oder spazieren"));
        }
    }
}
