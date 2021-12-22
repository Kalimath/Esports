package com.example.backend.repo;

import com.example.backend.model.Adres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdresDAO extends JpaRepository<Adres, Long> {
    Optional<Adres> findAdresByGemeenteAndHuisnrAndPostcodeAndStraat(String gemeente,String huisnr,String Postcode, String straat);
}
