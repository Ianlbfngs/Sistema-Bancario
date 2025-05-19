package com.ib.authservice.service;

import com.ib.authservice.response.CredentialCreationResponse;
import com.ib.authservice.response.CredentialLoginResponse;
import com.ib.authservice.response.CredentialUpdateResponse;
import com.ib.authservice.entity.Credential;
import com.ib.authservice.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class CredentialService implements ICredentialService {

    private final CredentialRepository credentialRepository;

    @Autowired
    public CredentialService(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }

    @Override
    public CredentialLoginResponse isValidCredential(Credential credential) {
        try{
            Optional<Credential> resultCredential = credentialRepository.getCredentialByUserAndPasswordAndActive(credential.getUser(),credential.getPassword(),true);
            if(resultCredential.isEmpty()) return new CredentialLoginResponse(CredentialLoginResponse.Status.NOT_FOUND,null);
            if(resultCredential.get().getId() == 0L) return new CredentialLoginResponse(CredentialLoginResponse.Status.ADMIN,resultCredential.get().getDniClient());
            return new CredentialLoginResponse(CredentialLoginResponse.Status.ACCEPTED,resultCredential.get().getDniClient());
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new CredentialLoginResponse(CredentialLoginResponse.Status.BAD_REQUEST,null);
        }


    }

    @Override
    public CredentialCreationResponse addCredential(Credential credential) {
        if(credentialRepository.existsByUser(credential.getUser())){
            return new CredentialCreationResponse(CredentialCreationResponse.Status.USER_ALREADY_EXISTS,null);
        }
        if(credentialRepository.existsByDniClient(credential.getDniClient())){
            return new CredentialCreationResponse(CredentialCreationResponse.Status.DNI_ALREADY_EXISTS,null); //caso muy raro, dni asociado ya tiene credenciales
        }
        try{
            Credential newCredential = credentialRepository.save(credential);
            return new CredentialCreationResponse(CredentialCreationResponse.Status.SUCCESS,newCredential);
        }catch(DataIntegrityViolationException e){
            System.out.println(e.getMessage());
            return new CredentialCreationResponse(CredentialCreationResponse.Status.INVALID_DATA,null);
        }
    }

    @Override
    public List<Credential> listAllCredentials() {
        return credentialRepository.findByActive(true);
    }

    @Override
    public CredentialUpdateResponse updateCredential(Credential credential) {
        if(credential.getDniClient().equals("00000000")) return new CredentialUpdateResponse(CredentialUpdateResponse.Status.FORBIDDEN,null);
        Optional<Credential> oldCredential = credentialRepository.getCredentialByDniClientAndActive(credential.getDniClient(),true);
        if(oldCredential.isEmpty()) return new CredentialUpdateResponse(CredentialUpdateResponse.Status.NOT_FOUND,null);
        if(!credential.getUser().equals(oldCredential.get().getUser()) && credentialRepository.existsCredentialByUser(credential.getUser())) return new CredentialUpdateResponse(CredentialUpdateResponse.Status.USER_ALREADY_EXISTS,null);
        try{
            Credential updatedCredential = credentialRepository.save(credential);
            return new CredentialUpdateResponse(CredentialUpdateResponse.Status.SUCCESS,updatedCredential);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new CredentialUpdateResponse(CredentialUpdateResponse.Status.INVALID_DATA,null);
        }
    }

    @Override
    public Optional<Credential> getCredentialByDni(String dni) {
        return credentialRepository.findCredentialByDniClientAndActive(dni,true);
    }

    @Override
    public Optional<Credential> updatePassword(Credential credential) {
        return credentialRepository.findByUser((credential.getUser()))
                .map(existingCredential ->{
                    existingCredential.setPassword(credential.getPassword());
                    return credentialRepository.save(existingCredential);
                });
    }

    @Override
    public Optional<Credential> updateUser(Credential credential) {
        return  credentialRepository.findByDniClient(credential.getDniClient()).map(
          existingCredential ->{
              existingCredential.setUser(credential.getUser());
              return credentialRepository.save(existingCredential);
          }
        );

    }

    @Override
    public boolean disableCredential(String dni) {
        return credentialRepository.findByDniClient(dni).map(existingCredential -> {
            existingCredential.setActive(false);
            credentialRepository.save(existingCredential);
            return true;
        }).orElse(false);
    }


}
