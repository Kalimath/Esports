package com.example.backend.repo;

import com.example.backend.model.Manager;
import com.example.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerDAO extends JpaRepository<Manager, Long> {
    Optional<Manager> findFirstByUsername(String username);


}
