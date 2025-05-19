package com.ib.clientsservice.service;

import com.ib.clientsservice.entity.Province;

import java.util.List;

public interface IProvinceService {
    public List<Province> listAllProvinces();
    public String getProvinceDescription(Long id);

}
