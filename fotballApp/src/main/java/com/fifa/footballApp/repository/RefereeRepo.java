package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefereeRepo extends JpaRepository<Referee, Long> {
}
