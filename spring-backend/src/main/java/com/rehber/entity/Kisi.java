package com.rehber.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Kisiler")
public class Kisi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ad;
    private String soyad;
    private String telefon;
    private String email;
    private String adres;
    private String fotoYolu;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "KisiGrup",
        joinColumns = @JoinColumn(name = "kisi_id"),
        inverseJoinColumns = @JoinColumn(name = "grup_id")
    )
    
    @JsonManagedReference
    private Set<Grup> gruplar = new HashSet<>();
    
    public String getFotoUrl() {
        if (fotoYolu == null) return null;
        return "/images/" + Paths.get(fotoYolu).getFileName().toString();
    }

}