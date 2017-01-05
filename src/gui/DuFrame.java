package gui;

import java.awt.BorderLayout;
import javax.swing.*;
import logic.DefaultSettings;

class DuFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public DuFrame()
	{
		DefaultSettings df = new DefaultSettings();
		setTitle("Duplicate Searcher by rtld");
		setBounds(150, 100, df.getFrameWidth(), df.getFrameHeigth());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DuPanel dp = new DuPanel();
		add(dp, BorderLayout.CENTER);
		
		setLayout(null);
		setVisible(true);
	}
}