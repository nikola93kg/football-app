package com.fifa.footballApp;

import com.fifa.footballApp.dto.CompetitionPlayerStats;
import com.fifa.footballApp.dto.CompetitionTeamStats;
import com.fifa.footballApp.dto.StandingRow;
import com.fifa.footballApp.enums.MatchEventType;
import com.fifa.footballApp.enums.MatchStatus;
import com.fifa.footballApp.model.CompetitionTeam;
import com.fifa.footballApp.model.Match;
import com.fifa.footballApp.model.MatchEvent;
import com.fifa.footballApp.model.PlayerRegistration;
import com.fifa.footballApp.model.Round;
import com.fifa.footballApp.model.Season;
import com.fifa.footballApp.service.CompetitionService;
import com.fifa.footballApp.service.MatchEventService;
import com.fifa.footballApp.service.MatchService;
import com.fifa.footballApp.service.PlayerService;
import com.fifa.footballApp.service.SeasonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:football_app_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password="
})
@ActiveProfiles("test")
@Transactional
class FotballAppApplicationTests {

	@Autowired
	private CompetitionService competitionService;

	@Autowired
	private SeasonService seasonService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchEventService matchEventService;

	@Autowired
	private PlayerService playerService;

	@Test
	void contextLoads() {
	}

	@Test
	void calculatesCompetitionStandingsFromFinishedMatches() {
		List<StandingRow> standings = competitionService.getStandings("comp-ucl");

		Assertions.assertEquals(2, standings.size());
		Assertions.assertEquals("Arsenal", standings.get(0).getTeam().getName());
		Assertions.assertEquals(3, standings.get(0).getPoints());
		Assertions.assertEquals(1, standings.get(0).getGoalDifference());
		Assertions.assertEquals(0, standings.get(0).getPenaltyPoints());
		Assertions.assertEquals(List.of("W"), standings.get(0).getForm());
		Assertions.assertEquals("Barcelona", standings.get(1).getTeam().getName());
		Assertions.assertEquals(0, standings.get(1).getPoints());
		Assertions.assertEquals(List.of("L"), standings.get(1).getForm());
	}

	@Test
	void calculatesCompetitionPlayerAndTeamStatsFromEvents() {
		List<CompetitionPlayerStats> topScorers = competitionService.getCompetitionTopScorers("comp-ucl");
		List<CompetitionPlayerStats> topAssists = competitionService.getCompetitionTopAssists("comp-ucl");
		List<CompetitionPlayerStats> discipline = competitionService.getCompetitionDisciplineTable("comp-ucl");
		List<CompetitionTeamStats> teamStats = competitionService.getCompetitionTeamStats("comp-ucl");

		Assertions.assertEquals(2, topScorers.size());
		Assertions.assertEquals("Bukayo Saka", topScorers.getFirst().getPlayer().getName());
		Assertions.assertEquals(1, topScorers.getFirst().getGoals());
		Assertions.assertEquals(1, topScorers.getFirst().getAppearances());

		Assertions.assertEquals(1, topAssists.size());
		Assertions.assertEquals("Declan Rice", topAssists.getFirst().getPlayer().getName());
		Assertions.assertEquals(1, topAssists.getFirst().getAssists());

		Assertions.assertEquals(1, discipline.size());
		Assertions.assertEquals("Pedri", discipline.getFirst().getPlayer().getName());
		Assertions.assertEquals(1, discipline.getFirst().getYellowCards());

		Assertions.assertEquals(2, teamStats.size());
		Assertions.assertEquals("Arsenal", teamStats.getFirst().getTeam().getName());
		Assertions.assertEquals(3, teamStats.getFirst().getPoints());
		Assertions.assertEquals(2, teamStats.getFirst().getGoalsFor());
		Assertions.assertEquals(List.of("W"), teamStats.getFirst().getForm());
		Assertions.assertEquals(1, teamStats.get(1).getYellowCards());
	}

	@Test
	void loadsLeagueSeasonRoundsTeamsAndPlayerRegistrations() {
		List<Season> activeSeasons = seasonService.getActiveSeasons();
		List<Round> rounds = competitionService.getCompetitionRounds("comp-ucl");
		List<CompetitionTeam> teams = competitionService.getCompetitionTeams("comp-ucl");
		List<PlayerRegistration> registrations = competitionService.getCompetitionPlayerRegistrations("comp-ucl", null);

		Assertions.assertEquals(1, activeSeasons.size());
		Assertions.assertEquals("Zimska liga 2026", activeSeasons.getFirst().getName());
		Assertions.assertEquals(2, rounds.size());
		Assertions.assertEquals("Kolo 1", rounds.getFirst().getName());
		Assertions.assertEquals(2, teams.size());
		Assertions.assertEquals(4, registrations.size());
	}

	@Test
	void recordsGoalAndUpdatesLiveMatchScore() {
		Match liveMatch = matchService.startMatch("match-bar-ars");

		MatchEvent goal = new MatchEvent();
		goal.setMatch(liveMatch);
		goal.setTeam(liveMatch.getHomeTeam());
		goal.setPlayer(playerService.getPlayerById("player-lewa"));
		goal.setReferee(liveMatch.getReferee());
		goal.setEventType(MatchEventType.GOAL);
		goal.setMinute(12);

		matchEventService.createMatchEvent(goal);

		Match updatedMatch = matchService.getMatchById("match-bar-ars");
		Assertions.assertEquals(MatchStatus.LIVE, updatedMatch.getStatus());
		Assertions.assertEquals(1, updatedMatch.getHomeScore());
		Assertions.assertEquals(0, updatedMatch.getAwayScore());
		Assertions.assertEquals("1-0", updatedMatch.getScore());
	}

}
