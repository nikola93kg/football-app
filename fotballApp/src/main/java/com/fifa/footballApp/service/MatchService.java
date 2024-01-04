package com.fifa.footballApp.service;

import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.repository.MatchRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepo matchRepo;
    public MatchService(MatchRepo matchRepo) {
        this.matchRepo = matchRepo;
    }

    public List<Match> getAllMatches() {
        return matchRepo.findAll();
    }
}
