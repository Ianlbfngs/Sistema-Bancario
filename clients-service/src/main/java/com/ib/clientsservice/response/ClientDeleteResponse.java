package com.ib.clientsservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientDeleteResponse {
    public enum Status{
        SUCCESS,NOT_FOUND, STILL_HAS_ACTIVE_ACCOUNTS,ALREADY_DELETED,FORBIDDEN, INVALID_DATA
    }

    private final Status status;
    private final Boolean result;

}
