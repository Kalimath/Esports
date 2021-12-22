package com.example.backend.DTO;

import com.example.backend.model.Team;

import java.time.LocalDateTime;

public class WedstrijdListDTO {

    private Team teamA;
    private int scoreteama;
    private Team teamB;
    private int scoreteamb;
    private LocalDateTime tijdstip;
    private long wedstrijdId;

    public WedstrijdListDTO() {
    }

    public WedstrijdListDTO(Team teamA, int scoreTeamA, Team teamB, int scoreTeamB, LocalDateTime tijdstip, long wedstrijdId) {
        this.teamA = teamA;
        this.scoreteama = scoreTeamA;
        this.teamB = teamB;
        this.scoreteamb = scoreTeamB;
        this.tijdstip = tijdstip;
        this.wedstrijdId = wedstrijdId;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public int getScoreteama() {
        return scoreteama;
    }

    public void setScoreteama(int scoreteama) {
        this.scoreteama = scoreteama;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public int getScoreteamb() {
        return scoreteamb;
    }

    public void setScoreteamb(int scoreteamb) {
        this.scoreteamb = scoreteamb;
    }

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(LocalDateTime tijdstip) {
        this.tijdstip = tijdstip;
    }

    public long getWedstrijdId() {
        return wedstrijdId;
    }

    public void setWedstrijdId(long wedstrijdId) {
        this.wedstrijdId = wedstrijdId;
    }
}
