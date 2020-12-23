package app.admintools.discord;

import app.admintools.util.AtLogger;
import app.admintools.util.Version;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DRPC {
    public static void initilise(){
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            AtLogger.logger.info( "Connected to discord rpc as " + user.username + ":" + user.discriminator);
        }).build();
        DiscordRPC.discordInitialize("790613792702726185", handlers ,true);
        DiscordRPC.discordRegister("790613792702726185", "");
        DiscordRPC.discordRunCallbacks();
        DiscordRichPresence rich = new DiscordRichPresence.Builder("In home").setDetails("").setStartTimestamps(System.currentTimeMillis()).setBigImage("default", "AdminTools release " + Version.getInstance().getStrippedVersion()).build();
        DiscordRPC.discordUpdatePresence(rich);
    }

    public static void updateStatus(DiscordRichPresence richPresence){
        DiscordRPC.discordUpdatePresence(richPresence);
    }
}
