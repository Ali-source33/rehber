package com.rehber.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Kisiler")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("ad")
    private String ad;

    @JsonProperty("soyad")
    private String soyad;

    @JsonProperty("telefon")
    private String telefon;

    @JsonProperty("email")
    private String email;

    @JsonProperty("adres")
    private String adres;

    @JsonProperty("fotoYolu")
    private String fotoYolu;
    
    @Column(name = "group_id")
    @JsonProperty("group_id")
    private int grupId;  
}
