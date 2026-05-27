package com.fifa.footballApp.model;

import com.fifa.footballApp.enums.MatchEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {

    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "assist_player_id")
    private Player assistPlayer;

    @ManyToOne
    @JoinColumn(name = "related_player_id")
    private Player relatedPlayer;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private Referee referee;

    @Enumerated(EnumType.STRING)
    private MatchEventType eventType;

    private LocalDateTime eventTime;
    @Column(name = "match_minute")
    private Integer minute;
    private Integer additionalMinute;
    private String note;
    private String recordedBy;
    private LocalDateTime recordedAt = LocalDateTime.now();
}
