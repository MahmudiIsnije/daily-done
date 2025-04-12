package com.dailydone.dailydone;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @GetMapping
    public String getHabits() {
        return "Hallo von DailyDone!";
    }
}
