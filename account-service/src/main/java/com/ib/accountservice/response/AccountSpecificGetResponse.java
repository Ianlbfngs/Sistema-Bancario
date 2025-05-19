package com.ib.accountservice.response;

import com.ib.accountservice.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountSpecificGetResponse {
    public enum Status{
        SUCCESS, NOT_FOUND,DELETED,FORBIDDEN, INVALID_DATA
    }

    private final Status status;
    private final Account account;

}