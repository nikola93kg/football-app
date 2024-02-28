package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fifa.footballApp.enums.PlayerPosition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @UuidGenerator
    private String id;

    private String name;
    private String defaultPosition;
    private int age;
    private String nationality;
    private int jerseyNumber;
    private Double transferFee; //wrapper klasa zato sto moze imati null, primitivni tip bi imao 0.0
    private LocalDateTime transferDate;

    @Enumerated(EnumType.STRING) // vrednost enuma treba da bude sacuvana kao string u bazi
    @ElementCollection(targetClass = PlayerPosition.class) // ova anotacija sluzi za modelovanje enuma
    @CollectionTable(name = "player_position", joinColumns = @JoinColumn(name = "player_id")) // anotacija definise ime tabele i nacin kako se elementi kolekcije mapiraju na tabelu
    @Column(name = "position") // definise ime kolone u tabeli
    private Set<PlayerPosition> positions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Team team;
}
