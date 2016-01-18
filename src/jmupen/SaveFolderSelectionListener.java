/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author xela92
 */
public class SaveFolderSelectionListener extends JFrame implements ActionListener {

    private final File workdir;
    private File file;

    public SaveFolderSelectionListener(File workdir) {
        this.workdir = workdir;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.getAbsoluteFile().toString() == null) {
                    return false;
                }
                return f.isDirectory() && f.canRead() && f.canWrite();

            }

            @Override
            public String getDescription() {
                return "Folder for save files";
            }
        });
        chooser.setCurrentDirectory(workdir);
        int result = chooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {

            file = chooser.getSelectedFile();
            JMupenUtils.setSaveFolder(file);

        }

    }

}
