package app.admintools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lukak
 */
public class Utill {

    public static String getDate() {
        SimpleDateFormat sd = new SimpleDateFormat("[HH:mm:ss] ");
        return sd.format(new Date());
    }

    /**
     * Gets responce of <b>GET</b> request sent to a <b>url</b>
     *
     * @param getUrl The url of the server to witch we send the GET request
     * @return String of what the servers responce was to the get request
     * @throws IOException When there is a IO error
     */
    public static String getHTTPRequest(String getUrl) throws IOException {
        URL obj = new URL(getUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection(); //create HTTP connection
        con.setRequestMethod("GET"); //Send get request

        int responseCode = con.getResponseCode(); //responce code
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //return the results lol
            return response.toString();
        } else {
            throw new IOException();
        }
    }

    public static String removeSpigotFormatting(String commandReply) {
        commandReply = commandReply.replaceAll("[§][1-9a-zA-Z]", ""); //remove numbers and letters

        return commandReply;
    }

    public static void exit(int code) {
        System.exit(code);
    }
    
    /**
     * https://stackoverflow.com/questions/924394/how-to-get-the-filename-without-the-extension-in-java
     * @param str
     * @return
     */
    public static String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }
    
    public static String removeArrrayFormatting(String string){
        return string.replace(",", "").replace("[", "").replace("]", "");
    }
}
