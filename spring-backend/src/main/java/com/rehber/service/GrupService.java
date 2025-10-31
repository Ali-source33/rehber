package com.rehber.service;

import com.rehber.entity.Grup;
import com.rehber.repository.GrupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupService {

    @Autowired
    private GrupRepository grupRepository;

    public Grup grupGetir(int id) {
        Optional<Grup> optionalGrup = grupRepository.findById(id);
        return optionalGrup.orElse(null);
    }
    public List<Grup> tumGruplariGetir() {
        return grupRepository.findAll();
    }

}