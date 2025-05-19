package com.ib.accountservice.response;

import com.ib.accountservice.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreationResponse {
    public enum Status{
        SUCCESS,CBU_ALREADY_EXISTS,THREE_ACCOUNTS_MAX,FORBIDDEN,WRONG_STRING_FORMAT, INVALID_DATA
    }

    private final Status status;
    private final Account account;


}
