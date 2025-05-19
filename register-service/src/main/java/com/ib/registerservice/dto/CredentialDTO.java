package com.ib.registerservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CredentialDTO {
    private String dniClient;
    private String user;
    private String password;
    private Boolean active;
}
