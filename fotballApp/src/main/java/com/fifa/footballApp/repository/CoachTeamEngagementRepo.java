package com.fifa.footballApp.repository;

import com.fifa.footballApp.model.CoachTeamEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachTeamEngagementRepo extends JpaRepository<CoachTeamEngagement, Long> {

//    Ovde bih mogao da koristim @Query anotaciju sa sql upitima, npr:

//    @Query("select nesto from CoachTeamEngagement where nesto.isActive = true")
//    List<CoachTeamEngagement> findActiveCoach();
//    List<CoachTeamEngagement> findRetiredCoach();

//    Spring Data JPA ce automatski prepoznati  i implementirati ove metode jer automatski generisu SQL upite
//    koji dohvataju da li je boolean true ili je false

    List<CoachTeamEngagement> findByIsActiveTrue();
    List<CoachTeamEngagement> findByIsActiveFalse(); //ovo necemo jos uvek
}
