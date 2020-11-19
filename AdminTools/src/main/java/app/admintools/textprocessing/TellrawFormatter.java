package app.admintools.textprocessing;

/**
 *
 * @author lukak
 */
public class TellrawFormatter {

    public static String assembleSimpleTellraw(String message) {
        return "tellraw @a [\"\",{\"text\":\"[Admin Tools]\",\"color\":\"#00FF82\"},{\"text\":\" " + message + "\",\"color\":\"#E1E1E1\"},{\"text\":\" \"}]";
    }

    public static String assembleLoginTellraw(String username) {
        return "tellraw @a [\"\",{\"text\":\"[Admin Tools] \",\"color\":\"#00FF82\"},{\"text\":\"" + username + " \",\"color\":\"aqua\"},{\"text\":\"logged in\",\"color\":\"#E1E1E1\"}]";
    }

    public static String assembleLogoutTellraw(String username) {
        return "tellraw @a [\"\",{\"text\":\"[Admin Tools] \",\"color\":\"#00FF82\"},{\"text\":\"" + username + " \",\"color\":\"aqua\"},{\"text\":\"logged out\",\"color\":\"#E1E1E1\"}]";
    }

    public static String assembleSayTellraw(String username, String message) {
        return "tellraw @a [\"\",{\"text\":\"[Admin Tools] \",\"color\":\"#00FF82\"},{\"text\":\"" + username + " \",\"color\":\"aqua\"},{\"text\":\"said : " + message + "\",\"color\":\"#E1E1E1\"}]";
    }
}
