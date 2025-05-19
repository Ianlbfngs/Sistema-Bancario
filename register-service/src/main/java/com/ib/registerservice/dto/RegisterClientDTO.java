package com.ib.registerservice.dto;

import lombok.Data;

@Data
public class RegisterClientDTO {
    private ClientDTO client;
    private CredentialDTO credential;
}
