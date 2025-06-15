package com.dailydone.dailydone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitCheckRepository extends JpaRepository<HabitCheck, Long> {

    Optional<HabitCheck> findByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitCheck> findByHabitIdOrderByDateAsc(Long habitId);

    List<HabitCheck> findByDate(LocalDate date);

    List<HabitCheck> findByDateBetween(LocalDate startDate, LocalDate endDate);
}