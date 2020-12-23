package app.admintools;

import app.admintools.util.AtLogger;
import app.admintools.util.Data;
import app.admintools.util.Version;

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
        AtLogger.init();
        Version ver = Version.getInstance();
        if(ver.isDevelopmentVersion()){
            AtLogger.logger.info("Is development version, snapshot " + ver.getSnapshotVersion());
        }
        AtLogger.logger.info("AdminTools version : " + ver.getStrippedVersion() + "\n\tAs int : " + ver.getVersionAsInt() + "\n\tAs full : " + ver.getFullVersionNumber());
        AdminToolsLauncher.launchAdminTools(args);
        System.exit(0);
    }
}
