package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoundRepository extends JpaRepository<Round, String> {
    List<Round> findByCompetitionCompetitionIdOrderByRoundNumberAsc(String competitionId);
}
