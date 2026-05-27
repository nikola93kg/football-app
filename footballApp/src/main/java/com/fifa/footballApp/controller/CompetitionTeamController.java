package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.CompetitionTeam;
import com.fifa.footballApp.service.CompetitionTeamService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/competition-teams")
public class CompetitionTeamController {

    private final CompetitionTeamService competitionTeamService;

    public CompetitionTeamController(CompetitionTeamService competitionTeamService) {
        this.competitionTeamService = competitionTeamService;
    }

    @GetMapping("/{id}")
    public CompetitionTeam getCompetitionTeamById(@PathVariable String id) {
        return competitionTeamService.getCompetitionTeamById(id);
    }

    @PutMapping("/{id}")
    public CompetitionTeam updateCompetitionTeam(@PathVariable String id, @RequestBody CompetitionTeam details) {
        return competitionTeamService.updateCompetitionTeam(id, details);
    }

    @DeleteMapping("/{id}")
    public void removeCompetitionTeam(@PathVariable String id) {
        competitionTeamService.removeCompetitionTeam(id);
    }
}
