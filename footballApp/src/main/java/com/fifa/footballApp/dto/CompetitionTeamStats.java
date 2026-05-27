package com.fifa.footballApp.dto;

import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.model.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompetitionTeamStats {

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
    private int yellowCards;
    private int secondYellowCards;
    private int redCards;
    private int cleanSheets;
    private List<String> form = new ArrayList<>();

    public CompetitionTeamStats(Team team) {
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
        if (conceded == 0) {
            cleanSheets++;
        }

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

    public void recordDiscipline(MatchEventType eventType) {
        if (eventType == MatchEventType.YELLOW_CARD) {
            yellowCards++;
        } else if (eventType == MatchEventType.SECOND_YELLOW_CARD) {
            yellowCards++;
            secondYellowCards++;
        } else if (eventType == MatchEventType.RED_CARD) {
            redCards++;
        }
    }

    private void addFormResult(String result) {
        form.add(result);
        if (form.size() > 5) {
            form.removeFirst();
        }
    }
}
