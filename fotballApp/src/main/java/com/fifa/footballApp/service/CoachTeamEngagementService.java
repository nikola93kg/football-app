package com.fifa.footballApp.service;

import com.fifa.footballApp.model.CoachTeamEngagement;
import com.fifa.footballApp.repository.CoachTeamEngagementRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachTeamEngagementService {

    private final CoachTeamEngagementRepo engagementRepo;

    public CoachTeamEngagementService(CoachTeamEngagementRepo engagementRepo) {
        this.engagementRepo = engagementRepo;
    }

    public List<CoachTeamEngagement> getAllCoaches() {
        return engagementRepo.findAll();
    }

    public List<CoachTeamEngagement> getActiveCoaches() {
        return engagementRepo.findByIsActiveTrue();
    }

}
