package com.bob.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Roy on 04-08-14.
 */
@Entity
public class Band {
    @Id @GeneratedValue
    private Long id;
    @Column(length = 100)
    private String merk;
    private String type;
    private String maat;
    private boolean winterband;

    public Band() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMaat() {
        return maat;
    }

    public void setMaat(String maat) {
        this.maat = maat;
    }

    public boolean isWinterband() {
        return winterband;
    }

    public void setWinterband(boolean winterband) {
        this.winterband = winterband;
    }
}
