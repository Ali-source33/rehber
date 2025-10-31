package com.rehber.controller;

import com.rehber.entity.Contact;
import com.rehber.entity.UploadResponse;
import com.rehber.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final ContactRepository contactRepository;

    public PhotoController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Value("${photo.upload.dir:/home/ubuntu/photos}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("contactId") Integer contactId) { 

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "-" + originalFileName;
            Path filePath = uploadPath.resolve(uniqueFileName);

           
            Files.copy(file.getInputStream(), filePath);

            
            Contact contact = contactRepository.findById(contactId)
                    .orElseThrow(() -> new RuntimeException("Contact not found"));
            contact.setFotoYolu(uniqueFileName);
            contactRepository.save(contact);

            String url = "/photos/" + uniqueFileName;
            return ResponseEntity.ok(new UploadResponse(uniqueFileName, url));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
