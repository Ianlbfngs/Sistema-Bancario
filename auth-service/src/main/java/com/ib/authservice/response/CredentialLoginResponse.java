package com.ib.authservice.response;

import com.ib.authservice.entity.Credential;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CredentialLoginResponse {
    public enum Status{
        ACCEPTED, ADMIN,NOT_FOUND,BAD_REQUEST
    }

    private Status status;
    private String dniClient;

}