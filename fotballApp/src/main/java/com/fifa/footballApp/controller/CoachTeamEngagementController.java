package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.CoachTeamEngagement;
import com.fifa.footballApp.service.CoachTeamEngagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/coach-engagements")
public class CoachTeamEngagementController {

    private final CoachTeamEngagementService service;

    public CoachTeamEngagementController(CoachTeamEngagementService service) {
        this.service = service;
    }

    @GetMapping
    public List<CoachTeamEngagement> getAllCoaches() {
        return service.getAllCoaches();
    }

    @GetMapping("/active")
    public List<CoachTeamEngagement> getActiveCoaches() {
        return service.getActiveCoaches();
    }
}
