package com.fifa.footballApp;

import com.fifa.footballApp.dto.StandingRow;
import com.fifa.footballApp.service.CompetitionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:football_app_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password="
})
@ActiveProfiles("test")
class FotballAppApplicationTests {

	@Autowired
	private CompetitionService competitionService;

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
		Assertions.assertEquals("Barcelona", standings.get(1).getTeam().getName());
		Assertions.assertEquals(0, standings.get(1).getPoints());
	}

}
