package com.bob.model;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.bob.controller.DBHandler;
import com.bob.entities.*;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.hibernate.Session;

public class ReadExcel {

    private String inputFile;

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void read() throws IOException  {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        ArrayList<Klant> klanten = new ArrayList<Klant>();

        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            w = Workbook.getWorkbook(inputWorkbook, ws);
            Sheet sheet = w.getSheet("Winterbanden 2014");

            ArrayList<String> columns = new ArrayList<String>();

            for (int i = 0; i < sheet.getRows(); i++) {
                String klantnaam = "";
                String kenteken = "";
                Locatie locatie = null;
                String merkType = "";
                String bandMerk = "";
                String bandMaat = "";
                String velgType = "";
                boolean wieldop = false;
                String wieldopString = "";
                boolean wielbout = false;
                String wielboutString = "";
                double LV = 0, LA = 0, RV = 0, RA = 0;
                String opmerking = "";

                for (int j = 0; j < 14; j++) {
                    Cell cell = sheet.getCell(j, i);

                    if(i == 0) {
                        columns.add(cell.getContents());
                        continue;
                    }


                    if(cell.getContents().equals("")) continue;

                    switch(j) {
                        case 0:     klantnaam = cell.getContents();
                                    break;
                        case 1:     merkType = cell.getContents();
                                    break;
                        case 2:     kenteken = cell.getContents();
                                    break;
                        case 3:     locatie = parseLocatie(cell.getContents());
                                    break;
                        case 4:     bandMerk = cell.getContents();
                                    break;
                        case 5:     bandMaat = cell.getContents();
                                    break;
                        case 6:     velgType = cell.getContents();
                                    break;
                        case 7:     wieldop = (cell.getContents().equals("Ja"));
                                    break;
                        case 8:     wielbout = (cell.getContents().equals("Ja"));
                                    break;
                        case 9:     LV = parseProfiel(cell.getContents());
                                    break;
                        case 10:    LA = parseProfiel(cell.getContents());
                                    break;
                        case 11:    RV = parseProfiel(cell.getContents());
                                    break;
                        case 12:    RA = parseProfiel(cell.getContents());
                                    break;
                        case 13:    opmerking = cell.getContents();
                                    break;
                    }

                    System.out.println(columns.get(cell.getColumn()) + ": " + cell.getContents());
                }

                if(klantnaam.length() == 0) continue;
                if(bandMerk.length() == 0) continue;

                // Haal de klant, auto en band op
                Klant klant = parseKlant(klantnaam);
                Auto auto = parseAuto(kenteken, merkType, klant);
                Band band = parseBand(bandMerk, bandMaat);

                // Maak de set aan
                SetWielen setWielen = new SetWielen();
                setWielen.setKlant(klant);
                setWielen.setAuto(auto);
                setWielen.setBand(band);
                if(locatie != null) setWielen.setLocatie(locatie);
                setWielen.setVelg_type(velgType);
                setWielen.setWieldoppen(wieldop);
                setWielen.setWielbouten(wielbout);
                setWielen.setProfiel_lv(LV);
                setWielen.setProfiel_ra(RA);
                setWielen.setProfiel_la(LA);
                setWielen.setProfiel_rv(RV);
                setWielen.setOpmerking(opmerking);

                /* Hier commenten voor winter
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    setWielen.setDatum(formatter.parse("2013/10/01"));
                } catch(ParseException e) {
                    e.printStackTrace();
                }
                */

                // Maak users aan
                User user = new User("Roy", DBHandler.cryptWithMD5("password"));

                DBHandler.setsOpgehaald(auto);

                Session session = HibernateUtil.getSession();
                session.beginTransaction();

                session.saveOrUpdate(klant);
                session.saveOrUpdate(auto);
                session.saveOrUpdate(band);
                session.saveOrUpdate(setWielen);
                session.saveOrUpdate(user);
                if(locatie != null) session.saveOrUpdate(locatie);

                session.getTransaction().commit();
                session.close();



            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private Klant parseKlant(String klantnaam) {
        String[] klantnaamSplit = new String[3];
        int length = 0;
        Klant klant = null;

        if(klantnaam.contains(",")) {
            klantnaamSplit = klantnaam.split(",");
            length = klantnaamSplit.length;
        } else {
            klant = DBHandler.createOrGetKlant(klantnaam, null, null);
        }

        switch(length) {
            case 3:
                klant = DBHandler.createOrGetKlant(klantnaamSplit[0], klantnaamSplit[1], klantnaamSplit[2].substring(1));
                break;
            case 2:
                klant = DBHandler.createOrGetKlant(klantnaamSplit[0], klantnaamSplit[1].substring(1), null);
                break;
            case 1:
                klant = DBHandler.createOrGetKlant(klantnaamSplit[0], null, null);
                break;
        }

        return klant;
    }

    private Auto parseAuto(String kenteken, String merkType, Klant eigenaar) {
        String[] merkTypeSplit = merkType.split(" ");
        String merk = null;
        String type = null;

        switch(merkTypeSplit.length) {
            case 2 : merk = merkTypeSplit[0];
                     type = merkTypeSplit[1];
                     break;
            case 1 : type = merkType;
                     break;
        }

        Auto auto = DBHandler.createOrGetAuto(kenteken, merk, type, eigenaar);

        return auto;
    }

    private Band parseBand(String bandMerk, String bandMaat) {
        // Hier laatste boolean veranderen voor winter/zomer
        Band band = DBHandler.createOrGetBand(bandMerk, null, bandMaat, true);

        return band;
    }

    private double parseProfiel(String profiel) {
        double profielDouble = 0;

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(profiel);
            profielDouble = number.doubleValue();
        } catch(ParseException e) {}

        return profielDouble;
    }

    private Locatie parseLocatie(String locatie) {
        String[] locatieSplit = locatie.split(" - ");
        int nummer = 0;

        if(locatieSplit.length > 1) {
            locatie = locatieSplit[0];
            nummer = Integer.parseInt(locatieSplit[1]);
        }

        return DBHandler.createOrGetLocatie(locatie, nummer);
    }

    public static void main(String[] args) throws IOException {
        ReadExcel test = new ReadExcel();
        test.setInputFile("src/main/resources/Volledige bandenlijst.xls");
        test.read();
    }

}
