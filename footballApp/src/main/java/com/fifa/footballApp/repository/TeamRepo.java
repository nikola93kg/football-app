package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepo extends JpaRepository<Team, Long> {

    List<Team> findByName(String name);

    List<Team> findByNameContaining(String name);
}
