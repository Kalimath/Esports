package com.example.backend.config;


import com.example.backend.model.Manager;
import com.example.backend.model.Speler;
import com.example.backend.repo.ManagerDAO;
import com.example.backend.repo.SpelerDao;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GebruikerUserDetailsService implements UserDetailsService {

    private final SpelerDao spelerDao;
    private final ManagerDAO managerDAO;


    public GebruikerUserDetailsService(SpelerDao spelerDao, ManagerDAO managerDAO) {
        this.spelerDao = spelerDao;
        this.managerDAO = managerDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Speler> speler = spelerDao.findFirstByUsername(s);
        if (speler.isPresent()){
            return new SpelerPrincipal(speler.get());
        }
        Optional<Manager> manager = managerDAO.findFirstByUsername(s);
        if (manager.isPresent()){
            return new ManagerPrincipal(manager.get());
        }
        throw new UsernameNotFoundException(String.format("User %s niet gevonden",s));
    }
}