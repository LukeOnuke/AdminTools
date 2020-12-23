package app.admintools;

import app.admintools.gui.splash.SplashScreen;
import app.admintools.textprocessing.TellrawFormatter;
import app.admintools.util.AtLogger;
import app.admintools.util.CustomRcon;
import app.admintools.util.Data;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.kronos.rkon.core.ex.AuthenticationException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Launches the admintools application
 */
public class AdminToolsLauncher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FileHandler fh; //Create writer
        File pathToLogDir = new File("log/"); //Path to log dir
        try {
            //See if the log exists
            if(!pathToLogDir.exists()){
                pathToLogDir.mkdir();
            }
            // This block configure the logger with handler and formatter
            //File name for log file
            String fileName = new SimpleDateFormat("dd-MM-yyyy-@hh-mm-ss").format(new Date());
            fileName = "log-" + fileName + ".log";
            //Initilise the file handeler
            fh = new FileHandler("log/" + fileName);
            AtLogger.logger.addHandler(fh);
            //Formatter for the logger
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log initialsation
            AtLogger.logger.info("Initilising");

        } catch (SecurityException | IOException e) {
            System.err.println("Error in startup");
        }

        Data d = Data.getInstance();

        Parent root;

        //Load homepage
        root = FXMLLoader.load(getClass().getResource("/gui/fxml/HomeWindow.fxml"));

        //Set selected theme css
        root.getStylesheets().add("file:Assets/themes/" + d.getSelectedTheme() + "/style.css");

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setOnCloseRequest((event) -> {
            Thread closer = new Thread(() -> {
                AtLogger.logger.info("Closing");
                //magic
                Platform.runLater(stage::hide);

                try {
                    CustomRcon cr = CustomRcon.getInstance();
                    if (d.getMessageNotify()) {
                        cr.command(TellrawFormatter.assembleLogoutTellraw(d.getMessageUsername()));
                    }
                    cr.disconnect();
                    AtLogger.logger.info( "Successfully disconnected");
                } catch (IOException | AuthenticationException ex) {
                    AtLogger.logger.warning(AtLogger.formatException(ex));
                } catch (NullPointerException ex) {
                    //Its normal for it to trow a null pointer exception on exit if no connection is avalable
                }
                DiscordRPC.discordShutdown();
                AtLogger.logger.info( "Shutdown discordRPC");
                Platform.exit();
                System.exit(0);
            }, "CloserThread");
            closer.start();
        });

        //title
        stage.setTitle("Admin Tools");
        //Setting the icon
        stage.getIcons().add(new Image(AdminTools.class.getResourceAsStream("/gui/icon.png")));



        Stage splash = new Stage(StageStyle.TRANSPARENT);
        splash.setScene(new Scene(new SplashScreen()));
        splash.show();
        Thread splashThread = new Thread(() -> {
            try{
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                AtLogger.logger.warning(AtLogger.formatException(ie));
            }
            Platform.runLater(() -> {
                splash.hide();
                stage.setAlwaysOnTop(true);
                stage.show();
                stage.setAlwaysOnTop(false);
            });
        }, "splash/launcher");
        splashThread.start();

        //Setting the max width and max height
        stage.setMinHeight(650.0d);
        stage.setMinWidth(973.0d);

        //Discord
        AtLogger.logger.info( "Connecting to discord");

        AtLogger.logger.info( "Set rpc presence");
    }

    public static void launchAdminTools(String[] args){
        launch(args);
    }
}
