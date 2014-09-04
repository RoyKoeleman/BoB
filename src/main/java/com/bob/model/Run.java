package com.bob.model;

import com.bob.controller.DBHandler;
import com.bob.entities.*;
import org.hibernate.Session;

/**
 * Created by Roy on 31-07-14.
 */
public class Run {
    public static void main(String[] args) {
        final Session session = HibernateUtil.getSession();
        session.beginTransaction();

        String klantAchternaam = "Koeleman";
        String klantTussenvoegsel = "";
        String klantVoornaam = "Roy";
        Klant klant = DBHandler.createOrGetKlant(klantAchternaam, klantTussenvoegsel, klantVoornaam);

        Auto auto = new Auto();
        auto.setKenteken("30-RJ-SF");
        auto.setMerk("Suzuki");
        auto.setType("Swift");
        auto.setEigenaar(klant);

        Band band = new Band();
        band.setMerk("Kleber");
        band.setType("Dynaxer HP3");
        band.setMaat("185/60x15 84H");
        band.setWinterband(false);

        String locatie_string = "Zolder";
        Locatie locatie;

        if(DBHandler.createOrGetLocatie(locatie_string, 0) == null) {
            locatie = new Locatie();
            locatie.setNaam("Zolder");
        } else {
            locatie = DBHandler.createOrGetLocatie(locatie_string, 0);
        }

        SetWielen setWielen = new SetWielen();
        setWielen.setKlant(klant);
        setWielen.setAuto(auto);
        setWielen.setBand(band);
        setWielen.setLocatie(locatie);
        setWielen.setVelg_type("LM");
        setWielen.setWielbouten(false);
        setWielen.setWieldoppen(false);
        setWielen.setProfiel_lv(7.5);
        setWielen.setProfiel_rv(7.5);
        setWielen.setProfiel_la(7.5);
        setWielen.setProfiel_ra(7.5);

        Band band2 = new Band();
        band2.setMerk("Michelin");
        band2.setType("WinterContact");
        band2.setMaat("185/65x15 84H");
        band2.setWinterband(true);

        SetWielen setWielen2 = new SetWielen();
        setWielen2.setKlant(klant);
        setWielen2.setAuto(auto);
        setWielen2.setBand(band2);
        setWielen2.setLocatie(locatie);
        setWielen2.setVelg_type("Staal");
        setWielen2.setWielbouten(false);
        setWielen2.setWieldoppen(false);
        setWielen2.setProfiel_lv(7.5);
        setWielen2.setProfiel_rv(7.5);
        setWielen2.setProfiel_la(7.5);
        setWielen2.setProfiel_ra(7.5);

        User user = new User("Roy", DBHandler.cryptWithMD5("password"));

        session.save(user);
        session.saveOrUpdate(klant);
        session.saveOrUpdate(auto);
        session.saveOrUpdate(band);
        session.saveOrUpdate(band2);
        session.saveOrUpdate(locatie);
        session.saveOrUpdate(setWielen);
        session.saveOrUpdate(setWielen2);

        session.getTransaction().commit();
        session.close();
    }
}
