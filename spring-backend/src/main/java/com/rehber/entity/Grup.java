package com.rehber.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "\"Gruplar\"")
public class Grup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ad;

    @ManyToMany(mappedBy = "gruplar")
    @JsonBackReference
    private Set<Kisi> kisiler = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grup)) return false;
        Grup grup = (Grup) o;
        return id == grup.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}