package com.ib.clientsservice.service;

import com.ib.clientsservice.entity.Gender;

import java.util.List;

public interface IGenderService {
    public List<Gender> listAllGenders();
    public String getGender(Long id);
}
