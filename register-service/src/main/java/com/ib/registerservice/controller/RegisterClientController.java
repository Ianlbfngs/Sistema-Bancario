package com.ib.registerservice.controller;

import ch.qos.logback.core.net.server.Client;
import com.ib.registerservice.dto.ClientDTO;
import com.ib.registerservice.dto.CredentialDTO;
import com.ib.registerservice.dto.RegisterClientDTO;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/register")
public class RegisterClientController {

    private final RestTemplate restTemplate;
    public RegisterClientController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> serviceIsOnline(){
        return ResponseEntity.ok(true);
    }

    @PostMapping
    public ResponseEntity<?> registerClient(@RequestBody RegisterClientDTO dto){
        try{
            ResponseEntity<ClientDTO> clientResponse = restTemplate.postForEntity("http://api-gateway:8080/api/clients",dto.getClient(),ClientDTO.class);
            ClientDTO addedClient = clientResponse.getBody();
            try{
                restTemplate.postForEntity("http://api-gateway:8080/api/credentials",dto.getCredential(), CredentialDTO.class);
                return ResponseEntity.ok(addedClient);
            }catch(HttpClientErrorException e){
                assert addedClient != null;
                restTemplate.delete("http://api-gateway:8080/api/clients/"+addedClient.getDni());
                return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
            }
        }catch (HttpClientErrorException e){
            return  ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

}
