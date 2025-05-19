package com.ib.accountservice.service;

import com.ib.accountservice.entity.AccountType;
import com.ib.accountservice.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountTypeService implements IAccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    @Autowired
    public AccountTypeService(AccountTypeRepository accountTypeRepository){
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    public List<AccountType> getAllAccountTypes() {
        return accountTypeRepository.findAllByIdNot(0L);
    }

    @Override
    public String getAccountType(Long id) {
        if(id.compareTo(0L)==0) return "";
        AccountType accountType = accountTypeRepository.findById(id).orElse(null);
        if(accountType != null) return accountType.getDescription();
        return "";
    }
}
