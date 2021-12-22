package com.example.backend.model;

import javax.persistence.*;

@Entity
@Table(name="team_wedstrijd", schema = "esports")
public class Team_wedstrijd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "wedstrijd_id")
    private Wedstrijd wedstrijd;

    private int score;

    public Team_wedstrijd() {
    }

    private Team_wedstrijd(Builder builder) {
        setId(builder.id);
        setTeam(builder.team);
        setWedstrijd(builder.wedstrijd);
        setScore(builder.score);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Wedstrijd getWedstrijd() {
        return wedstrijd;
    }

    public void setWedstrijd(Wedstrijd wedstrijd) {
        this.wedstrijd = wedstrijd;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static final class Builder {
        private Long id;
        private Team team;
        private Wedstrijd wedstrijd;
        private int score;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder team(Team val) {
            team = val;
            return this;
        }

        public Builder wedstrijd(Wedstrijd val) {
            wedstrijd = val;
            return this;
        }

        public Builder score(int val) {
            score = val;
            return this;
        }

        public Team_wedstrijd build() {
            return new Team_wedstrijd(this);
        }
    }
}
