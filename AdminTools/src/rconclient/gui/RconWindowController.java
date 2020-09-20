package rconclient.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.kronos.rkon.core.ex.AuthenticationException;
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
    private WebView rcon;
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

    //Web engine
    private WebEngine rconWebEngine;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Instantiate web engine
        rcon.contextMenuEnabledProperty().set(false);
        rconWebEngine = rcon.getEngine();

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
            refresh();
        } else {
            //Initilise rcon text data
            Data.rconTextData = new ArrayList<String>();
            //Write welcome message if rcon children data is clear
            //Write welcome message
            write("§aAdmin§bTools§r, a administration tool by §9§lLukeOnuke§r");
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
                write("§bConnecting to " + d.getHost() + ":" + d.getPort());
                try {
                    CustomRcon cr = CustomRcon.getInstance();
                    //write connected message if its enabled
                    if (d.getMessageNotify()) {
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
                        write("§bConnected to " + d.getHost() + ": " + d.getPort());
                    });
                    rconSend.setDisable(false);
                    sendButton.setDisable(false);
                    sidePane.setDisable(false);
                } else {
                    Platform.runLater(() -> {
                        write("§cCouldnt connect to " + d.getHost() + ": " + d.getPort());
                    });
                }
            });
            connect.start();

            Data.startingUp = false;
        }
    }

    public void write(String message) {
        Data.rconTextData.add(Markup.generateMarkupParagraph(message));
        refresh(); //refresh
        //scroll to bottom
        Platform.runLater(() -> {
            rcon.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    if (newValue == Worker.State.SUCCEEDED) {
                        rcon.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    }
                }
            });
        });
    }

    public void refresh() {
        StringBuilder sb = new StringBuilder();
        Data.rconTextData.set(0, "<style>body{background-color: #383838; font-family: Consolas, monaco, monospace;}</style>" + Data.rconTextData.get(0)); //add the style shit
        for (String element : Data.rconTextData) {

            sb.append(element);
        }
        Platform.runLater(() -> {
            rconWebEngine.loadContent(sb.toString());
        });
    }

    @FXML
    private void send() {
        CustomRcon cRcon;
        try {
            cRcon = CustomRcon.getInstance();
            if (!rconSend.getText().equals("")) {
                write(Utill.getDate() + rconSend.getText());

                //Internal command interpreter
                boolean isRightToSend = true; //Boolean that is checked when sending commands to server

                switch (rconSend.getText()) {
                    //Stop command
                    case "stop":
                        if (Dialog.okCancelDialog(DialogImage.warning, "Are you sure?", "Do you realy want to stop the server?"
                                + System.lineSeparator()
                                + "By pressing on you will be issuing a stop command to the server")) {

                            isRightToSend = false;
                            write("§bStopping the server and exiting in 2 (two) seconds");
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
                        write("§bAdmin Tools internal command interpreter help: " + System.lineSeparator()
                                + "\t!help - Help command" + System.lineSeparator()
                                + "\t!clear - Clear the console" + System.lineSeparator()
                                + "\t!exit - Exit the program and close the rcon connection" + System.lineSeparator());
                        break;
                    //Clear command - clears the console
                    case "!clear":
                        isRightToSend = false;
                        Data.rconTextData.clear();
                        write("§bCleared console");
                        break;
                    //Clear command - clears the console
                    case "!login":
                        isRightToSend = false;
                        Data.rconTextData.clear();
                        WindowLoader.loadLogin(rootPane);
                        break;

                    //Exit command - exits the program
                    case "!exit":
                        isRightToSend = false;

                        write("§bClosing connection and exiting...");

                        cRcon.disconnect();
                        System.exit(0);
                        break;
                    default:
                        break;
                }

                if (isRightToSend) {
                    //Send and recive recsponce
                    write(cRcon.command(rconSend.getText()));
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
        WindowLoader.loadSettings(rootPane);
    }

    @FXML
    private void loadStatus() {
        WindowLoader.loadStatus(rootPane);
    }
}
