package com.example.backend.DTO;

public class TeamDTO {
    private long id;
    private String naam;

    public TeamDTO() {
    }
    public TeamDTO(String naam){
        this.naam = naam;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
