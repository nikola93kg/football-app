package com.fifa.footballApp.service;

import com.fifa.footballApp.model.CoachTeamEngagement;
import com.fifa.footballApp.repository.CoachTeamEngagementRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachTeamEngagementService {

    private final CoachTeamEngagementRepo engagementRepo;

    public CoachTeamEngagementService(CoachTeamEngagementRepo engagementRepo) {
        this.engagementRepo = engagementRepo;
    }


    public List<CoachTeamEngagement> getAllEngagements() {
        return engagementRepo.findAll();
    }

    public CoachTeamEngagement getEngagementById(String id) {
        return engagementRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Engagement not found with id: " + id));
    }

    public CoachTeamEngagement createEngagement(CoachTeamEngagement engagement) {
        return engagementRepo.save(engagement);
    }

    public CoachTeamEngagement updateEngagement(String id, CoachTeamEngagement engagementDetails) {
        CoachTeamEngagement existingEngagement = engagementRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Engagement not found with id: " + id));

        existingEngagement.setTeam(engagementDetails.getTeam());
        existingEngagement.setCoach(engagementDetails.getCoach());
        existingEngagement.setRole(engagementDetails.getRole());
        existingEngagement.setStartDate(engagementDetails.getStartDate());
        existingEngagement.setEndDate(engagementDetails.getEndDate());
        existingEngagement.setActive(engagementDetails.isActive());

        return engagementRepo.save(existingEngagement);
    }

    public void deleteEngagement(String id) {
        if (!engagementRepo.existsById(id)) {
            throw new EntityNotFoundException("Engagement not found with id: " + id);
        }
        engagementRepo.deleteById(id);
    }

    public List<CoachTeamEngagement> getActiveEngagements() {
        return engagementRepo.findByIsActiveTrue();
    }

}
