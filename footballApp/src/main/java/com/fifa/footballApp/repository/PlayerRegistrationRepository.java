package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.PlayerRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRegistrationRepository extends JpaRepository<PlayerRegistration, String> {
    List<PlayerRegistration> findByCompetitionCompetitionId(String competitionId);
    List<PlayerRegistration> findByCompetitionCompetitionIdAndTeamId(String competitionId, String teamId);
    List<PlayerRegistration> findByPlayerId(String playerId);
    boolean existsByCompetitionCompetitionIdAndPlayerIdAndActiveTrue(String competitionId, String playerId);
}
