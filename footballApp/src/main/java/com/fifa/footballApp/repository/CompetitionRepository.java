package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompetitionRepository extends JpaRepository<Competition, UUID> {
}
