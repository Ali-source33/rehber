package com.rehber.service;

import com.rehber.entity.Kisi;
import com.rehber.repository.KisiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KisiService {

    @Autowired
    private KisiRepository kisiRepository;
    
	public Kisi kisiGetir(int id) {
		  Optional<Kisi> optionalKisi = kisiRepository.findById(id);
		  return optionalKisi.orElse(null);    
	}
}
