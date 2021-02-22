package app.admintools.util;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DRPC {
    /**
     * Initialise the discord rich presence.
     */
    public static void initialise(){
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            AtLogger.logger.info( "Connected to discord rpc as " + user.username + ":" + user.discriminator);
        }).build();
        DiscordRPC.discordInitialize("790613792702726185", handlers ,true);
        DiscordRPC.discordRegister("790613792702726185", "");
        DiscordRPC.discordRunCallbacks();
        DiscordRichPresence rich = new DiscordRichPresence.Builder("In home").setDetails("").setStartTimestamps(System.currentTimeMillis()).setBigImage("default", "AdminTools release " + Version.getInstance().getStrippedVersion()).build();
        DiscordRPC.discordUpdatePresence(rich);
    }

    /**
     * Update the discord rich presence status.
     * @param richPresence The next rich presence.
     */
    public static void updateStatus(DiscordRichPresence richPresence){
        DiscordRPC.discordUpdatePresence(richPresence);
        AtLogger.logger.info("Updated status : " + richPresence.details);
    }

    /**
     * Simple generation of a instance of DiscordRichPresence.
     * @param state The current state of the user <i>for example In Home</i>.
     * @param details More details.
     * @return A instance of DiscordRichPresence.
     */
    public static DiscordRichPresence generatePresence(String state, String details){
        String bigImage;
        if(Version.getInstance().isDevelopmentVersion()){
            bigImage = "dev";
        }else {
            bigImage = "default";
        }
        return new DiscordRichPresence.Builder(state).setDetails(details).setStartTimestamps(System.currentTimeMillis()).setBigImage(bigImage, "AdminTools release " + Version.getInstance().getStrippedVersion()).build();
    }

    /**
     * Simple generation of a instance of DiscordRichPresence.
     * @param state The current state of the user <i>for example In Home</i>.
     * @return A instance of DiscordRichPresence.
     */
    public static DiscordRichPresence generatePresence(String state){
        return generatePresence(state, "");
    }

    public static void statusManagingServer() {
        DRPC.updateStatus(DRPC.generatePresence("Managing server"));
    }
}
