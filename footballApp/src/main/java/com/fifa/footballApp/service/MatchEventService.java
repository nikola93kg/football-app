package com.fifa.footballApp.service;

import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.model.Referee;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.repository.MatchRepo;
import com.fifa.footballApp.repository.RefereeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchEventService {

    private final MatchEventRepo matchEventRepo;
    private final RefereeRepo refereeRepo;
    private final MatchRepo matchRepo;

    public MatchEventService(MatchEventRepo matchEventRepo, RefereeRepo refereeRepo, MatchRepo matchRepo) {
        this.matchEventRepo = matchEventRepo;
        this.refereeRepo = refereeRepo;
        this.matchRepo = matchRepo;
    }

    public List<MatchEvent> getAllMatchEvents() {
        return matchEventRepo.findAll();
    }

    public MatchEvent recordMatchEvent(MatchEvent matchEvent) {
        return createMatchEvent(matchEvent);
    }

    private void updateRefereeStatistics(MatchEvent matchEvent) {
        if(matchEvent.getEventType() == MatchEventType.YELLOW_CARD ||
                matchEvent.getEventType() == MatchEventType.SECOND_YELLOW_CARD ||
                matchEvent.getEventType() == MatchEventType.RED_CARD) {
            Referee referee = matchEvent.getReferee();
            if (referee == null) {
                return;
            }
            if (matchEvent.getEventType() == MatchEventType.YELLOW_CARD ||
                    matchEvent.getEventType() == MatchEventType.SECOND_YELLOW_CARD) {
                referee.setTotalYellowCardsGiven(referee.getTotalYellowCardsGiven() + 1);
            } else {
                referee.setTotalRedCardsGiven(referee.getTotalRedCardsGiven() + 1);
            }
            refereeRepo.save(referee);
        }

    }

    public MatchEvent createMatchEvent(MatchEvent matchEvent) {
        if (matchEvent.getRecordedAt() == null) {
            matchEvent.setRecordedAt(LocalDateTime.now());
        }
        MatchEvent savedEvent = matchEventRepo.save(matchEvent);
        updateRefereeStatistics(matchEvent);
        updateMatchScore(matchEvent);
        return savedEvent;
    }

    private void updateMatchScore(MatchEvent matchEvent) {
        if (!isScoringEvent(matchEvent) || matchEvent.getMatch() == null || matchEvent.getTeam() == null) {
            return;
        }

        Match match = matchRepo.findById(matchEvent.getMatch().getId())
                .orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + matchEvent.getMatch().getId()));

        int homeScore = match.getHomeScore() == null ? 0 : match.getHomeScore();
        int awayScore = match.getAwayScore() == null ? 0 : match.getAwayScore();

        boolean eventTeamIsHome = match.getHomeTeam() != null &&
                match.getHomeTeam().getId().equals(matchEvent.getTeam().getId());
        boolean eventTeamIsAway = match.getAwayTeam() != null &&
                match.getAwayTeam().getId().equals(matchEvent.getTeam().getId());

        if (matchEvent.getEventType() == MatchEventType.OWN_GOAL) {
            if (eventTeamIsHome) {
                awayScore++;
            } else if (eventTeamIsAway) {
                homeScore++;
            }
        } else {
            if (eventTeamIsHome) {
                homeScore++;
            } else if (eventTeamIsAway) {
                awayScore++;
            }
        }

        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setScore(homeScore + "-" + awayScore);
        matchRepo.save(match);
    }

    private boolean isScoringEvent(MatchEvent matchEvent) {
        return matchEvent.getEventType() == MatchEventType.GOAL ||
                matchEvent.getEventType() == MatchEventType.GOAL_FROM_PENALTY ||
                matchEvent.getEventType() == MatchEventType.OWN_GOAL;
    }

    public List<MatchEvent> searchMatchEvent(String playerId) {
        return matchEventRepo.findByPlayerId(playerId);
    }

    public MatchEvent updateMatchEvent(String id, MatchEvent matchEventDetails) {
        MatchEvent event = matchEventRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match Event not found wit id: " + id));

        event.setEventTime(matchEventDetails.getEventTime());
        event.setMinute(matchEventDetails.getMinute());
        event.setAdditionalMinute(matchEventDetails.getAdditionalMinute());
        event.setNote(matchEventDetails.getNote());
        event.setRecordedBy(matchEventDetails.getRecordedBy());
        event.setAssistPlayer(matchEventDetails.getAssistPlayer());
        event.setRelatedPlayer(matchEventDetails.getRelatedPlayer());

        return matchEventRepo.save(event);
    }

    public void deleteMatchEvent(String id) {
        matchEventRepo.deleteById(id);
    }
}
