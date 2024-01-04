package com.fifa.footballApp.service;

import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.repository.MatchEventRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchEventService {

    private final MatchEventRepo matchEventRepo;

    public MatchEventService(MatchEventRepo matchEventRepo) {
        this.matchEventRepo = matchEventRepo;
    }

    public List<MatchEvent> getAllMatchEvents() {
        return matchEventRepo.findAll();
    }
}
