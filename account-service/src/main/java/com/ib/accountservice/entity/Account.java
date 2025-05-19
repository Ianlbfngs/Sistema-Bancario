package com.ib.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name= "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account_type")
    AccountType accountType;

    @NotNull
    @Size(min=8,max=8, message= "El dni debe tener 8 caracteres")
    @Column(name="dni_client")
    String dniClient;

    @NotNull
    @Column(name="creation_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date creationDate;

    @NotNull
    @Size(min=22,max=22, message="El cbu debe tener 22 caracteres")
    String cbu;

    @NotNull
    BigDecimal balance;

    @NotNull
    boolean active;


}
