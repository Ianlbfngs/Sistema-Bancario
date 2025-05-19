package com.ib.clientsservice.mapper;

import com.ib.clientsservice.dto.ClientDTO;
import com.ib.clientsservice.entity.Client;
import com.ib.clientsservice.entity.Gender;
import com.ib.clientsservice.entity.Locality;

public class ClientMapper {
    public static Client toClient(ClientDTO dto){
        Client client = new Client();
        client.setActive(dto.isActive());
        client.setDni(dto.getDni());
        client.setDob(dto.getDob());
        client.setCuil(dto.getCuil());
        client.setEmail(dto.getEmail());
        client.setAddress(dto.getAddress());
        client.setName(dto.getName());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setSurname(dto.getSurname());

        Locality locality = new Locality();
        locality.setId(dto.getIdLocality());
        client.setLocality(locality);
        Gender gender = new Gender();
        gender.setId(dto.getIdGender());
        client.setGender(gender);

        return client;
    }
}
