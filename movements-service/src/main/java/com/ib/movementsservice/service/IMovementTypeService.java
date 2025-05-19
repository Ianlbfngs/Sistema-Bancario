package com.ib.movementsservice.service;

import com.ib.movementsservice.entity.MovementType;

import java.util.List;
import java.util.Optional;

public interface IMovementTypeService {

    public List<MovementType> getAllMovementTypes();
    public String getMovementType(Long id);

}
