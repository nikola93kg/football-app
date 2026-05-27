package com.fifa.footballApp.dto;

import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.model.Player;
import com.fifa.footballApp.model.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompetitionPlayerStats {

    private Player player;
    private Team team;
    private int appearances;
    private int goals;
    private int penaltyGoals;
    private int ownGoals;
    private int assists;
    private int yellowCards;
    private int secondYellowCards;
    private int redCards;
    private int missedPenalties;
    private int shotsOnGoal;
    private int shotsOutOfGoal;
    private int totalShots;

    public CompetitionPlayerStats(Player player, Team team) {
        this.player = player;
        this.team = team;
    }

    public void recordEvent(MatchEventType eventType) {
        if (eventType == null) {
            return;
        }

        switch (eventType) {
            case GOAL -> goals++;
            case GOAL_FROM_PENALTY -> {
                goals++;
                penaltyGoals++;
            }
            case OWN_GOAL -> ownGoals++;
            case ASSIST -> assists++;
            case YELLOW_CARD -> yellowCards++;
            case SECOND_YELLOW_CARD -> {
                yellowCards++;
                secondYellowCards++;
            }
            case RED_CARD -> redCards++;
            case MISSED_PENALTY -> missedPenalties++;
            case SHOT_ON_GOAL -> {
                shotsOnGoal++;
                totalShots++;
            }
            case SHOT_OUT_OF_GOAL -> {
                shotsOutOfGoal++;
                totalShots++;
            }
            default -> {
            }
        }
    }

    public void recordAssist() {
        assists++;
    }
}
