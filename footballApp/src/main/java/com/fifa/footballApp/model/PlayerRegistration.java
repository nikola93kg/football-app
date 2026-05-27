package com.fifa.footballApp.model;

import com.fifa.footballApp.enums.RegistrationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRegistration {

    @Id
    @UuidGenerator
    @Column(name = "player_registration_id", insertable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Integer jerseyNumber;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.APPROVED;

    private boolean active = true;
    private LocalDate suspendedUntil;
    private LocalDateTime registeredAt = LocalDateTime.now();
}
