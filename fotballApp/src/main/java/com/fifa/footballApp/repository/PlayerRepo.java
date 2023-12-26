package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepo extends JpaRepository<Player, Long> {
}
