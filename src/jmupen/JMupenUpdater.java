/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author xela92
 */
public class JMupenUpdater {

    private static final String JarURL = "http://xela92.linuxzogno.org/JMupen/JMupen.jar";
    private static boolean update_available = false;
    private static File jarFile = null;
    private static File updatePackage = null;

    public static void checkForUpdates() {
        try {
            jarFile = getJarFile();
        } catch (URISyntaxException ex) {
            System.err.println("Error getting jarfile path. " + ex.getLocalizedMessage());
            return;
        }
        try {
            if (jarFile != null) {
                // Get URL connection and lastModified time
                URL url = new URL(JarURL);
                URLConnection connection = url.openConnection();
                long localMod = jarFile.lastModified(), onlineMod = connection.getLastModified();
                if (localMod >= onlineMod) {
                    System.out.println("No update available at " + JarURL + '(' + localMod + '>' + onlineMod + ')');
                    JMupenUpdater.setUpdateAvailable(false);
                    return;
                } else {
                    JMupenUpdater.setUpdateAvailable(true);
                }

                System.out.println("Loading update from " + JarURL);
                byte bytes[] = getBytes(connection);
                System.out.println("Update loaded");
                updatePackage = new File(JMupenUtils.getConfigDir().concat(JMupenUtils.getBar()).concat("jmupen-update.jar"));
                writeBytes(updatePackage, bytes);
                System.out.println("Update saved: " + updatePackage.getAbsolutePath());

                //jarFile.setLastModified(onlineMod);
                JMupenGUI.getInstance().showUpdateDialog();
            }
        } catch (Exception e) {
            System.err.println("Error updating JMupen. "+e.getLocalizedMessage());
        }
    }

    /**
     * Returns bytes for connection.
     *
     * @param aConnection
     * @return bytes
     * @throws java.io.IOException
     */
    public static byte[] getBytes(URLConnection aConnection) throws IOException {
        InputStream stream = aConnection.getInputStream(); // Get stream for connection
        byte bytes[] = getBytes(stream); // Get bytes for stream
        stream.close();  // Close stream
        return bytes;  // Return bytes
    }

    /**
     * Returns bytes for an input stream.
     *
     * @param aStream
     * @return bytes
     * @throws java.io.IOException
     */
    public static byte[] getBytes(InputStream aStream) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        byte chunk[] = new byte[8192];
        for (int len = aStream.read(chunk, 0, 8192); len > 0; len = aStream.read(chunk, 0, 8192)) {
            bs.write(chunk, 0, len);
        }
        return bs.toByteArray();
    }

    private static File getJarFile() throws URISyntaxException {
        System.out.println(JMupenUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        return new File(JMupenUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }

    public static boolean getUpdateAvailable() {
        return update_available;
    }

    public static void installUpdate() {
        jarFile.delete();
        try {
            FileUtils.moveFile(updatePackage, jarFile);
        } catch (IOException ex) {
            System.err.println("Error moving file. You can find updated file at " + JMupenUtils.getConfigDir());
            JMupenGUI.getInstance().showError("Error updating JMupen.", "I couldn't move update file in place. You can find the app file in " + JMupenUtils.getConfigDir() + " folder.");
        }
    }

    public static void restartApplication() {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        File currentJar = null;
        try {
            currentJar = getJarFile();
        } catch (URISyntaxException ex) {
            System.err.println("Errore - url del jar malformato " + ex.getLocalizedMessage());
        }

        /* is it a jar file? */
        if (!currentJar.getName().endsWith(".jar")) {
            return;
        }

        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException ex) {
            System.err.println("Errore nell'esecuzione del riavvio " + ex.getLocalizedMessage());
        }
        System.exit(0);
    }

    private static void setUpdateAvailable(boolean b) {
        update_available = b;
    }

    /**
     * Writes the given bytes (within the specified range) to the given file.
     *
     * @param aFile File where bytes will be written to
     * @param theBytes The bytes to write
     * @throws java.io.IOException
     */
    public static void writeBytes(File aFile, byte theBytes[]) throws IOException {
        if (theBytes == null) {
            aFile.delete();
            return;
        }
        FileOutputStream fileStream = new FileOutputStream(aFile);
        fileStream.write(theBytes);
        fileStream.close();
    }

}
