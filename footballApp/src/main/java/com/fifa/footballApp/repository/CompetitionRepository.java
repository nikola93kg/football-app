package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, String> {
}
