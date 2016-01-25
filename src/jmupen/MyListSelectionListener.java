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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author xela92
 */
public class MyListSelectionListener extends MouseAdapter {

    private final JList list;
    private String gamePathToBeOpened;
    private final DefaultListModel model;

    public MyListSelectionListener(JList list, DefaultListModel model) {
        JMupenUtils.setGames(JMupenUtils.getGamesFromFile(JMupenUtils.getRecents()));
        this.list = list;
        this.model = model;
    }

    public String getGamePathToBeOpened() {
        return gamePathToBeOpened;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int index = list.locationToIndex(e.getPoint());
        if (e.getClickCount() == 2) {
            if (list.getSelectedIndex() != -1) {
                System.out.println("INDEX: " + index + " Total:" + JMupenUtils.getGames().size());
                System.out.println("Games: " + JMupenUtils.getGames().get(index));
                System.out.println("Recents file: " + JMupenUtils.getRecents().toString());
                gamePathToBeOpened = JMupenUtils.getGames().get(index).split("\\|")[1];
                File file = new File(gamePathToBeOpened);
                System.out.println("os: " + System.getProperty("os.name"));
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

    @Override
    public void mousePressed(MouseEvent e) {
        list.setSelectedIndex(list.locationToIndex(e.getPoint()));
        int index = list.getSelectedIndex();
        if (SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem item = new JMenuItem("Remove");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (index != -1) {
                        try {
                            System.out.println("Linea: " + index + " " + model.get(index));
                            model.removeElementAt(index);
                            removeLines(index, JMupenUtils.getRecents().toFile());
                            JMupenUtils.setGames(JMupenUtils.getGamesFromFile(JMupenUtils.getRecents()));
                        } catch (IOException ex) {
                            System.err.println("Error removing recent game. " + ex.getLocalizedMessage());
                        }
                    }
                }
            });
            menu.add(item);
            menu.show(list, e.getX(), e.getY());
        }
    }

    private void removeLines(int removeLine, File text) throws IOException {
        List<String> textLines = FileUtils.readLines(text, StandardCharsets.UTF_8);

        textLines.remove(removeLine);

        StringBuilder builder = new StringBuilder();

        for (String line : textLines) {
            builder.append(line).append(System.lineSeparator());
        }

        FileUtils.writeStringToFile(text, builder.toString());

    }

}
