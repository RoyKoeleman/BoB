package com.bob.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Roy on 04-08-14.
 */
@Entity
public class Auto {
    @Id @Column(length = 8)
    private String kenteken;
    private String merk;
    private String type;
    @ManyToOne
    private Klant eigenaar;

    public String getKenteken() {
        return kenteken;
    }

    public void setKenteken(String kenteken) {
        this.kenteken = kenteken;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Klant getEigenaar() {
        return eigenaar;
    }

    public void setEigenaar(Klant eigenaar) {
        this.eigenaar = eigenaar;
    }
}
