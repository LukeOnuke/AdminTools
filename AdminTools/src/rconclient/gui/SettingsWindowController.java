/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
public class SettingsWindowController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField rconIP;
    @FXML
    private TextField rconPort;
    @FXML
    private PasswordField rconPassword;
    @FXML
    private RadioButton rconRemember;
    @FXML
    private Slider settingsApiRequestR;
    @FXML
    private TextField settingsQuerryRR;
    @FXML
    private Label sARR; //settings api request rate
    @FXML
    private Label sQRR; //settings querry request rate
    @FXML
    private ProgressBar connectionIndicator;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Data data = Data.getInstance();
        rconIP.setText(data.getHost());
        rconPort.setText(String.valueOf(data.getPort()));
        rconPassword.setText(data.getPasswordAsString());
        rconRemember.setSelected(data.getRconRemember());
        settingsApiRequestR.setValue(data.getQuerryMojangApiRefreshRate());
        settingsQuerryRR.setText(String.valueOf(data.getQuerryMcRefreshRate()));
        refresh();
    }

    @FXML
    private void loadRcon() {
        WindowLoader.loadRcon(rootPane);
    }

    @FXML
    private void loadStatus() {
        WindowLoader.loadStatus(rootPane);
    }

    @FXML
    private void login() {
        connectionIndicator.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        Thread thread = new Thread(() -> {
            Data datao = Data.getInstance();
            boolean isSuccsesfull = true;

            try {
                CustomRcon rcon = CustomRcon.getInstance();
                rcon.disconnect();

                rcon = CustomRcon.getInstance(rconIP.getText(), Integer.valueOf(rconPort.getText()), rconPassword.getText().getBytes());
            } catch (IOException ex) {
                isSuccsesfull = false;
                Dialog.okDialog(DialogImage.error, "Server error", "Incorect ip or server is not up");
                rconIP.setText(datao.getHost());
                rconPort.setText(String.valueOf(datao.getPort()));
            } catch (AuthenticationException ex) {
                isSuccsesfull = false;
                Dialog.okDialog(DialogImage.error, "Credentials error", "Credentials arent correct");
                rconPassword.setText(datao.getPasswordAsString());
            }

            if (isSuccsesfull) {
                ArrayList<String> data = Data.read();
                data.set(3, String.valueOf(rconRemember.isSelected()));
                Data.write(data);

                ArrayList<String> credentials = Data.readCredentials();
                credentials.set(0, rconIP.getText());
                credentials.set(1, rconPort.getText());
                credentials.set(2, rconPassword.getText());
                try {
                    Data.writeCredentials(credentials);
                } catch (IOException ex) {
                    Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }

                Data.refresh();

                Stage stage = (Stage) rootPane.getScene().getWindow();
                Data d = Data.getInstance();
                String barebonesq = "";
                if (Data.arguments.length > 0) {
                    if (Data.arguments[0].equals("barebones")) {
                        barebonesq = " [Barebones mode]";
                    }
                }
                stage.setTitle("Admin Tools - " + d.getHost() + ":" + d.getPort() + barebonesq);
                stage.show();
            }

        });

        connectionIndicator.setProgress(0.0);
    }

    @FXML
    private void refresh() {
        sARR.setText(String.valueOf(Math.round(settingsApiRequestR.getValue())) + "s");
    }

    @FXML
    private void apply() {
        ArrayList<String> data = Data.read();
        data.set(4, settingsQuerryRR.getText());
        data.set(5, String.valueOf(Math.round(settingsApiRequestR.getValue())));
        Data.write(data);
        Data.refresh();
    }

    @FXML
    private void reset() {
        Data d = Data.getInstance();
        settingsQuerryRR.setText(String.valueOf(d.getQuerryMcRefreshRate()));
        settingsApiRequestR.setValue(d.getQuerryMojangApiRefreshRate());
        refresh();
    }
}
