package com.dailydone.dailydone;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kategorie-Name darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Kategorie-Name muss zwischen 2 und 50 Zeichen haben")
    private String name;

    @NotBlank(message = "Icon darf nicht leer sein")
    @Size(min = 1, max = 10, message = "Icon muss zwischen 1 und 10 Zeichen haben")
    private String icon;

    @NotBlank(message = "Farbe darf nicht leer sein")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Ungültiges Farbformat. Bitte verwenden Sie Hex-Format (#RRGGBB)")
    private String color;

    public Category() {}

    public Category(String name, String icon, String color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon != null ? icon.trim() : null;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color != null ? color.trim() : null;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}