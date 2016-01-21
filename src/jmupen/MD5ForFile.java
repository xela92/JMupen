/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author xela92
 */
public class MD5ForFile {

    private static MessageDigest md;

    public static String getDigest(InputStream is, int byteArraySize) {
        String result = "";
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Impossible error. " + e.getLocalizedMessage());
        }
        byte[] bytes = new byte[byteArraySize];
        int numBytes;
        try {
            while ((numBytes = is.read(bytes)) != -1) {
                md.update(bytes, 0, numBytes);
            }
            byte[] digest = md.digest();
            result = new BigInteger(1, digest).toString(16);
        } catch (IOException e) {
            System.err.println("Error reading update file for hashing. " + e.getLocalizedMessage());
        }
        return result;
    }

    public static String getMd5FromUrl(URLConnection connection) {
        InputStream input = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int length = connection.getContentLength();
            input = connection.getInputStream();
            if (input != null) {
                byte[] buffer = new byte[length];
                int n = - 1;
                while ((n = input.read(buffer)) != -1) {
                    baos.write(buffer, 0, n);
                }

                input.close();
            }
        } catch (IOException ex) {
            System.err.println("Server MD5 not available. " + ex.getLocalizedMessage());
            return "";
        }
        return new String(baos.toByteArray(), Charset.defaultCharset());
    }

}
