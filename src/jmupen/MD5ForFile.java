/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

   /* public static String getMd5FromUrl(URLConnection connection) {
        InputStream input = null;
        try {
            input = connection.getInputStream();

            byte[] buffer = new byte[4096];
            int n = - 1;
            while ((n = input.read(buffer)) != -1) {
                .write(buffer, 0, n);
            }
            output.close();
        } catch (IOException ex) {
            System.err.println("Server MD5 not available. " + ex.getLocalizedMessage());
            return "";
        }
        return
    }
    */

}
