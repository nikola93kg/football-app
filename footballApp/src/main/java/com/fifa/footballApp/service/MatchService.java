package com.fifa.footballApp.service;

import com.fifa.footballApp.enums.MatchStatus;
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
        match.setScore(matchDetails.getScore());
        match.setHomeScore(matchDetails.getHomeScore());
        match.setAwayScore(matchDetails.getAwayScore());
        match.setRoundNumber(matchDetails.getRoundNumber());
        match.setCurrentMinute(matchDetails.getCurrentMinute());
        match.setStatus(matchDetails.getStatus());
        match.setCompetition(matchDetails.getCompetition());
        match.setRound(matchDetails.getRound());
        match.setHomeTeam(matchDetails.getHomeTeam());
        match.setAwayTeam(matchDetails.getAwayTeam());
        match.setReferee(matchDetails.getReferee());
        match.setHomeTeamFormation(matchDetails.getHomeTeamFormation());
        match.setAwayTeamFormation(matchDetails.getAwayTeamFormation());

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

    public Match startMatch(String id) {
        Match match = getMatchById(id);
        match.setStatus(MatchStatus.LIVE);
        match.setCurrentMinute(0);
        if (match.getHomeScore() == null) {
            match.setHomeScore(0);
        }
        if (match.getAwayScore() == null) {
            match.setAwayScore(0);
        }
        updateScoreLabel(match);
        return matchRepo.save(match);
    }

    public Match setHalfTime(String id) {
        Match match = getMatchById(id);
        match.setStatus(MatchStatus.HALF_TIME);
        match.setCurrentMinute(45);
        return matchRepo.save(match);
    }

    public Match resumeMatch(String id) {
        Match match = getMatchById(id);
        match.setStatus(MatchStatus.LIVE);
        if (match.getCurrentMinute() == null || match.getCurrentMinute() < 45) {
            match.setCurrentMinute(45);
        }
        return matchRepo.save(match);
    }

    public Match finishMatch(String id) {
        Match match = getMatchById(id);
        match.setStatus(MatchStatus.FINISHED);
        match.setCurrentMinute(90);
        updateScoreLabel(match);
        return matchRepo.save(match);
    }

    public Match postponeMatch(String id) {
        Match match = getMatchById(id);
        match.setStatus(MatchStatus.POSTPONED);
        return matchRepo.save(match);
    }

    private void updateScoreLabel(Match match) {
        if (match.getHomeScore() != null && match.getAwayScore() != null) {
            match.setScore(match.getHomeScore() + "-" + match.getAwayScore());
        }
    }

}
