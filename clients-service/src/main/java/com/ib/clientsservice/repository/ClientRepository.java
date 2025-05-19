package com.ib.clientsservice.repository;

import com.ib.clientsservice.entity.Client;
import io.micrometer.observation.ObservationFilter;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,String> {



    Optional<Client> findByDniAndActive(@Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dni, boolean active);

    Optional<Client> findByDni(@Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dni);

    boolean existsByDni(@Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dni);

    List<Client> findByActive(boolean active);

    boolean existsByEmail(@Size(max = 45) String email);

    boolean existsByCuil(@Size(min=11,max=11, message = "El CUIL debe ser de 11 caracteres") String cuil);

    Integer deleteByDni(@Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dni);

    List<Client> findByActiveAndDniNot(boolean active, @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dni);

    Optional<Client> getClientByDni(@Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dni);

    Optional<Client> getClientByDniAndActive(@Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres") String dni, boolean active);

    boolean existsClientByCuilAndActive(@Size(min=11,max=11, message = "El CUIL debe ser de 11 caracteres") String cuil, boolean active);

    boolean existsClientByCuil(@Size(min=11,max=11, message = "El CUIL debe ser de 11 caracteres") String cuil);

    boolean existsClientByEmail(@Size(max = 45) String email);
}
