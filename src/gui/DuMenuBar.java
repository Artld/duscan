package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import logic.DefaultSettings;

public class DuMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	public DuMenuBar(final DefaultSettings settings) 
	{
		JMenu menu = new JMenu("Menu");
		add(menu);

		final JCheckBox chbox = new JCheckBox();
		chbox.setSelected(settings.isImageSearchMode());
		chbox.setAction(new AbstractAction("Focus on images")
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e)
			{
				settings.setImageSearchMode(chbox.isSelected());
			}
		});
		menu.add(chbox);

		JMenuItem itemMinSize = new JMenuItem(new AbstractAction("Specify minimum file size...")
		{
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e)
			{
				SpinnerNumberModel numModel = new SpinnerNumberModel(settings.getMinFileLength()/1024,0,611392,1);
				JSpinner spin = new JSpinner(numModel);
				if (JOptionPane.showOptionDialog(null, spin, " Minimum file size (kb): ", 2, 1, null, null, null)==0)
				{
					settings.setMinFileLength(1024 * (Integer) numModel.getNumber());
				}
			}
		});
		menu.add(itemMinSize);

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
		menu.add(itemAbout);
	}
}
