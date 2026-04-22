INSERT INTO coach (id, name, age, nationality, trophies_won) VALUES
('coach-arsenal', 'Mikel Arteta', 42, 'Spain', 6),
('coach-barca', 'Xavi Hernandez', 44, 'Spain', 5);

INSERT INTO referee (id, name, age, nationality, total_red_cards_given, total_yellow_cards_given) VALUES
('ref-main', 'Mark Clattenburg', 49, 'England', 0, 0);

INSERT INTO competition (competition_id, name, current_winner, prize_amount, description) VALUES
('comp-ucl', 'UEFA Champions League', 'Manchester City', 'EUR 20000000', 'Top European club competition');

INSERT INTO team (id, name, logo, coach_id, default_formation) VALUES
('team-arsenal', 'Arsenal', 'https://upload.wikimedia.org/wikipedia/en/5/53/Arsenal_FC.svg', 'coach-arsenal', 'FORMATION_4_2_3_1'),
('team-barca', 'Barcelona', 'https://upload.wikimedia.org/wikipedia/en/4/47/FC_Barcelona_%28crest%29.svg', 'coach-barca', 'FORMATION_4_3_3');

INSERT INTO team_competition_association (competition_id, id) VALUES
('comp-ucl', 'team-arsenal'),
('comp-ucl', 'team-barca');

INSERT INTO coach_team_engagement (id, team_id, coach_id, role, start_date, end_date, is_active) VALUES
('eng-arsenal', 'team-arsenal', 'coach-arsenal', 'HEAD_COACH', '2023-07-01', NULL, true),
('eng-barca', 'team-barca', 'coach-barca', 'HEAD_COACH', '2023-07-01', NULL, true);

INSERT INTO player (id, name, default_position, age, nationality, jersey_number, transfer_fee, transfer_date, team_id) VALUES
('player-saka', 'Bukayo Saka', 'Right Winger', 22, 'England', 7, 120000000, '2023-08-01 12:00:00', 'team-arsenal'),
('player-rice', 'Declan Rice', 'Defensive Mid', 25, 'England', 41, 116000000, '2023-08-01 12:00:00', 'team-arsenal'),
('player-lewa', 'Robert Lewandowski', 'Centre Forward', 35, 'Poland', 9, 50000000, '2022-07-15 12:00:00', 'team-barca'),
('player-pedri', 'Pedri', 'Central Mid', 21, 'Spain', 8, 100000000, '2020-09-01 12:00:00', 'team-barca');

INSERT INTO player_position (player_id, position) VALUES
('player-saka', 'RIGHT_WINGER'),
('player-saka', 'LEFT_WINGER'),
('player-rice', 'DEFENSIVE_MID'),
('player-rice', 'CENTRAL_MID'),
('player-lewa', 'CENTRE_FORWARD'),
('player-pedri', 'CENTRAL_MID'),
('player-pedri', 'ATTACKING_MID');

INSERT INTO match (id, date, time, location, stadium, score, home_score, away_score, round_number, current_minute, status, competition_id, home_team_id, away_team_id, referee_id, home_team_formation, away_team_formation) VALUES
('match-ars-bar', '2026-04-10', '20:45:00', 'London', 'Emirates Stadium', '2-1', 2, 1, 1, 90, 'FINISHED', 'comp-ucl', 'team-arsenal', 'team-barca', 'ref-main', 'FORMATION_4_2_3_1', 'FORMATION_4_3_3'),
('match-bar-ars', '2026-04-24', '20:45:00', 'Barcelona', 'Camp Nou', NULL, NULL, NULL, 2, NULL, 'SCHEDULED', 'comp-ucl', 'team-barca', 'team-arsenal', 'ref-main', 'FORMATION_4_3_3', 'FORMATION_4_2_3_1');

INSERT INTO match_event (id, match_id, player_id, team_id, referee_id, event_type, event_time) VALUES
('event-goal-saka', 'match-ars-bar', 'player-saka', 'team-arsenal', 'ref-main', 'GOAL', '2026-04-10 21:05:00'),
('event-assist-rice', 'match-ars-bar', 'player-rice', 'team-arsenal', 'ref-main', 'ASSIST', '2026-04-10 21:05:00'),
('event-goal-lewa', 'match-ars-bar', 'player-lewa', 'team-barca', 'ref-main', 'GOAL', '2026-04-10 21:32:00'),
('event-yellow-pedri', 'match-ars-bar', 'player-pedri', 'team-barca', 'ref-main', 'YELLOW_CARD', '2026-04-10 21:40:00');
