package com.ib.authservice.controller;

import com.ib.authservice.response.CredentialCreationResponse;
import com.ib.authservice.response.CredentialLoginResponse;
import com.ib.authservice.response.CredentialUpdateResponse;
import com.ib.authservice.entity.Credential;
import com.ib.authservice.service.CredentialService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/credentials")
public class CredentialController {
    @Autowired
    private CredentialService credentialService;


    @GetMapping("/status")
    public ResponseEntity<Boolean> serviceIsOnline(){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<?> verifyCredential(@RequestBody Credential credential){
        CredentialLoginResponse result = credentialService.isValidCredential(credential);
        return switch(result.getStatus()){
            case ACCEPTED -> ResponseEntity.ok(Map.of("type","CLIENT","DNI",result.getDniClient()));
            case ADMIN -> ResponseEntity.ok(Map.of("type","ADMIN"));
            case NOT_FOUND -> ResponseEntity.notFound().build();
            case BAD_REQUEST -> ResponseEntity.badRequest().build();
        };
    }

    @PostMapping
    public ResponseEntity<?> addCredential(@RequestBody Credential credential){
        CredentialCreationResponse addResult = credentialService.addCredential(credential);
        return switch(addResult.getStatus()){
            case SUCCESS -> ResponseEntity.ok(addResult.getCredential());
            case USER_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","USER_ALREADY_EXISTS"));
            case DNI_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","DNI_ALREADY_EXISTS"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Credential> getCredentialByDni(@PathVariable String dni){
        return credentialService.getCredentialByDni(dni).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{dni}/update")
    public ResponseEntity<?> updateCredential (@PathVariable String dni,@RequestBody Credential credential){
        if(!credential.getDniClient().equals(dni)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        CredentialUpdateResponse updateResult = credentialService.updateCredential(credential);
        return switch(updateResult.getStatus()){
            case SUCCESS -> ResponseEntity.ok(updateResult.getCredential());
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
            case NOT_FOUND -> ResponseEntity.notFound().build();// ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND"));
            case USER_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","USER_ALREADY_EXISTS"));
            case INVALID_DATA ->ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }

    @PutMapping("/{dni}/delete")
    public ResponseEntity<Boolean> credentialDelete(@PathVariable String dni){
        if(credentialService.disableCredential(dni)) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }
}
