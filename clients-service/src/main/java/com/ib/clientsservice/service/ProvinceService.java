package com.ib.clientsservice.service;

import com.ib.clientsservice.entity.Province;
import com.ib.clientsservice.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService implements IProvinceService{

    private final ProvinceRepository provinceRepository;

    @Autowired
    public ProvinceService(ProvinceRepository provinceRepository){
        this.provinceRepository = provinceRepository;
    }

    @Override
    public List<Province> listAllProvinces() {
        return provinceRepository.findAll();
    }

    @Override
    public String getProvinceDescription(Long id) {
        Province province = provinceRepository.findById(id).orElse(null);
        if(province != null){
            return province.getDescription();
        }
        return "";
    }
}
