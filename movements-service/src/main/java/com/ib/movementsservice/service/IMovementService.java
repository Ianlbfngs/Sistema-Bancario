package com.ib.movementsservice.service;

import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.entity.MovementType;
import com.ib.movementsservice.response.MovementCreationResponse;

import java.util.List;
import java.util.Optional;

public interface IMovementService {
    public List<Movement> getAllMovements();
    public List<Movement> getMovementsByType(Long id);
    public List<Movement> getMovementsByOriginAccount(Long id);
    public List<Movement> getMovementsByTargetCBU(String cbu);
    public Optional<Movement>getMovementById(Long id);
    public MovementCreationResponse addMovement(Movement movement);
    public boolean deleteMovement(Long id);
}
