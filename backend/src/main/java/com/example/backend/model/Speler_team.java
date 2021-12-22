package com.example.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "speler_team", schema = "esports")
public class Speler_team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "speler_id")
    private Speler speler;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Team team;

    private Boolean isreserve;

    public Speler_team() {
    }

    private Speler_team(Builder builder) {
        setId(builder.id);
        setSpeler(builder.speler);
        setTeam(builder.team);
        isreserve = builder.isreserve;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Speler getSpeler() {
        return speler;
    }

    public void setSpeler(Speler speler_id) {
        this.speler = speler_id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team_id) {
        this.team = team_id;
    }

    public Boolean getReserve() {
        return isreserve;
    }

    public void setreserve(Boolean reserve) {
        isreserve = reserve;
    }


    public static final class Builder {
        private Long id;
        private Speler speler;
        private Team team;
        private Boolean isreserve;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder speler(Speler val) {
            speler= val;
            return this;
        }

        public Builder team(Team val) {
            team = val;
            return this;
        }
        public Builder team_id(Team val) {
            team = val;
            return this;
        }

        public Builder isreserve(Boolean val) {
            isreserve = val;
            return this;
        }

        public Speler_team build() {
            return new Speler_team(this);
        }
    }
}
