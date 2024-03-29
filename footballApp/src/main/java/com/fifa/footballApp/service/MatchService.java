package com.fifa.footballApp.service;

import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.repository.MatchRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepo matchRepo;
    private final MatchEventRepo matchEventRepo;
    public MatchService(MatchRepo matchRepo, MatchEventRepo matchEventRepo) {
        this.matchRepo = matchRepo;
        this.matchEventRepo = matchEventRepo;
    }

    public List<Match> getAllMatches() {
        return matchRepo.findAll();
    }

    public Match getMatchById(String id) {
        return matchRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found with id " + id));
    }

    public Match createMatch(Match match) {
        return matchRepo.save(match);
    }

    public Match updateMatch(String id, Match matchDetails) {
        Match match = matchRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not foudn with id " + id));

        match.setDate(matchDetails.getDate());
        match.setTime(matchDetails.getTime());
        match.setLocation(matchDetails.getLocation());
        match.setStadium(matchDetails.getStadium());

        return matchRepo.save(match);
    }

    public void deleteMatch(String id) {
//    odradi prvo check pa onda delete
        if(matchRepo.existsById(id)) {
            matchRepo.deleteById(id);
        }else {
            throw new EntityNotFoundException("Match not found with id " + id);
        }
    }

    public List<Match> searchMatches(String stadium) {
        if(stadium == null) {
            return matchRepo.findAll();
        }
        return matchRepo.findByStadiumContaining(stadium);
    }

    public List<MatchEvent> getMatchEvents(String matchId) {
        if(matchId == null) {
            throw new IllegalArgumentException("Match id can't be null");
        }
        return matchEventRepo.findByMatchId(matchId);
    }

}
