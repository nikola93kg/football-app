package com.fifa.footballApp.controller;

import com.fifa.footballApp.dto.CompetitionPlayerStats;
import com.fifa.footballApp.dto.CompetitionTeamStats;
import com.fifa.footballApp.dto.StandingRow;
import com.fifa.footballApp.enums.MatchStatus;
import com.fifa.footballApp.model.Competition;
import com.fifa.footballApp.model.CompetitionTeam;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.PlayerRegistration;
import com.fifa.footballApp.model.Round;
import com.fifa.footballApp.service.CompetitionService;
import com.fifa.footballApp.service.CompetitionTeamService;
import com.fifa.footballApp.service.PlayerRegistrationService;
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
    private final CompetitionTeamService competitionTeamService;
    private final PlayerRegistrationService playerRegistrationService;

    public CompetitionController(CompetitionService competitionService,
                                 CompetitionTeamService competitionTeamService,
                                 PlayerRegistrationService playerRegistrationService) {
        this.competitionService = competitionService;
        this.competitionTeamService = competitionTeamService;
        this.playerRegistrationService = playerRegistrationService;
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

    @GetMapping("/{id}/team-stats")
    public List<CompetitionTeamStats> getCompetitionTeamStats(@PathVariable String id) {
        return competitionService.getCompetitionTeamStats(id);
    }

    @GetMapping("/{id}/player-stats")
    public List<CompetitionPlayerStats> getCompetitionPlayerStats(@PathVariable String id) {
        return competitionService.getCompetitionPlayerStats(id);
    }

    @GetMapping("/{id}/top-scorers")
    public List<CompetitionPlayerStats> getCompetitionTopScorers(@PathVariable String id) {
        return competitionService.getCompetitionTopScorers(id);
    }

    @GetMapping("/{id}/top-assists")
    public List<CompetitionPlayerStats> getCompetitionTopAssists(@PathVariable String id) {
        return competitionService.getCompetitionTopAssists(id);
    }

    @GetMapping("/{id}/discipline")
    public List<CompetitionPlayerStats> getCompetitionDisciplineTable(@PathVariable String id) {
        return competitionService.getCompetitionDisciplineTable(id);
    }

    @GetMapping("/{id}/rounds")
    public List<Round> getCompetitionRounds(@PathVariable String id) {
        return competitionService.getCompetitionRounds(id);
    }

    @PostMapping("/{id}/rounds")
    public Round createCompetitionRound(@PathVariable String id, @RequestBody Round round) {
        return competitionService.createCompetitionRound(id, round);
    }

    @GetMapping("/{id}/teams")
    public List<CompetitionTeam> getCompetitionTeams(@PathVariable String id) {
        return competitionService.getCompetitionTeams(id);
    }

    @PostMapping("/{competitionId}/teams/{teamId}")
    public CompetitionTeam addTeamToCompetition(@PathVariable String competitionId,
                                                @PathVariable String teamId,
                                                @RequestBody(required = false) CompetitionTeam details) {
        return competitionTeamService.addTeamToCompetition(competitionId, teamId, details);
    }

    @GetMapping("/{id}/player-registrations")
    public List<PlayerRegistration> getCompetitionPlayerRegistrations(@PathVariable String id,
                                                                      @RequestParam(required = false) String teamId) {
        return competitionService.getCompetitionPlayerRegistrations(id, teamId);
    }

    @PostMapping("/{competitionId}/teams/{teamId}/players/{playerId}")
    public PlayerRegistration registerPlayer(@PathVariable String competitionId,
                                             @PathVariable String teamId,
                                             @PathVariable String playerId,
                                             @RequestBody(required = false) PlayerRegistration details) {
        return playerRegistrationService.registerPlayer(competitionId, teamId, playerId, details);
    }
}
