package com.fifa.footballApp.dto;

import com.fifa.footballApp.model.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandingRow {

    private Team team;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points;

    public StandingRow(Team team) {
        this.team = team;
    }

    public void recordMatch(int scored, int conceded) {
        played++;
        goalsFor += scored;
        goalsAgainst += conceded;
        goalDifference = goalsFor - goalsAgainst;

        if (scored > conceded) {
            wins++;
            points += 3;
        } else if (scored == conceded) {
            draws++;
            points += 1;
        } else {
            losses++;
        }
    }
}
