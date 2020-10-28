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
import app.admintools.util.Utill;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
        Data d = Data.getInstance();
        Parent root;

        //Load homepage
        root = FXMLLoader.load(getClass().getResource("/app/admintools/gui/HomeWindow.fxml"));


        //Set selected theme css
        root.getStylesheets().add("file:Assets/Themes/" + d.getSelectedTheme() + "/style.css");

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
                } catch (IOException ex) {

                } catch (AuthenticationException ex) {

                } catch(NullPointerException npex){
                    
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
