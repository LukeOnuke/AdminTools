/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.gui.credentials.credwizard;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import app.admintools.util.Utill;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import app.admintools.security.credentials.Credentials;
import app.admintools.util.Data;
import javafx.stage.StageStyle;

/**
 *
 * @author lukak
 */
public class CredWizard {

    private static final double OFFSET = 50d;

    /**
     * Creates and shows a credentials wizard
     * @param credentials The credentials to edit
     * @return Credentials
     */
    public static Credentials showCredWizard(Credentials credentials) {
        //SPOOKY SCARY JFX CODE
        AnchorPane ap;
        AnchorPane anchorPane;
        Text text;
        DropShadow dropShadow;
        TextField name;
        TextField ipAdress;
        TextField port;
        PasswordField password;
        TextField pingPort;
        Label button;
        Label status;

        ap = new AnchorPane();
        anchorPane = new AnchorPane();
        text = new Text();
        dropShadow = new DropShadow();
        name = new TextField();
        ipAdress = new TextField();
        port = new TextField();
        password = new PasswordField();
        pingPort = new TextField();
        button = new Label();
        status = new Label();

        ap.setId("AnchorPane");
        ap.setPrefHeight(377.0);
        ap.setPrefWidth(354.0);
        ap.getStyleClass().add("sidePane");

        AnchorPane.setLeftAnchor(anchorPane, 0.0);
        AnchorPane.setRightAnchor(anchorPane, 0.0);
        AnchorPane.setTopAnchor(anchorPane, 0.0);
        anchorPane.setPrefHeight(71.0);
        anchorPane.setPrefWidth(600.0);
        anchorPane.getStyleClass().add("titlePane");

        AnchorPane.setLeftAnchor(text, 12.0);
        AnchorPane.setTopAnchor(text, 7.8359375);
        text.setLayoutX(12.0);
        text.setLayoutY(51.0);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        text.getStyleClass().add("title");
        text.setText("Credentials Wizard");

        dropShadow.setColor(javafx.scene.paint.Color.web("#000000a3"));
        dropShadow.setHeight(11.86);
        dropShadow.setRadius(7.715);
        dropShadow.setSpread(0.4);
        anchorPane.setEffect(dropShadow);

        AnchorPane.setLeftAnchor(name, 9.0);
        AnchorPane.setRightAnchor(name, 9.0);
        AnchorPane.setTopAnchor(name, 96.0);
        name.setLayoutX(9.0);
        name.setLayoutY(96.0);
        name.setPrefHeight(27.0);
        name.setPrefWidth(331.0);
        name.setPromptText("Name");
        name.getStyleClass().add("rconTextField");

        AnchorPane.setLeftAnchor(ipAdress, 9.0);
        AnchorPane.setRightAnchor(ipAdress, 9.0);
        AnchorPane.setTopAnchor(ipAdress, 96.0 + OFFSET);
        ipAdress.setLayoutX(9.0);
        ipAdress.setLayoutY(96.0 + OFFSET);
        ipAdress.setPrefHeight(27.0);
        ipAdress.setPrefWidth(331.0);
        ipAdress.setPromptText("IP adress");
        ipAdress.getStyleClass().add("rconTextField");

        AnchorPane.setLeftAnchor(port, 9.0);
        AnchorPane.setRightAnchor(port, 9.0);
        AnchorPane.setTopAnchor(port, 96.0 + 2 * OFFSET);
        port.setLayoutX(9.0);
        port.setLayoutY(96.0 + 2 * OFFSET);
        port.setPrefHeight(27.0);
        port.setPrefWidth(331.0);
        port.setPromptText("Port");
        port.getStyleClass().add("rconTextField");

        AnchorPane.setLeftAnchor(password, 9.0);
        AnchorPane.setRightAnchor(password, 9.0);
        AnchorPane.setTopAnchor(password, 96.0 + 3 * OFFSET);
        password.setLayoutX(9.0);
        password.setLayoutY(96.0 + 3 * OFFSET);
        password.setPrefHeight(25.0);
        password.setPrefWidth(331.0);
        password.setPromptText("Password");
        password.getStyleClass().add("rconTextField");

        AnchorPane.setLeftAnchor(pingPort, 9.0);
        AnchorPane.setRightAnchor(pingPort, 9.0);
        AnchorPane.setTopAnchor(pingPort, 96.0 + 4 * OFFSET);
        pingPort.setLayoutX(9.0);
        pingPort.setLayoutY(96.0 + 4 * OFFSET);
        pingPort.setPrefHeight(27.0);
        pingPort.setPrefWidth(331.0);
        pingPort.setPromptText("Ping port - optional (if left unfilled it defaults to 25565)");
        pingPort.getStyleClass().add("rconTextField");

        AnchorPane.setBottomAnchor(button, 9.0);
        AnchorPane.setRightAnchor(button, 9.0);
        button.setLayoutX(312.0);
        button.setLayoutY(313.0);
        button.getStyleClass().add("labelButton");
        button.setText("Connect");

        AnchorPane.setBottomAnchor(status, 12.0);
        AnchorPane.setLeftAnchor(status, 9.0);
        status.setLayoutX(9.0);
        status.setLayoutY(286.0);
        status.setPrefHeight(20.0);
        status.setPrefWidth(272.0);
        status.getStyleClass().add("text");
        status.setText("");

        anchorPane.getChildren().add(text);
        ap.getChildren().add(name);
        ap.getChildren().add(anchorPane);
        ap.getChildren().add(ipAdress);
        ap.getChildren().add(port);
        ap.getChildren().add(password);
        ap.getChildren().add(pingPort);
        ap.getChildren().add(button);
        ap.getChildren().add(status);
        
        //Read the credentials and set the stuff acordingly
        
        //Check if credentials aren't null
        if(credentials != null){
            //Set all the stuffs
            name.setText(credentials.getName());
            ipAdress.setText(credentials.getIP());
            port.setText(String.valueOf(credentials.getPort()));
            password.setText(credentials.getPassword());
            pingPort.setText(String.valueOf(credentials.getPingPort()));
        }

        //Create window
        Scene scene = new Scene(ap);
        Utill.setSelectedTheme(scene);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);

        //Credentials to return
        AtomicBoolean isValid = new AtomicBoolean(false);

        button.setOnMouseClicked((me) -> {
            boolean valid = true;
            try {
                //Check all the imputs
                Integer.valueOf(port.getText());
                //Dont check pingport if empty
                if (!pingPort.getText().equals("")) {
                    Integer.valueOf(pingPort.getText());
                }
                InetAddress.getByName(ipAdress.getText());
                //Catch all exeptions and set valid to false and set indicator
            } catch (NumberFormatException ex) {
                valid = false;
                status.setText("Please input a valid intiger into the port or ping port field");
            } catch (UnknownHostException ex) {
                valid = false;
                status.setText("Please input a valid IP address");
            }

            //Is everything valid?
            if (valid) {
                status.setText("Setting up");
                isValid.set(valid);
                stage.close();
            }
        });

        stage.showAndWait();

        if (isValid.get()) {
            //If the user left the pingport field empty fill it in with the default port
            if (pingPort.getText().equals("")) {
                pingPort.setText("25565");
            }
            return new Credentials(ipAdress.getText(), Integer.valueOf(port.getText()), password.getText(), name.getText(), Integer.valueOf(pingPort.getText()));
        }
        return null;
    }
    
    /**
     * Show credentials wizard without having a credentials to edit.
     * @return Credentials created out of the good stuff.
     */
    public static Credentials showCredWizard(){
        return showCredWizard(null);
        //Give ya meet a good old rub. Wow somebody reading the source code, thanks for that -LukeOnuke 24.10.2020 00:40 .
    }
}
