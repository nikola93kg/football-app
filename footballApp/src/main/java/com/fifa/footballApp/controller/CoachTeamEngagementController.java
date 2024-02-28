package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.CoachTeamEngagement;
import com.fifa.footballApp.service.CoachTeamEngagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/coach-engagements")
public class CoachTeamEngagementController {

    private final CoachTeamEngagementService coachTeamEngagementService;

    public CoachTeamEngagementController(CoachTeamEngagementService coachTeamEngagementService) {
        this.coachTeamEngagementService = coachTeamEngagementService;
    }

    @GetMapping
    public List<CoachTeamEngagement> getAllEngagements() {
        return coachTeamEngagementService.getAllEngagements();
    }

    @GetMapping("/{id}")
    public CoachTeamEngagement getEngagementById(@PathVariable String id) {
        return coachTeamEngagementService.getEngagementById(id);
    }

    @PostMapping
    public CoachTeamEngagement createEngagement(@RequestBody CoachTeamEngagement engagement) {
        return coachTeamEngagementService.createEngagement(engagement);
    }

    @PutMapping("/{id}")
    public CoachTeamEngagement updateEngagement(@PathVariable String id, @RequestBody CoachTeamEngagement engagementDetails) {
        return coachTeamEngagementService.updateEngagement(id, engagementDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteEngagement(@PathVariable String id) {
        coachTeamEngagementService.deleteEngagement(id);
    }

    @GetMapping("/active")
    public List<CoachTeamEngagement> getActiveCoaches() {
        return coachTeamEngagementService.getActiveEngagements();
    }
}
