package com.fifa.footballApp.service;

import com.fifa.footballApp.dto.PlayerStats;
import com.fifa.footballApp.dto.TransferDetails;
import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.repository.PlayerRepo;
import com.fifa.footballApp.repository.TeamRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerRepo playerRepo;
    private final MatchEventRepo matchEventRepo;
    private final TeamRepo teamRepo;

    public PlayerService(PlayerRepo playerRepo, MatchEventRepo matchEventRepo, TeamRepo teamRepo) {
        this.playerRepo = playerRepo;
        this.matchEventRepo = matchEventRepo;
        this.teamRepo = teamRepo;
    }

    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }

    public Player getPlayerById(String id) {
        return playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }

    public Player createPlayer(Player player) {
        return playerRepo.save(player);
    }

    public Player updatePlayer(String id, Player playerDetails) {
        Player player = playerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));

        player.setName(playerDetails.getName());
        player.setDefaultPosition(playerDetails.getDefaultPosition());
        player.setAge(playerDetails.getAge());
        player.setNationality(playerDetails.getNationality());
        player.setJerseyNumber(playerDetails.getJerseyNumber());

        return playerRepo.save(player);
    }

    public void deletePlayer(String id) {
        if (!playerRepo.existsById(id)) {
            throw new EntityNotFoundException("Player not found with id: " + id);
        }
        playerRepo.deleteById(id);
    }

    public List<Player> searchPlayers(String name, String nationality) {
        return playerRepo.findByNameContainingAndNationality(name, nationality);
    }

    public MatchEvent getPlayerStatistics(String id) {
        return null;
    }

    public PlayerStats getPlayerStats(String playerId) {
        List<MatchEvent> playerEvents = matchEventRepo.findByPlayerId(playerId);

//        mozda ovde da prodjem kroz neku petlju (switch npr)

        PlayerStats stats = new PlayerStats();
        stats.setTotalGoals(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.GOAL).count());
        stats.setTotalAssists(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.ASSIST).count());
        stats.setTotalShotsOnGoal(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.SHOT_ON_GOAL).count());
        stats.setTotalShotsOutOfGoal(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.SHOT_OUT_OF_GOAL).count());
        stats.setTotalShots(stats.getTotalShotsOnGoal() + stats.getTotalShotsOutOfGoal()); // ovo bi bilo van petlje
        stats.setTotalFreeKicks(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.FREE_KICK).count());
        stats.setTotalOffsides(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.OFFSIDE).count());
        stats.setMissedPenalties(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.MISSED_PENALTY).count());
        stats.setGoalsFromPenalty(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.GOAL_FROM_PENALTY).count());
        stats.setTotalYellowCards(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.YELLOW_CARD).count());
        stats.setTotalRedCards(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.RED_CARD).count());
        stats.setSubIns(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.PLAYER_SUB_IN).count());
        stats.setSubOuts(playerEvents.stream().filter(e -> e.getEventType() == MatchEventType.PLAYER_SUB_OUT).count());

        return stats;
    }

    public void transferPlayer(String playerId, String toTeamId, TransferDetails transferDetails) {
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));
        Team newTeam = teamRepo.findById(toTeamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + toTeamId));

        player.setTeam(newTeam);
        player.setTransferFee(transferDetails.getTransferFee());
        player.setTransferDate(transferDetails.getTransferDate());
        playerRepo.save(player);
    }

    public List<Match> getPlayerMatches(String playerId) {
        // proveri da li igrac postoji

        List<MatchEvent> events = matchEventRepo.findByPlayerId(playerId);

        return events.stream()
                .map(MatchEvent::getMatch)
                .distinct() //da se izbegne dupliranje utakmica
                .collect(Collectors.toList());
    }
}
