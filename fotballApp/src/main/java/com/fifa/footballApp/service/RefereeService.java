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
        return refereeRepo.save(referee);
    }

    public Referee getRefereeById(Long id) {
        return refereeRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Referee not found"));
    }

    public Referee updateReferee(Long id, Referee refereeDetails) {
        Referee referee = refereeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Referee not found")); //za ovo nisam siguran jel moze ovako

        referee.setName(refereeDetails.getName());
        referee.setNationality(refereeDetails.getNationality());
        referee.setAge(refereeDetails.getAge());

        return refereeRepo.save(referee);
    }


    public void deleteReferee(Long id) {
        refereeRepo.deleteById(id);
    }

    public List<Referee> searchReferees(String nationality, String name, Integer age) {
        return null; // ovo mozda u RefereeRepo da pises custom sql? Query
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
