package app.admintools.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author lukak
 */
public class AtLogger {
    public static Logger logger = null;

    public static void init(){
        if(logger == null){
            try {
                //%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n
                //"[%1$tF %1$tT] [%6$-10s] [%4$-7s] %5$s %n"
                System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] (%2$s) [%4$-7s] %5$s %n");
                LogManager.getLogManager().readConfiguration();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger = Logger.getLogger(AtLogger.class.getName());
        }
    }
    
    public static String formatException(Exception ex){
        return ex.getClass().getName() + " : " + ex.getMessage();
    }
}
