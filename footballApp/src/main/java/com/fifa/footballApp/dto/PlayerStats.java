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

    private long totalGoals;
    private long totalAssists;
    private long totalMatches;
    private long totalShotsOnGoal;
    private long totalShotsOutOfGoal;
    private long totalShots;
    private long totalFreeKicks;
    private long totalOffsides;
    private long missedPenalties;
    private long goalsFromPenalty;
    private long totalYellowCards;
    private long totalRedCards;
    private long subIns;
    private long subOuts;

}
