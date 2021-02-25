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

import app.admintools.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import app.admintools.gui.theme.ThemeReader;
import com.lukeonuke.simplefxdialog.Dialog;
import com.lukeonuke.simplefxdialog.img.DialogImage;

/**
 * FXML Controller class
 *
 * @author lukak
 */
public class SettingsWindowController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Slider settingsApiRequestR;
    @FXML
    private TextField settingsQuerryRR;
    @FXML
    private Label sARR; //settings api request rate
    //@FXML
    //private Label sQRR; //settings querry request rate

    @FXML
    private RadioButton settingsMsgNotify;
    @FXML
    private RadioButton settingsMsgOverrideSay;
    @FXML
    private TextField settingsMsgUsername;

    @FXML
    private ChoiceBox themeChoice;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Data data = Data.refresh();
        try{
            settingsApiRequestR.setValue(data.getQuerryMojangApiRefreshRate());
            settingsQuerryRR.setText(String.valueOf(data.getQuerryMcRefreshRate()));
            settingsMsgOverrideSay.setSelected(data.getMessageOverwriteSay());
            settingsMsgNotify.setSelected(data.getMessageNotify());
            settingsMsgUsername.setText(data.getMessageUsername());

            //Set choicebox choices
            ThemeReader.listThemes().forEach((choice) -> {
                themeChoice.getItems().add(choice);
            });

            themeChoice.setValue(data.getSelectedTheme());
        } catch (IOException e) {
            AtLogger.logger.severe(AtLogger.formatException(e));
        }

        refresh();

        DRPC.statusManagingServer();
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
    private void loadHome() {
        WindowLoader.loadHome(rootPane);
    }

    @FXML
    private void onKeyPressed(KeyEvent ke) {
        if (ke.getText().equals("s") && ke.isControlDown()) {
            apply();
            Dialog.okDialog(DialogImage.INFO, "Settings saved", "Your settings have been saved");
        }
    }

    @FXML
    private void refresh() {
        sARR.setText(String.valueOf(Math.round(settingsApiRequestR.getValue())) + "s");
    }

    @FXML
    private void apply() {

        try {
            Data.write(DataType.QUERRY_MC_REFRESHRATE, settingsQuerryRR.getText());
            Data.write(DataType.QUERRY_API_REFRESHRATE, String.valueOf(Math.round(settingsApiRequestR.getValue())));
            Data.write(DataType.MESSAGE_SEND_ON_LOGON, String.valueOf(settingsMsgNotify.selectedProperty().get()));
            Data.write(DataType.MESSAGE_OVERWRITE_SAY, String.valueOf(settingsMsgOverrideSay.selectedProperty().get()));
            Data.write(DataType.USERNAME, settingsMsgUsername.getText());
            Data.write(DataType.THEME, themeChoice.getValue().toString());
        } catch (IOException e) {
            AtLogger.logger.severe(AtLogger.formatException(e));
        }


        //Actualy have to refresh the theme
        //And refresh the Data singleton
        Utill.setSelectedTheme(rootPane);
    }

    @FXML
    private void reset() {
        settingsQuerryRR.setText(Data.defaults.get(DataType.QUERRY_MC_REFRESHRATE));
        settingsApiRequestR.setValue(Double.valueOf(Data.defaults.get(DataType.QUERRY_API_REFRESHRATE)));
        refresh();
    }
}
