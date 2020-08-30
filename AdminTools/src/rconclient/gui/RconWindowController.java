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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import net.kronos.rkon.core.ex.AuthenticationException;
import rconclient.security.Mozaic;
import rconclient.util.CustomRcon;
import rconclient.textprocessing.Markup;
import rconclient.textprocessing.TellrawFormatter;
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

    //Command history
    private ArrayList<String> commandHistory = new ArrayList<String>();
    private int commandHistoryDeviation = 0;

    //FXML
    @FXML
    private TextField rconSend;
    @FXML
    private TextFlow rcon;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label sendButton;
    @FXML
    private ScrollPane rconScroll;

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

        //Auto scroll
        rcon.heightProperty().addListener((observable, oldValue, newValue) -> {
            rconScroll.setVvalue(1.0d);
        });

        //setup history entery
        rconSend.setOnKeyPressed((KeyEvent event) -> {
            boolean hasAction = false;
            int indexAfterAction = commandHistory.size() + commandHistoryDeviation + 1;
            if (event.getCode() == KeyCode.UP && -1 < indexAfterAction && indexAfterAction < commandHistory.size()) {
                commandHistoryDeviation++;
                hasAction = true;
            }
            indexAfterAction = commandHistory.size() + (commandHistoryDeviation - 1);
            if (event.getCode() == KeyCode.DOWN && -1 < indexAfterAction && indexAfterAction < commandHistory.size()) {
                commandHistoryDeviation--;
                hasAction = true;
            }
            if (hasAction) {
                int index = commandHistory.size() + commandHistoryDeviation;
                rconSend.setText(commandHistory.get(index));
            }
        });

        if (Data.startingUp) {
            //Connect to rcon

            Thread connect = new Thread(() -> {
                //setup variables
                boolean connected = true;
                //Disable all the text elements and buttons
                rconSend.setDisable(true);
                sendButton.setDisable(true);
                sidePane.setDisable(true);

                Data d = Data.getInstance();
                writeRconInternal("Connecting to " + d.getHost() + ":" + d.getPort());
                try {
                    CustomRcon cr = CustomRcon.getInstance();
                    //write connected message if its enabled
                    if(d.getMessageNotify()){
                        cr.command(TellrawFormatter.assembleLoginTellraw(d.getMessageUsername()));
                    }
                } catch (IOException ex) {
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.error, "Connnection Error", "Couldn't connect to server.\n Probably an incorect IP.");
                        WindowLoader.loadLogin(rootPane);
                    });
                } catch (AuthenticationException ex) {
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.error, "Connnection Error", "Couldn't authenticate with server. \nIncorrect password.");
                        WindowLoader.loadLogin(rootPane);
                    });
                } catch (Exception ex) {
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.error, "Error", "General exception\n" + ex.getMessage());
                        WindowLoader.loadLogin(rootPane);
                    });
                }
                //Write succsesfull connection
                if (connected) {
                    Platform.runLater(() -> {
                        writeRconInternal("Connected to " + d.getHost() + ": " + d.getPort());
                    });
                    rconSend.setDisable(false);
                    sendButton.setDisable(false);
                    sidePane.setDisable(false);
                } else {
                    Platform.runLater(() -> {
                        write("Couldnt connect to " + d.getHost() + ": " + d.getPort(), Color.RED);
                    });
                }
            });
            connect.start();
            Data.startingUp = false;
        }
        
    }

    public void writeRcon(String message) {
        write(message + "\n", Color.WHITE);
    }

    public void writeRconInternal(String message) {
        write(message + "\n", Color.rgb(0, 213, 255));
    }

    public void writeRconResponce(String message) {
        write(message + "\n", Markup.rconReplyMarkup(message));
    }

    public void write(String message, Color color) {
        Text t = new Text(message);
        t.setFill(color);
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
                        }
                        break;
                    //Help command
                    case "!help":
                        isRightToSend = false;
                        writeRconInternal("Admin Tools internal command interpreter help: " + System.lineSeparator()
                                + "\t!help - Help command" + System.lineSeparator()
                                + "\t!clear - Clear the console" + System.lineSeparator()
                                + "\t!exit - Exit the program and close the rcon connection" + System.lineSeparator());
                        break;
                    //Clear command - clears the console
                    case "!clear":
                        isRightToSend = false;
                        rcon.getChildren().clear();
                        writeRconInternal("Cleared console");
                        break;
                    //Clear command - clears the console
                    case "!login":
                        isRightToSend = false;
                        rcon.getChildren().clear();
                        WindowLoader.loadLogin(rootPane);
                        break;

                    //Exit command - exits the program
                    case "!exit":
                        isRightToSend = false;

                        writeRconInternal("Closing connection and exiting...");

                        cRcon.disconnect();
                        System.exit(0);
                        break;
                    default:
                        break;
                }

                if (isRightToSend) {
                    //Send and recive recsponce
                    writeRconResponce(Utill.removeSpigotFormatting(cRcon.command(rconSend.getText())));
                }
            }
        } catch (IOException | AuthenticationException ex) {
            Logger.getLogger(RconWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Set command history
        commandHistory.add(rconSend.getText());
        //Reset command history deviation
        commandHistoryDeviation = 0;
        //Reset textfield
        rconSend.setText("");
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
