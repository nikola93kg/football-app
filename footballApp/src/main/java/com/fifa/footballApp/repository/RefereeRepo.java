package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RefereeRepo extends JpaRepository<Referee, String> {

    List<Referee> findByNationality(String nationality); //probaj i sa getBy ili findAllByNationality
    List<Referee> findByName(String name);
    List<Referee> findByAge(Integer age);

    @Query("SELECT r FROM Referee r WHERE (:nationality IS NULL OR r.nationality = :nationality) " +
            "AND (:name IS NULL OR r.name LIKE %:name%) AND (:age IS NULL OR r.age = :age)")
    List<Referee> findByQuery(@Param("nationality") String nationality, @Param("name") String name, @Param("age") Integer age);
}
