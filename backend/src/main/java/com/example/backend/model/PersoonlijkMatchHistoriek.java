package com.example.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "persoonlijke_match_historiek", schema = "esports")
public class PersoonlijkMatchHistoriek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "speler_id")
    private Speler speler;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_wedstrijd_id")
    private Team_wedstrijd teamWedstrijd;

    public PersoonlijkMatchHistoriek() {
    }

    private PersoonlijkMatchHistoriek(Builder builder) {
        setId(builder.id);
        setSpeler(builder.speler);
        setTeamWedstrijd(builder.teamWedstrijd);
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

    public void setSpeler(Speler speler) {
        this.speler = speler;
    }

    public Team_wedstrijd getTeamWedstrijd() {
        return teamWedstrijd;
    }

    public void setTeamWedstrijd(Team_wedstrijd team_wedstrijd) {
        this.teamWedstrijd = team_wedstrijd;
    }


    public static final class Builder {
        private Long id;
        private Speler speler;
        private Team_wedstrijd teamWedstrijd;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder speler(Speler val) {
            speler = val;
            return this;
        }

        public Builder team_wedstrijd(Team_wedstrijd val) {
            teamWedstrijd = val;
            return this;
        }

        public PersoonlijkMatchHistoriek build() {
            return new PersoonlijkMatchHistoriek(this);
        }
    }
}
