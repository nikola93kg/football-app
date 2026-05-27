package com.fifa.footballApp.service;

import com.fifa.footballApp.enums.RegistrationStatus;
import com.fifa.footballApp.model.Competition;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.PlayerRegistration;
import com.fifa.footballApp.model.Team;
import com.fifa.footballApp.repository.CompetitionRepository;
import com.fifa.footballApp.repository.CompetitionTeamRepository;
import com.fifa.footballApp.repository.PlayerRegistrationRepository;
import com.fifa.footballApp.repository.PlayerRepo;
import com.fifa.footballApp.repository.TeamRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerRegistrationService {

    private final PlayerRegistrationRepository playerRegistrationRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionTeamRepository competitionTeamRepository;
    private final TeamRepo teamRepo;
    private final PlayerRepo playerRepo;

    public PlayerRegistrationService(PlayerRegistrationRepository playerRegistrationRepository,
                                     CompetitionRepository competitionRepository,
                                     CompetitionTeamRepository competitionTeamRepository,
                                     TeamRepo teamRepo,
                                     PlayerRepo playerRepo) {
        this.playerRegistrationRepository = playerRegistrationRepository;
        this.competitionRepository = competitionRepository;
        this.competitionTeamRepository = competitionTeamRepository;
        this.teamRepo = teamRepo;
        this.playerRepo = playerRepo;
    }

    public List<PlayerRegistration> getCompetitionRegistrations(String competitionId) {
        return playerRegistrationRepository.findByCompetitionCompetitionId(competitionId);
    }

    public List<PlayerRegistration> getTeamRegistrations(String competitionId, String teamId) {
        return playerRegistrationRepository.findByCompetitionCompetitionIdAndTeamId(competitionId, teamId);
    }

    public PlayerRegistration getRegistrationById(String id) {
        return playerRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player registration not found with id: " + id));
    }

    public PlayerRegistration registerPlayer(String competitionId, String teamId, String playerId, PlayerRegistration details) {
        if (!competitionTeamRepository.existsByCompetitionCompetitionIdAndTeamId(competitionId, teamId)) {
            throw new IllegalStateException("Team is not registered for this competition");
        }
        if (playerRegistrationRepository.existsByCompetitionCompetitionIdAndPlayerIdAndActiveTrue(competitionId, playerId)) {
            throw new IllegalStateException("Player is already actively registered for this competition");
        }

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + competitionId));
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        PlayerRegistration registration = details == null ? new PlayerRegistration() : details;
        registration.setCompetition(competition);
        registration.setTeam(team);
        registration.setPlayer(player);
        if (registration.getJerseyNumber() == null) {
            registration.setJerseyNumber(player.getJerseyNumber());
        }
        if (registration.getStatus() == null) {
            registration.setStatus(RegistrationStatus.APPROVED);
        }
        return playerRegistrationRepository.save(registration);
    }

    public PlayerRegistration updateRegistration(String id, PlayerRegistration details) {
        PlayerRegistration registration = getRegistrationById(id);
        registration.setJerseyNumber(details.getJerseyNumber());
        registration.setStatus(details.getStatus());
        registration.setActive(details.isActive());
        registration.setSuspendedUntil(details.getSuspendedUntil());
        return playerRegistrationRepository.save(registration);
    }

    public void deleteRegistration(String id) {
        if (!playerRegistrationRepository.existsById(id)) {
            throw new EntityNotFoundException("Player registration not found with id: " + id);
        }
        playerRegistrationRepository.deleteById(id);
    }
}
