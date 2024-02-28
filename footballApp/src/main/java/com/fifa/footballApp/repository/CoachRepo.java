package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachRepo extends JpaRepository<Coach, Long> {
    List<Coach> findByNameContaining(String name);
    List<Coach> findByNationality(String nationality);
    List<Coach> findByNameContainingAndNationality(String name, String nationality);
}
