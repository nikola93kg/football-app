package com.fifa.footballApp.model;

import com.fifa.footballApp.enums.TeamFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate date;
    private LocalTime time;
    private String location;
    private String stadium;
    private String score;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @OneToOne
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
