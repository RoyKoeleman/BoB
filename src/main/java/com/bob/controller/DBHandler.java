package com.bob.controller;

import com.bob.entities.*;
import com.bob.model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Roy on 05-08-14.
 */
public class DBHandler {
    private static Session session = HibernateUtil.getSession();

    public static Auto getAutoByKenteken(String kenteken) {
        Query query = session.createQuery("FROM Auto WHERE kenteken = :kenteken");
        query.setParameter("kenteken", kenteken);

        if(query.list().size() > 0) return (Auto) query.list().get(0);

        return null;
    }

    public static Auto createOrGetAuto(String kenteken, String merk, String type, Klant eigenaar) {
        Auto auto;

        Query query = session.createQuery("FROM Auto WHERE kenteken = :kenteken");
        query.setParameter("kenteken", kenteken);

        if(query.list().size() > 0) {
            auto = (Auto) query.list().get(0);
        } else {
            auto = new Auto();
            auto.setKenteken(kenteken);
            auto.setMerk(merk);
            auto.setType(type);
            auto.setEigenaar(eigenaar);
        }

        return auto;
    }

    public static void setsOpgehaald(Auto auto) {
        Query query = session.createQuery("FROM SetWielen WHERE auto = :auto");
        query.setParameter("auto", auto);

        if(query.list().size() == 0) return;

        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        for(SetWielen set : (ArrayList<SetWielen>) query.list()) {
            set.setOpgehaald(true);
            session.saveOrUpdate(set);
        }

        session.getTransaction().commit();
        session.close();
    }

    public static SetWielen getSetWielenByAuto(Auto auto, Boolean winterband) {
        Criteria c = session.createCriteria(SetWielen.class);
        c.add(Restrictions.eq("auto", auto));
        c.addOrder(Order.desc("datum"));

        if(c.list().size() > 0) {
            for(SetWielen wielen : (List<SetWielen>) c.list()) {
                if(wielen.getBand().isWinterband() == winterband) return wielen;
            }
        }

        return null;
    }

    public static Klant createOrGetKlant(String achternaam, String tussenvoegsel, String voornaam) {
        Klant klant;

        if(achternaam == null) achternaam = "";
        if(tussenvoegsel == null) tussenvoegsel = "";
        if(voornaam == null) voornaam = "";

        Criteria c = session.createCriteria(Klant.class);
        c.add(achternaam.equals("") ? Restrictions.isNull("achternaam") : Restrictions.eq("achternaam", achternaam));
        c.add(tussenvoegsel.equals("") ? Restrictions.isNull("tussenvoegsel") : Restrictions.eq("tussenvoegsel", tussenvoegsel));
        c.add(voornaam.equals("") ? Restrictions.isNull("voornaam") : Restrictions.eq("voornaam", voornaam));
        System.out.println(c.list().size());
        if(c.list().size() > 0) {
            klant = (Klant) c.list().get(0);
            return klant;
        } else {
            klant = new Klant();
            klant.setAchternaam(achternaam);
            if(!tussenvoegsel.equals("")) klant.setTussenvoegsel(tussenvoegsel);
            if(!voornaam.equals(""))  klant.setVoornaam(voornaam);
        }

        return klant;
    }

    public static Locatie createOrGetLocatie(String naam, int nummer) {
        Criteria c = session.createCriteria(Locatie.class);
        c.add(Restrictions.eq("naam", naam));
        c.add(Restrictions.eq("nummer", nummer));

        if(c.list().size() > 0) return (Locatie) c.list().get(0);

        return new Locatie(naam, nummer);
    }

    public static Band createOrGetBand(String merk, String type, String maat, boolean winterband) {
        Band band;
        if(type == null) type = "";

        Criteria c = session.createCriteria(Band.class);
        c.add(merk.equals("") ? Restrictions.isNull("merk") : Restrictions.eq("merk", merk));
        c.add(type.equals("") ? Restrictions.isNull("type") : Restrictions.eq("type", type));
        c.add(maat.equals("") ? Restrictions.isNull("maat") : Restrictions.eq("maat", maat));
        c.add(Restrictions.eq("winterband", winterband));

        if(c.list().size() > 0) {
            band = (Band) c.list().get(0);
        } else {
            band = new Band();
            band.setMerk(merk);
            if(!type.equals("")) band.setType(type);
            band.setMaat(maat);
            band.setWinterband(winterband);
        }

        return band;
    }

    public static boolean saveSetWielen(Klant klant, Auto auto, Band band, SetWielen setWielen) {
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();

            session.saveOrUpdate(klant);
            session.saveOrUpdate(auto);
            session.saveOrUpdate(band);
            session.saveOrUpdate(setWielen);

            session.getTransaction().commit();
            session.close();

            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<SetWielen> getWielen() {
        Criteria c = session.createCriteria(SetWielen.class);

        if(c.list().size() == 0) return null;

        return (List<SetWielen>) c.list();
    }

    public static String cryptWithMD5(String pass){
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;


    }
}
