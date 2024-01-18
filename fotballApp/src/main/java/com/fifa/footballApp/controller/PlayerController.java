package com.fifa.footballApp.controller;

import com.fifa.footballApp.dto.PlayerStats;
import com.fifa.footballApp.dto.TransferDetails;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.repository.PlayerRepo;
import com.fifa.footballApp.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player playerDetails) {
        return playerService.updatePlayer(id, playerDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    @GetMapping("/search")
    public List<Player> searchPlayers(String name, String nationality) {
        return playerService.searchPlayers(name, nationality);
    }

    @GetMapping("/{id}/statistics")
//    da li bi ovde trebalo DTO? (PlayerStats)
    public MatchEvent getPlayerStatistics(@PathVariable Long id) {
        return playerService.getPlayerStatistics(id);

    }

    @GetMapping("/{id}/stats")
    public PlayerStats getPlayerStats(@PathVariable Long id) {
        return playerService.getPlayerStats(id);
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<?> transferPlayer(@PathVariable Long id, @RequestParam Long toTeamId, @RequestBody TransferDetails transferDetails ) {
        playerService.transferPlayer(id, toTeamId, transferDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/matches")
    public List<Match> getPlayerMatches(@PathVariable Long id) {
        return playerService.getPlayerMatches(id);
    }

}
