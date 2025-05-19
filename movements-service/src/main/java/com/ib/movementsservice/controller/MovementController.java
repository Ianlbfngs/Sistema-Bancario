package com.ib.movementsservice.controller;

import com.ib.movementsservice.dto.AccountDTO;
import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.service.MovementService;
import com.ib.movementsservice.response.MovementCreationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    @Autowired
    private MovementService movementService;

    private final RestTemplate restTemplate;
    public MovementController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping("/origin/{id}") //Origin account
    public ResponseEntity<List<Movement>> getMovementsByOriginAccount(@PathVariable Long id){
        List<Movement> list = movementService.getMovementsByOriginAccount(id);
        if(list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/target/{cbu}") //Target cbu
    public ResponseEntity<List<Movement>> getMovementsByTargetCBU(@PathVariable String cbu){
        List<Movement> list = movementService.getMovementsByTargetCBU(cbu);
        if(list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @PostMapping //add
    public ResponseEntity<?> addMovement(@RequestBody Movement movement){
        try{
            String urlOrigin = "http://api-gateway:8080/api/accounts/id/"+movement.getIdAccount();
            String urlTarget ="http://api-gateway:8080/api/accounts/cbu/"+movement.getCBU();
            AccountDTO originAccount = restTemplate.getForEntity(urlOrigin, AccountDTO.class).getBody();

            if(originAccount !=null && movement.getCBU().equals(originAccount.getCbu())) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","CANT_TRANSFER_TO_ORIGIN"));

            AccountDTO targetAccount = restTemplate.getForEntity(urlTarget, AccountDTO.class).getBody();

            if(originAccount == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","ORIGIN_NOT_FOUND"));
            if(targetAccount == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","TARGET_NOT_FOUND"));
            if(originAccount.getBalance().compareTo(movement.getAmount()) < 0) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","BALANCE_NOT_ENOUGH"));

            originAccount.setBalance(originAccount.getBalance().subtract(movement.getAmount()));
            targetAccount.setBalance(targetAccount.getBalance().add(movement.getAmount()));
            try{
                MovementCreationResponse addedMovement = movementService.addMovement(movement);
                switch(addedMovement.getStatus()){
                    case SUCCESS ->  {
                        try{
                            restTemplate.put("http://api-gateway:8080/api/accounts/"+originAccount.getId()+"/update",originAccount);
                            restTemplate.put("http://api-gateway:8080/api/accounts/"+targetAccount.getId()+"/update",targetAccount);
                            return ResponseEntity.ok().body(Map.of("name",addedMovement.getMovement()));
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                            movementService.deleteMovement(addedMovement.getMovement().getId());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","ERR_UPDATING_ACCOUNTS"));
                        }
                    }
                    case INVALID_DATA -> {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_MOVEMENT_DATA"));
                    }
                };

            }catch(Exception e){
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","ERR_CREATING_MOVEMENT"));
            }
        }catch(HttpClientErrorException.NotFound e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","ERR_GETTING_ACCOUNTS_NOT_FOUND"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","ERR_GETTING_ACCOUNTS_SERVER"));
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> getMovementsByTargetCBU(@PathVariable Long id){
        boolean deleteResult = movementService.deleteMovement(id);
        if(deleteResult) return ResponseEntity.ok(true);
        else return ResponseEntity.badRequest().build();
    }




}
