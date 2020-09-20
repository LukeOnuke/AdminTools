package rconclient;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.kronos.rkon.core.ex.AuthenticationException;
import rconclient.textprocessing.TellrawFormatter;
import rconclient.util.*;

/*
*   Image credits 
*   "Gear Icon made by Freepik from www.flaticon.com"
*
 */
/**
 *
 * @author lukak
 */
public class RconClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Data d = Data.getInstance();
        Parent root;
        
        //Load login if there is no credentials
        if(new File("rconclient.properties").exists()){
            root = FXMLLoader.load(getClass().getResource("/rconclient/gui/RconWindow.fxml"));
        }else{
            root = FXMLLoader.load(getClass().getResource("/rconclient/gui/LoginWindow.fxml"));
        }

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setOnCloseRequest((event) -> {
            System.out.println("Closing");
            try {
                CustomRcon cr = CustomRcon.getInstance();
                if(d.getMessageNotify()){
                        cr.command(TellrawFormatter.assembleLogoutTellraw(d.getMessageUsername()));
                    }
                cr.disconnect();
            } catch (IOException ex) {

            } catch (AuthenticationException ex) {

            }
            System.exit(0);
        });

        //Setting the title
        String barebonesq = "";
        if (Data.arguments.length > 0) {
            if (Data.arguments[0].equals("barebones")) {
                barebonesq = " [Barebones mode]";
            }
        }
        stage.setTitle("Admin Tools - " + d.getHost() + ":" + d.getPort() + barebonesq);
        //Setting the icon
        stage.getIcons().add(new Image(RconClient.class.getResourceAsStream("/rconclient/image/icon.png")));
        
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
