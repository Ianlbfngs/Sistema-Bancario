package com.ib.clientsservice.response;

import com.ib.clientsservice.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientSpecificGetResponse {
    public enum Status{
        SUCCESS, NOT_FOUND,DELETED,FORBIDDEN,WRONG_DNI_FORMAT, INVALID_DATA
    }

    private final Status status;
    private final Client client;

}
