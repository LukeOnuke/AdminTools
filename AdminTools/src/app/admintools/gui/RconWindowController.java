package app.admintools.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import net.kronos.rkon.core.ex.AuthenticationException;
import app.admintools.gui.theme.ThemeReader;
import app.admintools.util.CustomRcon;
import app.admintools.textprocessing.Markup;
import app.admintools.textprocessing.TellrawFormatter;
import app.admintools.util.AtLogger;
import app.admintools.util.Data;
import app.admintools.util.Utill;
import app.admintools.util.WindowLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lukeonuke.simplefxdialog.Dialog;
import com.lukeonuke.simplefxdialog.img.DialogImage;

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

        //Debug
        rconWebEngine.setOnError(new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent event) {
                System.err.println(event);
            }
        });

        //Setup autoscrolldown
        rcon.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    rcon.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);"); //JS JS JS JS JS JS JS JS JS
                }
            }
        });

        //Read and set the rcon text from the data property if Data.rconTextData isnt null or write welcon message and set data
        if (Data.rconTextData != null) {
            refresh();
        } else {
            //Initilise rcon text data
            Data.rconTextData = new ArrayList<String>();
            //Write welcome message if rcon children data is clear
            //Write welcome message
            write("§aAdmin§bTools§r, an administration tool by §9§lLukeOnuke§r");

            Thread vChecker = new Thread(() -> {
                //Write if new version avalable
                write("§a[AVCS] §fChecking for new verison");

                JsonObject updateStats;
                try {
                    updateStats = new Gson().fromJson(Utill.getHTTPRequest("https://api.github.com/repos/LukeOnuke/AdminTools/releases/latest"), JsonObject.class);

                    boolean isUpToDate = true;
                    if (!updateStats.get("draft").getAsBoolean()) {
                        if (!updateStats.get("tag_name").getAsString().equals("v" + this.getClass().getPackage().getImplementationVersion())) {
                            write("§a[AVCS] §4Newer version found §9" + updateStats.get("tag_name").getAsString() + "\n      §f" + updateStats.get("name").getAsString() + "\n      Get it from github: §ahttps://get.admintools.app/");
                            
                            isUpToDate = false;
                        }
                    }
                    if (isUpToDate) {
                        write("§a[AVCS] §fLatest version");
                    }
                } catch (IOException ex) {
                    AtLogger.logException(ex);
                    write("§a[AVCS] §4Couldnt fetch version info - Probably reached the api rate limit");
                }

            }, "Autonomous version control");
            vChecker.start();
        }

        //Auto scroll
        rcon.heightProperty().addListener((observable, oldValue, newValue) -> {
            rconScroll.setVvalue(1.0d);
        });

        //setup history entery
        rconSend.setOnKeyPressed((KeyEvent event) -> {
            boolean hasAction = false;
            int indexAfterAction = commandHistory.size() + commandHistoryDeviation + 1;
            if (event.getCode() == KeyCode.DOWN && -1 < indexAfterAction && indexAfterAction < commandHistory.size()) {
                commandHistoryDeviation++;
                hasAction = true;
            }
            indexAfterAction = commandHistory.size() + (commandHistoryDeviation - 1);
            if (event.getCode() == KeyCode.UP && -1 < indexAfterAction && indexAfterAction < commandHistory.size()) {
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
                write("§bConnecting to " + d.getSelectedCredentials().getIP() + ":" + d.getSelectedCredentials().getPort());
                try {
                    CustomRcon cr = CustomRcon.getInstance();
                    //write connected message if its enabled
                    if (d.getMessageNotify()) {
                        cr.command(TellrawFormatter.assembleLoginTellraw(d.getMessageUsername()));
                    }
                } catch (IOException ex) {
                    AtLogger.logException(ex);
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.ERROR, "Connnection Error", "Couldn't connect to server.\n Probably an incorect IP.");
                        WindowLoader.loadHome(rootPane);
                    });
                } catch (AuthenticationException ex) {
                    AtLogger.logException(ex);
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.ERROR, "Connnection Error", "Couldn't authenticate with server. \nIncorrect password.");
                        WindowLoader.loadHome(rootPane);
                    });
                } catch (Exception ex) {
                    AtLogger.logException(ex);
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.ERROR, "Error", "General exception\n" + ex.getMessage());
                        WindowLoader.loadHome(rootPane);
                    });
                }
                //Write succsesfull connection
                if (connected) {
                    Platform.runLater(() -> {
                        write("§bConnected to " + d.getSelectedCredentials().getIP() + ":" + d.getSelectedCredentials().getPort());
                    });
                    rconSend.setDisable(false);
                    sendButton.setDisable(false);
                    sidePane.setDisable(false);
                } else {
                    Platform.runLater(() -> {
                        write("§cCouldnt connect to " + d.getSelectedCredentials().getIP() + ":" + d.getSelectedCredentials().getPort());
                    });
                }
            }, "Connector Thread");
            connect.start();

            Data.startingUp = false;
        }
    }

    public void write(String message) {
        Data.rconTextData.add(Markup.generateMarkupParagraph(message));
        refresh(); //refresh
    }

    public void refresh() {
        StringBuilder sb = new StringBuilder();
        String color = "#5"; //Default is purple to indicate something went wrong
        try {
            color = ThemeReader.getConsoleColor(Data.getInstance().getSelectedTheme());
        } catch (FileNotFoundException ex) {
            AtLogger.logException(ex);
        }

        Data.rconTextData.set(0, "<style>body {background-color: " + color + "; font-family: \"Lucida Console\", Courier, monospace;}</style>" + Data.rconTextData.get(0)); //add the style shit
        for (String element : Data.rconTextData) {
            sb.append(element);
        }
        Platform.runLater(() -> {
            rconWebEngine.loadContent(sb.toString());

        });
    }

    private void sendCommand(String command) {
        CustomRcon cRcon;
        try {
            cRcon = CustomRcon.getInstance();
            if (!command.equals("")) {
                write(Utill.getDate() + command);
                AtLogger.log(Level.INFO, "Command sent to execution manager: " + command);
                //Internal command interpreter
                boolean isRightToSend = true; //Boolean that is checked when sending commands to server

                switch (command) {
                    //Stop command
                    case "stop":
                        if (Dialog.okCancelDialog(DialogImage.WARNING, "Are you sure?", "Do you realy want to stop the server?"
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
                                    AtLogger.logException(ex);
                                }
                                Utill.exit(0);
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
                        WindowLoader.loadHome(rootPane);
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
                    write(cRcon.command(command));
                }
            }
        } catch (IOException | AuthenticationException ex) {
            AtLogger.logException(ex);
        }

        //Set command history
        commandHistory.add(command);
        //Reset command history deviation
        commandHistoryDeviation = 0;
        //Reset textfield
        rconSend.setText("");
    }

    @FXML
    private void send() {
        sendCommand(rconSend.getText());
    }

    @FXML
    private void loadSettings() {
        WindowLoader.loadSettings(rootPane);
    }

    @FXML
    private void loadStatus() {
        WindowLoader.loadStatus(rootPane);
    }

    @FXML
    private void loadHome() {
        WindowLoader.loadHome(rootPane);
    }
}
