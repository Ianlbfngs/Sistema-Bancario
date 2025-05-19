package com.ib.clientsservice.controller;

import com.ib.clientsservice.entity.Locality;
import com.ib.clientsservice.entity.Province;
import com.ib.clientsservice.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @GetMapping
    public ResponseEntity<List<Province>> getAllProvinces(){
        List<Province> provinces = provinceService.listAllProvinces();
        if(provinces.isEmpty()) return  ResponseEntity.noContent().build();
        else return  ResponseEntity.ok(provinces);

    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProvinceDescription(@PathVariable Long id){
        String description = provinceService.getProvinceDescription(id);
        if(description.isEmpty()) return ResponseEntity.notFound().build();
        else return  ResponseEntity.ok(description);

    }

}
