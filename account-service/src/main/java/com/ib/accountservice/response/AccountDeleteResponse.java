package com.ib.accountservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountDeleteResponse {
    public enum Status{
        SUCCESS,STILL_HAS_BALANCE,FORBIDDEN,NOT_FOUND,ALREADY_DELETED, INVALID_DATA
    }

    private final Status status;
    private final Boolean result;

}