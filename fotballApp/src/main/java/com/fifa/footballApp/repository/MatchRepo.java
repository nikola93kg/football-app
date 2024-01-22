package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepo extends JpaRepository<Match, Long> {
    List<Match> findByIdAndDateBefore(Long id, LocalDate date);
    List<Match> findByIdAndDateAfter(Long id, LocalDate date);
    List<Match> findByStadiumContaining(String stadium); //Containing ce pretraziti i polovicno podudaranje npr ako kucam old moze da nadje old trafford
    List<MatchEvent> findByMatchId(Long matchId);
}
