package com.ib.movementsservice.response;

import com.ib.movementsservice.entity.Movement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovementCreationResponse {
    public enum Status{
        SUCCESS, INVALID_DATA
    }

    private final Status status;
    private final Movement movement;

}
