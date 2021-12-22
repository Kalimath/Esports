package com.example.backend.repo;

import com.example.backend.model.Manager;
import com.example.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamDao extends JpaRepository<Team, Long> {
    Optional<Team> findFirstByNaam(String naam);
    Optional<List<Team>> findAllByManager(Manager manager);

}
