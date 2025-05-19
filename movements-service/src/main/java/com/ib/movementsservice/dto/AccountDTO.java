package com.ib.movementsservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class AccountDTO {
    private Long id;
    private AccountTypeDTO accountType;
    private String dniClient;
    private Date creationDate;
    private String cbu;
    private BigDecimal balance;
    private boolean active;
}

