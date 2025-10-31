package com.rehber.controller;

import com.rehber.entity.Contact;
import com.rehber.entity.Kisi;
import com.rehber.repository.ContactRepository;
import com.rehber.repository.KisiRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactRepository contactRepository;
    private final KisiRepository kisiRepository;

    public ContactController(ContactRepository contactRepository, KisiRepository kisiRepository) {
        this.contactRepository = contactRepository;
        this.kisiRepository = kisiRepository;
    }

    // EKLEME
    @PostMapping("/add")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        Contact saved = contactRepository.save(contact);
        return ResponseEntity.ok(saved);
    }

    
    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteKisi(@PathVariable Integer id) {
        Kisi kisi = kisiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kişi bulunamadı"));

        kisi.getGruplar().forEach(grup -> grup.getKisiler().remove(kisi));
        kisi.getGruplar().clear();

        kisiRepository.save(kisi);
        kisiRepository.delete(kisi);

        return ResponseEntity.ok("Kişi başarıyla silindi");
    }



    // GÜNCELLEME
    @PutMapping("/update/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Integer id, @RequestBody Contact updated) {
        return contactRepository.findById(id).map(existing -> {
            existing.setAd(updated.getAd());
            existing.setSoyad(updated.getSoyad());
            existing.setTelefon(updated.getTelefon());
            existing.setEmail(updated.getEmail());
            existing.setAdres(updated.getAdres());
            if (updated.getFotoYolu() != null) {
                existing.setFotoYolu(updated.getFotoYolu());
            }
            existing.setGrupId(updated.getGrupId());
            Contact saved = contactRepository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.status(404).build());
    }

    // TÜM LİSTE
    @GetMapping("/all")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactRepository.findAll());
    }
}
