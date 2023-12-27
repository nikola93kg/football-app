package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fifa.footballApp.enums.TeamFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String logo;

    @OneToMany(mappedBy = "team")
    @JsonManagedReference
    private List<Player> listOfPlayers;

    @OneToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    private Coach coach; //lista coach-a

    @Enumerated
    private TeamFormation defaultFormation;

}
