package com.fifa.footballApp.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Getter
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

    @Id
    @UuidGenerator
    @Column(name="competition_id",insertable=false, updatable=false)
    private String competitionId;

    @Nonnull
    private String name;

    @Nullable
    private String currentWinner;

    @Nonnull
    private String prizeAmount;

    @Nonnull
    private String description;

    @Nullable
    @ManyToMany(mappedBy = "competitions", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Team> participants;
}
