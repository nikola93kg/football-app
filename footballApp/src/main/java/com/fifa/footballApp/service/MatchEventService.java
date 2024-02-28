package com.fifa.footballApp.service;

import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.model.Referee;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.repository.RefereeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchEventService {

    private final MatchEventRepo matchEventRepo;
    private final RefereeRepo refereeRepo;

    public MatchEventService(MatchEventRepo matchEventRepo, RefereeRepo refereeRepo) {
        this.matchEventRepo = matchEventRepo;
        this.refereeRepo = refereeRepo;
    }

    public List<MatchEvent> getAllMatchEvents() {
        return matchEventRepo.findAll();
    }

    public MatchEvent recordMatchEvent(MatchEvent matchEvent) {
        MatchEvent savedEvent = matchEventRepo.save(matchEvent); //da sacuva match event prvo
        updateRefereeStatistics(matchEvent); //azuriranje statistike sudije ako je dogadjaj karton
        return savedEvent;
    }

    private void updateRefereeStatistics(MatchEvent matchEvent) {
        if(matchEvent.getEventType() == MatchEventType.YELLOW_CARD || matchEvent.getEventType() == MatchEventType.RED_CARD) {
            Referee referee = matchEvent.getReferee();
//            mozda ovo sve da stavim u if referee != null?
            if (matchEvent.getEventType() == MatchEventType.YELLOW_CARD) {
                referee.setTotalYellowCardsGiven(referee.getTotalRedCardsGiven() + 1);
            } else {
                referee.setTotalRedCardsGiven(referee.getTotalRedCardsGiven() + 1);
            }
            refereeRepo.save(referee);
        }

    }

    public MatchEvent createMatchEvent(MatchEvent matchEvent) {
        MatchEvent savedEvent = matchEventRepo.save(matchEvent);
        updateRefereeStatistics(matchEvent);
        return savedEvent;
    }

    public List<MatchEvent> searchMatchEvent(Long playerId) {
        return matchEventRepo.findByPlayerId(playerId);
    }

    public MatchEvent updateMatchEvent(Long id, MatchEvent matchEventDetails) {
        MatchEvent event = matchEventRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match Event not found wit id: " + id));

        event.setEventTime(matchEventDetails.getEventTime());

        return matchEventRepo.save(event);
    }

    public void deleteMatchEvent(Long id) {
        matchEventRepo.deleteById(id);
    }
}
