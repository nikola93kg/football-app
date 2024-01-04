package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.service.MatchEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/match-events")
public class MatchEventController {

    private final MatchEventService matchEventService;

    public MatchEventController(MatchEventService matchEventService) {
        this.matchEventService = matchEventService;
    }

    @GetMapping
    public List<MatchEvent> getAllMatchEvents() {
        return matchEventService.getAllMatchEvents();
    }
}
