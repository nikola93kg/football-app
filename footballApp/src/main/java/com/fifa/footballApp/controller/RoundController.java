package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.Round;
import com.fifa.footballApp.service.RoundService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rounds")
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("/{id}")
    public Round getRoundById(@PathVariable String id) {
        return roundService.getRoundById(id);
    }

    @PutMapping("/{id}")
    public Round updateRound(@PathVariable String id, @RequestBody Round roundDetails) {
        return roundService.updateRound(id, roundDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteRound(@PathVariable String id) {
        roundService.deleteRound(id);
    }

    @GetMapping("/{id}/matches")
    public List<Match> getRoundMatches(@PathVariable String id) {
        return roundService.getRoundMatches(id);
    }
}
