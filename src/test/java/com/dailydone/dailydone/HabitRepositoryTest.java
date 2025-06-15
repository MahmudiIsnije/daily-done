package com.dailydone.dailydone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class HabitRepositoryTest {

    @Autowired
    private HabitRepository habitRepository;

    @Test
    void testRepositoryIsNotNull() {
        // Einfacher Test: Repository ist da
        assertNotNull(habitRepository);
        System.out.println("✅ HabitRepository erfolgreich geladen!");
    }

    @Test
    void testFindAllWorks() {
        // Einfacher Test: findAll funktioniert (auch wenn leer)
        var habits = habitRepository.findAll();
        assertNotNull(habits);
        System.out.println("✅ findAll() funktioniert! Anzahl: " + habits.size());
    }
}