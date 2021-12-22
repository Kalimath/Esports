package com.example.backend.DTO;

public class ScoreDTO {
    private int scoreTeamA, scoreTeamB;
    private long teamA, teamB;

    public ScoreDTO(int scoreTeamA, int scoreTeamB, long teamA, long teamB) {
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public int getScoreTeamA() {
        return scoreTeamA;
    }

    public void setScoreTeamA(int scoreTeamA) {
        this.scoreTeamA = scoreTeamA;
    }

    public int getScoreTeamB() {
        return scoreTeamB;
    }

    public void setScoreTeamB(int scoreTeamB) {
        this.scoreTeamB = scoreTeamB;
    }

    public long getTeamA() {
        return teamA;
    }

    public void setTeamA(long teamA) {
        this.teamA = teamA;
    }

    public long getTeamB() {
        return teamB;
    }

    public void setTeamB(long teamB) {
        this.teamB = teamB;
    }
}
