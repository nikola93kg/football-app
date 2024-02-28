package com.fifa.footballApp.model;

import com.fifa.footballApp.enums.CoachRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoachTeamEngagement {

    @Id
    @UuidGenerator
    private String id;

    @OneToOne //@ManyToOne ako trener moze da promeni tim?
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Enumerated(EnumType.STRING)
    private CoachRole role;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
}

