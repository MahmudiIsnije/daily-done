package com.dailydone.dailydone;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitCheckRepository extends JpaRepository<HabitCheck, Long> {
    List<HabitCheck> findByHabitIdAndDate(Long habitId, LocalDate date);
}
