package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepo extends JpaRepository<Player, String> {

    List<Player> findByName(String name);
    List<Player> findByNameContainingAndNationality(String name, String nationality);

}
