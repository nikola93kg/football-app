package com.fifa.footballApp.service;

import com.fifa.footballApp.enums.RegistrationStatus;
import com.fifa.footballApp.model.Competition;
import com.fifa.footballApp.model.CompetitionTeam;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.repository.CompetitionRepository;
import com.fifa.footballApp.repository.CompetitionTeamRepository;
import com.fifa.footballApp.repository.TeamRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionTeamService {

    private final CompetitionTeamRepository competitionTeamRepository;
    private final CompetitionRepository competitionRepository;
    private final TeamRepo teamRepo;

    public CompetitionTeamService(CompetitionTeamRepository competitionTeamRepository,
                                  CompetitionRepository competitionRepository,
                                  TeamRepo teamRepo) {
        this.competitionTeamRepository = competitionTeamRepository;
        this.competitionRepository = competitionRepository;
        this.teamRepo = teamRepo;
    }

    public List<CompetitionTeam> getCompetitionTeams(String competitionId) {
        return competitionTeamRepository.findByCompetitionCompetitionIdOrderBySeedNumberAsc(competitionId);
    }

    public CompetitionTeam getCompetitionTeamById(String id) {
        return competitionTeamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competition team not found with id: " + id));
    }

    public CompetitionTeam addTeamToCompetition(String competitionId, String teamId, CompetitionTeam details) {
        if (competitionTeamRepository.existsByCompetitionCompetitionIdAndTeamId(competitionId, teamId)) {
            throw new IllegalStateException("Team is already registered for this competition");
        }

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + competitionId));
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        CompetitionTeam competitionTeam = details == null ? new CompetitionTeam() : details;
        competitionTeam.setCompetition(competition);
        competitionTeam.setTeam(team);
        if (competitionTeam.getStatus() == null) {
            competitionTeam.setStatus(RegistrationStatus.APPROVED);
        }
        return competitionTeamRepository.save(competitionTeam);
    }

    public CompetitionTeam updateCompetitionTeam(String id, CompetitionTeam details) {
        CompetitionTeam competitionTeam = getCompetitionTeamById(id);
        competitionTeam.setStatus(details.getStatus());
        competitionTeam.setPenaltyPoints(details.getPenaltyPoints());
        competitionTeam.setGroupName(details.getGroupName());
        competitionTeam.setSeedNumber(details.getSeedNumber());
        competitionTeam.setActive(details.isActive());
        return competitionTeamRepository.save(competitionTeam);
    }

    public void removeCompetitionTeam(String id) {
        if (!competitionTeamRepository.existsById(id)) {
            throw new EntityNotFoundException("Competition team not found with id: " + id);
        }
        competitionTeamRepository.deleteById(id);
    }
}
