package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepo extends JpaRepository<Coach, Long> {
}
