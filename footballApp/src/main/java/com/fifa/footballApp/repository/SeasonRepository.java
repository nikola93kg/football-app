package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, String> {
    List<Season> findByActiveTrue();
}
