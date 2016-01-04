/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.io.File;
import java.io.IOException;
import static java.lang.Runtime.getRuntime;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author xela92
 */
public class CoreWin {
     private final Runtime run = getRuntime();
    //private String engine = "glide64";
    private String fullscreen = "";
    private String corepath = "src\\bin\\win";
    private File f;

    public CoreWin(File f) {
        this.f = f;
    }

    public void runGame() {
        try {
            
            Process process = run.exec(new String[]{corepath + "\\mupen64plus-ui-console.exe", fullscreen, f.getAbsolutePath()}, null, new File(corepath));
            //System.out.println("Cmd: "+Arrays.toString(new String[]{corepath + "\\mupen64plus-ui-console.exe", fullscreen, f.getAbsolutePath()}, null, new File(corepath));
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

    
}
