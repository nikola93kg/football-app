package com.fifa.footballApp.service;

import com.fifa.footballApp.model.Season;
import com.fifa.footballApp.repository.SeasonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;

    public SeasonService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public List<Season> getAllSeasons() {
        return seasonRepository.findAll();
    }

    public List<Season> getActiveSeasons() {
        return seasonRepository.findByActiveTrue();
    }

    public Season getSeasonById(String id) {
        return seasonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Season not found with id: " + id));
    }

    public Season createSeason(Season season) {
        return seasonRepository.save(season);
    }

    public Season updateSeason(String id, Season seasonDetails) {
        Season season = getSeasonById(id);
        season.setName(seasonDetails.getName());
        season.setStartDate(seasonDetails.getStartDate());
        season.setEndDate(seasonDetails.getEndDate());
        season.setActive(seasonDetails.isActive());
        return seasonRepository.save(season);
    }

    public void deleteSeason(String id) {
        if (!seasonRepository.existsById(id)) {
            throw new EntityNotFoundException("Season not found with id: " + id);
        }
        seasonRepository.deleteById(id);
    }
}
