package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.service.MatchEventService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public MatchEvent createMatchEvent(@RequestBody MatchEvent matchEvent) {
        return matchEventService.createMatchEvent(matchEvent);
    }

    @GetMapping("/search")
    public List<MatchEvent> searchMatchEvent(@RequestParam String playerId) {
        return matchEventService.searchMatchEvent(playerId);
    }

    @PutMapping("/{id}")
    public MatchEvent updateMatchEvent(@PathVariable String id, @RequestBody MatchEvent matchEventDetails) {
        return matchEventService.updateMatchEvent(id, matchEventDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteMatchEvent(@PathVariable String id) {
        matchEventService.deleteMatchEvent(id);
    }
}
