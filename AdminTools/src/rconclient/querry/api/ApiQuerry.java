/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rconclient.querry.api;

import java.io.IOException;
import java.util.ArrayList;
import rconclient.util.Utill;

/**
 *
 * @author lukak
 */
public class ApiQuerry {
    /**
     * Gets a querry
     * 
     * <b>minecraft.net
     * session.minecraft.net
     * account.mojang.com
     * authserver.mojang.com
     * sessionserver.mojang.com
     * api.mojang.com
     * textures.minecraft.net
     * mojang.com</b>
     * 
     * @throws IOException
     */
    public static ArrayList<String> querry() throws IOException {
        ArrayList<String> data = new ArrayList<String>();
        String querryRaw = Utill.getHTTPRequest("https://status.mojang.com/check");
        //replace all the pescy JSON (delet dis JSON)
        querryRaw = querryRaw.replace("}", "");
        querryRaw = querryRaw.replace("{", "");
        querryRaw = querryRaw.replace("\"", "");
        querryRaw = querryRaw.replace("]", "");
        querryRaw = querryRaw.replace("[", "");
        //replace green with more appetising colour
        querryRaw = querryRaw.replace("green", "yellowgreen");
        String[] splitQuerry = querryRaw.split(",");
        for(String s : splitQuerry){
            data.add(s.split(":")[1]);
        }
        return data;
    }
}
