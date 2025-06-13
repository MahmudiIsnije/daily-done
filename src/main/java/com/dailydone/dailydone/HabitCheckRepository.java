package com.dailydone.dailydone;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitCheckRepository extends JpaRepository<HabitCheck, Long> {


    Optional<HabitCheck> findByHabitIdAndDate(Long habitId, LocalDate date);


    List<HabitCheck> findByHabitIdOrderByDateAsc(Long habitId);
    List<HabitCheck> findByHabitIdOrderByDateDesc(Long habitId);
}