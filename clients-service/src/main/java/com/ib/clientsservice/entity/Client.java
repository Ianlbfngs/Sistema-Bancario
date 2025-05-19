package com.ib.clientsservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name= "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Client implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "DNI")
    @Size(min=8,max=8, message = "El DNI debe ser de 8 caracteres")
    private String dni;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="id_locality",nullable = false)
    private Locality locality;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="id_gender",nullable = false)
    private Gender gender;
    @Column(name = "CUIL")
    @Size(min=11,max=11, message = "El CUIL debe ser de 11 caracteres")
    private String cuil;

    @Size(max = 45)
    private String surname;
    @Size(max = 45)
    private String name;


    @Column(name="date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob; //date of birth
    @Size(max = 45)
    private String address;
    @Size(max = 45)
    private String email;
    @Column(name="phone_number")
    @Size(max = 45)
    private String phoneNumber;
    boolean active;

}