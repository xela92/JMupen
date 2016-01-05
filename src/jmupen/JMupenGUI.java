/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author xela92
 */
public class JMupenGUI extends JFrame {

    private final JFrame win = this;
    private JPanel mainPnl;
    private JList gamelist;
    private ActionListener openFileAction;
    private static JMupenGUI instance;
    private JScrollPane scroll;
    private ArrayList<String> games;
    private final static String version = "1.6";

    public JMupenGUI() {
        super.setTitle("JMupen N64 "+version);
        instance = JMupenGUI.this;
        initUI();
        games = JMupenUtils.getGamesFromFile(Paths.get(JMupenUtils.getHome() + JMupenUtils.getBar() + "jmupen.recents"));
        JMupenUtils.setGames(games);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initRecentGamesList();
        pack();

    }

    public static JMupenGUI getInstance() {
        if (instance == null) {
            instance = new JMupenGUI();
        }
        return instance;
    }

    public void initUI() {
        openFileAction = new GameSelectionListener(new File(JMupenUtils.getHome()));
        //creo il contenitore dell'interfaccia e lo dimensiono
        Container cont = getContentPane();
        cont.setLayout(new BoxLayout(cont, BoxLayout.PAGE_AXIS));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dimension.width / 3, dimension.height / 3);
        mainPnl = new JPanel();
        mainPnl.setBorder(new TitledBorder("JMupen Selector"));
        mainPnl.setPreferredSize(new Dimension(300, 200));
        mainPnl.setMinimumSize(new Dimension(120, 150));

        JButton btn = new JButton("Select Game");
        btn.setMinimumSize(new Dimension(200, 200));

        btn.addActionListener(openFileAction);
        mainPnl.add(btn);

        //AGGIUNGO TUTTO
        cont.add(mainPnl);
        this.setVisible(true);
    }

    public void initRecentGamesList() {
        ArrayList<String> list = new ArrayList<String>();
        for (String game : games) {
            list.add(game.split("\\|")[0]);
        }
        gamelist = new JList(list.toArray());
        gamelist.setBorder(new TitledBorder("Recent Games"));
        gamelist.addListSelectionListener(new MyListSelectionListener(gamelist));
        gamelist.setVisible(true);
        mainPnl.add(gamelist);
        if (scroll == null) {
            scroll = new JScrollPane(gamelist,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scroll.setVisible(true);
            this.add(scroll);
            this.setMinimumSize(new Dimension(200, 200));
        }

    }

    public void addRecentGame(File game) {
        games.add(game.getName() + "|" + game.getAbsolutePath());
        JMupenUtils.setGames(games);
        ArrayList<String> list = new ArrayList<String>();
        for (String sgame : games) {
            list.add(sgame.split("\\|")[0]);
        }
        gamelist.setListData(list.toArray());
        if (scroll == null) {
            scroll = new JScrollPane(gamelist,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scroll.setVisible(true);
            this.add(scroll);
            this.setMinimumSize(new Dimension(200, 200));
        }
    }

    public void showError(String mainMsg, String fullMess) {
        JOptionPane.showMessageDialog(this, mainMsg + "\n Full message: " + fullMess, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
