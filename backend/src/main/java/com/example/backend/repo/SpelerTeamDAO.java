package com.example.backend.repo;

import com.example.backend.model.Speler;
import com.example.backend.model.Speler_team;
import com.example.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpelerTeamDAO extends JpaRepository<Speler_team, Long> {
    Optional<Speler_team> findSpeler_teamBySpelerAndTeam(Speler speler, Team team);
    Optional<List<Speler_team>> findSpeler_teamsByTeam(Team team);
    Optional<List<Speler_team>> findSpeler_teamsByTeamAndIsreserve(Team team, boolean isreserve);
}
