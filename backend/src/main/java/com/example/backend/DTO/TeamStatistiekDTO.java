package com.example.backend.DTO;

public class TeamStatistiekDTO {
    private int wins, losses;
    private double wn8;

    public TeamStatistiekDTO() {
    }

    private TeamStatistiekDTO(Builder builder) {
        setWins(builder.wins);
        setLosses(builder.losses);
        setWn8(builder.wn8);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public double getWn8() {
        return wn8;
    }

    public void setWn8(double wn8) {
        this.wn8 = wn8;
    }


    public static final class Builder {
        private int wins;
        private int losses;
        private double wn8;

        public Builder() {
        }

        public Builder wins(int val) {
            wins = val;
            return this;
        }

        public Builder losses(int val) {
            losses = val;
            return this;
        }

        public Builder wn8(double val) {
            wn8 = val;
            return this;
        }

        public TeamStatistiekDTO build() {
            return new TeamStatistiekDTO(this);
        }
    }
}
