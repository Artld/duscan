package gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import logic.DuSettings;

public class DuMenuBar extends JMenuBar
{
    private static final long serialVersionUID = 1L;
    private JTextField text;
    private JButton btnPath;
    private JMenuItem itemLog;
    private JMenuItem itemTable;

    public DuMenuBar(ActionListener aToggle) 
    {
        setLayout(null);

        // MENU "CONFIG"

        JMenu menuConfig = new JMenu("Config");
        menuConfig.setBounds(10, 0, 60, 25);
        add(menuConfig);

        menuConfig.add(new JMenuItem(new AbstractAction("Select mode...")
        {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e)
            {
                JPanel panel = new JPanel(new GridLayout(3,1));
                ButtonGroup group = new ButtonGroup();

                String[] names = {"Find duplicates", "Find dupliates among images", "Find images by resolution filter"};
                for (int i=0; i<names.length; i++)
                {
                    JRadioButton radio = new JRadioButton(names[i]);
                    radio.setActionCommand(i+"");
                    group.add(radio);
                    panel.add(radio);
                    if (i == DuSettings.getMode())
                    {
                        radio.setSelected(true);
                    }
                }
                if (JOptionPane.showOptionDialog(null, panel, "Mode", 2, 1, null, null, null) == 0)
                {
                    ButtonModel model = group.getSelection();
                    DuSettings.setMode(Integer.parseInt(model.getActionCommand()));
                }
            }
        }));

        menuConfig.add(new JMenuItem(new AbstractAction("Set number of checkpoints...")
        {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e)
            {
                SpinnerNumberModel numModel = new SpinnerNumberModel(DuSettings.getPoints(),1,100,1);
                JSpinner spin = new JSpinner(numModel);
                if (JOptionPane.showOptionDialog(null, spin, "The number of checkpoints: ", 2, 1, null, null, null)==0)
                {
                    DuSettings.setPoints((Integer) numModel.getNumber());
                }
            }
        }));

        menuConfig.add(new JMenuItem(new AbstractAction("Configure resolution filter...")
        {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e)
            {
                SpinnerNumberModel modelW = new SpinnerNumberModel(DuSettings.getResolutionBorders()[0],0,611392,1);
                SpinnerNumberModel modelH = new SpinnerNumberModel(DuSettings.getResolutionBorders()[1],0,611392,1);
                JSpinner spinW = new JSpinner(modelW);
                JSpinner spinH = new JSpinner(modelH);
                JPanel panel = new JPanel(new GridLayout(2,2));
                panel.add(new JLabel("Find if width less then "));
                panel.add(spinW);
                panel.add(new JLabel("Find if height less than "));
                panel.add(spinH);
                if (JOptionPane.showOptionDialog(null, panel, "Resolution borders (pixels):", 2, 1, null, null, null)==0)
                {
                    DuSettings.setResolutionBorders((Integer) modelW.getNumber(), (Integer) modelH.getNumber());
                }
            }
        }));

        menuConfig.add(new JMenuItem(new AbstractAction("About")
        {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e)
            {
                JLabel label = new JLabel("<html>" +
                        "<br>   Autor: artld " +
                        "<br>   2017" +
                        "<br>   License: BSD" +
                        "</html>");
                JOptionPane.showOptionDialog(null, label, "About", -1, 1, null, null, null);
            }
        }));

        // MENU "VIEW"

        JMenu menuView = new JMenu("View");
        menuView.setBounds(70, 0, 45, 25);
        add(menuView);

        itemLog = new JMenuItem(new AbstractAction("Show log")
        {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e)
            {
                itemLog.setText("Show log    \u2714");
                itemTable.setText("Show table");
            }
        });
        itemLog.addActionListener(aToggle);
        itemLog.setActionCommand("showlog");
        menuView.add(itemLog);

        itemTable = new JMenuItem(new AbstractAction("Show table \u2714")
        {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e)
            {
                itemTable.setText("Show table \u2714");
                itemLog.setText("Show log");
            }
        });
        itemTable.addActionListener(aToggle);
        itemTable.setActionCommand("showtable");
        menuView.add(itemTable);

        // OTHER BAR COMPONENTS

        // Label "Path:"
        JLabel label = new JLabel("Path:");
        label.setFont(new Font("Times New Roman",2,14));
        label.setBounds(125, 0, 30, 25);
        add(label);

        // Text Field to display Path
        text = new JTextField();
        text.setText(DuSettings.getPath());
        text.setFont(new Font("Times New Roman",2,14));
        add(text);

        // Button to edit Path
        btnPath = new JButton("...");
        btnPath.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = fc.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    text.setText(file.getPath());
                }
            }
        });
        add(btnPath);
    }
    public void resizeBarComponents(Rectangle r)
    {
        text.setBounds(155,2,r.width-310,21);
        btnPath.setBounds(r.width-155, 2, 35, 20);
    }
    public String getText()
    {
        return text.getText();
    }
}
