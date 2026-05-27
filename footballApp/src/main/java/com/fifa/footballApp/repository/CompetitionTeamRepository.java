package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.CompetitionTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionTeamRepository extends JpaRepository<CompetitionTeam, String> {
    List<CompetitionTeam> findByCompetitionCompetitionIdOrderBySeedNumberAsc(String competitionId);
    List<CompetitionTeam> findByTeamId(String teamId);
    boolean existsByCompetitionCompetitionIdAndTeamId(String competitionId, String teamId);
}
