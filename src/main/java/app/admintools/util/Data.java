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
import java.util.HashMap;
import java.util.Properties;
import app.admintools.security.credentials.Credentials;

/**
 *
 * @author lukak
 */
public class Data {
    //All the variables

    static File config = new File("admintools.properties");

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
            try {
                write(DataType.RCON_REMEMBER, defaults.get(DataType.RCON_REMEMBER));
                write(DataType.QUERRY_API_REFRESHRATE, defaults.get(DataType.QUERRY_API_REFRESHRATE));
                write(DataType.QUERRY_MC_REFRESHRATE, defaults.get(DataType.QUERRY_MC_REFRESHRATE));
                write(DataType.MESSAGE_SEND_ON_LOGON, defaults.get(DataType.MESSAGE_SEND_ON_LOGON));
                write(DataType.MESSAGE_OVERWRITE_SAY, defaults.get(DataType.MESSAGE_OVERWRITE_SAY));
                write(DataType.USERNAME, defaults.get(DataType.USERNAME));
                write(DataType.THEME, defaults.get(DataType.THEME));
            } catch (IOException e) {
                AtLogger.logger.severe(AtLogger.formatException(e));
            }
        }
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
    public boolean getRconRemember() throws IOException {
        return Boolean.parseBoolean(read(DataType.RCON_REMEMBER));
    }

    /**
     * Gets the refresh rate for mc serv querry
     *
     * @return refresh rate
     */
    public int getQuerryMcRefreshRate() throws IOException {
        return Integer.parseInt(read(DataType.QUERRY_MC_REFRESHRATE));
    }

    /**
     * Gets refresh rate for Mojang API
     *
     * @return refresh rate
     */
    public double getQuerryMojangApiRefreshRate() throws IOException {
        return Double.parseDouble(read(DataType.QUERRY_API_REFRESHRATE));
    }

    public boolean getMessageNotify() throws IOException {
        return Boolean.parseBoolean(read(DataType.MESSAGE_SEND_ON_LOGON));
    }

    public boolean getMessageOverwriteSay() throws IOException {
        return Boolean.parseBoolean(read(DataType.MESSAGE_OVERWRITE_SAY));
    }

    public String getMessageUsername() throws IOException {
        return read(DataType.USERNAME);
    }

    public String getSelectedTheme() throws IOException {
        return read(DataType.THEME);
    }
    /**
     * Default valiues for the properties
     * "false", "10", "100", "false", "false", "username", "Default"
     */
    public static HashMap<String, String> defaults = new HashMap<>(){{
        put(DataType.RCON_REMEMBER, "false");
        put(DataType.QUERRY_MC_REFRESHRATE, "10");
        put(DataType.QUERRY_API_REFRESHRATE, "100");
        put(DataType.MESSAGE_SEND_ON_LOGON, "false");
        put(DataType.MESSAGE_OVERWRITE_SAY, "false");
        put(DataType.USERNAME, "username");
        put(DataType.THEME, "default");
    }};

    /**
     * Writes the properties to disk
     *
     * @param key Key
     * @param value The valiue to write
     */
    public static void write(String key, String value) throws IOException {
        Properties prop = new Properties();
        OutputStream output = new FileOutputStream(config);
        prop.setProperty(key, value);
        prop.store(output, "AdminTools properties" + System.lineSeparator() + "Created by: LukeOnuke - https://github.com/LukeOnuke");
    }

    /**
     * Reads the properties from disk
     *
     * @return ArrayList of properites
     */
    public static String read(String key) throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(config);
        prop.load(input);
        return prop.getProperty(key, defaults.get(key));
    }
}
