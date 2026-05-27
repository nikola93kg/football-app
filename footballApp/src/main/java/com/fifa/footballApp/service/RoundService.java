package com.fifa.footballApp.service;

import com.fifa.footballApp.model.Competition;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.Round;
import com.fifa.footballApp.repository.CompetitionRepository;
import com.fifa.footballApp.repository.MatchRepo;
import com.fifa.footballApp.repository.RoundRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final CompetitionRepository competitionRepository;
    private final MatchRepo matchRepo;

    public RoundService(RoundRepository roundRepository, CompetitionRepository competitionRepository, MatchRepo matchRepo) {
        this.roundRepository = roundRepository;
        this.competitionRepository = competitionRepository;
        this.matchRepo = matchRepo;
    }

    public List<Round> getRoundsByCompetition(String competitionId) {
        return roundRepository.findByCompetitionCompetitionIdOrderByRoundNumberAsc(competitionId);
    }

    public Round getRoundById(String id) {
        return roundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Round not found with id: " + id));
    }

    public Round createRound(String competitionId, Round round) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + competitionId));
        round.setCompetition(competition);
        return roundRepository.save(round);
    }

    public Round updateRound(String id, Round roundDetails) {
        Round round = getRoundById(id);
        round.setRoundNumber(roundDetails.getRoundNumber());
        round.setName(roundDetails.getName());
        round.setStartDate(roundDetails.getStartDate());
        round.setEndDate(roundDetails.getEndDate());
        return roundRepository.save(round);
    }

    public void deleteRound(String id) {
        if (!roundRepository.existsById(id)) {
            throw new EntityNotFoundException("Round not found with id: " + id);
        }
        roundRepository.deleteById(id);
    }

    public List<Match> getRoundMatches(String id) {
        getRoundById(id);
        return matchRepo.findByRoundIdOrderByDateAscTimeAsc(id);
    }
}
