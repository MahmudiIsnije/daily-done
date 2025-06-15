package com.dailydone.dailydone.service;

import com.dailydone.dailydone.Category;
import com.dailydone.dailydone.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        validateCategory(category);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategorie nicht gefunden mit ID: " + id));

        validateCategory(categoryDetails);

        category.setName(categoryDetails.getName());
        category.setIcon(categoryDetails.getIcon());
        category.setColor(categoryDetails.getColor());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Kategorie nicht gefunden mit ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private void validateCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategorie-Name darf nicht leer sein");
        }

        if (category.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Kategorie-Name muss mindestens 2 Zeichen haben");
        }

        if (category.getName().trim().length() > 50) {
            throw new IllegalArgumentException("Kategorie-Name darf maximal 50 Zeichen haben");
        }

        if (category.getIcon() == null || category.getIcon().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategorie-Icon darf nicht leer sein");
        }

        if (category.getColor() == null || category.getColor().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategorie-Farbe darf nicht leer sein");
        }

        // Validate hex color format
        if (!category.getColor().matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new IllegalArgumentException("Ungültiges Farbformat. Bitte verwenden Sie Hex-Format (#RRGGBB)");
        }
    }
}