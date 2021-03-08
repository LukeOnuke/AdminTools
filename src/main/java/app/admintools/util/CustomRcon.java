package app.admintools.util;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;
import app.admintools.textprocessing.TellrawFormatter;

public class CustomRcon extends Rcon {

    private static CustomRcon instance;

    private CustomRcon(String host, int port, byte[] password) throws IOException, AuthenticationException {
        super(host, port, password);
    }

    public static CustomRcon getInstance() throws IOException, AuthenticationException {
        if (instance == null) {
            //Variable declaration
            Data data = Data.getInstance();
            String host = data.getSelectedCredentials().getIP();
            int port = data.getSelectedCredentials().getPort();
            byte[] password = data.getSelectedCredentials().getPassword().getBytes(StandardCharsets.UTF_8);

            //Reading configuration from file
            //Constructing the instance
            instance = new CustomRcon(host, port, password);
        }
        //Returns the instance
        return instance;
    }

    public static CustomRcon getInstance(String host, int port, byte[] password) throws IOException, AuthenticationException {
        instance = null;
        instance = new CustomRcon(host, port, password);
        return instance;
    }
    
    public static void setToNull(){
        instance = null;
    }

    //Short-circuit evaluation
    private static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public String command(String payload) throws IOException {
        String uc = "Unknown command";
        Data data = Data.getInstance();
        if (!empty(payload)) {
            if (data.getMessageOverwriteSay()) {
                if (payload.split(" ")[0].equals("say")) {
                    payload = TellrawFormatter.assembleSayTellraw(data.getMessageUsername(), payload.substring(payload.indexOf(" ") + 1));
                }
            }
            String response = super.command(payload);
            if (empty(response)) {
                return "No command response";
            }
            if (response.startsWith(uc)) {
                return uc;
            }
            return response;
        }
        return uc;
    }

    @Override
    public String toString() {
        Socket s = getSocket();
        return s.getInetAddress() + ":" + s.getPort();
    }
}
