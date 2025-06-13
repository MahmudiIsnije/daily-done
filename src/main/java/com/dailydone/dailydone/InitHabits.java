package com.dailydone.dailydone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitHabits implements CommandLineRunner {

    private final HabitRepository habitRepository;
    private final CategoryRepository categoryRepository;

    public InitHabits(HabitRepository habitRepository, CategoryRepository categoryRepository) {
        this.habitRepository = habitRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        // Kategorien erstellen falls noch keine da sind
        if (categoryRepository.count() == 0) {
            Category gesundheit = categoryRepository.save(new Category("Gesundheit", "💪", "#10B981"));
            Category produktivitat = categoryRepository.save(new Category("Produktivität", "🧠", "#3B82F6"));
            Category lifestyle = categoryRepository.save(new Category("Lifestyle", "🏠", "#8B5CF6"));
            Category kreativitat = categoryRepository.save(new Category("Kreativität", "🎨", "#EC4899"));
            Category sozial = categoryRepository.save(new Category("Sozial", "🤝", "#F59E0B"));
            Category finanzen = categoryRepository.save(new Category("Finanzen", "💰", "#059669"));

            // Default Habits mit Kategorien erstellen
            if (habitRepository.count() == 0) {
                // Gesundheit Habits
                habitRepository.save(new Habit("Wasser trinken", "Trinke 2 Liter Wasser täglich", gesundheit));
                habitRepository.save(new Habit("Joggen gehen", "20 Minuten laufen oder spazieren", gesundheit));
                habitRepository.save(new Habit("Gesund frühstücken", "Vollwertig und ausgewogen essen", gesundheit));
                habitRepository.save(new Habit("Meditieren", "10 Minuten Achtsamkeit üben", gesundheit));

                // Produktivität Habits
                habitRepository.save(new Habit("Buch lesen", "30 Minuten täglich lesen", produktivitat));
                habitRepository.save(new Habit("To-Do Liste erstellen", "Tägliche Aufgaben planen", produktivitat));
                habitRepository.save(new Habit("E-Mails bearbeiten", "Inbox aufräumen und organisieren", produktivitat));

                // Lifestyle Habits
                habitRepository.save(new Habit("Bett machen", "Zimmer ordentlich halten", lifestyle));
                habitRepository.save(new Habit("Kochen", "Selbst eine gesunde Mahlzeit zubereiten", lifestyle));
                habitRepository.save(new Habit("Aufräumen", "15 Minuten Wohnung organisieren", lifestyle));

                // Kreativität Habits
                habitRepository.save(new Habit("Zeichnen", "Kreativ sein und etwas skizzieren", kreativitat));
                habitRepository.save(new Habit("Musik hören", "Neue Songs oder Künstler entdecken", kreativitat));
                habitRepository.save(new Habit("Fotografieren", "Ein schönes Motiv einfangen", kreativitat));

                // Sozial Habits
                habitRepository.save(new Habit("Familie anrufen", "Mit Liebsten in Kontakt bleiben", sozial));
                habitRepository.save(new Habit("Freunde treffen", "Soziale Kontakte pflegen", sozial));
                habitRepository.save(new Habit("Kompliment machen", "Jemandem eine Freude bereiten", sozial));

                // Finanzen Habits
                habitRepository.save(new Habit("Ausgaben notieren", "Tägliche Ausgaben dokumentieren", finanzen));
                habitRepository.save(new Habit("Sparziel überprüfen", "Fortschritt beim Sparen kontrollieren", finanzen));
                habitRepository.save(new Habit("Finanz-App checken", "Kontostand und Investitionen prüfen", finanzen));
            }
        }
    }
}