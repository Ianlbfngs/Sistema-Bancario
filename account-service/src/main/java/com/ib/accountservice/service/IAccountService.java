package com.ib.accountservice.service;

import com.ib.accountservice.response.AccountCreationResponse;
import com.ib.accountservice.response.AccountDeleteResponse;
import com.ib.accountservice.response.AccountSpecificGetResponse;
import com.ib.accountservice.response.AccountUpdateResponse;
import com.ib.accountservice.entity.Account;

import java.util.List;

public interface IAccountService {
    public List<Account> getAllAccounts();
    public AccountSpecificGetResponse getAccountById(Long id);
    public AccountSpecificGetResponse getAccountByCBU(String cbu);
    public List<Account> getAccountsByDni(String dni);
    public AccountCreationResponse addAccount(Account account);
    public Integer countAccountsByDni(String dni);
    public AccountUpdateResponse updateAccount(Account account);
    public AccountDeleteResponse deleteAccount(Long id);
}
