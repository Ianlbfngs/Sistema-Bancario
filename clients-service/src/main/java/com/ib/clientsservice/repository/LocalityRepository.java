package com.ib.clientsservice.repository;

import com.ib.clientsservice.entity.Locality;
import com.ib.clientsservice.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalityRepository extends JpaRepository<Locality,Long> {
    List<Locality> findAllByProvince(Province province);
}
