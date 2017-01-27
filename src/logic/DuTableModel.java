package logic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class DuTableModel extends AbstractTableModel
{
	private ArrayList<Duplicate> displayedList = new ArrayList<>(); //list displayed inside table
	private JTable tbl = null;
	public ActionOpen   aOpen   = new ActionOpen();
	public ActionDelete aDelete = new ActionDelete();
	public ActionAutoSelect aAutoSelect = new ActionAutoSelect();
	public ActionMirrorSelection aMirror = new ActionMirrorSelection();
	public ActionDeselect aDeselect = new ActionDeselect();
	public ListenerDoubleClick dClickListener = new ListenerDoubleClick();

	/**Inicialization method*/
	public void showList(ArrayList<ArrayList<File>> bigList, JTable tbl)
	{
		this.tbl = tbl;
		this.displayedList = new ArrayList<Duplicate>();
		for (int i=0; i<bigList.size(); i++)
		{
			for (File file : bigList.get(i))
			{
				displayedList.add(new Duplicate(file,i));
			}
		}
		updateRowColours();
		fireTableDataChanged();
	}
	@Override 
	public int getColumnCount() 
	{
		return 4;
	}
	@Override
	public String getColumnName(int col) 
	{
		String[] colNames = {"", "Name", "Size/KiB", "Folder"};
		return colNames[col];
	}
	@Override
	public int getRowCount() 
	{
		return displayedList.size();
	}
	@Override
	public Object getValueAt(int row, int col) 
	{
		Duplicate d = displayedList.get(row);
		File file = d.getFile();
		Object ret = null;
		switch (col)
		{
		case 0: ret = d.isSelected();		break;
		case 1: ret = file.getName();       break;
		case 2: ret = file.length()/1024;   break;
		case 3: ret = file.getParentFile(); break;
		}
		return ret;
	}
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return (column == 0);
	}
	@Override
	public void setValueAt(Object aValue, int row, int column)
	{
		if (aValue instanceof Boolean && column == 0)
		{
			displayedList.get(row).setSelected((Boolean) aValue);
			fireTableCellUpdated(row, column);
		}
	}
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		Class<?> cl = String.class;
		switch (columnIndex)
		{
		case 0: cl = Boolean.class; break;
		default: cl = String.class; break;
		}
		return cl;
	}
	private void openFile()
	{
		File file = displayedList.get(tbl.getSelectedRow()).getFile();
		if(file.exists())
		{
			try
			{
				if(Desktop.isDesktopSupported())
				{
					Desktop.getDesktop().open(file);
				}
				else
				{
					System.out.println("Can not open file: desktop is not supported");
				}
			} 
			catch (IOException e1)
			{
				System.out.println(e1.getMessage());
			}
		}
	}
	private void updateRowColours() 
	{
		if (tbl != null)
		{
			MyTableCellRenderer renderer = new MyTableCellRenderer();
			tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
			tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
			tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
		}
	}
	private Color getRowColour(int row)
	{
		if (displayedList.get(row).getGroupId()%2 == 0)
		{
			return Color.GREEN;
		}
		else
		{
			return Color.ORANGE;
		}
	}
	private void autoSelect()
	{
		for (int i=1; i<displayedList.size(); i++)
		{
			if (displayedList.get(i).getGroupId() == displayedList.get(i-1).getGroupId())
			{
				displayedList.get(i).setSelected(true);
			}
			else
			{
				displayedList.get(i).setSelected(false);
			}
		}
		fireTableDataChanged();
	}
	private void mirrorSelection()
	{
		for (Duplicate d : displayedList)
		{
			d.setSelected(!d.isSelected());
		}
		fireTableDataChanged();
	}
	private void deselect()
	{
		for (Duplicate d : displayedList)
		{
			d.setSelected(false);
		}
		fireTableDataChanged();
	}
	private class ActionDelete implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			for (int i=0; i<displayedList.size(); i++)
			{
				Duplicate d = displayedList.get(i);
				if (d.isSelected())
				{
					System.out.println("Deleting "+d.getFile());
					d.getFile().delete();
					displayedList.remove(i);
					i--;
				}
			}
			updateRowColours();
			fireTableDataChanged();
		}		
	}
	private class ActionOpen implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(tbl != null && tbl.getSelectedRowCount() > 0)
			{
				openFile();
			}
		}		
	}
	private class ListenerDoubleClick implements MouseListener
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
	private class MyTableCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component comp = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			DuTableModel model = (DuTableModel) table.getModel();
			comp.setBackground(model.getRowColour(row));
			return comp;
		}
	}
	private class ActionAutoSelect implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			autoSelect();
		}
	}
	private class ActionMirrorSelection implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			mirrorSelection();
		}
	}
	private class ActionDeselect implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			deselect();
		}
	}
}
