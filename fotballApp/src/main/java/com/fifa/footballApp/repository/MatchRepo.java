package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepo extends JpaRepository<Match, Long> {
    List<Match> findByTeamIdAndDateBefore(Long teamId, LocalDate date);
    List<Match> findByTeamIdAndDateAfter(Long teamId, LocalDate date);
}
