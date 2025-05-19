package com.ib.clientsservice.service;

import com.ib.clientsservice.entity.Locality;
import com.ib.clientsservice.entity.Province;

import java.util.List;
import java.util.Optional;

public interface ILocalityService {

    public List<Locality> listAllLocalities();
    public List<Locality> listAllLocalitiesByProvince(Long id);
    public Optional<Locality> getLocality(Long id);
}
