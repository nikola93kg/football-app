package com.fifa.footballApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStats {

    private Long totalGoals;
    private Long totalAssists;
    private Long totalMatches;
    private Long totalShotsOnGoal;
    private Long totalShotsOutOfGoal;
    private Long totalShots;
    private Long totalFreeKicks;
    private Long totalOffsides;
    private Long missedPenalties;
    private Long goalsFromPenalty;
    private Long totalYellowCards;
    private Long totalRedCards;
    private Long subIns;
    private Long subOuts;

}
