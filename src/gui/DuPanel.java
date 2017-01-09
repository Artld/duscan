package gui;

import java.awt.Rectangle;
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
	private DuMenuBar bar;
	private JLabel label;
	private JTextField text;
	private JScrollPane scroll;
	private JButton btnPath;
	private JButton btnOpen;
	private JButton btnDelete;
	private JProgressBar progress;
	private JButton btnStart;
	
	DuPanel()
	{
		setLayout(null);
		
		final DefaultSettings settings = new DefaultSettings();
		
		bar = new DuMenuBar(settings);
		add(bar);
		
		//Label "Path:"
		label = new JLabel("Path:");
		add(label);

		//Text Field to display Path
		text = new JTextField();		
		text.setText(settings.getPath());
		add(text);

		//Button to edit Path
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

		//Table actions
		final TableModel model = new TableModel();

		//Table to view list of duplicates
		final JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(2).setMaxWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.addMouseListener(model.dClickListener);

		//ScrollPane to hold the Table inside
		scroll = new JScrollPane(table);
		add(scroll);

		//Open file
		btnOpen = new JButton("Open");
		btnOpen.addActionListener(model.aOpen);
		add(btnOpen);

		//Delete selected files
		btnDelete = new JButton("<html><center>Delete checked</center></html>");
		btnDelete.addActionListener(model.aDelete);
		add(btnDelete);
		
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
				if (new File(text.getText()).isDirectory())
				{
					settings.setPath(text.getText());

					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							btnStart.setEnabled(false);

							Searcher searcher = new Searcher(settings, progress);
							ArrayList<ArrayList<File>> bigList = searcher.getDubList();
							model.showList(bigList,table);

							btnStart.setEnabled(true);
						}
					}).start();
				}
			}
		});
		add(btnStart);
	}

	public void resizePanel(final Rectangle r)
	{
		setBounds(          0,             0,           r.width,       r.height);
		label.setBounds(    10,            30,          40,            20);
		bar.setBounds(      0,             0,           r.width,       20);
		text.setBounds(     50,            30,          r.width-125,   20);
		btnPath.setBounds(  r.width-65,    30,          50,            20);
		btnOpen.setBounds(  r.width-110,   80,          95,            25);
		btnDelete.setBounds(r.width-110,   115,         95,            35);
		scroll.setBounds(   10,            60,          r.width-130,   r.height-130);
		btnStart.setBounds( r.width/2-100, r.height-65, 120,           25);
		progress.setBounds( r.width/2+30,  r.height-65, r.width/2-150, 25);
	}
}
