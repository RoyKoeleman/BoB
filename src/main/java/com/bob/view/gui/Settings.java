package com.bob.view.gui;

import java.io.*;
import java.util.Properties;

/**
 * Created by Roy on 07-08-14.
 */
public class Settings {
    public static void writeToSettings(String key, String value) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("config.properties");

            prop.setProperty(key, value);

            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String getFromSettings(String key) {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            return prop.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
            try {
                writeDefaults();
                return getFromSettings(key);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeDefaults() throws IOException {
        writeToSettings("standaard_type_band", "Zomerbanden");
    }
}
