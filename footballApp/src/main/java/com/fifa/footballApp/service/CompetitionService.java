package com.fifa.footballApp.service;

import com.fifa.footballApp.dto.StandingRow;
import com.fifa.footballApp.enums.MatchStatus;
import com.fifa.footballApp.model.Competition;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.repository.CompetitionRepository;
import com.fifa.footballApp.repository.MatchRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final MatchRepo matchRepo;

    public CompetitionService(CompetitionRepository competitionRepository, MatchRepo matchRepo) {
        this.competitionRepository = competitionRepository;
        this.matchRepo = matchRepo;
    }

    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAll();
    }

    public Competition getCompetitionById(String id) {
        return competitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + id));
    }

    public Competition createCompetition(Competition competition) {
        return competitionRepository.save(competition);
    }

    public Competition updateCompetition(String id, Competition competitionDetails) {
        Competition competition = getCompetitionById(id);
        competition.setName(competitionDetails.getName());
        competition.setCurrentWinner(competitionDetails.getCurrentWinner());
        competition.setPrizeAmount(competitionDetails.getPrizeAmount());
        competition.setDescription(competitionDetails.getDescription());
        return competitionRepository.save(competition);
    }

    public void deleteCompetition(String id) {
        if (!competitionRepository.existsById(id)) {
            throw new EntityNotFoundException("Competition not found with id: " + id);
        }
        competitionRepository.deleteById(id);
    }

    public List<Match> getCompetitionMatches(String competitionId) {
        getCompetitionById(competitionId);
        return matchRepo.findByCompetitionCompetitionIdOrderByDateAscTimeAsc(competitionId);
    }

    public List<Match> getCompetitionMatchesByStatus(String competitionId, MatchStatus status) {
        getCompetitionById(competitionId);
        if (status == null) {
            return getCompetitionMatches(competitionId);
        }
        return matchRepo.findByCompetitionCompetitionIdAndStatusOrderByDateAscTimeAsc(competitionId, status);
    }

    @Transactional(readOnly = true)
    public List<StandingRow> getStandings(String competitionId) {
        Competition competition = getCompetitionById(competitionId);
        Map<String, StandingRow> rows = new LinkedHashMap<>();

        if (competition.getParticipants() != null) {
            for (Team team : competition.getParticipants()) {
                rows.put(team.getId(), new StandingRow(team));
            }
        }

        List<Match> finishedMatches = matchRepo.findByCompetitionCompetitionIdAndStatusOrderByDateAscTimeAsc(
                competitionId,
                MatchStatus.FINISHED
        );

        for (Match match : finishedMatches) {
            if (match.getHomeTeam() == null || match.getAwayTeam() == null ||
                    match.getHomeScore() == null || match.getAwayScore() == null) {
                continue;
            }

            StandingRow homeRow = rows.computeIfAbsent(match.getHomeTeam().getId(),
                    id -> new StandingRow(match.getHomeTeam()));
            StandingRow awayRow = rows.computeIfAbsent(match.getAwayTeam().getId(),
                    id -> new StandingRow(match.getAwayTeam()));

            homeRow.recordMatch(match.getHomeScore(), match.getAwayScore());
            awayRow.recordMatch(match.getAwayScore(), match.getHomeScore());
        }

        return rows.values().stream()
                .sorted(Comparator.comparingInt(StandingRow::getPoints).reversed()
                        .thenComparing(Comparator.comparingInt(StandingRow::getGoalDifference).reversed())
                        .thenComparing(Comparator.comparingInt(StandingRow::getGoalsFor).reversed())
                        .thenComparing(row -> row.getTeam().getName()))
                .toList();
    }
}
