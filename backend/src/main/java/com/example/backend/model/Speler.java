package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "speler", schema = "esports")
public class Speler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voornaam")
    @NotNull
    private String voornaam;

    @NotNull
    @Column(name = "naam")
    private String naam;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "geboortedatum")
    private LocalDate geboortedatum;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adres_id")
    private Adres adres;


    public Speler() {
    }

    private Speler(Builder builder) {
        setId(builder.id);
        setVoornaam(builder.voornaam);
        setNaam(builder.naam);
        setEmail(builder.email);
        setGeboortedatum(builder.geboortedatum);
        setUsername(builder.username);
        setPassword(builder.password);
        setAdres(builder.adres);
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

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }


    public static final class Builder {
        private Long id;
        private String voornaam;
        private String naam;
        private String email;
        private LocalDate geboortedatum;
        private String username;
        private String password;
        private Adres adres;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder voornaam(String val) {
            voornaam = val;
            return this;
        }

        public Builder naam(String val) {
            naam = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder geboortedatum(LocalDate val) {
            geboortedatum = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder adres(Adres val) {
            adres = val;
            return this;
        }

        public Speler build() {
            return new Speler(this);
        }
    }
}
