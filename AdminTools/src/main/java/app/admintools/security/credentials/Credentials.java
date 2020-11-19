/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.security.credentials;

import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import net.kronos.rkon.core.ex.AuthenticationException;
import app.admintools.gui.credentials.credcard.CredCard;
import app.admintools.util.CustomRcon;

/**
 *
 * @author lukak
 */
public class Credentials {

    private String ip;
    private int port;
    private String password;
    private String name;
    private int pingPort;

    private Credentials() {
    }

    /**
     * Create a new <code>Credentials</code> instance
     *
     * @param ip Ip of server
     * @param port Port of server
     * @param password Password of server
     * @param name Name of server
     * @param pingPort Port to ping
     */
    public Credentials(String ip, int port, String password, String name, int pingPort) {
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.name = name;
        this.pingPort = pingPort;
    }

    /**
     * Get password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the port.
     *
     * @return Port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get IP.
     *
     * @return IP
     */
    public String getIP() {
        return ip;
    }

    /**
     * Get the name of the credential. Used in title of CredCard.
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ping port.
     *
     * @return Port
     */
    public int getPingPort() {
        return pingPort;
    }

    /**
     * Create a <b>CredCard</b> with the credentials
     *
     * @return CredCard AnchorPane
     */
    public AnchorPane getCredCard() {
        return CredCard.createCredCard(name, ip + ":" + port, this);
    }

    /**
     * Creates a <b>CustomRcon</b> instance and refreshes the singleton
     *
     * @return CustomRcon instance
     * @throws IOException
     * @throws AuthenticationException
     */
    public CustomRcon createRcon() throws IOException, AuthenticationException {
        return CustomRcon.getInstance(ip, port, password.getBytes());
    }

    @Override
    public boolean equals(Object obj) {
        Credentials credcheck = (Credentials) obj;
        return ip.equals(credcheck.ip) && port == credcheck.port && password.equals(credcheck.password) && name.equals(credcheck.name);
    }
}
