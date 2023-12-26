package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchEventRepo extends JpaRepository<MatchEvent, Long> {

}
