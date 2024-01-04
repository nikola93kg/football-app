package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepo extends JpaRepository<Player, Long> {

    List<Player> findByName(String name);
}
