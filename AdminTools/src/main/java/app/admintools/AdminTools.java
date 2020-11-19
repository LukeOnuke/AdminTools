package app.admintools;

import app.admintools.util.Data;

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
        AdminToolsLauncher.launchAdminTools(args);
        System.exit(0);
    }
}
