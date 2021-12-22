package com.example.backend.repo;

import com.example.backend.model.Wedstrijd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WedstrijdDao extends JpaRepository<Wedstrijd, Long> {
    Optional<List<Wedstrijd>> findAllByTijdstipGreaterThan(LocalDateTime localDateTime);
}
