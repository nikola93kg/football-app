package com.fifa.footballApp.service;

import com.fifa.footballApp.model.Referee;
import com.fifa.footballApp.repository.RefereeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefereeService {

    private final RefereeRepo refereeRepo;

    public RefereeService(RefereeRepo refereeRepo) {
        this.refereeRepo = refereeRepo;
    }

    public List<Referee> getAllReferees() {
        return refereeRepo.findAll();
    }
}
