package rconclient.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Mosaic algorithm for saving and writing encripred files
 *
 * @author lukak
 */
public class Mozaic {

    private final static String META_FILE_PATH = "meta.encdat";
    private final static String DATA_FILE_PATH = "prop.encdat";

    public static byte[] readMetaData() {
        try {
            return readBinary(META_FILE_PATH);
        } catch (IOException ex) {
            byte[] key = generateKey();
            try {
                writeMetaData(key);
            } catch (IOException ex1) {

            }
            return key;
        }
    }

    public static void writeMetaData(byte[] data) throws IOException {
        writeBinary(META_FILE_PATH, data);
    }

    public static byte[] generateKey() {
        String protoKey = "";
        for (int i = 0; i < 16; i++) {
            Random rand = new Random();
            protoKey += rand.nextInt(10);
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(protoKey.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Mozaic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "0-sadwa546aAD4a87da3sAD".getBytes();
    }

    public static String read() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] data = readBinary(DATA_FILE_PATH);

        SecureIO sec = new SecureIO();

        data = sec.decrypt(data, readMetaData());
        return new String(data);
    }

    public static void write(String propsToWrite) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, IOException {
        SecureIO sec = new SecureIO();

        writeBinary(DATA_FILE_PATH, sec.encrypt(propsToWrite.getBytes(), readMetaData()));
    }

    public static byte[] readBinary(String filePath) throws IOException {
        File file = new File(filePath);
        return Files.readAllBytes(file.toPath());
    }

    public static void writeBinary(String filePath, byte[] data) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }
}
