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
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author xela92
 */
public class CoreMac implements Runnable {

    private final Runtime run = getRuntime();
    private String engine = "glide64mk2";
    private String fullscreen = JMupenUtils.getFullscreen() == true ? "--fullscreen" : "--windowed";
    private String bin = "bin";
    private Path tmpFolder = Paths.get("/tmp/jmupen");
    private String corepath;
    private String corelibpath;
    private String pluginpath;
    private String respath;
    private File f;
    private String saveFileDir = "";
    private String saveIntFileDir = "";
    private String set = "";

    public CoreMac(File f) {
        if (JMupenUtils.getUsingLegacyVersion()) {
            bin = "bin_legacy";
            engine = "glide64";
        } else {
            bin = "bin";
        }
        String tmpFolderPath = tmpFolder.toFile().getAbsolutePath();
        corepath = tmpFolderPath.concat("/").concat(bin).concat("/mac/core");
        corelibpath = tmpFolderPath.concat("/").concat(bin).concat("/mac/core/libmupen64plus.dylib");
        pluginpath = corepath;
        respath = tmpFolderPath.concat("/").concat(bin).concat("/mac/res");
        if (JMupenUtils.getSaveFolder() != null && JMupenUtils.getSaveFolder().exists()) {
            saveFileDir = "Core[SaveStatePath]=" + JMupenUtils.getSaveFolder().getAbsolutePath();
            saveIntFileDir = "Core[SaveSRAMPath]=" + JMupenUtils.getSaveFolder().getAbsolutePath();
            set = "--set";
        }
        this.f = f;
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
        System.out.println("target: " + targetLocation.getAbsolutePath().concat("/mac/core/mupen64plus"));
        new File(targetLocation.getAbsolutePath().concat("/mac/core/mupen64plus")).setExecutable(true);
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

    @Override
    public void run() {
        this.runGame();
    }

    public void runGame() {
        try {
            File jarfile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            System.out.println("JAR Path: " + jarfile.getAbsolutePath());
            if (tmpFolder.toFile().exists()) {
                JMupenUtils.deleteDirectory(tmpFolder.toFile());
            }

            if (jarfile.exists() && !jarfile.isDirectory()) {
                File target = Files.createDirectory(tmpFolder).toFile();
                extractJar(jarfile, target);
                new File(corepath.concat("/mupen64plus")).setExecutable(true);
            } else {
                System.out.println("URI: " + getClass().getClassLoader().getResource(bin).toString());
                Files.createDirectory(tmpFolder);
                copyProgramToTmp(new File(getClass().getClassLoader().getResource(bin).toURI()), new File("/tmp/jmupen/" + bin));
            }            
            Process process = run.exec(new String[]{corepath + "/mupen64plus", fullscreen, "--corelib", corelibpath, "--plugindir", pluginpath, "--gfx", "mupen64plus-video-" + engine, "--datadir", respath, set, saveFileDir, set, saveIntFileDir, f.getAbsolutePath()});
            Scanner scanner = new Scanner(process.getErrorStream());
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
            JMupenGUI.getInstance().hideProgress();
        } catch (IOException ex) {
            JMupenGUI.getInstance().showError("Error opening game", ex.getLocalizedMessage());
        } catch (URISyntaxException ex) {
            Logger.getLogger(CoreMac.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setFullscreen() {
        fullscreen = "--fullscreen";
    }

    //ATTENZIONE, meglio non utilizzarlo in quanto gli engine potrebbero cambiare a seconda della versione di mupen.
    public void setGFX(String value) {
        switch (value) {
            case "arach":
                engine = "arachnoid";
            case "rice":
                engine = "rice";
            default:
                engine = "glide64";
        }
    }

}
