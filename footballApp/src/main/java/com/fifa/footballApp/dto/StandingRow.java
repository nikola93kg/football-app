package com.fifa.footballApp.dto;

import com.fifa.footballApp.model.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private int penaltyPoints;
    private List<String> form = new ArrayList<>();

    public StandingRow(Team team) {
        this.team = team;
    }

    public void applyPenaltyPoints(int penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
        this.points -= penaltyPoints;
    }

    public void recordMatch(int scored, int conceded) {
        played++;
        goalsFor += scored;
        goalsAgainst += conceded;
        goalDifference = goalsFor - goalsAgainst;

        if (scored > conceded) {
            wins++;
            points += 3;
            addFormResult("W");
        } else if (scored == conceded) {
            draws++;
            points += 1;
            addFormResult("D");
        } else {
            losses++;
            addFormResult("L");
        }
    }

    private void addFormResult(String result) {
        form.add(result);
        if (form.size() > 5) {
            form.remove(0);
        }
    }
}
