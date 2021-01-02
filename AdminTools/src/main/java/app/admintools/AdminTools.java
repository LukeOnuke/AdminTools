package app.admintools;

import app.admintools.util.AtLogger;
import app.admintools.util.Data;
import app.admintools.util.Version;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/*
 *   Image credits
 *   "Gear Icon made by Freepik from www.flaticon.com"
 *
 */

/**
 * AdminTools , cool ascii art got messed up
 *
 * Admintools by lukeonuke
 */
public class AdminTools {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Data.arguments = args;
        AtLogger.init(); //Initilise at logger

        //Log writer
        FileHandler fh; //Create writer
        File pathToLogDir = new File("log/"); //Path to log dir
        try {
            //See if the log exists
            if(!pathToLogDir.exists()){
                pathToLogDir.mkdir();
            }
            // This block configure the logger with handler and formatter
            //File name for log file
            String fileName = new SimpleDateFormat("dd-MM-yyyy-@hh-mm-ss").format(new Date());
            fileName = "log-" + fileName + ".log";
            //Initilise the file handeler
            fh = new FileHandler("log/" + fileName);
            AtLogger.logger.addHandler(fh);
            //Formatter for the logger
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log initialsation
            AtLogger.logger.info("Initilising");

        } catch (SecurityException | IOException e) {
            System.err.println("Error in startup");
        }

        Version ver = Version.getInstance();
        if(ver.isDevelopmentVersion()){
            AtLogger.logger.info("Is development version, snapshot " + ver.getSnapshotVersion());
        }

        AtLogger.logger.info("\nAdminTools version : " + ver.getStrippedVersion() + "\n\tAs int : " + ver.getVersionAsInt() + "\n\tAs full : " + ver.getFullVersionNumber()+ "\n\tSnapshot: " +ver.getSnapshotVersion());
        AdminToolsLauncher.launchAdminTools(args);
        System.exit(0);
    }
}
