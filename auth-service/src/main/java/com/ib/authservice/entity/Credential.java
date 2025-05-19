package com.ib.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table (name= "credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Credential implements Serializable {
    @Serial
    private static final long serialVersionUID= 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name="dni_client")
    @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres")
    private String dniClient;
    @NotEmpty
    private String user;
    @NotEmpty
    private String password;
    private Boolean active;
}
