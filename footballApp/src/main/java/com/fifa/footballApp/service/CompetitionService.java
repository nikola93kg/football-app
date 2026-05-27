package com.fifa.footballApp.service;

import com.fifa.footballApp.dto.CompetitionPlayerStats;
import com.fifa.footballApp.dto.CompetitionTeamStats;
import com.fifa.footballApp.dto.StandingRow;
import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.enums.MatchStatus;
import com.fifa.footballApp.model.Competition;
import com.fifa.footballApp.model.CompetitionTeam;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.PlayerRegistration;
import com.fifa.footballApp.model.Round;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.repository.CompetitionRepository;
import com.fifa.footballApp.repository.CompetitionTeamRepository;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.repository.MatchRepo;
import com.fifa.footballApp.repository.PlayerRegistrationRepository;
import com.fifa.footballApp.repository.RoundRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final MatchRepo matchRepo;
    private final MatchEventRepo matchEventRepo;
    private final RoundRepository roundRepository;
    private final CompetitionTeamRepository competitionTeamRepository;
    private final PlayerRegistrationRepository playerRegistrationRepository;

    public CompetitionService(CompetitionRepository competitionRepository,
                              MatchRepo matchRepo,
                              MatchEventRepo matchEventRepo,
                              RoundRepository roundRepository,
                              CompetitionTeamRepository competitionTeamRepository,
                              PlayerRegistrationRepository playerRegistrationRepository) {
        this.competitionRepository = competitionRepository;
        this.matchRepo = matchRepo;
        this.matchEventRepo = matchEventRepo;
        this.roundRepository = roundRepository;
        this.competitionTeamRepository = competitionTeamRepository;
        this.playerRegistrationRepository = playerRegistrationRepository;
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
        competition.setSeason(competitionDetails.getSeason());
        competition.setStatus(competitionDetails.getStatus());
        competition.setType(competitionDetails.getType());
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

    public List<Round> getCompetitionRounds(String competitionId) {
        getCompetitionById(competitionId);
        return roundRepository.findByCompetitionCompetitionIdOrderByRoundNumberAsc(competitionId);
    }

    public Round createCompetitionRound(String competitionId, Round round) {
        Competition competition = getCompetitionById(competitionId);
        round.setCompetition(competition);
        return roundRepository.save(round);
    }

    public List<CompetitionTeam> getCompetitionTeams(String competitionId) {
        getCompetitionById(competitionId);
        return competitionTeamRepository.findByCompetitionCompetitionIdOrderBySeedNumberAsc(competitionId);
    }

    public List<PlayerRegistration> getCompetitionPlayerRegistrations(String competitionId, String teamId) {
        getCompetitionById(competitionId);
        if (teamId == null) {
            return playerRegistrationRepository.findByCompetitionCompetitionId(competitionId);
        }
        return playerRegistrationRepository.findByCompetitionCompetitionIdAndTeamId(competitionId, teamId);
    }

    @Transactional(readOnly = true)
    public List<StandingRow> getStandings(String competitionId) {
        Competition competition = getCompetitionById(competitionId);
        Map<String, StandingRow> rows = new LinkedHashMap<>();

        List<CompetitionTeam> competitionTeams = competitionTeamRepository.findByCompetitionCompetitionIdOrderBySeedNumberAsc(competitionId);
        for (CompetitionTeam competitionTeam : competitionTeams) {
            if (!competitionTeam.isActive() || competitionTeam.getTeam() == null) {
                continue;
            }

            StandingRow row = rows.computeIfAbsent(competitionTeam.getTeam().getId(),
                    id -> new StandingRow(competitionTeam.getTeam()));
            row.applyPenaltyPoints(safeInt(competitionTeam.getPenaltyPoints()));
        }

        if (competition.getParticipants() != null) {
            for (Team team : competition.getParticipants()) {
                rows.computeIfAbsent(team.getId(), id -> new StandingRow(team));
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

    @Transactional(readOnly = true)
    public List<CompetitionTeamStats> getCompetitionTeamStats(String competitionId) {
        Competition competition = getCompetitionById(competitionId);
        Map<String, CompetitionTeamStats> rows = new LinkedHashMap<>();

        List<CompetitionTeam> competitionTeams = competitionTeamRepository.findByCompetitionCompetitionIdOrderBySeedNumberAsc(competitionId);
        for (CompetitionTeam competitionTeam : competitionTeams) {
            if (!competitionTeam.isActive() || competitionTeam.getTeam() == null) {
                continue;
            }

            CompetitionTeamStats row = rows.computeIfAbsent(competitionTeam.getTeam().getId(),
                    id -> new CompetitionTeamStats(competitionTeam.getTeam()));
            row.applyPenaltyPoints(safeInt(competitionTeam.getPenaltyPoints()));
        }

        if (competition.getParticipants() != null) {
            for (Team team : competition.getParticipants()) {
                rows.computeIfAbsent(team.getId(), id -> new CompetitionTeamStats(team));
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

            CompetitionTeamStats homeRow = rows.computeIfAbsent(match.getHomeTeam().getId(),
                    id -> new CompetitionTeamStats(match.getHomeTeam()));
            CompetitionTeamStats awayRow = rows.computeIfAbsent(match.getAwayTeam().getId(),
                    id -> new CompetitionTeamStats(match.getAwayTeam()));

            homeRow.recordMatch(match.getHomeScore(), match.getAwayScore());
            awayRow.recordMatch(match.getAwayScore(), match.getHomeScore());
        }

        List<MatchEvent> events = matchEventRepo.findByMatchCompetitionCompetitionId(competitionId);
        for (MatchEvent event : events) {
            if (event.getTeam() == null) {
                continue;
            }

            CompetitionTeamStats row = rows.computeIfAbsent(event.getTeam().getId(),
                    id -> new CompetitionTeamStats(event.getTeam()));
            row.recordDiscipline(event.getEventType());
        }

        return rows.values().stream()
                .sorted(teamStatsComparator())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompetitionPlayerStats> getCompetitionPlayerStats(String competitionId) {
        getCompetitionById(competitionId);
        List<MatchEvent> events = matchEventRepo.findByMatchCompetitionCompetitionId(competitionId);
        Map<String, CompetitionPlayerStats> rows = new LinkedHashMap<>();
        Map<String, Set<String>> appearances = new HashMap<>();

        for (MatchEvent event : events) {
            recordPlayerEvent(rows, appearances, event.getPlayer(), event.getTeam(), event.getMatch(), event.getEventType());

            if (isScoringEvent(event.getEventType()) && event.getAssistPlayer() != null) {
                CompetitionPlayerStats assistRow = getPlayerStatsRow(rows, event.getAssistPlayer(), event.getAssistPlayer().getTeam());
                assistRow.recordAssist();
                recordAppearance(appearances, event.getAssistPlayer(), event.getMatch());
            }

            if (event.getRelatedPlayer() != null) {
                getPlayerStatsRow(rows, event.getRelatedPlayer(), event.getRelatedPlayer().getTeam());
                recordAppearance(appearances, event.getRelatedPlayer(), event.getMatch());
            }
        }

        for (Map.Entry<String, CompetitionPlayerStats> entry : rows.entrySet()) {
            Set<String> playerAppearances = appearances.get(entry.getKey());
            entry.getValue().setAppearances(playerAppearances == null ? 0 : playerAppearances.size());
        }

        return rows.values().stream()
                .sorted(playerStatsComparator())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompetitionPlayerStats> getCompetitionTopScorers(String competitionId) {
        return getCompetitionPlayerStats(competitionId).stream()
                .filter(stats -> stats.getGoals() > 0)
                .sorted(Comparator.comparingInt(CompetitionPlayerStats::getGoals).reversed()
                        .thenComparing(Comparator.comparingInt(CompetitionPlayerStats::getAssists).reversed())
                        .thenComparing(this::playerName))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompetitionPlayerStats> getCompetitionTopAssists(String competitionId) {
        return getCompetitionPlayerStats(competitionId).stream()
                .filter(stats -> stats.getAssists() > 0)
                .sorted(Comparator.comparingInt(CompetitionPlayerStats::getAssists).reversed()
                        .thenComparing(Comparator.comparingInt(CompetitionPlayerStats::getGoals).reversed())
                        .thenComparing(this::playerName))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompetitionPlayerStats> getCompetitionDisciplineTable(String competitionId) {
        return getCompetitionPlayerStats(competitionId).stream()
                .filter(stats -> stats.getYellowCards() > 0 || stats.getSecondYellowCards() > 0 || stats.getRedCards() > 0)
                .sorted(Comparator.comparingInt(this::disciplineRedCards).reversed()
                        .thenComparing(Comparator.comparingInt(CompetitionPlayerStats::getYellowCards).reversed())
                        .thenComparing(this::playerName))
                .toList();
    }

    private void recordPlayerEvent(Map<String, CompetitionPlayerStats> rows,
                                   Map<String, Set<String>> appearances,
                                   Player player,
                                   Team eventTeam,
                                   Match match,
                                   MatchEventType eventType) {
        if (player == null) {
            return;
        }

        CompetitionPlayerStats row = getPlayerStatsRow(rows, player, eventTeam);
        row.recordEvent(eventType);
        recordAppearance(appearances, player, match);
    }

    private CompetitionPlayerStats getPlayerStatsRow(Map<String, CompetitionPlayerStats> rows, Player player, Team eventTeam) {
        Team team = eventTeam == null ? player.getTeam() : eventTeam;
        return rows.computeIfAbsent(player.getId(), id -> new CompetitionPlayerStats(player, team));
    }

    private void recordAppearance(Map<String, Set<String>> appearances, Player player, Match match) {
        if (player == null || player.getId() == null || match == null || match.getId() == null) {
            return;
        }

        appearances.computeIfAbsent(player.getId(), id -> new HashSet<>()).add(match.getId());
    }

    private Comparator<CompetitionPlayerStats> playerStatsComparator() {
        return Comparator.comparingInt(CompetitionPlayerStats::getGoals).reversed()
                .thenComparing(Comparator.comparingInt(CompetitionPlayerStats::getAssists).reversed())
                .thenComparing(Comparator.comparingInt(CompetitionPlayerStats::getYellowCards).reversed())
                .thenComparing(this::playerName);
    }

    private Comparator<CompetitionTeamStats> teamStatsComparator() {
        return Comparator.comparingInt(CompetitionTeamStats::getPoints).reversed()
                .thenComparing(Comparator.comparingInt(CompetitionTeamStats::getGoalDifference).reversed())
                .thenComparing(Comparator.comparingInt(CompetitionTeamStats::getGoalsFor).reversed())
                .thenComparing(this::teamName);
    }

    private boolean isScoringEvent(MatchEventType eventType) {
        return eventType == MatchEventType.GOAL ||
                eventType == MatchEventType.GOAL_FROM_PENALTY ||
                eventType == MatchEventType.OWN_GOAL;
    }

    private int disciplineRedCards(CompetitionPlayerStats stats) {
        return stats.getRedCards() + stats.getSecondYellowCards();
    }

    private String playerName(CompetitionPlayerStats stats) {
        return stats.getPlayer() == null || stats.getPlayer().getName() == null ? "" : stats.getPlayer().getName();
    }

    private String teamName(CompetitionTeamStats stats) {
        return stats.getTeam() == null || stats.getTeam().getName() == null ? "" : stats.getTeam().getName();
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }
}
