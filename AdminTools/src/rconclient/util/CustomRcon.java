package rconclient.util;

import java.io.IOException;
import java.net.Socket;
import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

public class CustomRcon extends Rcon {

    private static CustomRcon instance;

    private CustomRcon(String host, int port, byte[] password) throws IOException, AuthenticationException {
        super(host, port, password);
    }

    public static CustomRcon getInstance() throws IOException, AuthenticationException {
        if (instance == null) {

            //Variable declaration
            Data data = Data.getInstance();
            String host = data.getHost();
            int port = data.getPort();
            byte[] password = data.getPassword();

            //Reading configuration from file

            //Constructing the instance
            instance = new CustomRcon(host, port, password);
        }
        //Returns the instance
        return instance;
    }
    
    public static CustomRcon getInstance(String host, int port, byte[] password) throws IOException, AuthenticationException{
        instance = null;
        instance = new CustomRcon(host, port, password);
        return instance;
    }

    //Short-circuit evaluation
    private static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public String command(String payload) throws IOException {
        String uc = "Unknown command";
        if (!empty(payload)) {
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
