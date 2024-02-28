package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Coach;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.service.TeamService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

//@CrossOrigin(origins = "http://localhost:3000")
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
    public Team getTeamById(@PathVariable String id) {
        return teamService.getTeamById(id);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Team addTeam(@RequestBody Team team) {
        return teamService.addTeam(team);
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable String id, @RequestBody Team teamDetails) {
        return teamService.updateTeam(id, teamDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable String id) {
        teamService.deleteTeam(id);
    }

    @GetMapping("/search")
    public List<Team> searchTeams(String name) {
        return teamService.searchTeams(name);
    }

    @GetMapping("/{id}/players-of-the-team")
    public List<Player> getPlayersOfTheTeam(@PathVariable String id) {
        return teamService.getPlayersOfTheTeam(id);
    }

    @GetMapping("/{id}/coach")
    public ResponseEntity<Coach> getTeamCoach(@PathVariable String id) {
        Coach coach = teamService.getTeamCoach(id);
        return ResponseEntity.ok(coach);
    }

//    mogu da vidim formaciju za meceve koji su bili
    @GetMapping("/{id}/past-matches")
    public ResponseEntity<List<Match>> getPastMatchesOfTeam(@PathVariable String id) {
        List<Match> pastMatches = teamService.getPastMatches(id);
        return ResponseEntity.ok(pastMatches);
    }

//    ovde mogu da vidim formaciju za meceve koji slede
    @GetMapping("/{id}/upcoming-matches")
    public ResponseEntity<List<Match>> getUpcomingMatchesOfTeam(@PathVariable String id) {
        List<Match> upcomingMatches = teamService.getUpcomingMatches(id);
        return ResponseEntity.ok(upcomingMatches);
    }

    @PostMapping("/{teamId}/add-player/{playerId}")
    public ResponseEntity<?> addPlayerToTeam(@PathVariable String teamId, @PathVariable String playerId) {
        teamService.assignPlayerToTeam(teamId, playerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}/remove-player/{playerId}")
    public ResponseEntity<?> removePlayerFromTeam(@PathVariable String teamId, @PathVariable String playerId) {
        teamService.removePlayerFromTeam(teamId, playerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{teamId}/assign-coach/{coachId}")
    public ResponseEntity<?> assignCoachToTeam(@PathVariable String teamId, @PathVariable String coachId) {
        teamService.assignCoachToTeam(teamId, coachId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}/remove-coach/{coachId}")
    public ResponseEntity<?> removeCoachFromTeam(@PathVariable String teamId, @PathVariable String coachId) {
        teamService.removeCoachFromTeam(teamId, coachId);
        return ResponseEntity.ok().build();
    }

}
