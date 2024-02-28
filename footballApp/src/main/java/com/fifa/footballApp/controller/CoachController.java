package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Coach;
import com.fifa.footballApp.service.CoachService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coach")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    public List<Coach> getAllCoaches() {
        return  coachService.getAllCoaches();
    }

    @GetMapping("/{id}")
    public Coach getCoachById(@PathVariable String id) {
        return coachService.getCoachById(id);
    }

    @PostMapping
    public Coach createCoach(@RequestBody Coach coach) {
        return coachService.createCoach(coach);
    }

    @PutMapping("/{id}")
    public Coach updateCoach(@PathVariable String id, @RequestBody Coach coachDetails) {
        return coachService.updateCoach(id, coachDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteCoach(@PathVariable String id) {
        coachService.deleteCoach(id);
    }

    @GetMapping("/search")
    public List<Coach> searchCoaches(String name, String nationality) {
        return coachService.searchCoaches(name, nationality);
    }

}
