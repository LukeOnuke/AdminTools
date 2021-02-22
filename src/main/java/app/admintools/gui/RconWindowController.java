package app.admintools.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

import app.admintools.util.*;
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
import app.admintools.textprocessing.Markup;
import app.admintools.textprocessing.TellrawFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lukeonuke.simplefxdialog.Dialog;
import com.lukeonuke.simplefxdialog.img.DialogImage;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

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
                AtLogger.logger.severe(event.getMessage());
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
                AtLogger.logger.info("Connecting to server");
                try {
                    CustomRcon cr = CustomRcon.getInstance();
                    //write connected message if its enabled
                    if (d.getMessageNotify()) {
                        cr.command(TellrawFormatter.assembleLoginTellraw(d.getMessageUsername()));
                    }
                } catch (IOException ex) {
                    AtLogger.logger.warning(AtLogger.formatException(ex));
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.ERROR, "Connnection Error", "Couldn't connect to server.\n Probably an incorect IP.");
                        WindowLoader.loadHome(rootPane);
                    });
                } catch (AuthenticationException ex) {
                    AtLogger.logger.warning(AtLogger.formatException(ex));
                    connected = false;
                    Platform.runLater(() -> {
                        Dialog.okDialog(DialogImage.ERROR, "Connnection Error", "Couldn't authenticate with server. \nIncorrect password.");
                        WindowLoader.loadHome(rootPane);
                    });
                } catch (Exception ex) {
                    AtLogger.logger.warning(AtLogger.formatException(ex));
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

        DRPC.statusManagingServer();
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
            AtLogger.logger.warning(AtLogger.formatException(ex));
        }

        Data.rconTextData.add(0, "<style>body {background-color: " + color + "; font-family: \"Lucida Console\", Courier, monospace;}</style>"); //add the style shit
        for (String element : Data.rconTextData) {
            sb.append(element);
        }
        Platform.runLater(() -> {
            rconWebEngine.loadContent(sb.toString());
        });
    }

    /**
     * 1-st layer interpreter command send
     *
     * @param command the command to be sent
     */
    private void sendCommand(String command) {
        CustomRcon cRcon;
        try {
            cRcon = CustomRcon.getInstance();
            if (!command.equals("")) {
                write(Utill.getDate() + command);
                AtLogger.logger.info("Command sent to execution manager: " + command);
                //Internal command interpreter
                boolean isRightToSend = true; //Boolean that is checked when sending commands to server

                ArrayList<String> splitCommand = new ArrayList<>(Arrays.asList(command.trim().split("\\s+")));

                if (command.equals("stop")) {
                    if (Dialog.okCancelDialog(DialogImage.WARNING, "Are you sure?", "Do you realy want to stop the server?"
                            + System.lineSeparator()
                            + "By pressing on you will be issuing a stop command to the server")) {

                        isRightToSend = false;
                        write("§bStopping the server.");
                        cRcon.command("stop");
                        sendButton.disableProperty().set(true);
                        rconSend.disableProperty().set(true);
                    }
                } else if (command.equals("!help")) {
                    isRightToSend = false;
                    write("§bAdmin Tools internal command interpreter help: " + System.lineSeparator()
                            + "\t!help - Help command" + System.lineSeparator()
                            + "\t!clear - Clear the console" + System.lineSeparator()
                            + "\t!exit - Exit the program and close the rcon connection" + System.lineSeparator());
                } else if (command.equals("!clear")) {
                    isRightToSend = false;
                    Data.rconTextData.clear();
                    write("§bCleared console");
                } else if (command.equals("!exit")) {
                    isRightToSend = false;

                    write("§bClosing connection and exiting...");
                    CustomRcon.getInstance().disconnect();

                    Utill.exit(commandHistoryDeviation);
                } else if (splitCommand.get(0).equals("!if")) {
                    isRightToSend = false;
                    if (logicalOperations(splitCommand.get(1))) {
                        splitCommand.remove(0);
                        splitCommand.remove(0);
                        /*
                        *Send the command that is after the if instruction ex. &if yes==yes <command>
                        *Send it this way so that it allows for nested if-s
                         */
                        sendCommand(Utill.removeArrrayFormatting(splitCommand.toString()));
                    }
                    //Print statement
                } else if (splitCommand.get(0).equals("!print")) {
                    isRightToSend = false;
                    splitCommand.remove(0);
                    write(Utill.removeArrrayFormatting(splitCommand.toString())); //Send the text after the print instruction

                }

                for (int i = 0; i < listScripts().size(); i++) {
                    if (command.startsWith("@" + Utill.stripExtension(listScripts().get(i)))) {
                        isRightToSend = false;
                        ArrayList<String> args = new ArrayList<>(Arrays.asList(command.split("[ ]")));
                        args.remove(0);
                        executeScript(getScript(listScripts().get(i)), args);
                    }
                }

                if (isRightToSend) {
                    //Send and recive response
                    write(cRcon.command(command));
                    AtLogger.logger.info("Command sent to server : " + command);
                }
            }
        } catch (IOException | AuthenticationException ex) {
            AtLogger.logger.warning(AtLogger.formatException(ex));
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

    public static ArrayList<String> listScripts() {
        ArrayList<String> themeDir = new ArrayList<String>(); //Netbeans takes a shite than complaians
        File[] themes = new File(Utill.getPath("assets/scripts/")).listFiles(); //Get a array of all files in the script folder
        for (File theme : themes) { //Go through them all
            if (theme.isFile()) {
                themeDir.add(theme.getName());  //Add its name to the returning arraylist if its a directory
            }
        }
        return themeDir;
    }

    public static String getScript(String scriptName) {
        return Utill.getPath("assets/scripts/" + scriptName);
    }

    private void executeScript(String scriptPath, ArrayList<String> args) throws FileNotFoundException {
        // pass the path to the file as a parameter 
        File file = new File(scriptPath);
        Scanner sc = new Scanner(file);

        write("┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉");

        while (sc.hasNextLine()) {
            String instruction = sc.nextLine();
            if (instruction != null) {
                for (int i = 0; i < args.size(); i++) {
                    instruction = instruction.replace("arg" + i, args.get(i));
                }

                if (instruction.startsWith("#")) {
                    //Comment
                } else if (instruction.startsWith("@")) {
                    //Call to new script, ignored

                } else {
                    //Minecraft command, or 1-st layer interpreter command
                    sendCommand(instruction);
                }
            }
        }
    }

    public static boolean logicalOperations(String operation) {
        if (operation.contains("==")) {
            return operation.split("==")[0].equals(operation.split("==")[1]); //Return true if the bit before the == is equal to the bit after the ==
        } else if (operation.contains("!=")) {
            return !operation.split("!=")[0].equals(operation.split("!=")[1]); //Return true if the bit before the != is not equal to the bit after the !=
        }
        return false;
    }
}
