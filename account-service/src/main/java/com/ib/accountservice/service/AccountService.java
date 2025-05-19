package com.ib.accountservice.service;

import com.ib.accountservice.response.AccountCreationResponse;
import com.ib.accountservice.response.AccountDeleteResponse;
import com.ib.accountservice.response.AccountSpecificGetResponse;
import com.ib.accountservice.response.AccountUpdateResponse;
import com.ib.accountservice.entity.Account;
import com.ib.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService{

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }



    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAllByActiveAndAccountType_IdNot(true,0L);
    }

    @Override
    public List<Account> getAccountsByDni(String dni) {
        return accountRepository.findByDniClientAndActiveAndDniClientNot(dni,true,"00000000");
    }

    @Override
    public AccountSpecificGetResponse getAccountById(Long id) {
        if(id.equals(0L)) return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.FORBIDDEN,null); //Bank account is FORBIDDEN
        try{
            Optional<Account> accountSearched = accountRepository.findByIdAndActive(id,true);
            if(accountSearched.isEmpty()) return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.NOT_FOUND,null); //Account not found
            else if(!accountSearched.get().isActive()) return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.DELETED,null); //The account is deleted
            else return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.SUCCESS,accountSearched.get());
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.INVALID_DATA,null);

        }
    }

    @Override
    public AccountSpecificGetResponse getAccountByCBU(String cbu) {
        if(cbu.equals("000000000000000000000")) return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.FORBIDDEN,null); //Bank account is FORBIDDEN
        try{
            Optional<Account> accountSearched = accountRepository.findByCbuAndActive(cbu,true);
            if(accountSearched.isEmpty()) return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.NOT_FOUND,null); //Account not found
            else if(!accountSearched.get().isActive()) return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.DELETED,null); //The account is deleted
            else return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.SUCCESS,accountSearched.get());
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return new AccountSpecificGetResponse(AccountSpecificGetResponse.Status.INVALID_DATA,null);

        }
    }

    @Override
    public AccountCreationResponse addAccount(Account account) {
        if(account.getDniClient().equals("00000000")) return new AccountCreationResponse(AccountCreationResponse.Status.FORBIDDEN,null); //Bank account is FORBIDDEN
        if(!account.getDniClient().matches("^\\d+$") || !account.getCbu().matches("^\\d+$")){
            return new AccountCreationResponse(AccountCreationResponse.Status.WRONG_STRING_FORMAT,null);
        }
        if(accountRepository.countByDniClient(account.getDniClient())>=3){
            return new AccountCreationResponse(AccountCreationResponse.Status.THREE_ACCOUNTS_MAX,null);//client already has 3 accounts (3 == max)
        }
        if(accountRepository.existsByCbu(account.getCbu())){
            return  new AccountCreationResponse(AccountCreationResponse.Status.CBU_ALREADY_EXISTS,null); //CBU is in use
        }
        try{
            Account newAccount = accountRepository.save(account);
            return new AccountCreationResponse(AccountCreationResponse.Status.SUCCESS,newAccount); //success

        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return new AccountCreationResponse(AccountCreationResponse.Status.INVALID_DATA,null); //error when adding
        }
    }

    @Override
    public Integer countAccountsByDni(String dni) {
        return accountRepository.countByDniClientAndDniClientNotAndActive(dni,"00000000",true); //Return the amount of accounts with X dni
    }

    @Override
    public AccountUpdateResponse updateAccount(Account account){
        if(account.getDniClient().equals("00000000") || account.getId().equals(0L)) return new AccountUpdateResponse(AccountUpdateResponse.Status.FORBIDDEN,null);
        Optional<Account> oldAccount = accountRepository.getAccountByIdAndActive(account.getId(),true);
        if(oldAccount.isEmpty()) return new AccountUpdateResponse(AccountUpdateResponse.Status.NOT_FOUND,null);
        if(!account.getCbu().equals(oldAccount.get().getCbu()) && accountRepository.existsAccountByCbu(account.getCbu())) return new AccountUpdateResponse(AccountUpdateResponse.Status.CBU_ALREADY_EXISTS,null);
        try{
            Account updatedAccount = accountRepository.save(account);
            return new AccountUpdateResponse(AccountUpdateResponse.Status.SUCCESS,updatedAccount);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new AccountUpdateResponse(AccountUpdateResponse.Status.INVALID_DATA,null);
        }
    }

    @Override
    public AccountDeleteResponse deleteAccount(Long id) {
        if (id.equals(0L)) return new AccountDeleteResponse(AccountDeleteResponse.Status.FORBIDDEN,null); //Bank account is FORBIDDEN
        try{
            Optional<Account> accountToDelete = accountRepository.findById(id);
            if(accountToDelete.isPresent()){
                if(!accountToDelete.get().isActive()) return new AccountDeleteResponse(AccountDeleteResponse.Status.ALREADY_DELETED,null); //account is there but not active/deleted

                if(accountToDelete.get().getBalance().compareTo(BigDecimal.ZERO) ==0){
                    accountToDelete.get().setActive(false);
                    accountRepository.save(accountToDelete.get());
                    return new AccountDeleteResponse(AccountDeleteResponse.Status.SUCCESS,true); //success

                }else{
                    return new AccountDeleteResponse(AccountDeleteResponse.Status.STILL_HAS_BALANCE,null); //account still has balance

                }
            }else{
                return new AccountDeleteResponse(AccountDeleteResponse.Status.NOT_FOUND,null); //account searched not found
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new AccountDeleteResponse(AccountDeleteResponse.Status.INVALID_DATA,null); //error when searching the account
        }
    }
}
