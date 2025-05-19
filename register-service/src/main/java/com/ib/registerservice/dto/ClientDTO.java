package com.ib.registerservice.dto;

import lombok.*;

import java.util.Date;



@Data
public class ClientDTO {
    private String dni;
    private Long idLocality;
    private Long idGender;
    private String cuil;
    private String surname;
    private String name;
    private Date dob;
    private String address;
    private String email;
    private String phoneNumber;
    private boolean active;
}