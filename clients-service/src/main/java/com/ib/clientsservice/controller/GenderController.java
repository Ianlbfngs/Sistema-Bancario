package com.ib.clientsservice.controller;

import com.ib.clientsservice.entity.Gender;
import com.ib.clientsservice.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genders")
public class GenderController {
    @Autowired
    private GenderService genderService;

    @GetMapping
    public ResponseEntity<List<Gender>> getAllGenders(){
        List<Gender> genders = genderService.listAllGenders();
        if(genders.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(genders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getGenderDescription(@PathVariable Long id){
        String description = genderService.getGender(id);
        if(description.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(description);
    }


}
