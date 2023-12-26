package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepo extends JpaRepository<Team, Long> {
}
