package rconclient;

import java.io.IOException;
import java.net.SocketException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.kronos.rkon.core.ex.AuthenticationException;
import rconclient.util.*;
import simplefxdialog.Dialog;
import simplefxdialog.img.DialogImage;

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

        Parent root = FXMLLoader.load(getClass().getResource("/rconclient/gui/RconWindow.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setOnCloseRequest((event) -> {
            System.out.println("Closing");
            try {
                CustomRcon.getInstance().disconnect();
            } catch (IOException ex) {

            } catch (AuthenticationException ex) {

            }
            System.exit(0);
        });

        //Setting the title
        Data d = Data.getInstance();
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
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Data.arguments = args;
        launch(args);
    }

}
