package com.ib.accountservice.controller;

import com.ib.accountservice.response.AccountCreationResponse;
import com.ib.accountservice.response.AccountDeleteResponse;
import com.ib.accountservice.response.AccountSpecificGetResponse;
import com.ib.accountservice.response.AccountUpdateResponse;
import com.ib.accountservice.entity.Account;
import com.ib.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {


    @Autowired
    private AccountService accountService;


    @GetMapping //all
    public ResponseEntity<List<Account>> getAllAccounts(){
        List<Account> list = accountService.getAllAccounts();
        if(list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/id/{id}") //id
    public ResponseEntity<?> getAccountById(@PathVariable Long id){
        AccountSpecificGetResponse getResult = accountService.getAccountById(id);
        return getResponseEntity(getResult);
    }
    @GetMapping("/cbu/{cbu}") //cbu
    public ResponseEntity<?> getAccountByCBU(@PathVariable String cbu){
        AccountSpecificGetResponse getResult = accountService.getAccountByCBU(cbu);
        return getResponseEntity(getResult);

    }

    private ResponseEntity<?> getResponseEntity(AccountSpecificGetResponse getResult) {
        return switch(getResult.getStatus()){
            case SUCCESS ->  ResponseEntity.ok(getResult.getAccount());
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND"));
            case DELETED -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","DELETED"));
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }

    @GetMapping("/dni/{dni}") //dni
    public ResponseEntity<List<Account>> getAccountByDni(@PathVariable String dni){
        List<Account> list = accountService.getAccountsByDni(dni);
        if(list.isEmpty())return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);

    }
    @GetMapping("/dni/{dni}/count") //dni
    public ResponseEntity<Integer> countAccountsByDni(@PathVariable String dni){
        return ResponseEntity.ok(accountService.countAccountsByDni(dni));
    }

    @PostMapping //add
    public ResponseEntity<?> addAccount(@RequestBody Account account){
        AccountCreationResponse addResult = accountService.addAccount(account);
        return switch (addResult.getStatus()){
            case SUCCESS -> ResponseEntity.ok(addResult.getAccount());
            case THREE_ACCOUNTS_MAX -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","THREE_ACCOUNTS_MAX"));
            case CBU_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","CBU_ALREADY_EXISTS"));
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
            case WRONG_STRING_FORMAT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","WRONG_STRING_FORMAT"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }

    @PutMapping("/{id}/update") //update
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account account){
        if(!Objects.equals(id, account.getId())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","BAD_REQUEST"));
        AccountUpdateResponse updateResult = accountService.updateAccount(account);
        return switch(updateResult.getStatus()) {
            case SUCCESS -> ResponseEntity.ok(updateResult.getAccount());
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "FORBIDDEN"));
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "NOT_FOUND"));
            case CBU_ALREADY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "CBU_ALREADY_EXISTS"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "INVALID_DATA"));
        };
    }


    @PutMapping("/{id}/delete") //del
    public ResponseEntity<?> deleteAccount(@PathVariable Long id){
        AccountDeleteResponse deleteResult = accountService.deleteAccount(id);
        return switch (deleteResult.getStatus()){
            case SUCCESS -> ResponseEntity.ok(deleteResult.getResult());
            case STILL_HAS_BALANCE -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","STILL_HAS_BALANCE"));
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","FORBIDDEN"));
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND"));
            case ALREADY_DELETED -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","ALREADY_DELETED"));
            case INVALID_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","INVALID_DATA"));
        };
    }
}
