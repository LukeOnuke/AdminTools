package app.admintools;

import app.admintools.util.Data;
import app.admintools.util.CustomRcon;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.kronos.rkon.core.ex.AuthenticationException;
import app.admintools.textprocessing.TellrawFormatter;
import app.admintools.util.AtLogger;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
*   Image credits 
*   "Gear Icon made by Freepik from www.flaticon.com"
*
 */
/**
 *
 * AdminTools , cool ascii art got messed up
 *
 */
public class RconClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Initilise logger
        Logger logger = Logger.getLogger("ATLOG"); //Create logger instance
        FileHandler fh; //Create writer
        File pathToLogDir = new File("log/"); //Path to log dir
        try {
            if(!pathToLogDir.exists()){
                pathToLogDir.mkdir();
            }
            // This block configure the logger with handler and formatter
            //File name for log file
            String fileName = new SimpleDateFormat("dd-MM-yyyy-@hh-mm-ss").format(new Date());
            fileName = "log-" + fileName + ".log";
            //Initilise the file handeler
            fh = new FileHandler("log/" + fileName);
            logger.addHandler(fh);
            //Formatter for the logger
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            logger.info("Initilising");

        } catch (SecurityException | IOException e) {
            System.err.println("Error in startup");
        }

        Data d = Data.getInstance();

        Parent root;

        //Load homepage
        root = FXMLLoader.load(getClass().getResource("/app/admintools/gui/HomeWindow.fxml"));

        //Set selected theme css
        root.getStylesheets().add("file:Assets/themes/" + d.getSelectedTheme() + "/style.css");

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setOnCloseRequest((event) -> {
            Thread closer = new Thread(() -> {
                Platform.runLater(() -> {
                    stage.hide(); //magic
                });

                try {
                    CustomRcon cr = CustomRcon.getInstance();
                    if (d.getMessageNotify()) {
                        cr.command(TellrawFormatter.assembleLogoutTellraw(d.getMessageUsername()));
                    }
                    cr.disconnect();
                } catch (IOException | AuthenticationException ex) {
                    AtLogger.logException(ex);
                } catch (NullPointerException ex) {
                    //Its normal for it to trow a null pointer exception on exit if no connection is avalable
                }
                Platform.exit();
                System.exit(0);
            });
            closer.setName("Closer Thread");
            closer.start();
        });

        //title
        stage.setTitle("Admin Tools");
        //Setting the icon
        stage.getIcons().add(new Image(RconClient.class.getResourceAsStream("/app/admintools/img/icon.png")));

        stage.show();

        //Setting the max width and max height
        stage.setMinHeight(650.0d);
        stage.setMinWidth(973.0d);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Data.arguments = args;
        launch(args);
    }
}
