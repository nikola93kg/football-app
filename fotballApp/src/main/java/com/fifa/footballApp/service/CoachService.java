package com.fifa.footballApp.service;


import com.fifa.footballApp.model.Coach;
import com.fifa.footballApp.repository.CoachRepo;
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
}
