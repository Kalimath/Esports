package com.example.backend.DTO;

import com.example.backend.model.Team;
import java.time.LocalDateTime;
import java.util.Set;

public class WedstrijdDTO {

    private long team_id_1;
    private long team_id_2;
    private long wedstrijd_id;
    private LocalDateTime tijdstip;

    public WedstrijdDTO() {
    }

    public WedstrijdDTO(long team_id_1, long team_id_2, LocalDateTime tijdstip) {
        this.team_id_1 = team_id_1;
        this.team_id_2 = team_id_2;
        this.tijdstip = tijdstip;
    }


    public long getTeam_id_1() {
        return team_id_1;
    }

    public void setTeam_id_1(long team_id) {
        this.team_id_1 = team_id;
    }

    public long getTeam_id_2() {
        return team_id_2;
    }

    public void setTeam_id_2(long team_id_2) {
        this.team_id_2 = team_id_2;
    }

    public long getWedstrijd_id() {
        return wedstrijd_id;
    }

    public void setWedstrijd_id(long wedstrijd_id) {
        this.wedstrijd_id = wedstrijd_id;
    }


    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(LocalDateTime tijdstip) {
        this.tijdstip = tijdstip;
    }
}
