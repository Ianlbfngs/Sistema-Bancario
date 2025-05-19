package com.ib.accountservice.repository;

import com.ib.accountservice.entity.Account;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {



    Optional<Account> findByIdAndActive(Long id, @NotNull Boolean active);


    int countByDniClient(@NotNull @Size(min=8,max=8, message= "El dni debe tener 8 caracteres") String dniClient);



    List<Account> findAllByActiveAndAccountType_IdNot(@NotNull Boolean active, Long accountTypeId);


    List<Account> findByDniClientAndActiveAndDniClientNot(@NotNull @Size(min=8,max=8, message= "El dni debe tener 8 caracteres") String dniClient, @NotNull Boolean active, @NotNull @Size(min=8,max=8, message= "El dni debe tener 8 caracteres") String dniClient1);


    boolean existsByCbu(@NotNull @Size(min=22,max=22, message="El cbu debe tener 22 caracteres") String cbu);

    Optional<Account> getAccountByIdAndActive(Long id, @NotNull boolean active);

    boolean existsAccountByCbu(@NotNull @Size(min=22,max=22, message="El cbu debe tener 22 caracteres") String cbu);

    Integer countByDniClientAndDniClientNotAndActive(@NotNull @Size(min=8,max=8, message= "El dni debe tener 8 caracteres") String dniClient, @NotNull @Size(min=8,max=8, message= "El dni debe tener 8 caracteres") String dniClient1, @NotNull boolean active);

    Optional<Account> findByCbuAndActive(@NotNull @Size(min=22,max=22, message="El cbu debe tener 22 caracteres") String cbu, @NotNull boolean active);
}
