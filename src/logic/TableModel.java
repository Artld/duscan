package logic;

import java.awt.Desktop;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel
{
	private ArrayList<File> fList = null;
	private ArrayList<File> bigList = null;
	private ArrayList<File> zList = null;
	private JTable tbl = null;
	public ActionOpen   aOpen   = new ActionOpen();
	public ActionDelete aDelete = new ActionDelete();
	public ActionToggleShow aToggleShow = new ActionToggleShow();
	public ListenerDoubleClick dClickListener = new ListenerDoubleClick();

	@Override 
	public int getColumnCount() 
	{
		return 3;
	}

	public String getColumnName(int col) 
	{
		String[] colNames = {"Name", "Size", "Path"};
		return colNames[col];
	}

	@Override
	public int getRowCount() 
	{
		if (fList == null) return 0;
		return fList.size();
	}

	@Override
	public Object getValueAt(int row, int col) 
	{
		File file = fList.get(row);
		Object ret = null;
		switch (col)
		{
		case 0: ret = file.getName();   break;
		case 1: ret = file.length(); 	break;
		case 2: ret = file.getPath(); 	break;
		}
		return ret;
	}

	public void showList(ArrayList<ArrayList<File>> bList, ArrayList<File> zeroList, JTable tbl)
	{
		this.tbl = tbl;
		this.fList = new ArrayList<File>();
		this.bigList = fList;
		this.zList = zeroList;
		for (ArrayList<File> mList : bList)
		{
			this.bigList.addAll(mList);
		}
		fireTableDataChanged();
	}

	class ActionOpen implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			openFile();
		}		
	}
	class ActionDelete implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			File file = fList.get(tbl.getSelectedRow());
			fList.remove(tbl.getSelectedRow());
			file.delete();
			fireTableDataChanged();
		}		
	}
	class ActionToggleShow implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (((JToggleButton) e.getSource()).isSelected())
			{
				fList = zList;
			}
			else
			{
				fList = bigList;
			}
			fireTableDataChanged();
		}		
	}
	class ListenerDoubleClick implements MouseListener
	{
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e)
		{
			if (e.getClickCount() == 2)
			{
				openFile();
			}
		}
		@Override
		public void mousePressed(java.awt.event.MouseEvent e){}
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e){}
		@Override
		public void mouseEntered(java.awt.event.MouseEvent e){}
		@Override
		public void mouseExited(java.awt.event.MouseEvent e){}
	}
	private void openFile()
	{
		if(!Desktop.isDesktopSupported())
		{
			System.out.println("Desktop is not supported");
			return;
		}
		Desktop desktop = Desktop.getDesktop();
		File file = fList.get(tbl.getSelectedRow());
		if(file.exists())
		{
			try
			{
				desktop.open(file);
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
