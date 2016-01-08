/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import fr.stevecohen.jarmanager.JarUnpacker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Runtime.getRuntime;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import static jmupen.CoreMac.deleteDirectory;

/**
 *
 * @author xela92
 */
public class CoreWin implements Runnable {

    private final Runtime run = getRuntime();
    //private String engine = "glide64";
    private String fullscreen = JMupenUtils.getFullscreen() == true ? "--fullscreen" : "--windowed";
    private File f;
    private String bin = "bin";
    private Path tmpFolder = Paths.get(System.getProperty("java.io.tmpdir") + "\\jmupen");
    private String corepath;

    public CoreWin(File f) {
        this.f = f;
        if (JMupenUtils.getUsingLegacyVersion()) {
            bin = "bin_legacy";
        } else {
            bin = "bin";
        }
        corepath = tmpFolder.toFile().getAbsolutePath().concat("\\").concat(bin).concat("\\").concat("win");
    }

    public void runGame() {
        try {
            File jarfile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            System.out.println("JAR Path: " + jarfile.getAbsolutePath());
            if (tmpFolder.toFile().exists()) {
                deleteDirectory(tmpFolder.toFile());
            }

            if (jarfile.exists() && !jarfile.isDirectory()) {
                File target = Files.createDirectory(tmpFolder).toFile();
                extractJar(jarfile, target);
                new File(corepath.concat("\\mupen64plus")).setExecutable(true);
            } else {
                System.out.println("URI: " + getClass().getClassLoader().getResource("bin").toString());
                Files.createDirectory(tmpFolder);
                copyProgramToTmp(new File(getClass().getClassLoader().getResource("bin").toURI()), new File(tmpFolder.toFile().getAbsolutePath().concat("\\bin")));
            }

            Process process = run.exec(new String[]{corepath + "\\mupen64plus-ui-console.exe", fullscreen, f.getAbsolutePath()}, null, new File(corepath));
            //System.out.println("Cmd: "+Arrays.toString(new String[]{corepath + "\\mupen64plus-ui-console.exe", fullscreen, f.getAbsolutePath()}, null, new File(corepath));
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
            Scanner errScanner = new Scanner(process.getErrorStream());
            while (errScanner.hasNext()) {
                System.out.println(errScanner.nextLine());
            }
            JMupenGUI.getInstance().hideProgress();

        } catch (IOException ex) {
            JMupenGUI.getInstance().showError("Error opening game or extracting temporary resources. ", ex.getLocalizedMessage());
        } catch (URISyntaxException ex) {
            JMupenGUI.getInstance().showError("Error opening game ", ex.getLocalizedMessage());
        }
    }

    public void copyProgramToTmp(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyProgramToTmp(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        //System.out.println("target: " + targetLocation.getAbsolutePath().concat("\\win\\mupen64plus"));
        //new File(targetLocation.getAbsolutePath().concat("/mac/core/mupen64plus")).setExecutable(true);
    }

    public void setFullscreen() {
        fullscreen = "--fullscreen";
    }

    public void extractJar(File jarFile, File destDir) {
        try {
            System.out.println("Extracting JAR...");
            JarUnpacker jarUnpacker = new JarUnpacker();
            jarUnpacker.unpack(jarFile.getAbsolutePath(), destDir.getAbsolutePath());

        } catch (Exception e) {
            JMupenGUI.getInstance().showError("Error extracting jar", e.getLocalizedMessage());
        }
    }

    /*   public void setGFX(String value) {
     switch (value) {
     case "arach":
     engine = "arachnoid";
     case "rice":
     engine = "rice";
     default:
     engine = "glide64";
     }
     }
     */
    @Override
    public void run() {
        this.runGame();
    }
}
