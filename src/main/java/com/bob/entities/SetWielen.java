package com.bob.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Roy on 04-08-14.
 */
@Entity
public class SetWielen {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Klant klant;
    @ManyToOne
    private Auto auto;
    @ManyToOne
    private Band band;
    @ManyToOne
    private Locatie locatie;
    private String velg_type;
    private boolean wieldoppen;
    private boolean wielbouten;
    private double profiel_lv;
    private double profiel_rv;
    private double profiel_la;
    private double profiel_ra;
    private Date datum = new Date();
    private String opmerking;
    private boolean opgehaald = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Klant getKlant() {
        return klant;
    }

    public void setKlant(Klant klant) {
        this.klant = klant;
    }

    public Auto getAuto() {
        return auto;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    public String getVelg_type() {
        return velg_type;
    }

    public void setVelg_type(String velg_type) {
        this.velg_type = velg_type;
    }

    public boolean isWieldoppen() {
        return wieldoppen;
    }

    public void setWieldoppen(boolean wieldoppen) {
        this.wieldoppen = wieldoppen;
    }

    public boolean isWielbouten() {
        return wielbouten;
    }

    public void setWielbouten(boolean wielbouten) {
        this.wielbouten = wielbouten;
    }

    public double getProfiel_lv() {
        return profiel_lv;
    }

    public void setProfiel_lv(double profiel_lv) {
        this.profiel_lv = profiel_lv;
    }

    public double getProfiel_rv() {
        return profiel_rv;
    }

    public void setProfiel_rv(double profiel_rv) {
        this.profiel_rv = profiel_rv;
    }

    public double getProfiel_la() {
        return profiel_la;
    }

    public void setProfiel_la(double profiel_la) {
        this.profiel_la = profiel_la;
    }

    public double getProfiel_ra() {
        return profiel_ra;
    }

    public void setProfiel_ra(double profiel_ra) {
        this.profiel_ra = profiel_ra;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getOpmerking() {
        return opmerking;
    }

    public void setOpmerking(String opmerking) {
        this.opmerking = opmerking;
    }

    public boolean isOpgehaald() {
        return opgehaald;
    }

    public void setOpgehaald(boolean opgehaald) {
        this.opgehaald = opgehaald;
    }
}

