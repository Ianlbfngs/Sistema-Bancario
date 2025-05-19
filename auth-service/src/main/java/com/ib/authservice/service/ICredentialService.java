package com.ib.authservice.service;

import com.ib.authservice.response.CredentialCreationResponse;
import com.ib.authservice.response.CredentialLoginResponse;
import com.ib.authservice.response.CredentialUpdateResponse;
import com.ib.authservice.entity.Credential;

import java.util.List;
import java.util.Optional;

public interface ICredentialService {
    public CredentialLoginResponse isValidCredential(Credential credential);

    public CredentialCreationResponse addCredential(Credential credential);
    public Optional<Credential> getCredentialByDni(String dni);
    public List<Credential> listAllCredentials();
    public CredentialUpdateResponse updateCredential(Credential credential);
    public Optional<Credential> updatePassword(Credential credential);
    public Optional<Credential>updateUser(Credential credential);
    public boolean disableCredential(String dni);
}
