/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    private JPanel mainPnl;
    private JList gamelist;
    private DefaultListModel<String> model;
    private ActionListener openFileAction;
    private static JMupenGUI instance;
    private JScrollPane scroll;
    private final ArrayList<String> games;
    private JComponent comp;
    private final static String version = "1.8.0";

    public JMupenGUI() {
        super.setTitle("JMupen N64 " + version);
        instance = JMupenGUI.this;
        initUI();
        games = JMupenUtils.getGamesFromFile(Paths.get(JMupenUtils.getHome() + JMupenUtils.getBar() + "jmupen.recents"));
        JMupenUtils.setGames(games);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initRecentGamesList();
        JMupenUtils.loadParams();
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

        JButton opt = new JButton("Options");
        opt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JMupenOptions.getInstance();
            }
        });

        mainPnl.add(opt);

        //AGGIUNGO TUTTO
        cont.add(mainPnl);
        comp = new JComponent() {
            
        };
        comp.setLayout(new BorderLayout());
        comp.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("\\raw\\spinningwheel.gif"))));
        comp.setVisible(false);
        cont.add(comp);
        this.setVisible(true);
    }
    
    public void showProgress() {
        mainPnl.setVisible(false);
        comp.setVisible(true);
    }
    
    public void hideProgress() {
       mainPnl.setVisible(true);
       comp.setVisible(false);
    }

    public void initRecentGamesList() {
        ArrayList<String> list = new ArrayList<String>();
        for (String game : games) {
            list.add(game.split("\\|")[0]);
        }
        model = new DefaultListModel();
        gamelist = new JList(model);
        for (Object o : list.toArray()) {
            model.addElement((String) o);
        }

        //gamelist.setModel(new DefaultListModel<String>());
        gamelist.setBorder(new TitledBorder("Recent Games"));
        gamelist.addMouseListener(new MyListSelectionListener(gamelist, model));
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

    public DefaultListModel getModel() {
        return model;
    }

    public void showError(String mainMsg, String fullMess) {
        JOptionPane.showMessageDialog(this, mainMsg + "\n Full message: " + fullMess, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
