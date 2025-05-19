package com.ib.authservice.repository;

import com.ib.authservice.entity.Credential;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential,Long> {
    Optional<Credential>  findByDniClient(@NotEmpty @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dniClient);

    Optional<Credential> findByUser(@NotEmpty String user);



    List<Credential> findByActive(Boolean active);

    boolean existsByUserAndPasswordAndActive(@NotEmpty String user, @NotEmpty String password, Boolean active);

    boolean existsByUser(@NotEmpty String user);

    boolean existsByDniClient(@NotEmpty @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dniClient);

    Optional<Credential> getCredentialByDniClient(@NotEmpty @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dniClient);

    Optional<Credential> getCredentialByDniClientAndActive(@NotEmpty @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dniClient, Boolean active);

    boolean existsCredentialByUser(@NotEmpty String user);

    Optional<Credential> findCredentialByDniClientAndActive(@NotEmpty @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dniClient, Boolean active);

    Optional<Credential> getCredentialByUserAndPasswordAndActive(@NotEmpty String user, @NotEmpty String password, Boolean active);
}
