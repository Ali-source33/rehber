package com.rehber.controller;

import com.rehber.entity.Kisi;
import com.rehber.service.KisiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController  
@RequestMapping("/kisi")
public class KisiController {

    @Autowired
    private KisiService kisiService;

    @GetMapping("/{id}")
    public Kisi kisiGetir(@PathVariable int id) {
        return kisiService.kisiGetir(id);
    }
}