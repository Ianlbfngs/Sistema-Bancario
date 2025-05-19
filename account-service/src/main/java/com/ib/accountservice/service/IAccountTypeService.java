package com.ib.accountservice.service;

import com.ib.accountservice.entity.AccountType;

import java.util.List;
import java.util.Optional;

public interface IAccountTypeService {
    public List<AccountType> getAllAccountTypes();
    public String getAccountType(Long id);
}
