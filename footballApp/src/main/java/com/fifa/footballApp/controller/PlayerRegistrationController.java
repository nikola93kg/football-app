package com.fifa.footballApp.controller;

import com.fifa.footballApp.model.PlayerRegistration;
import com.fifa.footballApp.service.PlayerRegistrationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/player-registrations")
public class PlayerRegistrationController {

    private final PlayerRegistrationService playerRegistrationService;

    public PlayerRegistrationController(PlayerRegistrationService playerRegistrationService) {
        this.playerRegistrationService = playerRegistrationService;
    }

    @GetMapping("/{id}")
    public PlayerRegistration getRegistrationById(@PathVariable String id) {
        return playerRegistrationService.getRegistrationById(id);
    }

    @PutMapping("/{id}")
    public PlayerRegistration updateRegistration(@PathVariable String id, @RequestBody PlayerRegistration details) {
        return playerRegistrationService.updateRegistration(id, details);
    }

    @DeleteMapping("/{id}")
    public void deleteRegistration(@PathVariable String id) {
        playerRegistrationService.deleteRegistration(id);
    }
}
