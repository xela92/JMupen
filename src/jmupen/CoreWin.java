/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.io.File;
import java.io.IOException;
import static java.lang.Runtime.getRuntime;
import java.util.Scanner;

/**
 *
 * @author xela92
 */
public class CoreWin {
     private final Runtime run = getRuntime();
    private String engine = "glide64";
    private String fullscreen = "";
    private String corepath = "src\\bin\\win";
    //private String corelibpath = "./src/bin/mac/core/libmupen64plus.dylib";
    //private String pluginpath = corepath;
    //private String respath = "./src/bin/mac/res";
    private File f;

    public CoreWin(File f) {
        this.f = f;
    }

    public void runGame() {
        try {
            Process process = run.exec(new String[]{corepath + "\\mupen64plus-ui-console.exe", fullscreen, "--gfx", "mupen64plus-video-" + engine, f.getAbsolutePath()});
            Scanner scanner = new Scanner(process.getErrorStream());
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException ex) {
            JMupenGUI.getInstance().showError("Error opening game", ex.getLocalizedMessage());
        }
    }


    public void setFullscreen() {
        fullscreen = "--fullscreen";
    }

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
