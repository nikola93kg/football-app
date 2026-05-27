package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fifa.footballApp.enums.CompetitionStatus;
import com.fifa.footballApp.enums.CompetitionType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Getter
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

    @Id
    @UuidGenerator
    @Column(name="competition_id",insertable=false, updatable=false)
    private String competitionId;

    @Nonnull
    private String name;

    @Nullable
    private String currentWinner;

    @Nonnull
    private String prizeAmount;

    @Nonnull
    private String description;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    @Enumerated(EnumType.STRING)
    private CompetitionStatus status = CompetitionStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    private CompetitionType type = CompetitionType.LEAGUE;

    @Nullable
    @JsonIgnore
    @ManyToMany(mappedBy = "competitions", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Team> participants;

    @JsonIgnore
    @OneToMany(mappedBy = "competition")
    private Set<Round> rounds;

    @JsonIgnore
    @OneToMany(mappedBy = "competition")
    private Set<CompetitionTeam> competitionTeams;
}
