/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.util;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import app.admintools.gui.SettingsWindowController;

/**
 *
 * @author lukak
 */
public class WindowLoader {

    public static void loadRcon(AnchorPane rootPane) {
        loadWindow(rootPane, "/app/admintools/gui/RconWindow.fxml");
        Data.isOnStatusWindow = false;
    }

    public static void loadSettings(AnchorPane rootPane) {
        loadWindow(rootPane, "/app/admintools/gui/SettingsWindow.fxml");
        Data.isOnStatusWindow = false;
    }

    public static void loadStatus(AnchorPane rootPane) {
        Data.isOnStatusWindow = true;
        loadWindow(rootPane, "/app/admintools/gui/StatusWindow.fxml");
    }

    public static void loadHome(AnchorPane rootPane) {
        loadWindow(rootPane, "/app/admintools/gui/HomeWindow.fxml");
        Data.isOnStatusWindow = false;
    }

    private static void loadWindow(AnchorPane rootPane, String url) {

        try {
            Data d = Data.getInstance(); //Get instance of data 

            AnchorPane ap = FXMLLoader.load(SettingsWindowController.class.getResource(url)); //Get anchorpane

            //Set style for selected theme
            ap.getStylesheets().add("file:Assets/Themes/" + d.getSelectedTheme() + "/style.css");

            //Create stage      yes
            Scene scene2 = new Scene(ap);
            Stage windowStage = (Stage) rootPane.getScene().getWindow();
            //Get widht and height
            double width = windowStage.getWidth();
            double height = windowStage.getHeight();

            //Swich scene
            windowStage.setScene(scene2);

            //Get fade transition inbetween windows
            //Fade in
            //Foreach just netben
            ap.getChildren().stream().map((child) -> new FadeTransition(Duration.seconds(0.25), child)).map((ft) -> {
                ft.setFromValue(.3d);
                return ft;
            }).map((ft) -> {
                ft.setToValue(1.0d);
                return ft;
            }).forEachOrdered((ft) -> {
                ft.play();
            });

            //Refresh title
            windowStage.setTitle("Admin Tools - " + d.getSelectedCredentials().getIP() + ":" + d.getSelectedCredentials().getPort());

            //Set width after scene swithch
            windowStage.setWidth(width);
            windowStage.setHeight(height);

        } catch (IOException ex) {
            AtLogger.logException(ex);
        }

    }
}
