package com.ib.clientsservice.controller;

import com.ib.clientsservice.dto.*;
import com.ib.clientsservice.entity.Client;
import com.ib.clientsservice.mapper.ClientMapper;
import com.ib.clientsservice.response.ClientCreationResponse;
import com.ib.clientsservice.response.ClientDeleteResponse;
import com.ib.clientsservice.response.ClientSpecificGetResponse;
import com.ib.clientsservice.response.ClientUpdateResponse;
import com.ib.clientsservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/status")
    public ResponseEntity<Boolean> serviceIsOnline(){
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty())  return ResponseEntity.noContent().build();
        return ResponseEntity.ok(clients);

    }

    @GetMapping("/{dni}")
    public ResponseEntity<?> getClientByDNI(@PathVariable String dni) {
        ClientSpecificGetResponse getResult = clientService.getClientByDNI(dni);
        return switch(getResult.getStatus()){
            case SUCCESS ->  ResponseEntity.ok(getResult.getClient());
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND"));
            case DELETED -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","DELETED"));
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
            case WRONG_DNI_FORMAT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","WRONG_DNI_FORMAT"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }

    @GetMapping("/{dni}/name")
    public ResponseEntity<?> getClientNameByDNI(@PathVariable String dni) {
        ClientSpecificGetResponse getResult = clientService.getClientByDNI(dni);
        return switch(getResult.getStatus()){
            case SUCCESS ->  ResponseEntity.ok().body(Map.of("name",getResult.getClient().getName()));
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND"));
            case DELETED -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","DELETED"));
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
            case WRONG_DNI_FORMAT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","WRONG_DNI_FORMAT"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }

    @PostMapping
    public ResponseEntity<?> addClient(@RequestBody ClientDTO dtoClient) {
        Client client = ClientMapper.toClient(dtoClient);
        ClientCreationResponse addResult = clientService.addClient(client);
        return switch(addResult.getStatus()){
            case SUCCESS -> ResponseEntity.ok(addResult.getClient());
            case DNI_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","DNI_ALREADY_EXISTS"));
            case CUIL_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","CUIL_ALREADY_EXISTS"));
            case EMAIL_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","EMAIL_ALREADY_EXISTS"));
            case WRONG_STRING_FORMAT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","WRONG_STRING_FORMAT"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }




    @PutMapping("/{dni}/delete")
    public ResponseEntity<?> logicDeleteClient(@PathVariable String dni){
        try{
            int accountCount = Integer.parseInt(Objects.requireNonNull(restTemplate.getForObject("http://api-gateway:8080/api/accounts/dni/" + dni + "/count", String.class)));
            ClientDeleteResponse deleteResult;
            if(accountCount !=0){
                deleteResult = new ClientDeleteResponse(ClientDeleteResponse.Status.STILL_HAS_ACTIVE_ACCOUNTS,null);
            }else{
                deleteResult= clientService.logicDeleteClient(dni);
            }
            return switch(deleteResult.getStatus()){
                case SUCCESS -> ResponseEntity.ok(deleteResult.getResult());
                case STILL_HAS_ACTIVE_ACCOUNTS ->ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","STILL_HAS_ACTIVE_ACCOUNTS"));
                case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND"));
                case ALREADY_DELETED -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","ALREADY_DELETED"));
                case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
                case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
            };
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","ACCOUNT_COUNT_ERROR"));
        }

    }

    @PutMapping("/{dni}/update")
    public ResponseEntity<?> updateClient(@PathVariable String dni,@RequestBody ClientDTO dtoClient){
        Client client = ClientMapper.toClient(dtoClient);
        if(!dni.equals(client.getDni())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","BAD_REQUEST"));
        ClientUpdateResponse updateResult = clientService.updateClient(client);
        return switch(updateResult.getStatus()){
            case SUCCESS -> ResponseEntity.ok(updateResult.getClient());
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND"));
            case CUIL_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","CUIL_ALREADY_EXISTS"));
            case EMAIL_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","EMAIL_ALREADY_EXISTS"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable String dni){
        if(clientService.deleteClient(dni) == 1) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }


}