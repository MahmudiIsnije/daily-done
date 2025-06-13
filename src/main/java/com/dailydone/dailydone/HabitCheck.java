package com.dailydone.dailydone;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class HabitCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    private LocalDate date;

    // Constructors
    public HabitCheck() {}

    public HabitCheck(Habit habit, LocalDate date) {
        this.habit = habit;
        this.date = date;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
