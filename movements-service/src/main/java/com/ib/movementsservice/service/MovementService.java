package com.ib.movementsservice.service;

import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.entity.MovementType;
import com.ib.movementsservice.repository.MovementRepository;
import com.ib.movementsservice.response.MovementCreationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementService implements IMovementService {

    private final MovementRepository movementRepository;

    public MovementService(MovementRepository movementRepository){
        this.movementRepository = movementRepository;
    }


    @Override
    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }

    @Override
    public List<Movement> getMovementsByType(Long id) {
        MovementType type = new MovementType();
        type.setId(id);
        return movementRepository.findByMovementType(type);
    }

    @Override
    public List<Movement> getMovementsByOriginAccount(Long id) {
        return movementRepository.findByIdAccount(id);
    }

    @Override
    public List<Movement> getMovementsByTargetCBU(String cbu) {
        return movementRepository.findByCBU(cbu);
    }

    @Override
    public Optional<Movement> getMovementById(Long id) {
        return movementRepository.findById(id);
    }

    @Override
    public MovementCreationResponse addMovement(Movement movement) {
        try{
            Movement addedMovement = movementRepository.save(movement);
            return new MovementCreationResponse(MovementCreationResponse.Status.SUCCESS,addedMovement);
        }catch(Exception e){
            return new MovementCreationResponse(MovementCreationResponse.Status.INVALID_DATA,null);
        }
    }

    @Override
    public boolean deleteMovement(Long id) {
        try{
            movementRepository.deleteById(id);
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
