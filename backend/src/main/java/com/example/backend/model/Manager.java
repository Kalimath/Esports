package com.example.backend.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "manager", schema = "esports")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "naam")
    private String naam;

    @NotNull
    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    public Manager() {
    }

    private Manager(Builder builder) {
        setId(builder.id);
        setNaam(builder.naam);
        setEmail(builder.email);
        setUsername(builder.username);
        setPassword(builder.password);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    public static final class Builder {
        private Long id;
        private String naam;
        private String email;
        private String username;
        private String password;

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

        public Builder email(String val) {
            email = val;
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

        public Manager build() {
            return new Manager(this);
        }
    }
}