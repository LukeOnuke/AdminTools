/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import app.admintools.gui.credentials.credwizard.CredWizard;
import app.admintools.security.credentials.Credentials;
import app.admintools.security.credentials.CredentialsIO;
import app.admintools.util.Data;
import app.admintools.util.Utill;
import app.admintools.util.WindowLoader;
import simplefxdialog.Dialog;
import simplefxdialog.img.DialogImage;

/**
 * FXML Controller class
 *
 * @author lukak
 */
public class HomeWindowController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private FlowPane credCards;
    @FXML
    private ToggleButton rconButton;
    @FXML
    private ToggleButton statusButton;
    @FXML
    private ToggleButton settingsButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refresh();
    }

    //Refreshes the credcards
    private void refresh() {
        credCards.getChildren().clear();
        try {
            if (new File(CredentialsIO.PATH).exists()) {
                if (CredentialsIO.readCredentials() != null) {
                    for (Credentials credentials : CredentialsIO.readCredentials()) {
                        credCards.getChildren().add(credentials.getCredCard());
                    }
                }
            } else {
                new File(CredentialsIO.PATH).createNewFile();
            }
        } catch (IOException ioe) {
            Dialog.okDialog(DialogImage.error, "FATAL ERROR", ioe.getMessage());
            Utill.exit(1);
        }
    }

    @FXML
    private void addNew() {
        Credentials cred = CredWizard.showCredWizard();
        if (cred != null) {
            try {
                ArrayList<Credentials> credList = CredentialsIO.readCredentials();
                if (credList == null) {
                    credList = new ArrayList<Credentials>();
                }
                credList.add(cred);
                CredentialsIO.writeCredentials(credList);
                refresh();
            } catch (IOException ex) {
                Logger.getLogger(HomeWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void loadRcon() {
        if (Data.getInstance().getSelectedCredentials() != null) {
            WindowLoader.loadRcon(rootPane);
        } else {
            rconButton.setSelected(false);
        }
    }

    @FXML
    private void loadSettings() {
        if (Data.getInstance().getSelectedCredentials() != null) {
            WindowLoader.loadSettings(rootPane);
        }
        else{
            settingsButton.setSelected(false);
        }
    }

    @FXML
    private void loadStatus() {
        if (Data.getInstance().getSelectedCredentials() != null) {
            WindowLoader.loadStatus(rootPane);
        }
        else{
            statusButton.setSelected(false);
        }
    }
}
