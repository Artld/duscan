package gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.*;

import logic.DuFileSearch;
import logic.DuImgSearch;
import logic.DuSearch;
import logic.DuSettings;
import logic.DuOutputStream;
import logic.DuTableModel;

class DuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private DuMenuBar bar;
	private JScrollPane scroll;
	private JTable table;
	private JTextArea txtArea;
	private JButton btnOpen;
	private JButton btnDelete;
	private JButton btnAutoSelect;
	private JButton btnMirror;
	private JButton btnDeselect;
	private JProgressBar progress;
	private JButton btnStart;

	DuPanel()
	{
		setLayout(null);

		//final DuSettings settings = new DuSettings();

		ActionToggleView aToggle = new ActionToggleView();
		
		//extended JMenuBar class
		bar = new DuMenuBar(aToggle);
		add(bar);

		txtArea = new JTextArea();
		txtArea.setEditable(false);
		DuOutputStream out = new DuOutputStream (txtArea);
        System.setOut (new PrintStream (out));
        
		//Table actions
		final DuTableModel model = new DuTableModel();

		//Table to view list of duplicates
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(2).setMaxWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.addMouseListener(model.dClickListener);

		//ScrollPane to hold the Table inside
		scroll = new JScrollPane(txtArea);
		scroll = new JScrollPane(table);
		add(scroll);
		
		//Open file
		btnOpen = new JButton("Open");
		btnOpen.addActionListener(model.aOpen);
		add(btnOpen);

		//Delete selected files
		btnDelete = new JButton("<html><center>Delete selected</center></html>");
		btnDelete.addActionListener(model.aDelete);
		add(btnDelete);

		//Select every first file
		btnAutoSelect = new JButton("<html><center>Auto select</center></html>");
		btnAutoSelect.addActionListener(model.aAutoSelect);
		add(btnAutoSelect);

		//Mirror
		btnMirror = new JButton("<html><center>Mirror selection</center></html>");
		btnMirror.addActionListener(model.aMirror);
		add(btnMirror);

		//Undo Selection
		btnDeselect = new JButton("<html><center>Undo selection</center></html>");
		btnDeselect.addActionListener(model.aDeselect);
		add(btnDeselect);

		//ProgressBar
		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setVisible(false);
		add(progress);

		//Button to start search
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

							DuSearch searcher;
							
							if (DuSettings.isImageSearchMode())
							{
								searcher = new DuImgSearch(progress);
							}
							else
							{
								searcher = new DuFileSearch(progress);
							}
							
							ArrayList<ArrayList<File>> dubList = searcher.getDubList();
							model.showList(dubList,table);

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
		btnDelete.setBounds(     r.width-110,   90,          95,            35);
		btnAutoSelect.setBounds( r.width-110,   200,         95,            35);
		btnMirror.setBounds(     r.width-110,   245,         95,            35);
		btnDeselect.setBounds(   r.width-110,   290,         95,            35);
		scroll.setBounds(        10,            35,          r.width-130,   r.height-105);
		btnStart.setBounds(      r.width/2-105, r.height-65, 120,           25);
		progress.setBounds(      r.width/2+30,  r.height-65, r.width/2-150, 25);
		bar.resizeBarComponents(r);
	}
	public void appendText(String text)
	{
		txtArea.append(text);
	}
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
