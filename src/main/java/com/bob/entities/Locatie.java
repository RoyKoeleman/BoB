package com.bob.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Roy on 04-08-14.
 */
@Entity
public class Locatie {
    @Id @GeneratedValue
    private Long id;
    private String naam;
    private int nummer;

    public Locatie() {}

    public Locatie(String naam, int nummer) {
        this.naam = naam;
        this.nummer = nummer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }
}
