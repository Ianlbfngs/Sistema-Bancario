package com.ib.clientsservice.service;

import com.ib.clientsservice.entity.Locality;
import com.ib.clientsservice.entity.Province;
import com.ib.clientsservice.repository.LocalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalityService implements  ILocalityService{

    private final LocalityRepository localityRepository;

    @Autowired
    public LocalityService (LocalityRepository localityRepository){
        this.localityRepository = localityRepository;
    }


    @Override
    public List<Locality> listAllLocalities() {
        return localityRepository.findAll();
    }

    @Override
    public List<Locality> listAllLocalitiesByProvince(Long id) {
        Province province = new Province();
        province.setId(id);
        return localityRepository.findAllByProvince(province);
    }

    @Override
    public Optional<Locality> getLocality(Long id) {
        return localityRepository.findById(id);

    }
}
