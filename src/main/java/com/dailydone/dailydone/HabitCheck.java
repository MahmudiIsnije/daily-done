package com.dailydone.dailydone;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class HabitCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    private Habit habit;
    private LocalDate date;

    public HabitCheck() {}
    public HabitCheck(Habit habit, LocalDate date) {
        this.habit = habit;
        this.date = date;
    }

    public long getId() {
        return id;
    }
    public Habit getHabit() {
        return habit;
    }
    public LocalDate getDate() {
        return date;
    }
}
