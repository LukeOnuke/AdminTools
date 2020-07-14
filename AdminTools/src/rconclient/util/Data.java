/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 *
 * @author lukak
 */
public class Data {
    //All the variables

    static File config = new File("RconClient.properties");
    ArrayList<String> data;
    
    //public data
    /**
     * Java arguments
     */
    public static String[] arguments = new String[1];
    /**
     * Text elements from a text flow
     */
    public static ObservableList<Node> rconTextData = null;

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
     * Gets the IP
     *
     * @return String representation of IP
     */
    public String getHost() {
        return data.get(0);
    }

    /**
     * Gets the port
     *
     * @return port
     */
    public int getPort() {
        return Integer.parseInt(data.get(1));
    }

    /**
     * Gets password
     *
     * @return password
     */
    public byte[] getPassword() {
        return data.get(2).getBytes();
    }

    /**
     * Gets password but as string
     *
     * @return String of password
     */
    public String getPasswordAsString() {
        return data.get(2);
    }

    /**
     * Gets property for markdown coloring (error)
     *
     * @return Colour hex
     */
    public String getMarkdownErrorColour() {
        return data.get(3);
    }

    /**
     * Gets property for markdown coloring (sucsess)
     *
     * @return Colour hex
     */
    public String getMarkdownSuccsesfullReplyColour() {
        return data.get(4);
    }

    /**
     * Gets property for markdown coloring (no responce)
     *
     * @return Colour hex
     */
    public String getMarkdownNoCommandResponceColour() {
        return data.get(5);
    }

    /**
     * Gets remember property
     * @return remember property
     */
    public boolean getRconRemember() {
        return Boolean.parseBoolean(data.get(6));
    }

    /**
     * Gets the refresh rate for mc serv querry 
     * @return refresh rate
     */
    public int getQuerryMcRefreshRate() {
        return Integer.parseInt(data.get(7));
    }

    /**
     * Gets refresh rate for Mojang API
     * @return refresh rate
     */
    public double getQuerryMojangApiRefreshRate() {
        return Double.parseDouble(data.get(8));
    }

    /**
     * Default valiues for the properties
     */
    public static ArrayList<String> defaults = new ArrayList<>(Arrays.asList(new String[]{"localhost", "25575", "password", "#e02b2b", "#9cfc88", "#9cfc88", "false", "10", "100"}));

    /**
     * Writes the properties to disk
     * @param props the properties that need to be written
     */
    public static void write(ArrayList<String> props) {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream(config)) {
            prop.setProperty("rcon.host", props.get(0));
            prop.setProperty("rcon.port", props.get(1));
            prop.setProperty("rcon.password", props.get(2));
            prop.setProperty("markdown.error.colour", props.get(3));
            prop.setProperty("markdown.succsesfullreply.colour", props.get(4));
            prop.setProperty("markdown.nocommandresponce.colour", props.get(5));
            prop.setProperty("rcon.remember", props.get(6));
            prop.setProperty("querry.mc.refreshrate", props.get(7));
            prop.setProperty("querry.api.mojang.refreshrate", props.get(8));
            prop.store(output, "RconClient properties" + System.lineSeparator() + "Created by: LukeOnuke - https://github.com/LukeOnuke");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads the properties from disk
     * @return ArrayList of properites
     */
    public static ArrayList<String> read() {
        ArrayList<String> arl = new ArrayList<>();

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(config)) {
            prop.load(input);
            arl.add(prop.getProperty("rcon.host", defaults.get(0)));
            arl.add(prop.getProperty("rcon.port", defaults.get(1)));
            arl.add(prop.getProperty("rcon.password", defaults.get(2)));
            arl.add(prop.getProperty("markdown.error.colour", defaults.get(3)));
            arl.add(prop.getProperty("markdown.succsesfullreply.colour", defaults.get(4)));
            arl.add(prop.getProperty("markdown.nocommandresponce.colour", defaults.get(5)));
            arl.add(prop.getProperty("rcon.remember", defaults.get(6)));
            arl.add(prop.getProperty("querry.mc.refreshrate", defaults.get(7)));
            arl.add(prop.getProperty("querry.api.mojang.refreshrate", defaults.get(8)));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

        return arl;
    }
}
