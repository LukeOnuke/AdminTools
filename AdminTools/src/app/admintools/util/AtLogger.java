package app.admintools.util;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lukak
 */
public class AtLogger {
    public static void log(Level level, String message){
        Logger.getLogger("ATLOG").log(level, message);
    }
    
    public static void logException(Exception ex){
        Logger.getLogger("ATLOG").log(Level.SEVERE, "Exception : {0}", Arrays.toString(ex.getStackTrace()));
    }
}
