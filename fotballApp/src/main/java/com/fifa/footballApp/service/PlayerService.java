package com.fifa.footballApp.service;

import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.repository.PlayerRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepo playerRepo;

    public PlayerService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }
}
