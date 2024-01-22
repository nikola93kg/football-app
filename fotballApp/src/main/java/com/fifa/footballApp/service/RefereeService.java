package com.fifa.footballApp.service;

import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.model.Referee;
import com.fifa.footballApp.repository.MatchEventRepo;
import com.fifa.footballApp.repository.RefereeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefereeService {

    private final RefereeRepo refereeRepo;
    private final MatchEventRepo matchEventRepo;

    public RefereeService(RefereeRepo refereeRepo, MatchEventRepo matchEventRepo) {
        this.refereeRepo = refereeRepo;
        this.matchEventRepo = matchEventRepo;
    }

    public List<Referee> getAllReferees() {
        return refereeRepo.findAll();
    }

    public Referee addReferee(Referee referee) {
        if (refereeRepo.findByName(referee.getName()) != null) {
            throw new IllegalStateException("Referee with the name: " + referee.getName() + "already exists");
        }
        return refereeRepo.save(referee);
    }

    public Referee getRefereeById(Long id) {
        return refereeRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Referee not found"));
    }

    public Referee updateReferee(Long id, Referee refereeDetails) {
        Referee referee = refereeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Referee not found"));

        referee.setName(refereeDetails.getName());
        referee.setNationality(refereeDetails.getNationality());
        referee.setAge(refereeDetails.getAge());
//        referee.setTotalRedCardsGiven(refereeDetails.getTotalRedCardsGiven());
//        referee.setTotalYellowCardsGiven(refereeDetails.getTotalYellowCardsGiven());
        return refereeRepo.save(referee);
    }


    public void deleteReferee(Long id) {
//        odradi check da li referee koji ima taj id postoji
        if(refereeRepo.existsById(id)) {
            refereeRepo.deleteById(id);
        } else {
            throw new EntityNotFoundException("Referee with id: " + id + "not found");
        }
    }

//    public void deleteReferee(Long id) {
//        Referee referee = refereeRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Referee with id: " + id + " not found"));
//        refereeRepo.delete(referee);
//    }

    public List<Referee> searchReferees(String nationality, String name, Integer age) {
        return null; // ovo mozda u RefereeRepo da pises custom sql? Query
//        return refereeRepo.findByQuery(nationality, name, age);
    }

    public Map<String, Long> getRefereeStatistics(Long refereeId) {

        List<MatchEvent> events = matchEventRepo.findByRefereeId(refereeId);
        long totalMatches = events.stream().map(MatchEvent::getMatch).distinct().count();
        long totalYellowCards = events.stream().filter(e -> e.getEventType() == MatchEventType.YELLOW_CARD).count();
        long totalRedCards = events.stream().filter(e -> e.getEventType() == MatchEventType.RED_CARD).count();

        Map<String, Long> statistics = new HashMap<>();
        statistics.put("totalMatches", totalMatches);
        statistics.put("totalYellowCards", totalYellowCards);
        statistics.put("totalRedCards", totalRedCards);

        return statistics;
    }

}
