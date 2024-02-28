package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fifa.footballApp.enums.TeamFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @SequenceGenerator(
            name = "service_sequence",
            sequenceName ="service_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "service_sequence"
    )
    private long id;

    private String name;
    private String logo;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "team_competition_association",
            joinColumns = {
                    @JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn( name = "id", referencedColumnName = "Id")})
    @JsonIgnore
    private Set<Competition> competitions = new HashSet<>();

    @OneToMany(mappedBy = "team")
    @JsonManagedReference
    private List<Player> listOfPlayers;

    @OneToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    private Coach coach; //lista coach-a

    @Enumerated
    private TeamFormation defaultFormation;

}
