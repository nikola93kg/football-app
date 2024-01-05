package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefereeRepo extends JpaRepository<Referee, Long> {

    List<Referee> findByNationality(String nationality); //probaj i sa getBy ili findAllByNationality
    List<Referee> findByName(String name);
    List<Referee> findByAge(Integer age);
}
