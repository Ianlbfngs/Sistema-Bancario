package com.ib.accountservice.controller;

import com.ib.accountservice.entity.AccountType;
import com.ib.accountservice.repository.AccountTypeRepository;
import com.ib.accountservice.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/types")
public class AccountTypeController {

    @Autowired
    private AccountTypeService accountTypeService;

    @GetMapping
    public ResponseEntity<List<AccountType>> getAllAccountTypes(){
        List<AccountType> list = accountTypeService.getAllAccountTypes();
        if(list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getAccountType(@PathVariable Long id){
        String accountType = accountTypeService.getAccountType(id);
        if(accountType.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(accountType);
    }


}
