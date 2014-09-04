package com.bob.controller;

import java.util.regex.Pattern;

/**
 * Created by Roy on 07-08-14.
 */
public class Helper {
    private static int getSidecodeLicenseplate(String licenseplate){
        licenseplate = licenseplate.replace("-", "").toUpperCase();

        Pattern[] arrSC = new Pattern[10];
        Pattern scUitz;

        arrSC[0] = Pattern.compile("^[a-zA-Z]{2}[\\d]{2}[\\d]{2}$"); // 1 XX-99-99
        arrSC[1] = Pattern.compile("^[\\d]{2}[\\d]{2}[a-zA-Z]{2}$"); // 2 99-99-XX
        arrSC[2] = Pattern.compile("^[\\d]{2}[a-zA-Z]{2}[\\d]{2}$"); // 3 99-XX-99
        arrSC[3] = Pattern.compile("^[a-zA-Z]{2}[\\d]{2}[a-zA-Z]{2}$"); // 4 XX-99-XX
        arrSC[4] = Pattern.compile("^[a-zA-Z]{2}[a-zA-Z]{2}[\\d]{2}$"); // 5 XX-XX-99
        arrSC[5] = Pattern.compile("^[\\d]{2}[a-zA-Z]{2}[a-zA-Z]{2}$"); // 6 99-XX-XX
        arrSC[6] = Pattern.compile("^[\\d]{2}[a-zA-Z]{3}[\\d]{1}$"); // 7 99-XXX-9
        arrSC[7] = Pattern.compile("^[\\d]{1}[a-zA-Z]{3}[\\d]{2}$"); // 8 9-XXX-99
        arrSC[8] = Pattern.compile("^[a-zA-Z]{2}[\\d]{3}[a-zA-Z]{1}$"); // 9 XX-999-X
        arrSC[9] = Pattern.compile("^[a-zA-Z]{1}[\\d]{3}[a-zA-Z]{2}$"); // 10 X-999-XX

        //except licenseplates for diplomats
        scUitz = Pattern.compile("^CD[ABFJNST][0-9]{1,3}$"); //for example: CDB1 of CDJ45

        for(int i=0;i<arrSC.length;i++){
            if (arrSC[i].matcher(licenseplate).matches()) {
                return i+1;
            }
        }
        if (scUitz.matcher(licenseplate).matches()) {
            return 0;
        }
        return -1;
    }

    private static String FormatLicenseplate(String licenseplate, int sidecode) {

        licenseplate = licenseplate.replace("-", "").toUpperCase();

        if (sidecode <= 6) {
            return licenseplate.substring(0, 2) + '-' + licenseplate.substring(2, 4) + '-' + licenseplate.substring(4, 6);
        }
        if (sidecode == 7 || sidecode == 9) {
            return licenseplate.substring(0, 2) + '-' + licenseplate.substring(2, 5) + '-' + licenseplate.substring(5, 6);
        }
        if (sidecode == 8 || sidecode == 10) {
            return licenseplate.substring(0, 1) + '-' + licenseplate.substring(1, 4) + '-' + licenseplate.substring(4, 6);
        }
        return licenseplate;
    }

    public static String formatKenteken(String kenteken) {
        int sidecode = getSidecodeLicenseplate(kenteken);
        return FormatLicenseplate(kenteken, sidecode);
    }
}
