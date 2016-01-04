/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

/**
 *
 * @author xela92
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class GameSelectionListener extends JFrame implements ActionListener {

    private File workdir;
    private File file;

    public GameSelectionListener(File workdir) {
        this.workdir = workdir;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.getAbsoluteFile().toString() == null) {
                    return false;
                }
                return f.getAbsoluteFile().toString().endsWith("v64") || f.getAbsoluteFile().toString().endsWith("n64") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Nintendo 64 Game File (.n64 or .v64)";
            }
        });
        chooser.setCurrentDirectory(workdir);
        int result = chooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {

            file = chooser.getSelectedFile();
            JMupenGUI.getInstance().addRecentGame(file);
            JMupenUtils.writeGamesToFile();
            
            if (JMupenUtils.getOs().equals("win")) {
                CoreWin c = new CoreWin(file);
                c.setFullscreen();
                c.runGame();
            } else if (JMupenUtils.getOs().equals("mac")) {
                Core c = new Core(file);
                c.setFullscreen();
                c.runGame();
            } else {
                Core c = new Core(file);
                c.setFullscreen();
                c.runGame();
            }

        }

    }

}
