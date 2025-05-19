package com.ib.movementsservice.controller;

import com.ib.movementsservice.entity.MovementType;
import com.ib.movementsservice.service.MovementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movements/types")
public class MovementTypeController {

    @Autowired
    private MovementTypeService movementTypeService;

    @GetMapping
    public ResponseEntity<List<MovementType>> getAllMovementTypes(){
        List<MovementType> list = movementTypeService.getAllMovementTypes();
        if(list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovementType(@PathVariable Long id){
        String movementType = movementTypeService.getMovementType(id);
        if(movementType.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(movementType);
    }
}
