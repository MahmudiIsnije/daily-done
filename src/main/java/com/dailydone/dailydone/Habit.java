package com.dailydone.dailydone;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Habit-Name darf nicht leer sein")
    @Size(min = 2, max = 100, message = "Habit-Name muss zwischen 2 und 100 Zeichen haben")
    private String name;

    @NotBlank(message = "Beschreibung darf nicht leer sein")
    @Size(min = 5, max = 500, message = "Beschreibung muss zwischen 5 und 500 Zeichen haben")
    private String description;

    @NotNull(message = "Kategorie muss ausgewählt werden")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Habit() {}

    public Habit(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // Getters und Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : null;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + (category != null ? category.getName() : "null") +
                '}';
    }
}