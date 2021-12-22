package com.example.backend.service;

import com.example.backend.DTO.ManagerDTO;
import com.example.backend.config.ManagerPrincipal;
import com.example.backend.exceptions.ManagerNotFound;
import com.example.backend.exceptions.TeamNotFound;
import com.example.backend.model.Manager;
import com.example.backend.model.Speler;
import com.example.backend.model.Team;
import com.example.backend.repo.ManagerDAO;
import com.example.backend.repo.SpelerDao;
import com.example.backend.repo.TeamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerDAO managerDAO;
    private final TeamDao teamDao;
    private final SpelerDao spelerDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ManagerService(ManagerDAO managerDAO, TeamDao teamDao, SpelerDao spelerDao) {
        this.managerDAO = managerDAO;
        this.teamDao = teamDao;
        this.spelerDao = spelerDao;
    }

    /**
     * @return geeft de ingelogde manager terug
     */
    public Manager getIngelogdeManager(){
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Manager manager = principal.getManager();

            return manager;

    }

    /**
     * @param managerDTO  data met waarmee een manager moet worden opgeslagen
     * @return de aangemaakte manager
     */
    public ManagerDTO createManager(ManagerDTO managerDTO) {
        Manager manager = new Manager.Builder()
                .naam(managerDTO.getNaam())
                .email(managerDTO.getEmail())
                .username(managerDTO.getUsername())
                .password(passwordEncoder.encode(managerDTO.getPassword()))
                .build();
        managerDAO.save(manager);
        managerDTO.setId(manager.getId());
        return managerDTO;
     }

    /**
     * @param username
     * @return controlleerd of een username al dan niet al bestaad
     */
     public boolean usernameBestaad(String username){
        Optional<Speler> speler = spelerDao.findFirstByUsername(username);
        Optional<Manager> manager = managerDAO.findFirstByUsername(username);
        if (speler.isPresent()|| manager.isPresent()){
            return true;
        }
        return false;
     }




    /**
     * @param id
     * @param dto
     * @return aanpassen manager met de meegegeven waardes
     */
    public Optional<Manager> updateManager(Long id, ManagerDTO dto) {
        Optional<Manager> manager = managerDAO.findById(id);

        if(manager.isPresent()){
            Manager managerDB = manager.get();
            managerDB.setNaam(dto.getNaam());
            managerDB.setEmail(dto.getEmail());
            return manager;
        }

        return Optional.empty();
    }

    /**
     * @param id
     * @param teamid
     * @return geeft een team een manager
     */
    @Transactional
    public ResponseEntity<String> updateTeamManager(Long id, Long teamid) {
        Optional<Team> team = teamDao.findById(teamid);
        Optional<Manager> manager = managerDAO.findById(id);
        try {
            if(team.isPresent()) {
                if (manager.isPresent()) {
                    team.get().setManager(manager.get());
                    return ResponseEntity.status(HttpStatus.OK).build();
                } else {
                    throw new ManagerNotFound("Manager is niet gevonden");
                }
            }
            else {
                throw new TeamNotFound("Team is niet gevonden");
            }
        } catch (ManagerNotFound | TeamNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * @param id
     * @return haal een manager op met de meegegeven id
     */
    public Optional<Manager> getById(Long id) {
        return managerDAO.findById(id);
    }
}
