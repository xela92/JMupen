/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author xela92
 */
public class MyListSelectionListener extends MouseAdapter {

    private JList list;
    private String gamePathToBeOpened;
    private final DefaultListModel model;

    public MyListSelectionListener(JList list, DefaultListModel model) {
        this.list = list;
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int index = list.locationToIndex(e.getPoint());
        if (e.getClickCount() == 2 ) {
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
    
      @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                JPopupMenu menu = new JPopupMenu();
                JMenuItem item = new JMenuItem("Remove");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.removeElementAt(list.getSelectedIndex());
                    }
                });
                menu.add(item);
                list.setSelectedIndex(list.locationToIndex(e.getPoint()));
                menu.show(list, e.getX(), e.getY());
            }
        }

    public String getGamePathToBeOpened() {
        return gamePathToBeOpened;
    }

}
