package com.ib.clientsservice.controller;

import com.ib.clientsservice.entity.Locality;
import com.ib.clientsservice.entity.Province;
import com.ib.clientsservice.service.LocalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/localities")
public class LocalityController {

    @Autowired
    private LocalityService localityService;

    @GetMapping
    public ResponseEntity<List<Locality>> getAllLocalities(){
        List<Locality> list = localityService.listAllLocalities();
        if(list.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locality> getLocality(@PathVariable Long id){
        return localityService.getLocality(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());

    }

    @GetMapping("/province/{id}")
    public ResponseEntity<List<Locality>> getLocalityListByProvince(@PathVariable Long id){
        List<Locality> list = localityService.listAllLocalitiesByProvince(id);
        if(list.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(list);
    }
}
