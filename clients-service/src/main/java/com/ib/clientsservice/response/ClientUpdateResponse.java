package com.ib.clientsservice.response;


import com.ib.clientsservice.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientUpdateResponse {
    public enum Status {
        SUCCESS, FORBIDDEN,NOT_FOUND, CUIL_ALREADY_EXISTS, EMAIL_ALREADY_EXISTS, INVALID_DATA
    }

    private Status status;
    private Client client;
}
