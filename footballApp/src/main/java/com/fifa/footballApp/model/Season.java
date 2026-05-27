package com.fifa.footballApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    @Id
    @UuidGenerator
    @Column(name = "season_id", insertable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "season")
    private List<Competition> competitions;
}
