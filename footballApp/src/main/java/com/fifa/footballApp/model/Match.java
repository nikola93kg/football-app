package com.fifa.footballApp.model;

import com.fifa.footballApp.enums.MatchStatus;
import com.fifa.footballApp.enums.TeamFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @UuidGenerator
    private String id;

    private LocalDate date;
    private LocalTime time;
    private String location;
    private String stadium;
    private String score;
    private Integer homeScore;
    private Integer awayScore;
    private Integer roundNumber;
    private Integer currentMinute;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private Referee referee;

    @OneToMany(mappedBy = "match")
    private List<MatchEvent> events;

    @Enumerated(EnumType.STRING)
    private TeamFormation homeTeamFormation;

    @Enumerated(EnumType.STRING)
    private TeamFormation awayTeamFormation;

//    mec ce imati kolekciju mec iventova (sut na gol, postignut gol...statistika)

}
