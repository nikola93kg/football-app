package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Season;
import com.fifa.footballApp.service.SeasonService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping
    public List<Season> getSeasons(@RequestParam(required = false) Boolean active) {
        if (Boolean.TRUE.equals(active)) {
            return seasonService.getActiveSeasons();
        }
        return seasonService.getAllSeasons();
    }

    @GetMapping("/{id}")
    public Season getSeasonById(@PathVariable String id) {
        return seasonService.getSeasonById(id);
    }

    @PostMapping
    public Season createSeason(@RequestBody Season season) {
        return seasonService.createSeason(season);
    }

    @PutMapping("/{id}")
    public Season updateSeason(@PathVariable String id, @RequestBody Season seasonDetails) {
        return seasonService.updateSeason(id, seasonDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteSeason(@PathVariable String id) {
        seasonService.deleteSeason(id);
    }
}
