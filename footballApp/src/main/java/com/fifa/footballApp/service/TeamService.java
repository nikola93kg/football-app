package com.fifa.footballApp.service;

import com.fifa.footballApp.exception.TeamNotFoundException;
import com.fifa.footballApp.model.Coach;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.repository.CoachRepo;
import com.fifa.footballApp.repository.MatchRepo;
import com.fifa.footballApp.repository.PlayerRepo;
import com.fifa.footballApp.repository.TeamRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TeamService {

    private final TeamRepo teamRepo;
    private final MatchRepo matchRepo;
    private final PlayerRepo playerRepo;
    private final CoachRepo coachRepo;

    public TeamService(TeamRepo teamRepo, MatchRepo matchRepo, PlayerRepo playerRepo, CoachRepo coachRepo) {
        this.teamRepo = teamRepo;
        this.matchRepo = matchRepo;
        this.playerRepo = playerRepo;
        this.coachRepo = coachRepo;
    }

//    public List<Team> getAllTeams() {
//        return teamRepo.findAll();
//    }

    public List<Team> getAllTeams() {
        return teamRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Team getTeamById(String id) {
        return teamRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Team not found"));
    }

    public Team addTeam(Team team) {
        if (!teamRepo.findByName(team.getName()).isEmpty()) {
            throw new IllegalStateException("Team with name: " + team.getName() + " already exists");
        }
        return teamRepo.save(team);
    }


    public Team updateTeam(String id, Team teamDetails) {
        Team teamToUpdate = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id of: " + id));
        teamToUpdate.setName(teamDetails.getName());
        teamToUpdate.setLogo(teamDetails.getLogo());
//        teamToUpdate.setCoach(teamDetails.getCoach());
//        da li treba da radim update coach-a i igraca?
//        Mozda da imam neku metodu assignCoachToTeam ili assignPlayerToTeam?
        return teamRepo.save(teamToUpdate);
    }

    public void deleteTeam(String id) {
        if(teamRepo.findById(id).isEmpty()) {
            throw new TeamNotFoundException("Team with id '" + id + "' not found.");
        }
        teamRepo.deleteById(id);
    }

    public List<Team> searchTeams(String name) {
        return teamRepo.findByName(name);
    }

    public List<Player> getPlayersOfTheTeam(String id) {
        Team team = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));

        return team.getListOfPlayers();
    }

    public Coach getTeamCoach(String id) {
        Team team = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found wtih id: " + id));

        return team.getCoach();
    }

    public List<Match> getPastMatches(String id) {
        Team team = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
        return matchRepo.findByIdAndDateBefore(id, LocalDate.now());
    }

    public List<Match> getUpcomingMatches(String id) {
        Team team = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));

        return matchRepo.findByIdAndDateAfter(id, LocalDate.now());
    }

    public void assignPlayerToTeam(String teamId, String playerId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found wiht id: " + playerId));

        player.setTeam(team);
        playerRepo.save(player);
    }

    public void removePlayerFromTeam(String teamId, String playerId) {
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

//        mozda da uradim neku proveru kog igraca brisem (da li igrac pripada tom timu)
        player.setTeam(null);
        playerRepo.save(player);
    }

    public void assignCoachToTeam(String teamId, String coachId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        Coach coach = coachRepo.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with id: " + coachId));

        team.setCoach(coach);
        teamRepo.save(team);
    }

    public void removeCoachFromTeam(String teamId, String coachId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        team.setCoach(null);
        teamRepo.save(team);
    }
}
