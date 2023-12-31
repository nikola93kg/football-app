package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fifa.footballApp.enums.PlayerPosition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String defaultPosition; //string mozes da stavis kao enumeraciju
    private int age;
    private String nationality;
    private int jerseyNumber;

    @Enumerated(EnumType.STRING) // vrednost enuma treba da bude sacuvana kao string u bazi
    @ElementCollection(targetClass = PlayerPosition.class) // ova anotacija sluzi za modelovanje enuma
    @CollectionTable(name = "player_position", joinColumns = @JoinColumn(name = "player_id")) // anotacija definise ime tabele i nacin kako se elementi kolekcije mapiraju na tabelu
    @Column(name = "position") // definise ime kolone u tabeli
    private Set<PlayerPosition> positions;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    @JsonBackReference
    private Team team;

}
