/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import app.admintools.AdminTools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import app.admintools.util.WindowLoader;
import app.admintools.querry.api.ApiQuerry;
import app.admintools.querry.mc.MinecraftPing;
import app.admintools.querry.mc.MinecraftPingOptions;
import app.admintools.querry.mc.MinecraftPingReply;
import app.admintools.querry.mc.QuerryUtils;
import app.admintools.threadmanager.ThreadManager;
import app.admintools.threadmanager.ThreadType;
import app.admintools.util.AtLogger;
import app.admintools.util.Data;

/**
 * FXML Controller class
 *
 * @author lukak
 */
public class StatusWindowController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    /*
    Status dots
    Naming scheme is:
    sd<number>
    s-status
    d-dot
    statusDot<number> but shortened
     */

    //For mc serv status
    @FXML
    private ImageView sFavicon;
    @FXML
    private Label sVer;
    @FXML
    private Label sPlayers;
    @FXML
    private ListView sOnlinePlayers;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tickMcRefresh();
    }

    @FXML
    private void loadRcon() {
        WindowLoader.loadRcon(rootPane);
    }

    @FXML
    private void loadSettings() {
        WindowLoader.loadSettings(rootPane);
    }

    @FXML
    private void loadHome() {
        WindowLoader.loadHome(rootPane);
    }

    @FXML
    private void mcStatusRefresh() {
        Thread mcsr = new Thread(() -> {
            Data d = Data.getInstance(); //get data class instance
            MinecraftPingReply data; //get querry
            try {
                data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(d.getSelectedCredentials().getIP()).setPort(d.getSelectedCredentials().getPingPort())); //create querry

                //Set all the text and images 
                Platform.runLater(() -> {
                    sVer.setText("Version: " + data.getVersion().getName());
                    sPlayers.setText("Players: " + data.getPlayers().getOnline() + " / " + data.getPlayers().getMax());
                    if (data.getFavicon() != null) {
                        sFavicon.setImage(QuerryUtils.convertToImage(data.getFavicon()));
                    } else {
                        sFavicon.setImage(new Image(AdminTools.class.getResourceAsStream("/img/unknown-server.png")));
                    }
                });

                //Set player list
                Platform.runLater(() -> {
                    if (data.getPlayers().getSample() != null) {
                        try {
                            if (sOnlinePlayers.getItems() != null) {
                                sOnlinePlayers.getItems().clear();
                            }
                            for (int i = 0; i < data.getPlayers().getSample().size(); i++) {
                                sOnlinePlayers.getItems().add(data.getPlayers().getSample().get(i).getName());
                            }

                        } catch (Exception e) {
                            AtLogger.logger.warning(AtLogger.formatException(e));
                        }
                    }
                });

            } catch (IOException ex) {
                AtLogger.logger.warning(AtLogger.formatException(ex));
            }

        });
        mcsr.start();
    }

    private void tickMcRefresh() {
        Thread tmrt = new Thread(() -> {
            Platform.runLater(() -> {
                mcStatusRefresh();
            });
            Data d = Data.getInstance();
            try {
                int timeSleep = d.getQuerryMcRefreshRate() * 1000;
                for (int i = 0; i < timeSleep / 500; i++) {
                    if (!Data.isOnStatusWindow) {
                        return;
                    }
                    Thread.sleep(500);
                }
            } catch (InterruptedException | IOException ex) {
                AtLogger.logger.warning(AtLogger.formatException(ex));
            }
            tickMcRefresh();

        });
        tmrt.setName("McTick");
        ThreadManager.startThread(tmrt, ThreadType.ASYNCJOB);
    }
}
