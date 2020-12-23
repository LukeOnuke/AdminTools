package app.admintools.gui.splash;

import app.admintools.util.Data;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class SplashScreen extends AnchorPane {
    ProgressBar progressBar = new ProgressBar();
    AnchorPane windowArea = new AnchorPane();

    public SplashScreen() {
        this.getChildren().add(progressBar);
        this.getChildren().add(windowArea);
        setup();
    }

    private SplashScreen(Node... children) {
    }

    private void setup() {
        this.getStylesheets().add("file:Assets/themes/" + Data.getInstance().getSelectedTheme() + "/style.css");
        this.setPrefHeight(300d);
        this.setPrefWidth(510d);
        AnchorPane.setBottomAnchor(progressBar, 0d);
        AnchorPane.setLeftAnchor(progressBar, 0d);
        AnchorPane.setRightAnchor(progressBar, 0d);

        AnchorPane.setRightAnchor(windowArea, 0d);
        AnchorPane.setLeftAnchor(windowArea, 0d);
        AnchorPane.setTopAnchor(windowArea, 0d);
        AnchorPane.setBottomAnchor(windowArea, 20d);
        windowArea.setStyle("-fx-background-color: white;");

        // new Image(url)
        Image image = new Image("file:Assets/img/splash_screen.png", this.getPrefWidth(), this.getPrefHeight() - 20, false, true);

        ImageView img = new ImageView(image);
        img.setSmooth(true);
        windowArea.getChildren().add(img);

        this.getStyleClass().add("splash");
    }
}
