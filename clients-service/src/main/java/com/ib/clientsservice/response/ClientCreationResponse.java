package com.ib.clientsservice.response;

import com.ib.clientsservice.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientCreationResponse {
    public enum Status{
        SUCCESS, DNI_ALREADY_EXISTS,EMAIL_ALREADY_EXISTS,CUIL_ALREADY_EXISTS,WRONG_STRING_FORMAT, INVALID_DATA
    }

    private final Status status;
    private final Client client;

}
