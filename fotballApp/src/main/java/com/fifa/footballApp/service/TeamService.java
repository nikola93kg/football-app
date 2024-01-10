package com.fifa.footballApp.service;

import com.fifa.footballApp.model.Coach;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.repository.TeamRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepo teamRepo;

    public TeamService(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    public List<Team> getAllTeams() {
        return teamRepo.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Team not found"));
    }

    public List<Team> searchTeams(String name) {
        return teamRepo.findByName(name);
    }

    public Team addTeam(Team team) {
        return null;
    }


    public Team updateTeam(Long id, Team teamDetails) {
        return null;
    }

    public void deleteTeam(Long id) {

    }

    public List<Player> getPlayersOfTheTeam(Long id) {
        return null;
    }

    public Coach getTeamCoach(Long id) {
        return null;
    }

    public List<Match> getPastMatches(Long id) {
        return null;
    }

    public List<Match> getUpcomingMatches(Long id) {
        return null;
    }

    public void addPlayerToTeam(Long teamId, Long playerId) {

    }

    public void removePlayerFromTeam(Long teamId, Long playerId) {

    }

    public void assignCoachToTeam(Long teamId, Long coachId) {

    }

    public void removeCoachFromTeam(Long teamId, Long coachId) {

    }
}
