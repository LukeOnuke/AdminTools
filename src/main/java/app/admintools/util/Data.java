/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.module.ModuleDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import app.admintools.security.credentials.Credentials;

/**
 *
 * @author lukak
 */
public class Data {
    //All the variables

    static File config = new File("admintools.properties");
    private ArrayList<String> data;

    //public data
    /**
     * Java arguments
     */
    public static String[] arguments = new String[1];
    /**
     * Text and contents from the webview
     */
    public static ArrayList<String> rconTextData = null;

    //Used in rconWindow to get if its starting up
    //In versions >5.0.0 it is used to indicate if the selected credentials have changed
    public static boolean startingUp = true;

    /**
     * Is the view on the status window
     * <i>Used in stopping and starting tick threads in the status window</i>
     */
    public static boolean isOnStatusWindow = false;
    
    /**
     * Selected credentials
     */
    private static Credentials credentials = null;

    private static final String VERSION = "7.0.0.0.0";

    //Singleton
    private static Data instance = null;

    private Data() {
        if (!config.exists()) {
            write(defaults);
        }
        data = read();
    }

    /**
     * Gets the instance of DATA
     *
     * @return instance of DATA
     */
    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    /**
     * Refreshes Data instance
     *
     * @return Data instanceaa
     */
    public static Data refresh() {
        instance = new Data();
        return instance;
    }
    
    /**
     * Gets the stored selected credentials
     * @return <b>Credentials instance</b>
     */
    public Credentials getSelectedCredentials(){
        return credentials;
    }
    
    /**
     *
     * @param creds
     */
    public static void setSelectedCredentials(Credentials creds){
        credentials = creds;
        startingUp = true; //Sets starting up to true to indicate to the rconWindow that stuff has changed
    }
            

    /**
     * Gets remember property
     *
     * @return remember property
     */
    public boolean getRconRemember() {
        return Boolean.parseBoolean(data.get(0));
    }

    /**
     * Gets the refresh rate for mc serv querry
     *
     * @return refresh rate
     */
    public int getQuerryMcRefreshRate() {
        return Integer.parseInt(data.get(1));
    }

    /**
     * Gets refresh rate for Mojang API
     *
     * @return refresh rate
     */
    public double getQuerryMojangApiRefreshRate() {
        return Double.parseDouble(data.get(2));
    }

    public boolean getMessageNotify() {
        return Boolean.parseBoolean(data.get(3));
    }

    public boolean getMessageOverwriteSay() {
        return Boolean.parseBoolean(data.get(4));
    }

    public String getMessageUsername() {
        return data.get(5);
    }

    public String getSelectedTheme() {
        return data.get(6);
    }
    /**
     * Default valiues for the properties
     */
    public static ArrayList<String> defaults = new ArrayList<>(Arrays.asList(new String[]{"false", "10", "100", "false", "false", "username", "Default"}));

    /**
     * Writes the properties to disk
     *
     * @param props the properties that need to be written
     */
    public static void write(ArrayList<String> props) {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream(config)) {
            prop.setProperty("rcon.remember", props.get(0));
            prop.setProperty("querry.mc.refreshrate", props.get(1));
            prop.setProperty("querry.api.mojang.refreshrate", props.get(2));
            prop.setProperty("message.send.on.login", props.get(3));
            prop.setProperty("message.overwrite.say", props.get(4));
            prop.setProperty("message.username", props.get(5));
            prop.setProperty("theme.selected", props.get(6));
            prop.store(output, "AdminTools properties" + System.lineSeparator() + "Created by: LukeOnuke - https://github.com/LukeOnuke");

        } catch (FileNotFoundException ex) {
            AtLogger.logger.warning(AtLogger.formatException(ex));
        } catch (IOException ex) {
            AtLogger.logger.warning(AtLogger.formatException(ex));
        }
    }

    /**
     * Reads the properties from disk
     *
     * @return ArrayList of properites
     */
    public static ArrayList<String> read() {
        ArrayList<String> arl = new ArrayList<>();

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(config)) {
            prop.load(input);
            arl.add(prop.getProperty("rcon.remember", defaults.get(0)));
            arl.add(prop.getProperty("querry.mc.refreshrate", defaults.get(1)));
            arl.add(prop.getProperty("querry.api.mojang.refreshrate", defaults.get(2)));
            arl.add(prop.getProperty("message.send.on.login", defaults.get(3)));
            arl.add(prop.getProperty("message.overwrite.say", defaults.get(4)));
            arl.add(prop.getProperty("message.username", defaults.get(5)));
            arl.add(prop.getProperty("theme.selected", defaults.get(6)));
        } catch (IOException ex) {
            AtLogger.logger.warning(AtLogger.formatException(ex));
        }

        return arl;
    }
}
