package com.ib.accountservice.response;

import com.ib.accountservice.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountUpdateResponse {
    public enum Status{
        SUCCESS, FORBIDDEN,NOT_FOUND,CBU_ALREADY_EXISTS, INVALID_DATA
    }

    private Status status;
    private Account account;

}