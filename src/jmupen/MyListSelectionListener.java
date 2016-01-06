/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.io.File;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author xela92
 */
public class MyListSelectionListener implements ListSelectionListener {

    private JList list;
    private String gamePathToBeOpened;

    public MyListSelectionListener(JList list) {
        this.list = list;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int index = list.getSelectedIndex();
        if (e.getValueIsAdjusting() == false) {
            if (list.getSelectedIndex() != -1) {
                System.out.println("INDEX: " + index + " Total:" + JMupenUtils.getGames().size());
                System.out.println("Games: " + JMupenUtils.getGames().get(index));
                gamePathToBeOpened = JMupenUtils.getGames().get(index).split("\\|")[1];
                File file = new File(gamePathToBeOpened);
                System.out.println("os: "+System.getProperty("os.name"));
                if (JMupenUtils.getOs().equals("lin")) {
                    //TODO create CoreLin
                    CoreMac c = new CoreMac(file);
                    c.runGame();
                } else if (JMupenUtils.getOs().equals("mac")) {
                    CoreMac c = new CoreMac(file);
                    c.runGame();
                } else {
                   CoreWin c = new CoreWin(file);
                    c.runGame();
                }
            }
        }
    }

    public String getGamePathToBeOpened() {
        return gamePathToBeOpened;
    }

}
