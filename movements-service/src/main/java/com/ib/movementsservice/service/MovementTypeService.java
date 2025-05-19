package com.ib.movementsservice.service;

import com.ib.movementsservice.entity.MovementType;
import com.ib.movementsservice.repository.MovementTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementTypeService implements IMovementTypeService {

    private final MovementTypeRepository movementTypeRepository;

    @Autowired
    public MovementTypeService(MovementTypeRepository movementTypeRepository){
        this.movementTypeRepository = movementTypeRepository;
    }


    @Override
    public List<MovementType> getAllMovementTypes() {
        return movementTypeRepository.findAll();
    }

    @Override
    public String getMovementType(Long id) {
        MovementType movementType = movementTypeRepository.findById(id).orElse(null);
        if(movementType != null) return movementType.getDescription();
        return "";
    }
}
