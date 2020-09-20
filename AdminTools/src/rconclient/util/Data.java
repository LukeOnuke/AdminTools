/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import rconclient.security.Mozaic;

/**
 *
 * @author lukak
 */
public class Data {
    //All the variables

    static File config = new File("rconclient.properties");
    static File cred = new File("prop.encdat");
    private ArrayList<String> data;
    private ArrayList<String> credentials;

    //public data
    /**
     * Java arguments
     */
    public static String[] arguments = new String[1];
    /**
     * Text elements from a text flow
     */
    public static ArrayList<String> rconTextData = null;

    public static boolean startingUp = true;
    
    /**
     * Is the view on the status window
     * <i>Used in stopping and starting tick threads in the status window</i>
     */
    public static boolean isOnStatusWindow = false;

    //Singleton
    private static Data instance = null;

    private Data() {
        if (!config.exists()) {
            write(defaults);
        }
        data = read();
        if (!cred.exists()) {
            try {
                writeCredentials(credentialsDefaults);
            } catch (IOException ex) {

            }
        }
        credentials = readCredentials();
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
     * Gets the IP
     *
     * @return String representation of IP
     */
    public String getHost() {
        return credentials.get(0);
    }

    /**
     * Gets the port
     *
     * @return port
     */
    public int getPort() {
        return Integer.parseInt(credentials.get(1));
    }

    /**
     * Gets password
     *
     * @return password
     */
    public byte[] getPassword() {
        return credentials.get(2).getBytes();
    }

    /**
     * Gets password but as string
     *
     * @return String of password
     */
    public String getPasswordAsString() {
        return credentials.get(2);
    }

    /**
     * Gets property for markdown coloring (error)
     *
     * @return Colour hex
     */
    public String getMarkdownErrorColour() {
        return data.get(0);
    }

    /**
     * Gets property for markdown coloring (sucsess)
     *
     * @return Colour hex
     */
    public String getMarkdownSuccsesfullReplyColour() {
        return data.get(1);
    }

    /**
     * Gets property for markdown coloring (no responce)
     *
     * @return Colour hex
     */
    public String getMarkdownNoCommandResponceColour() {
        return data.get(2);
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
    /**
     * Default valiues for the properties
     */
    public static ArrayList<String> defaults = new ArrayList<>(Arrays.asList(new String[]{"false", "10", "100", "false", "false", "username"}));

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
            prop.store(output, "RconClient properties" + System.lineSeparator() + "Created by: LukeOnuke - https://github.com/LukeOnuke");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

        return arl;
    }

    public static ArrayList<String> credentialsDefaults = new ArrayList<>(Arrays.asList(new String[]{"localhost", "25575", "password"}));

    public static ArrayList<String> readCredentials() {
        ArrayList<String> cred = new ArrayList<>();

        Properties prop = new Properties();
        try (InputStream input = new ByteArrayInputStream(Mozaic.read().getBytes());) {
            prop.load(input);
            cred.add(prop.getProperty("rcon.host", credentialsDefaults.get(0)));
            cred.add(prop.getProperty("rcon.port", credentialsDefaults.get(1)));
            cred.add(prop.getProperty("rcon.password", credentialsDefaults.get(2)));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cred;
    }

    public static void writeCredentials(ArrayList<String> credentials) throws IOException {
        Properties prop = new Properties();
        StringWriter output = new StringWriter();
        prop.setProperty("rcon.host", credentials.get(0));
        prop.setProperty("rcon.port", credentials.get(1));
        prop.setProperty("rcon.password", credentials.get(2));
        prop.store(output, "RconClient credentials" + System.lineSeparator() + "Created by: LukeOnuke - https://github.com/LukeOnuke");

        try {
            Mozaic.write(output.getBuffer().toString());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
