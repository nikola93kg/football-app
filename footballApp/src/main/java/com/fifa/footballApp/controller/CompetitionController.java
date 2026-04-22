package com.fifa.footballApp.controller;

import com.fifa.footballApp.dto.StandingRow;
import com.fifa.footballApp.enums.MatchStatus;
import com.fifa.footballApp.model.Competition;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.service.CompetitionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping
    public List<Competition> getAllCompetitions() {
        return competitionService.getAllCompetitions();
    }

    @GetMapping("/{id}")
    public Competition getCompetitionById(@PathVariable String id) {
        return competitionService.getCompetitionById(id);
    }

    @PostMapping
    public Competition createCompetition(@RequestBody Competition competition) {
        return competitionService.createCompetition(competition);
    }

    @PutMapping("/{id}")
    public Competition updateCompetition(@PathVariable String id, @RequestBody Competition competitionDetails) {
        return competitionService.updateCompetition(id, competitionDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteCompetition(@PathVariable String id) {
        competitionService.deleteCompetition(id);
    }

    @GetMapping("/{id}/matches")
    public List<Match> getCompetitionMatches(@PathVariable String id, @RequestParam(required = false) MatchStatus status) {
        return competitionService.getCompetitionMatchesByStatus(id, status);
    }

    @GetMapping("/{id}/standings")
    public List<StandingRow> getStandings(@PathVariable String id) {
        return competitionService.getStandings(id);
    }
}
