package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import logic.DefaultSettings;
import logic.Searcher;
import logic.TableModel;

class DuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	DuPanel()
	{
		final DefaultSettings defSet = new DefaultSettings();

		setBounds(10, 10, defSet.getFrameWidth()-25, defSet.getFrameHeigth()-50);
		setLayout(null);

		//Label "Path:"
		JLabel label1 = new JLabel("Path:");
		label1.setBounds(10, 20, 40, 20);
		add(label1);

		//Text Field to display Path
		final JTextField tf1 = new JTextField();		
		tf1.setText(defSet.getPath());
		tf1.setBounds(50, 20, defSet.getFrameWidth()-140, 20);
		add(tf1);

		//Button to edit Path
		JButton btnPath = new JButton("...");
		btnPath.setBounds(defSet.getFrameWidth()-85,20,50,20);
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
					tf1.setText(file.getPath());
				}
			}
		});
		add(btnPath);
		
		//Table actions
		final TableModel tm = new TableModel();

		//Table to view list of duplicates
		final JTable tbl = new JTable(tm);
		tbl.getColumnModel().getColumn(1).setMaxWidth(250);
		tbl.getColumnModel().getColumn(1).setPreferredWidth(150);
		tbl.addMouseListener(tm.dClickListener);
		
		//ScrollPane to hold the Table inside
		JScrollPane scr = new JScrollPane(tbl);
		scr.setBounds(10, 50, defSet.getFrameWidth()-145, defSet.getFrameHeigth()-145);
		add(scr);

		//Open file
		JButton btnOpen = new JButton("Open");
		btnOpen.setBounds(defSet.getFrameWidth()-125,70,95,25);
		btnOpen.addActionListener(tm.aOpen);
		add(btnOpen);

		//Delete file
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(defSet.getFrameWidth()-125,105,95,25);
		btnDelete.addActionListener(tm.aDelete);
		add(btnDelete);

		//ShowZeroFiles button
		JToggleButton btnShowZero = new JToggleButton("Empty files");
		btnShowZero.setFont(new Font("Arial",1,11));
		btnShowZero.setBounds(defSet.getFrameWidth()-125,140,95,25);
		btnShowZero.addActionListener(tm.aToggleShow);
		add(btnShowZero);

		//Checkbox
		final JCheckBox chbox = new JCheckBox("ImageSearch mode");
		chbox.setBounds(10, defSet.getFrameHeigth()-85, 160, 25);
		chbox.setSelected(defSet.isImageSearchMode());
		add(chbox);

		//ProgressBar
		final JProgressBar prBar = new JProgressBar();
		prBar.setBounds(defSet.getFrameWidth()/2+10, defSet.getFrameHeigth()-85, 205, 25);
		prBar.setStringPainted(true);
		prBar.setVisible(false);
		add(prBar);

		//Button to start search
		JButton btnStart = new JButton("Run");
		btnStart.setBounds(defSet.getFrameWidth()/2-120,defSet.getFrameHeigth()-85,120,25);
		btnStart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (new File(tf1.getText()).isDirectory())
				{
					defSet.setPath(tf1.getText());
					if (chbox.isSelected())
					{
						defSet.setImageSearchMode(true);
					}
					Searcher sear = new Searcher(defSet, prBar);
					
					ArrayList<ArrayList<File>> bigList = sear.getDubList();
					ArrayList<File> zeroList = sear.getZeroList();
					tm.showList(bigList,zeroList, tbl);
				}
			}
		});
		add(btnStart);
	}
}
