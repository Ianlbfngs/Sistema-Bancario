package com.ib.authservice.response;

import com.ib.authservice.entity.Credential;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CredentialCreationResponse {
    public enum Status{
        SUCCESS, USER_ALREADY_EXISTS, INVALID_DATA,DNI_ALREADY_EXISTS
    }

    private Status status;
    private Credential credential;

}

