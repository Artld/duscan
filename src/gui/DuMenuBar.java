package gui;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import logic.DuSettings;

public class DuMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	private JTextField text;
	private JButton btnPath;
	private JMenuItem itemImageMode;
	private JMenuItem itemLog;
	private JMenuItem itemTable;
	
	public DuMenuBar(ActionListener aToggle) 
	{
		setLayout(null);

		//MENU "CONFIG"
		
		JMenu menuConfig = new JMenu("Config");
		menuConfig.setBounds(10, 0, 60, 25);
		add(menuConfig);

		itemImageMode = new JMenuItem(new AbstractAction()
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e)
			{
				DuSettings.setImageSearchMode(!DuSettings.isImageSearchMode());
				updateItemText();
			}
		});
		updateItemText();
		menuConfig.add(itemImageMode);
		
		JMenuItem itemMinSize = new JMenuItem(new AbstractAction("Specify minimum file size...")
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e)
			{
				SpinnerNumberModel numModel = new SpinnerNumberModel(DuSettings.getMinFileLength()/1024,0,611392,1);
				JSpinner spin = new JSpinner(numModel);
				if (JOptionPane.showOptionDialog(null, spin, " Minimum file size (kb): ", 2, 1, null, null, null)==0)
				{
					DuSettings.setMinFileLength(1024 * (Integer) numModel.getNumber());
				}
			}
		});
		menuConfig.add(itemMinSize);
		
		JMenuItem itemPointsNum = new JMenuItem(new AbstractAction("Specify number of checkpoints...")
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
		});
		menuConfig.add(itemPointsNum);
		
		JMenuItem itemAbout = new JMenuItem(new AbstractAction("About")
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
		});
		menuConfig.add(itemAbout);
		
		//MENU "VIEW"
		
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
		
		//OTHER BAR COMPONENTS
		
		//Label "Path:"
		JLabel label = new JLabel("Path:");
		label.setFont(new Font("Times New Roman",2,14));
		label.setBounds(125, 0, 30, 25);
		add(label);

		//Text Field to display Path
		text = new JTextField();
		text.setText(DuSettings.getPath());
		text.setFont(new Font("Times New Roman",2,14));
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
	private void updateItemText()
	{
		if (DuSettings.isImageSearchMode())
		{
			itemImageMode.setText("Focus on images                   \u2714");
		}
		else
		{
			itemImageMode.setText("Focus on images");
		}
	}
}
