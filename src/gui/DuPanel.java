package gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;

import logic.DuSearch;
import logic.DuFileSearch;
import logic.DuImgFilter;
import logic.DuImgSearch;
import logic.DuSettings;
import logic.DuOutputStream;
import logic.DuTableModel;

class DuPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private DuMenuBar bar;
    private DuTableModel model;
    private JScrollPane scroll;
    private JTable table;
    private JTextArea txtArea;
    private JButton btnOpen;
    private JButton btnOpenFolder;
    private JButton btnDelete;
    private JButton btnAutoSelect;
    private JButton btnMirror;
    private JButton btnDeselect;
    private JButton btnStart;
    private JToggleButton btnShowBad;
    private JProgressBar progress;

    DuPanel()
    {
        setLayout(null);

        ActionToggleView aToggle = new ActionToggleView();

        // extended JMenuBar class
        bar = new DuMenuBar(aToggle);
        add(bar);

        // redirect output stream to TextArea
        txtArea = new JTextArea();
        txtArea.setEditable(false);
        DuOutputStream out = new DuOutputStream (txtArea);
        System.setOut (new PrintStream (out));

        model = new DuTableModel();

        // Table to view list of duplicates
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMaxWidth(20);
        table.getColumnModel().getColumn(2).setMaxWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.addMouseListener(model.dClickListener);

        // ScrollPane to contain the table
        scroll = new JScrollPane(txtArea);
        scroll = new JScrollPane(table);
        add(scroll);

        // Open file
        btnOpen = new JButton("Open");
        btnOpen.addActionListener(model.aOpenFile);
        add(btnOpen);

        // Open folder
        btnOpenFolder = new JButton("<html><center>Open folder</center></html>");
        btnOpenFolder.addActionListener(model.aOpenFolder);
        add(btnOpenFolder);

        // Delete selected files
        btnDelete = new JButton("<html><center>Delete selected</center></html>");
        btnDelete.addActionListener(model.aDelete);
        add(btnDelete);

        // Auto-selection 
        btnAutoSelect = new JButton("<html><center>Auto select</center></html>");
        btnAutoSelect.addActionListener(model.aAutoSelect);
        add(btnAutoSelect);

        // Invert selection
        btnMirror = new JButton("<html><center>Mirror selection</center></html>");
        btnMirror.addActionListener(model.aMirror);
        add(btnMirror);

        // Undo selection
        btnDeselect = new JButton("<html><center>Undo selection</center></html>");
        btnDeselect.addActionListener(model.aDeselect);
        add(btnDeselect);

        // Toggle between duplicate list and bad image list show
        btnShowBad = new JToggleButton("<html><center>Show bad files</center></html>");
        btnShowBad.addActionListener(model.aToggleTableContent);
        btnShowBad.setVisible(false);
        add(btnShowBad);

        // ProgressBar
        progress = new JProgressBar();
        progress.setStringPainted(true);
        progress.setVisible(false);
        add(progress);

        // Button to start scan
        btnStart = new JButton("Run");
        btnStart.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (new File(bar.getText()).isDirectory())
                {
                    DuSettings.setPath(bar.getText());
                    txtArea.setText("");

                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnStart.setEnabled(false);

                            DateFormat time = SimpleDateFormat.getTimeInstance();
                            System.out.println("Launched at " + time.format(Calendar.getInstance().getTime()));

                            DuSearch searcher;

                            switch (DuSettings.getMode())
                            {
                                case 0: searcher = new DuFileSearch(progress);  break;
                                case 1: searcher = new DuImgSearch(progress);   break;
                                default: searcher = new DuImgFilter(progress);
                            }

                            model.showList(searcher.getDubList(), searcher.getBadList(), table);

                            // Show/hide button
                            if (searcher.getBadList().size() > 0)
                            {
                                btnShowBad.setVisible(true);
                            }
                            else
                            {
                                btnShowBad.setVisible(false);
                                btnShowBad.setSelected(false);
                            }

                            System.out.println("Finished at " + time.format(Calendar.getInstance().getTime()));
                            btnStart.setEnabled(true);
                        }
                    }).start();
                }
            }
        });
        add(btnStart);
    }
    public void resizePanelComponents(final Rectangle r)
    {
        setBounds(               0,             0,           r.width,       r.height);
        bar.setBounds(           0,             0,           r.width,       25);
        btnOpen.setBounds(       r.width-110,   55,          95,            25);
        btnOpenFolder.setBounds( r.width-110,   90,          95,            35);
        btnDelete.setBounds(     r.width-110,   135,         95,            35);
        btnAutoSelect.setBounds( r.width-110,   200,         95,            35);
        btnMirror.setBounds(     r.width-110,   245,         95,            35);
        btnDeselect.setBounds(   r.width-110,   290,         95,            35);
        btnShowBad.setBounds(    r.width-110,   355,         95,            35);
        scroll.setBounds(        10,            35,          r.width-130,   r.height-105);
        btnStart.setBounds(      r.width/2-105, r.height-65, 120,           25);  
        progress.setBounds(      r.width/2+30,  r.height-65, r.width/2-150, 25);
        bar.resizeBarComponents(r);
        model.fireTableDataChanged();
    }
    public void appendText(String text)
    {
        txtArea.append(text);
    }
    /**Class to toggle between table and textArea showing*/
    private class ActionToggleView implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            switch(e.getActionCommand())
            {
                case "showlog":   scroll.setViewportView(txtArea); break;
                case "showtable": scroll.setViewportView(table);   break;
            }
            scroll.revalidate();
            scroll.repaint();
        }
    }
}
