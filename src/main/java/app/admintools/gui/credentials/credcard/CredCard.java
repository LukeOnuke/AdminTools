/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.gui.credentials.credcard;

import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;
import app.admintools.gui.credentials.credwizard.CredWizard;
import app.admintools.security.credentials.Credentials;
import app.admintools.security.credentials.CredentialsIO;
import app.admintools.util.AtLogger;
import app.admintools.util.CustomRcon;
import app.admintools.util.Data;
import app.admintools.util.WindowLoader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author lukak
 */
public class CredCard {

    /**
     * Creates the credcards that are used in the loginscreen
     *
     * @param titleContents
     * @param ip
     * @param credentials
     * @return
     */
    public static AnchorPane createCredCard(String titleContents, String ip, Credentials credentials) {
        AnchorPane ap = new AnchorPane();
        Label title;
        Text minus;
        Label ipField;
        SVGPath editIcon;
        AnchorPane edit;
        AtomicBoolean isOpenWindow = new AtomicBoolean();

        title = new Label();
        minus = new Text();
        ipField = new Label();
        editIcon = new SVGPath();
        edit = new AnchorPane();

        ap.setPrefHeight(115.0);
        ap.setPrefWidth(370.0);
        ap.getStyleClass().add("loginCard");
        ap.setId("credCard");

        DropShadow dropShadow = new DropShadow(0, Color.web("#1f1f1f"));
        dropShadow.setOffsetX(0d);
        dropShadow.setOffsetY(0d);
        ap.setEffect(dropShadow);

        //Create the hovershadow effects
        Timeline dropShadowDrop = new Timeline(new KeyFrame(Duration.seconds(.35d), new KeyValue(dropShadow.radiusProperty(), 20.0)));
        Timeline dropShadowRemove = new Timeline(new KeyFrame(Duration.seconds(.35d), new KeyValue(dropShadow.radiusProperty(), 0.0)));

        //Display the events on mouse hover events
        ap.setOnMouseEntered(me -> {
            if (dropShadowRemove.getStatus() == Timeline.Status.RUNNING) {
                dropShadowRemove.stop();
            }
            dropShadowDrop.play();
        });
        ap.setOnMouseExited(me -> {
            if (dropShadowDrop.getStatus() == Timeline.Status.RUNNING) {
                dropShadowDrop.stop();
            }
            dropShadowRemove.play();
        });

        ap.setOnMouseClicked(me -> {
            String id = "credCard";
            if (((Node) me.getTarget()).getId() != null) {
                id = ((Node) me.getTarget()).getId();
            }
            if (id.equals("minus")) {
                try {
                    ArrayList<Credentials> creds = CredentialsIO.readCredentials();
                    creds.remove(credentials);
                    CredentialsIO.writeCredentials(creds);
                    ((FlowPane) ap.getParent()).getChildren().remove(ap);
                } catch (IOException ex) {
                    AtLogger.logger.warning(AtLogger.formatException(ex));
                }
            } else if (id.equals("edit")) {
                if (!isOpenWindow.get()) {
                    isOpenWindow.set(true);
                    //Show credentials wizard
                    Credentials editedCreds = CredWizard.showCredWizard(credentials);
                    if (editedCreds != null) {
                        try {
                            FlowPane parrent = (FlowPane) ap.getParent();
                            ArrayList<Credentials> creds = CredentialsIO.readCredentials();
                            creds.remove(credentials); //Remove the old one (this one)
                            creds.add(editedCreds); //Add the edited one (new one)
                            CredentialsIO.writeCredentials(creds);
                            parrent.getChildren().remove(ap); //Remove this card
                            //Display new card
                            parrent.getChildren().add(editedCreds.getCredCard());
                        } catch (IOException ex) {
                            AtLogger.logger.warning(AtLogger.formatException(ex));
                        }
                    }
                    isOpenWindow.set(false);
                }

            } else {
                //Nullify the current custom rcon instance
                CustomRcon.setToNull();
                //set tje credentials
                Data.getInstance().setSelectedCredentials(credentials);
                WindowLoader.loadRcon(((AnchorPane) ap.getScene().getRoot()));
            }
        });

        AnchorPane.setLeftAnchor(title, 4.0);
        AnchorPane.setTopAnchor(title, 4.0);
        title.setLayoutX(3.0);
        title.setLayoutY(4.0);
        title.setPrefHeight(30.0);
        title.setPrefWidth(319.0);

        title.getStyleClass().add("ctitle");
        title.setText(titleContents);

        AnchorPane.setRightAnchor(minus, 17.0);
        AnchorPane.setTopAnchor(minus, 24.0);
        minus.setBoundsType(javafx.scene.text.TextBoundsType.VISUAL);

        minus.setLayoutX(334.0);
        minus.setLayoutY(39.5029296875);
        minus.setLineSpacing(2.0);
        minus.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);

        minus.setStrokeWidth(0.0);
        minus.getStyleClass().add("plus");
        minus.setText("-");
        minus.setWrappingWidth(13.330078125);
        minus.setId("minus");

        ipField.setLayoutX(13.0);
        ipField.setLayoutY(57.0);
        ipField.setPrefHeight(30.0);
        ipField.setPrefWidth(334.0);
        ipField.getStyleClass().add("text");
        ipField.setText(ip);
        ipField.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        editIcon.setContent("M 13.96875 12.125 L 15.078125 11 C 15.253906 10.824219 15.554688 10.949219 15.554688 11.199219 L 15.554688 16.3125 C 15.554688 17.242188 14.808594 18 13.890625 18 L 1.667969 18 C 0.746094 18 0 17.242188 0 16.3125 L 0 3.9375 C 0 3.007812 0.746094 2.25 1.667969 2.25 L 11.164062 2.25 C 11.410156 2.25 11.535156 2.550781 11.359375 2.730469 L 10.25 3.855469 C 10.199219 3.910156 10.128906 3.9375 10.050781 3.9375 L 1.667969 3.9375 L 1.667969 16.3125 L 13.890625 16.3125 L 13.890625 12.320312 C 13.890625 12.25 13.917969 12.179688 13.96875 12.125 Z M 19.40625 5.03125 L 10.289062 14.261719 L 7.148438 14.613281 C 6.238281 14.714844 5.464844 13.9375 5.566406 13.011719 L 5.914062 9.832031 L 15.03125 0.601562 C 15.828125 -0.203125 17.109375 -0.203125 17.902344 0.601562 L 19.402344 2.121094 C 20.199219 2.925781 20.199219 4.230469 19.40625 5.03125 Z M 15.976562 6.117188 L 13.957031 4.074219 L 7.507812 10.609375 L 7.253906 12.90625 L 9.519531 12.648438 Z M 18.226562 3.316406 L 16.726562 1.796875 C 16.582031 1.652344 16.351562 1.652344 16.210938 1.796875 L 15.140625 2.882812 L 17.15625 4.925781 L 18.230469 3.839844 C 18.367188 3.691406 18.367188 3.460938 18.226562 3.316406 Z M 18.226562 3.316406");
        editIcon.getStyleClass().add("plus");
        editIcon.setId("edit");

        edit.getChildren().add(editIcon);
        edit.getStyleClass().add("edit");
        AnchorPane.setRightAnchor(edit, 37.0);
        AnchorPane.setTopAnchor(edit, 15.0);
        edit.setId("edit");

        ap.getChildren().add(title);
        ap.getChildren().add(minus);
        ap.getChildren().add(ipField);
        ap.getChildren().add(edit);

        return ap;
    }
}
