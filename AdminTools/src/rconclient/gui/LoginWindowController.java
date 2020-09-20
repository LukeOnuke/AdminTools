/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.kronos.rkon.core.ex.AuthenticationException;
import rconclient.util.CustomRcon;
import rconclient.util.Data;
import rconclient.util.WindowLoader;
import simplefxdialog.Dialog;
import simplefxdialog.img.DialogImage;

/**
 * FXML Controller class
 *
 * @author lukak
 */
public class LoginWindowController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private PasswordField password;
    @FXML
    private Label loginButton;
    @FXML
    private ProgressBar loginProgress;
    @FXML
    private Label loginInfo;
    @FXML
    private AnchorPane loginPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void login() {
        Thread loginThread = new Thread(() -> {
            Platform.runLater(() -> {
                loginPane.setDisable(true);
                loginProgress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                loginInfo.setText("Starting up");
            });

            try {
                Thread.sleep(new Random().nextInt(200));
            } catch (InterruptedException ex) {

            }

            Platform.runLater(() -> {
                loginInfo.setText("Connecting and autentificating");
            });

            boolean connected = true;
            try {
                CustomRcon rcon = CustomRcon.getInstance(ip.getText(), Integer.valueOf(port.getText()), password.getText().getBytes());
            } catch (IOException ex) {
                connected = false;
                Platform.runLater(() -> {
                    loginProgress.setProgress(0);
                    loginInfo.setText("");
                    Dialog.okDialog(DialogImage.error, "Connnection Error", "Couldnt connect to server.\n Probably an incorect IP.");
                    ip.setText("");
                });
            } catch (AuthenticationException ex) {
                connected = false;
                Platform.runLater(() -> {
                    loginProgress.setProgress(0);
                    loginInfo.setText("");
                    Dialog.okDialog(DialogImage.error, "Connnection Error", "Couldnt authenticate with server. \nIncorrect password.");
                    password.setText("");
                });
            } catch (NumberFormatException ex) {
                connected = false;
                Platform.runLater(() -> {
                    loginProgress.setProgress(0);
                    loginInfo.setText("");
                    Dialog.okDialog(DialogImage.error, "Conversion error", "Port you specified is not a valid integer");
                    port.setText("");
                });
            } catch (Exception ex) {
                connected = false;
                Platform.runLater(() -> {
                    loginProgress.setProgress(0);
                    loginInfo.setText("");
                    Dialog.okDialog(DialogImage.error, "Error", "General exception\n" + ex.getMessage());
                });
            }

            loginPane.setDisable(false);

            if (connected) {
                Data.rconTextData = null;

                //Write to credentials
                ArrayList<String> props = Data.credentialsDefaults;

                props.set(0, ip.getText()); //set ip
                props.set(1, port.getText()); //set port
                props.set(2, password.getText()); //set password
                try {
                    Data.writeCredentials(props);
                } catch (IOException ex) {
                    
                }
                Data.refresh();

                Platform.runLater(() -> {
                    loginInfo.setText("Login successful");
                    loginProgress.setProgress(0);
                    WindowLoader.loadRcon(rootPane);
                });
            }
        });
        loginThread.start();
    }
}
