package logic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel
{
	private ArrayList<File> displayedList = null;			//list displayed inside table
	private ArrayList<Boolean> coloredList = null;			//list of groups of duplicates
	private ArrayList<Boolean> selectList = null;			//list containing  user's checks
	private JTable tbl = null;
	public ActionOpen   aOpen   = new ActionOpen();
	public ActionDelete aDelete = new ActionDelete();
	public ListenerDoubleClick dClickListener = new ListenerDoubleClick();

	//Inicialization function
	public void showList(ArrayList<ArrayList<File>> bList, JTable tbl)
	{
		this.tbl = tbl;
		this.displayedList = new ArrayList<File>();
		this.coloredList = new ArrayList<Boolean>();
		boolean b = true;

		for (ArrayList<File> sList : bList)
		{
			this.displayedList.addAll(sList);

			List<Boolean> list = Arrays.asList(new Boolean[sList.size()]);
			Collections.fill(list, b);
			this.coloredList.addAll(list);
			b = !b;
		}
		selectList = new ArrayList<Boolean>(Arrays.asList(new Boolean[displayedList.size()])); //makes selectList size = displayedList size
		Collections.fill(selectList, Boolean.FALSE);

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
		String[] colNames = {"", "Name", "Size", "Path"};
		return colNames[col];
	}

	@Override
	public int getRowCount() 
	{
		if (displayedList == null) return 0;
		return displayedList.size();
	}

	@Override
	public Object getValueAt(int row, int col) 
	{
		File file = displayedList.get(row);
		Object ret = null;
		switch (col)
		{
		case 0: ret = selectList.get(row);	break;
		case 1: ret = file.getName();		break;
		case 2: ret = file.length();		break;
		case 3: ret = file.getPath();		break;
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
			selectList.set(row, (Boolean) aValue);
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

	class ActionDelete implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (selectList != null)
			{
				for (int i=0; i<selectList.size(); i++)
				{
					if (selectList.get(i))
					{
						File file = displayedList.get(i);
						file.delete();
						displayedList.remove(i);
						selectList.remove(i);
						coloredList.remove(i);
						i--;
					}
				}
				updateRowColours();
				fireTableDataChanged();
			}
		}		
	}

	class ActionOpen implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(displayedList != null)
			{
				if(tbl.getSelectedRowCount() > 0)
				{
					openFile();
				}
			}
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
		File file = displayedList.get(tbl.getSelectedRow());
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

	private void updateRowColours() 
	{
		MyTableCellRenderer renderer = new MyTableCellRenderer();
		tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
		tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
		tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
	}

	private Color getRowColour(int row)
	{
		if (coloredList.get(row))
		{
			return Color.GREEN;
		}
		else
		{
			return Color.ORANGE;
		}
	}

	//class extended to manipulate cell colors
	class MyTableCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			TableModel model = (TableModel) table.getModel();
			comp.setBackground(model.getRowColour(row));
			return comp;
		}
	}
}
