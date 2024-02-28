package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID"
    )
    private UUID id;

    @Nonnull
    private String name;

    @Nullable
    private String currentWinner;

    @Nonnull
    private String prizeAmount;

    @Nonnull
    private String description;

    @Nullable
    @JsonBackReference
    @ManyToMany(mappedBy = "competitions", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Team> participants = new HashSet<>();
}
