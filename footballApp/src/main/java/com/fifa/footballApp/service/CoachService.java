package com.fifa.footballApp.service;


import com.fifa.footballApp.model.Coach;
import com.fifa.footballApp.repository.CoachRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {

    private final CoachRepo coachRepo;

    public CoachService(CoachRepo coachRepo) {
        this.coachRepo = coachRepo;
    }

    public List<Coach> getAllCoaches() {
        return coachRepo.findAll();
    }

    public Coach getCoachById(String id) {
        return coachRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with id: " + id));
    }

    public Coach createCoach(Coach coach) {
        return coachRepo.save(coach);
    }

    public Coach updateCoach(String id, Coach coachDetails) {
        Coach coach = coachRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with id: " + id));

        coach.setName(coachDetails.getName());
        coach.setAge(coachDetails.getAge());
        coach.setNationality(coachDetails.getNationality());
        coach.setTrophiesWon(coachDetails.getTrophiesWon());

        return coachRepo.save(coach);
    }

    public void deleteCoach(String id) {
        if (!coachRepo.existsById(id)) {
            throw new EntityNotFoundException("Coach not found with id: " + id);
        }
        coachRepo.deleteById(id);
    }

    public List<Coach> searchCoaches(String name, String nationality) {
        if(name != null && nationality != null) {
            return coachRepo.findByNameContainingAndNationality(name, nationality);
        } else if (name != null) {
            return coachRepo.findByNameContaining(name);
        } else if (nationality != null) {
            return coachRepo.findByNationality(nationality);
        } else {
            return coachRepo.findAll(); // ili mozda da bacim exception? illegal argument exception
        }
    }
}
