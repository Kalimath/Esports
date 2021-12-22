package com.example.backend.model;

import com.example.backend.exceptions.SpelerToewijzingFout;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TEAM", schema = "ESPORTS")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naam")
    private String naam;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public Team() {
    }

    private Team(Builder builder) {
        setId(builder.id);
        setNaam(builder.naam);
        setManager(builder.manager);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public static final class Builder {
        private Long id;
        private String naam;
        private Manager manager;
        private Set<Speler> spelers;
        private Set<Speler> reserveSpelers;


        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder naam(String val) {
            naam = val;
            return this;
        }

        public Builder manager(Manager val) {
            manager = val;
            return this;
        }

        public Builder spelers(Set<Speler> val) {
            spelers = val;
            return this;
        }

        public Builder reserveSpelers(Set<Speler> val) {
            reserveSpelers = val;
            return this;
        }


        public Team build() {
            return new Team(this);
        }
    }
}
