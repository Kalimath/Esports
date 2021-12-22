package com.example.backend.DTO;

public class RegistratieDTO {
    private long team_id;
    private long speler_id;
    private boolean isreserve;

    public RegistratieDTO() {
    }

    public long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(long team_id) {
        this.team_id = team_id;
    }

    public long getSpeler_id() {
        return speler_id;
    }

    public void setSpeler_id(long speler_id) {
        this.speler_id = speler_id;
    }

    public boolean getIsreserve() {
        return isreserve;
    }

    public void setIsreserve(boolean isreserve) {
        this.isreserve = isreserve;
    }
}
