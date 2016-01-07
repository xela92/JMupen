/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupen;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author xela92
 */
public class JMupenOptions extends JFrame {

    private JPanel mainPnl;
    private static JMupenOptions instance;

    public JMupenOptions() {
        super.setTitle("JMupen N64 Options");
        instance = JMupenOptions.this;
        initUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

    }

    public static JMupenOptions getInstance() {
        if (instance == null) {
            instance = new JMupenOptions();
        }
        return instance;
    }

    public void initUI() {
        //creo il contenitore dell'interfaccia e lo dimensiono
        Container cont = getContentPane();
        cont.setLayout(new BoxLayout(cont, BoxLayout.PAGE_AXIS));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dimension.width / 3, dimension.height / 2);
        mainPnl = new JPanel();
        mainPnl.setBorder(new TitledBorder("JMupen Options"));
        mainPnl.setPreferredSize(new Dimension(300, 200));
        mainPnl.setMinimumSize(new Dimension(120, 150));

        JCheckBox fs = new JCheckBox("Fullscreen");
        fs.setSelected(JMupenUtils.getFullscreen());
        mainPnl.add(fs);
        JCheckBox lv = new JCheckBox("Use legacy version");
        lv.setToolTipText("Use this if you have problems running games");
        lv.setSelected(JMupenUtils.getUsingLegacyVersion());
        mainPnl.add(lv);
        JButton btn = new JButton("Save");
        btn.setMinimumSize(new Dimension(200, 200));

        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fs.isSelected()) {
                    System.out.println("Saving: not fullscreen");
                    JMupenUtils.setFullscreen(false);
                } else {
                    JMupenUtils.setFullscreen(true);
                    System.out.println("Saving: fullscreen");
                }

                if (lv.isSelected()) {
                    System.out.println("Saving: legacy version");
                    JMupenUtils.setUsingLegacyVersion(true);
                } else {
                    JMupenUtils.setUsingLegacyVersion(false);
                    System.out.println("Saving: not legacy version");
                }
                JMupenUtils.saveParamChanges();
                dispose();
            }
        });
        mainPnl.add(btn);

        //AGGIUNGO TUTTO
        cont.add(mainPnl);
        this.setVisible(true);
    }

    

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
