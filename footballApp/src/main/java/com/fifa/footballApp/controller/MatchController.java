package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.service.MatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable String id) {
        return matchService.getMatchById(id);
    }

    @PostMapping
    public Match createMatch(@RequestBody Match match) {
        return matchService.createMatch(match);
    }

    @PutMapping("/{id}")
    public Match updateMatch(@PathVariable String id, @RequestBody Match matchDetails) {
        return matchService.updateMatch(id, matchDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable String id) {
        matchService.deleteMatch(id);
    }

    @GetMapping("/search")
    public List<Match> searchMatches(String stadium) {
        return matchService.searchMatches(stadium);
    }

    @GetMapping("{id}/events")
    public List<MatchEvent> getMatchEvents(@PathVariable String id) {
        return matchService.getMatchEvents(id);
    }

    @PostMapping("/{id}/start")
    public Match startMatch(@PathVariable String id) {
        return matchService.startMatch(id);
    }

    @PostMapping("/{id}/half-time")
    public Match setHalfTime(@PathVariable String id) {
        return matchService.setHalfTime(id);
    }

    @PostMapping("/{id}/resume")
    public Match resumeMatch(@PathVariable String id) {
        return matchService.resumeMatch(id);
    }

    @PostMapping("/{id}/finish")
    public Match finishMatch(@PathVariable String id) {
        return matchService.finishMatch(id);
    }

    @PostMapping("/{id}/postpone")
    public Match postponeMatch(@PathVariable String id) {
        return matchService.postponeMatch(id);
    }
}
