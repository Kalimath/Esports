package com.example.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "ADRES", schema = "ESPORTS")
public class Adres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "straat")
    private String straat;

    @Column(name = "huisnr")
    private String huisnr;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "gemeente")
    private String gemeente;

    public Adres() {
    }

    private Adres(Builder builder) {
        setId(builder.id);
        setStraat(builder.straat);
        setHuisnr(builder.huisnr);
        setPostcode(builder.postcode);
        setGemeente(builder.gemeente);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getHuisnr() {
        return huisnr;
    }

    public void setHuisnr(String huisnr) {
        this.huisnr = huisnr;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }


    public static final class Builder {
        private Long id;
        private String straat;
        private String huisnr;
        private String postcode;
        private String gemeente;

        public Builder() {
        }

        public Builder(Adres copy) {
            this.id = copy.getId();
            this.straat = copy.getStraat();
            this.huisnr = copy.getHuisnr();
            this.postcode = copy.getPostcode();
            this.gemeente = copy.getGemeente();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder straat(String val) {
            straat = val;
            return this;
        }

        public Builder huisnr(String val) {
            huisnr = val;
            return this;
        }

        public Builder postcode(String val) {
            postcode = val;
            return this;
        }

        public Builder gemeente(String val) {
            gemeente = val;
            return this;
        }

        public Adres build() {
            return new Adres(this);
        }
    }
}