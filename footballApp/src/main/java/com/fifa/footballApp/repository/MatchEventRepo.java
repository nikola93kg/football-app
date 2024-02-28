package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepo extends JpaRepository<MatchEvent, String> {

    List<MatchEvent> findByRefereeId(String refereeId);
    List<MatchEvent> findByPlayerId(String playerId);
    List<MatchEvent> findByMatchId(String id);
}
