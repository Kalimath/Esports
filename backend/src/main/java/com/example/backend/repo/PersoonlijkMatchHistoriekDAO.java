package com.example.backend.repo;

import com.example.backend.model.PersoonlijkMatchHistoriek;
import com.example.backend.model.Speler;
import com.example.backend.model.Team_wedstrijd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersoonlijkMatchHistoriekDAO extends JpaRepository<PersoonlijkMatchHistoriek, Long> {
    Optional<List<PersoonlijkMatchHistoriek>> findPersoonlijkMatchHistoriekBySpeler(Speler speler);

   Optional<PersoonlijkMatchHistoriek> findPersoonlijkMatchHistoriekByTeamWedstrijd(Team_wedstrijd team_wedstrijd);
   Optional<PersoonlijkMatchHistoriek> findPersoonlijkMatchHistoriekBySpelerAndTeamWedstrijd(Speler speler, Team_wedstrijd team_wedstrijd);
}
