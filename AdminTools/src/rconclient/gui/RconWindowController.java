/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.kronos.rkon.core.ex.AuthenticationException;
import rconclient.util.CustomRcon;
import rconclient.textprocessing.Markup;
import rconclient.util.Data;
import rconclient.util.Utill;
import rconclient.util.WindowLoader;
import simplefxdialog.Dialog;
import simplefxdialog.img.DialogImage;

/**
 *
 * @author lukak
 */
public class RconWindowController implements Initializable {

    @FXML
    private TextField rconSend;
    @FXML
    private TextFlow rcon;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label sendButton;

    //Barebones mode variables
    @FXML
    private AnchorPane sidePane;
    @FXML
    private AnchorPane rconPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Get if barebones argument was given and transition to barebones mode if it was given
        if (Data.arguments.length > 0) {
            if (Data.arguments[0].equals("barebones")) {
                sidePane.disableProperty().set(true);
                sidePane.visibleProperty().set(false);

                AnchorPane.setLeftAnchor(rconPane, 0.0);
            }
        }

        //Read and set the rcon text from the data property if Data.rconTextData isnt null or write welcon message and set data
        if (Data.rconTextData != null) {
            rcon.getChildren().clear();
            rcon.getChildren().addAll(Data.rconTextData);
        } else {
            //Write welcome message if rcon children data is clear
            //Write welcome message
            /*writeRcon("|========================|");
            writeRcon("|Admin Tools by LukeOnuke|");
            writeRcon("|========================|");*/

            AnchorPane credits = new AnchorPane();
            Label creditsLabel = new Label("Admin Tools - Made by LukeOnuke");
            Hyperlink hl = new Hyperlink("https://github.com/LukeOnuke");

            credits.setPrefSize(200, 60);
            credits.setStyle("-fx-background-color: #3d3d3d;-fx-border-color: #5c5c5c;-fx-border-width: 1px;");

            creditsLabel.setStyle("-fx-text-fill: white;");

            hl.setOnAction(e -> {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/LukeOnuke"));
                    } catch (URISyntaxException | IOException ex) {
                        Logger.getLogger(RconWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            AnchorPane.setBottomAnchor(hl, 10.0);
            AnchorPane.setLeftAnchor(hl, 2.0);

            AnchorPane.setBottomAnchor(creditsLabel, 40.0);
            AnchorPane.setLeftAnchor(creditsLabel, 5.0);

            credits.getChildren().add(creditsLabel);
            credits.getChildren().add(hl);

            rcon.getChildren().add(credits);
            
            writeRcon("");

            //Set rcon children
            Data.rconTextData = rcon.getChildren();
        }

    }

    public void writeRcon(String message) {
        Text t = new Text(message + "\n");
        t.setFill(Color.WHITE);
        t.setFont(Font.font("Consolas", FontPosture.REGULAR, 14));
        rcon.getChildren().add(t);
    }

    public void writeRconInternal(String message) {
        Text t = new Text(message + "\n");
        t.setFill(Color.rgb(0, 213, 255));
        t.setFont(Font.font("Consolas", FontPosture.REGULAR, 14));
        rcon.getChildren().add(t);
    }

    public void writeRconResponce(String message) {
        Text t = new Text(message + "\n");
        t.setFill(Markup.rconReplyMarkup(message));
        t.setFont(Font.font("Consolas", FontPosture.REGULAR, 14));
        rcon.getChildren().add(t);
    }

    @FXML
    private void send() {
        CustomRcon cRcon;
        try {
            cRcon = CustomRcon.getInstance();
            if (!rconSend.getText().equals("")) {
                writeRcon(Utill.getDate() + rconSend.getText());

                //Internal command interpreter
                boolean isRightToSend = true; //Boolean that is checked when sending commands to server

                switch (rconSend.getText()) {
                    //Stop command
                    case "stop":
                        if (Dialog.okCancelDialog(DialogImage.warning, "Are you sure?", "Do you realy want to stop the server?"
                                + System.lineSeparator()
                                + "By pressing on you will be issuing a stop command to the server")) {

                            isRightToSend = false;
                            writeRconInternal("Stopping the server and exiting in 2 (two) seconds");
                            cRcon.command("stop");
                            sendButton.disableProperty().set(true);
                            rconSend.disableProperty().set(true);
                            Thread t = new Thread(() -> {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {

                                }
                                System.exit(0);
                            });
                            t.start();
                            rconSend.setText("");
                        }
                        break;
                    //Help command
                    case "!help":
                        rconSend.setText("");
                        isRightToSend = false;
                        writeRconInternal("Admin Tools internal command interpreter help: " + System.lineSeparator()
                                + "\t!help - Help command" + System.lineSeparator()
                                + "\t!clear - Clear the console" + System.lineSeparator()
                                + "\t!exit - Exit the program and close the rcon connection" + System.lineSeparator());
                        break;
                    //Clear command - clears the console
                    case "!clear":
                        isRightToSend = false;
                        rconSend.setText("");
                        rcon.getChildren().clear();
                        writeRconInternal("Cleared console");
                        break;

                    //Exit command - exits the program
                    case "!exit":
                        isRightToSend = false;
                        rconSend.setText("");

                        writeRconInternal("Closing connection and exiting...");

                        cRcon.disconnect();
                        System.exit(0);
                        break;
                    default:
                        break;
                }

                if (isRightToSend) {
                    //Send and recive recsponce
                    writeRconResponce(cRcon.command(rconSend.getText()));
                    //Reset textfield
                    rconSend.setText("");
                }
            }
        } catch (IOException | AuthenticationException ex) {
            Logger.getLogger(RconWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadSettings() {
        Data.rconTextData = rcon.getChildren();
        WindowLoader.loadSettings(rootPane);
    }

    @FXML
    private void loadStatus() {
        Data.rconTextData = rcon.getChildren();
        WindowLoader.loadStatus(rootPane);
    }
}
