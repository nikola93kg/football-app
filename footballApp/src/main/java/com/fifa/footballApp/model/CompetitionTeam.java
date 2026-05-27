package com.fifa.footballApp.model;

import com.fifa.footballApp.enums.RegistrationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "competition_team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionTeam {

    @Id
    @UuidGenerator
    @Column(name = "competition_team_id", insertable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.APPROVED;

    private Integer penaltyPoints = 0;
    private String groupName;
    private Integer seedNumber;
    private boolean active = true;
}
