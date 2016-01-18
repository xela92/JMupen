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

    private final File workdir;
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
                return f.getAbsoluteFile().toString().toLowerCase().endsWith("v64") || f.getAbsoluteFile().toString().toLowerCase().endsWith("n64") || f.getAbsoluteFile().toString().toLowerCase().endsWith("z64")
                        || f.getAbsoluteFile().toString().toLowerCase().endsWith("v64") || f.getAbsoluteFile().toString().toLowerCase().endsWith("u64")
                        || f.getAbsoluteFile().toString().toLowerCase().endsWith("rom") || f.getAbsoluteFile().toString().toLowerCase().endsWith("pal") || f.getAbsoluteFile().toString().toLowerCase().endsWith("bin") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Nintendo 64 Game File (.*64, .rom, .pal, .bin)";
            }
        });
        chooser.setCurrentDirectory(workdir);
        int result = chooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {

            file = chooser.getSelectedFile();
            JMupenUtils.addRecentGame(file);
            JMupenUtils.writeGamesToFile();
            JMupenGUI.getInstance().showProgress();
            if (JMupenUtils.getOs().equals("lin")) {
                CoreLin c = new CoreLin(file);
                Thread t = new Thread(c);
                t.start();
            } else if (JMupenUtils.getOs().equals("mac")) {
                CoreMac c = new CoreMac(file);
                Thread t = new Thread(c);
                t.start();
            } else {
                CoreWin c = new CoreWin(file);
                Thread t = new Thread(c);
                t.start();
            }
            
        }

    }

}
