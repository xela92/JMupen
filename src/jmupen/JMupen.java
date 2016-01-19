/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xela92
 */
public class JMupen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new JMupenGUI();
        JMupenUpdater.checkForUpdates();
    }

}
