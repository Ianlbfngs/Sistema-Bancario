package com.ib.movementsservice.repository;

import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.entity.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement,Long> {
    List<Movement> getMovementsByMovementType(MovementType movementType);

    List<Movement> findByMovementType(MovementType movementType);

    List<Movement> findByIdAccount(Long idAccount);

    List<Movement> findByCBU(String cbu);
}
