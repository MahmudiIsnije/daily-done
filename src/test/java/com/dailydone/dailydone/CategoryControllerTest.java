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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    public void setUp() {
        testCategory = new Category("Test Kategorie", "🧪", "#FF0000");
        testCategory.setId(1L);
    }

    @Test
    public void testGetAllCategories() throws Exception {
        List<Category> categories = Arrays.asList(
                testCategory,
                new Category("Zweite Kategorie", "📚", "#00FF00")
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test Kategorie"))
                .andExpect(jsonPath("$[0].icon").value("🧪"))
                .andExpect(jsonPath("$[1].name").value("Zweite Kategorie"));

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testCreateCategory() throws Exception {
        Category newCategory = new Category("Neue Kategorie", "✨", "#0000FF");
        Category savedCategory = new Category("Neue Kategorie", "✨", "#0000FF");
        savedCategory.setId(2L);

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        String categoryJson = objectMapper.writeValueAsString(newCategory);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Neue Kategorie"))
                .andExpect(jsonPath("$.icon").value("✨"))
                .andExpect(jsonPath("$.color").value("#0000FF"));

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        doNothing().when(categoryRepository).deleteById(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk());

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}