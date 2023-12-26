package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepo extends JpaRepository<Match, Long> {
}
