/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.querry.mc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import javafx.scene.image.Image;

/**
 *
 * @author lukak
 */
public class QuerryUtils {

    public static Image convertToImage(String favicon) {
        favicon = favicon.split(",")[1];
        InputStream in = Base64.getDecoder().wrap(new ByteArrayInputStream(favicon.getBytes()));
        return new Image(in);
    }
}
