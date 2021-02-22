/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.admintools.security.credentials;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 *
 * @author lukak
 */
public class CredentialsIO {

    public static final String PATH = "credentials.json";

    public static ArrayList<Credentials> readCredentials() throws IOException {
        Reader reader = Files.newBufferedReader(new File(PATH).toPath());

        ArrayList<Credentials> credentials = (ArrayList<Credentials>) new Gson().fromJson(reader, new TypeToken<ArrayList<Credentials>>() {
        }.getType());

        reader.close();

        return credentials;
    }

    public static void writeCredentials(ArrayList<Credentials> credentials) throws IOException {

        // create a writer
        Writer writer = Files.newBufferedWriter(new File(PATH).toPath());

        //credentials = (ArrayList<Credentials>) gson.fromJson(reader, new TypeToken<ArrayList<Credentials>>() {}.getType());
        new GsonBuilder().setPrettyPrinting().create().toJson(credentials, writer);

        // close writer
        writer.close();
    }
}
