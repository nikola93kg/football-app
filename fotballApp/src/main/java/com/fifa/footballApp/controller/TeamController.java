package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Coach;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PostMapping
    public Team addTeam(@RequestBody Team team) {
        return teamService.addTeam(team);
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
        return teamService.updateTeam(id, teamDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

    @GetMapping("/search")
    public List<Team> searchTeams(String name) {
        return teamService.searchTeams(name);
    }

    @GetMapping("/{id}/players-of-the-team")
    public List<Player> getPlayersOfTheTeam(@PathVariable Long id) {
        return teamService.getPlayersOfTheTeam(id);
    }

    @GetMapping("/{id}/coach")
    public ResponseEntity<Coach> getTeamCoach(@PathVariable Long id) {
        Coach coach = teamService.getTeamCoach(id);
        return ResponseEntity.ok(coach);
    }

//    mogu da vidim formaciju za meceve koji su bili
    @GetMapping("/{id}/past-matches")
    public ResponseEntity<List<Match>> getPastMatchesOfTeam(@PathVariable Long id) {
        List<Match> pastMatches = teamService.getPastMatches(id);
        return ResponseEntity.ok(pastMatches);
    }

//    ovde mogu da vidim formaciju za meceve koji slede
    @GetMapping("/{id}/upcoming-matches")
    public ResponseEntity<List<Match>> getUpcomingMatchesOfTeam(@PathVariable Long id) {
        List<Match> upcomingMatches = teamService.getUpcomingMatches(id);
        return ResponseEntity.ok(upcomingMatches);
    }

    @PostMapping("/{teamId}/add-player/{playerId}")
    public ResponseEntity<?> addPlayerToTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.addPlayerToTeam(teamId, playerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}/remove-player/{playerId}")
    public ResponseEntity<?> removePlayerFromTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.removePlayerFromTeam(teamId, playerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{teamId}/assign-coach/{coachId}")
    public ResponseEntity<?> assignCoachToTeam(@PathVariable Long teamId, @PathVariable Long coachId) {
        teamService.assignCoachToTeam(teamId, coachId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}/remove-coach/{coachId}")
    public ResponseEntity<?> removeCoachFromTeam(@PathVariable Long teamId, @PathVariable Long coachId) {
        teamService.removeCoachFromTeam(teamId, coachId);
        return ResponseEntity.ok().build();
    }

}
