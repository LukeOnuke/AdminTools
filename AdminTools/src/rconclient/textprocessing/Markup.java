/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.textprocessing;

import javafx.scene.paint.Color;
import rconclient.util.Data;

/**
 *
 * @author lukak
 */
public class Markup {
    public static Color rconReplyMarkup(String text){
        Data dat = Data.getInstance();
        switch(text){
            case "Unknown command":
                return Color.web(dat.getMarkdownErrorColour());
                
            case "No command response":
                return Color.web(dat.getMarkdownNoCommandResponceColour());
                
                
            default:
                return Color.web(dat.getMarkdownSuccsesfullReplyColour());
        
        }
        
    }
}
