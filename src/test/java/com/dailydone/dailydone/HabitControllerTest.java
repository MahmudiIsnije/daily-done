package com.dailydone.dailydone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HabitController.class)
public class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HabitRepository habitRepository;

    @MockBean
    private HabitCheckRepository habitCheckRepository;

    private Category testCategory;
    private Habit testHabit;

    @BeforeEach
    public void setUp() {
        testCategory = new Category("Test Kategorie", "🧪", "#FF0000");
        testCategory.setId(1L);

        testHabit = new Habit("Test Habit", "Test Beschreibung", testCategory);
        testHabit.setId(1L);
    }

    @Test
    public void testGetAllHabits() throws Exception {
        List<Habit> habits = Arrays.asList(testHabit);
        when(habitRepository.findAll()).thenReturn(habits);

        mockMvc.perform(get("/api/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test Habit"));

        verify(habitRepository, times(1)).findAll();
    }

    @Test
    public void testCreateHabit() throws Exception {
        when(habitRepository.save(any(Habit.class))).thenReturn(testHabit);

        String habitJson = objectMapper.writeValueAsString(testHabit);

        mockMvc.perform(post("/api/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(habitJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Habit"))
                .andExpect(jsonPath("$.description").value("Test Beschreibung"));

        verify(habitRepository, times(1)).save(any(Habit.class));
    }

    @Test
    public void testDeleteHabit() throws Exception {
        when(habitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(habitRepository).deleteById(1L);

        mockMvc.perform(delete("/api/habits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Habit erfolgreich gelöscht"));

        verify(habitRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteHabitNotFound() throws Exception {
        when(habitRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/habits/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Habit nicht gefunden mit ID: 999"));

        verify(habitRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testToggleHabitStatus() throws Exception {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(testHabit));
        when(habitCheckRepository.findByHabitIdAndDate(anyLong(), any())).thenReturn(Optional.empty());
        when(habitCheckRepository.save(any())).thenReturn(new HabitCheck());

        mockMvc.perform(patch("/api/habits/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("checked"));

        verify(habitCheckRepository, times(1)).save(any(HabitCheck.class));
    }

    @Test
    public void testToggleHabitStatusNotFound() throws Exception {
        when(habitRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/habits/999/toggle"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Habit nicht gefunden mit ID: 999"));
    }
}