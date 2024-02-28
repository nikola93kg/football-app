package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fifa.footballApp.enums.TeamFormation;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

    @Id
    @UuidGenerator
    @Column(name="id", insertable=false, updatable=false)
    private String id;

    @Nonnull
    private String name;
    @Nonnull
    private String logo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "team_competition_association",
            joinColumns = {
                    @JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn( name = "competition_id", referencedColumnName = "competition_id")})
    @Nullable
    private Set<Competition> competitions;

    @OneToMany(mappedBy = "team")
    @JsonManagedReference
    @Nullable
    private List<Player> listOfPlayers;

    @OneToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    @Nullable
    private Coach coach; //lista coach-a

    @Enumerated
    @Nullable
    private TeamFormation defaultFormation;

}
