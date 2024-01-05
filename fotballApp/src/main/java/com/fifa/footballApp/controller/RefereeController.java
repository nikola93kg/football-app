package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.Referee;
import com.fifa.footballApp.service.RefereeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/referees")
public class RefereeController {

    private final RefereeService refereeService;

    public RefereeController(RefereeService refereeService) {
        this.refereeService = refereeService;
    }

    @GetMapping
    public List<Referee> getAllReferees() {
        return refereeService.getAllReferees();
    }

    @PostMapping
    public Referee addReferee(@RequestBody Referee referee) {
        return refereeService.addReferee(referee);
    }

//    da dohvatim sudiju po id-u

    @GetMapping("/{id}")
    public Referee getRefereeById(@PathVariable Long id) {
        return refereeService.getRefereeById(id);
    }

    @PutMapping("/{id}")
    public Referee updateReferee(@PathVariable Long id, @RequestBody Referee refereeDetails) {
        return refereeService.updateReferee(id, refereeDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteReferee(@PathVariable Long id) {
        refereeService.deleteReferee(id);
    }

    @GetMapping("/search")
    public List<Referee> searchReferees(String nationality, String name, Integer age) {
        return refereeService.searchReferees(nationality, name, age);
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<?> getRefereeStatistics(@PathVariable Long id) {
        return null;
    }

}
