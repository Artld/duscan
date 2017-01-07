package gui;

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
		final DefaultSettings settings = new DefaultSettings();

		setBounds(10, 10, settings.getFrameWidth()-25, settings.getFrameHeigth()-50);
		setLayout(null);

		//Label "Path:"
		JLabel label1 = new JLabel("Path:");
		label1.setBounds(10, 20, 40, 20);
		add(label1);

		//Text Field to display Path
		final JTextField tf1 = new JTextField();		
		tf1.setText(settings.getPath());
		tf1.setBounds(50, 20, settings.getFrameWidth()-140, 20);
		add(tf1);

		//Button to edit Path
		JButton btnPath = new JButton("...");
		btnPath.setBounds(settings.getFrameWidth()-85,20,50,20);
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
		final TableModel model = new TableModel();

		//Table to view list of duplicates
		final JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(2).setMaxWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.addMouseListener(model.dClickListener);
		
		//ScrollPane to hold the Table inside
		JScrollPane scr = new JScrollPane(table);
		scr.setBounds(10, 50, settings.getFrameWidth()-145, settings.getFrameHeigth()-145);
		add(scr);

		//Open file
		JButton btnOpen = new JButton("Open");
		btnOpen.setBounds(settings.getFrameWidth()-125,70,95,25);
		btnOpen.addActionListener(model.aOpen);
		add(btnOpen);
		
		//Delete selected files
		JButton btnDelete = new JButton("<html><center>Delete checked</center></html>");
		btnDelete.setBounds(settings.getFrameWidth()-125,105,95,35);
		btnDelete.addActionListener(model.aDelete);
		add(btnDelete);

		//Mode selection
		final JCheckBox chbox = new JCheckBox("ImageSearch mode");
		chbox.setBounds(10, settings.getFrameHeigth()-85, 160, 25);
		chbox.setSelected(settings.isImageSearchMode());
		add(chbox);

		//ProgressBar
		final JProgressBar progress = new JProgressBar();
		progress.setBounds(settings.getFrameWidth()/2+10, settings.getFrameHeigth()-85, 205, 25);
		progress.setStringPainted(true);
		progress.setVisible(false);
		add(progress);

		//Button to start search
		JButton btnStart = new JButton("Run");
		btnStart.setBounds(settings.getFrameWidth()/2-120,settings.getFrameHeigth()-85,120,25);
		btnStart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (new File(tf1.getText()).isDirectory())
				{
					settings.setPath(tf1.getText());
					if (chbox.isSelected())
					{
						settings.setImageSearchMode(true);
					}
					
					new Thread(new Runnable()
					{
					    @Override
					    public void run()
					    {
					    	Searcher searcher = new Searcher(settings, progress);
					    	ArrayList<ArrayList<File>> bigList = searcher.getDubList();
					    	model.showList(bigList,table);
					    }
					}).start();
				}
			}
		});
		add(btnStart);
	}
}
