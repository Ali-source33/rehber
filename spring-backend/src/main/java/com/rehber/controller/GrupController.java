package com.rehber.controller;

import com.rehber.entity.Grup;
import com.rehber.service.GrupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grup")
public class GrupController {

    @Autowired
    private GrupService grupService;
    @GetMapping("/{id}")
    public ResponseEntity<Grup> grupGetir(@PathVariable int id) {
        Grup grup = grupService.grupGetir(id);
        if (grup == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(grup);
    }

    @GetMapping("/tum")
    public ResponseEntity<List<Grup>> tumGruplariGetir() {
        List<Grup> gruplar = grupService.tumGruplariGetir();
        return ResponseEntity.ok(gruplar);
    }
}
