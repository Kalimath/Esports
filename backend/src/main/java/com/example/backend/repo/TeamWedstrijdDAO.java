package com.example.backend.repo;

import com.example.backend.model.Speler;
import com.example.backend.model.Speler_team;
import com.example.backend.model.Team;
import com.example.backend.model.Team_wedstrijd;
import com.example.backend.model.Wedstrijd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamWedstrijdDAO extends JpaRepository<Team_wedstrijd, Long> {

    Optional<List<Team_wedstrijd>> findTeam_wedstrijdsByWedstrijd(Wedstrijd wedstrijd);
    Optional<Team_wedstrijd> findTeam_wedstrijdByTeam_Id(long teamId);
    Optional<List<Team_wedstrijd>> findTeam_wedstrijdsByTeam(Team team);

}
