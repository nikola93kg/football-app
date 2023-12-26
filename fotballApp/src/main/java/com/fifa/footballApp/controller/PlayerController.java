package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.repository.PlayerRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerRepo playerRepo;

    public PlayerController(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }
}
