package com.example.backend.repo;

import com.example.backend.model.Speler;
import com.example.backend.model.Speler_team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpelerDao extends JpaRepository<Speler, Long> {
    Optional<Speler> findFirstByVoornaamAndNaamIgnoreCase(String voornaam, String naam);
    Optional<Speler> findFirstByUsername(String username);

}
