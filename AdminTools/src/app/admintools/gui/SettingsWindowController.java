/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import app.admintools.util.Data;
import app.admintools.util.WindowLoader;
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
    private void loadHome() {
        WindowLoader.loadHome(rootPane);
    }

    @FXML
    private void onKeyPressed(KeyEvent ke) {
        if (ke.getText().equals("s") && ke.isControlDown()) {
            apply();
            Dialog.okDialog(DialogImage.info, "Settings saved", "Your settings have been saved");
        }
    }

    @FXML
    private void refresh() {
        sARR.setText(String.valueOf(Math.round(settingsApiRequestR.getValue())) + "s");
    }

    @FXML
    private void apply() {
        ArrayList<String> data = Data.read();
        data.set(1, settingsQuerryRR.getText());
        data.set(2, String.valueOf(Math.round(settingsApiRequestR.getValue())));
        data.set(3, String.valueOf(settingsMsgNotify.selectedProperty().get()));
        data.set(4, String.valueOf(settingsMsgOverrideSay.selectedProperty().get()));
        data.set(5, settingsMsgUsername.getText());
        data.set(6, themeChoice.getValue().toString());
        Data.write(data);

        //Actualy have to refresh the theme
        //And refresh the Data singleton
        rootPane.getStylesheets().add("file:Assets/Themes/" + Data.refresh().getSelectedTheme() + "/style.css");
    }

    @FXML
    private void reset() {
        settingsQuerryRR.setText(Data.defaults.get(1));
        settingsApiRequestR.setValue(Double.valueOf(Data.defaults.get(2)));
        refresh();
    }
}
