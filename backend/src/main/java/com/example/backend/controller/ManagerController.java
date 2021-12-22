package com.example.backend.controller;

import com.example.backend.DTO.ManagerDTO;
import com.example.backend.model.Manager;
import com.example.backend.service.ManagerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }



    @ApiOperation("Maakt een nieuwe manager aan")
    @PostMapping
    public ResponseEntity createManager(
            @RequestBody ManagerDTO managerDTO){
        if (managerService.usernameBestaad(managerDTO.getUsername())){
            return new ResponseEntity("Username bestaat al", HttpStatus.BAD_REQUEST);
        }
        managerService.createManager(managerDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("Geeft een manager")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Optional<Manager> getById(
            @ApiParam("Id van de manager")
            @PathVariable Long id){

        Manager manager = managerService.getIngelogdeManager();
        if (!manager.getId().equals(id)){
            return Optional.of(manager);
        }
        return managerService.getById(id);
    }

    @Transactional
    @ApiOperation("Update een manager")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Optional<Manager> updateManager(
            @ApiParam("Id van de manager")
            @PathVariable Long id,
            @ApiParam("Naam, Email")
            @RequestBody ManagerDTO dto) {
        return managerService.updateManager(id, dto);
    }

    @Transactional
    @ApiOperation("Veranderd de manager van een team")
    @PutMapping(value = "/update/{id}", params = "teamid")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> updateTeamManager(
            @ApiParam("Id van de manager")
            @PathVariable Long id,
            @ApiParam("Id van het team")
            @RequestParam Long teamid){
        return managerService.updateTeamManager(id, teamid);
    }
}
