package com.dailydone.dailydone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HabitController.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"

})
@Transactional
public class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testGetAllHabits() throws Exception {
        mockMvc.perform(get("/api/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateHabit() throws Exception {
        // Erstelle eine Kategorie für den Test
        Category category = new Category("Test Kategorie", "🧪", "#FF0000");
        category = categoryRepository.save(category);

        // Erstelle Habit JSON
        String habitJson = """
        {
            "name": "Test Habit",
            "description": "Test Beschreibung",
            "category": {
                "id": %d
            }
        }
        """.formatted(category.getId());

        mockMvc.perform(post("/api/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(habitJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Habit"))
                .andExpect(jsonPath("$.description").value("Test Beschreibung"));
    }

    @Test
    public void testToggleHabitStatus() throws Exception {
        // Erstelle eine Kategorie für den Test
        Category category = new Category("Test Kategorie", "🧪", "#FF0000");
        category = categoryRepository.save(category);

        // Erstelle Habit JSON
        String habitJson = """
        {
            "name": "Test Toggle Habit",
            "description": "Test für Toggle",
            "category": {
                "id": %d
            }
        }
        """.formatted(category.getId());

        // Erstelle Habit
        String response = mockMvc.perform(post("/api/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(habitJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse die Response um die ID zu bekommen
        Long habitId = 1L; // Vereinfacht für Test

        // Test PATCH Toggle (Check)
        mockMvc.perform(patch("/api/habits/" + habitId + "/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("checked"));

        // Test PATCH Toggle (Uncheck)
        mockMvc.perform(patch("/api/habits/" + habitId + "/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("unchecked"));
    }
}