package com.ib.accountservice.repository;

import com.ib.accountservice.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    List<AccountType> findAllByIdNot(Long id);
}
