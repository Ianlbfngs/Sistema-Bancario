package com.ib.clientsservice.service;

import com.ib.clientsservice.entity.Gender;
import com.ib.clientsservice.repository.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenderService implements IGenderService {

    private final GenderRepository genderRepository;

    @Autowired
    public GenderService(GenderRepository genderRepository){
        this.genderRepository = genderRepository;
    }

    @Override
    public List<Gender> listAllGenders() {
        return genderRepository.findAll();
    }

    @Override
    public String getGender(Long id) {
        Gender gender = genderRepository.findById(id).orElse(null);
        if(gender !=null){
            return gender.getDescription();
        }
        return null;

    }
}
