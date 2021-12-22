package com.example.backend.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

@Entity
@Table(name = "WEDSTRIJD", schema = "ESPORTS")
public class Wedstrijd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "tijdstip")
    private LocalDateTime tijdstip;



    public Wedstrijd() {

    }

    private Wedstrijd(Builder builder) {
        setId(builder.id);
        setTijdstip(builder.tijdstip);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(LocalDateTime tijdstip) {
        this.tijdstip = tijdstip;
    }


    public static final class Builder {
        private Long id;
        private LocalDateTime tijdstip;


        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder tijdstip(LocalDateTime val) {
            tijdstip = val;
            return this;
        }




        public Wedstrijd build() {
            return new Wedstrijd(this);
        }
    }
}
