package com.ib.authservice.response;

import com.ib.authservice.entity.Credential;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CredentialUpdateResponse {
    public enum Status{
        SUCCESS, FORBIDDEN,NOT_FOUND,USER_ALREADY_EXISTS, INVALID_DATA
    }

    private Status status;
    private Credential credential;

}