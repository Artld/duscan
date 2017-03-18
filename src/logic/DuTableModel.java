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
import javax.swing.JToggleButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import structure.Duplicate;

public class DuTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 1L;
    private ArrayList<Duplicate> dubList = new ArrayList<>();
    private ArrayList<Duplicate> badList = new ArrayList<>();
    private ArrayList<Duplicate> displayedList = dubList;       // list displayed inside table.
    private JTable tbl = null;
    private String path = "";
    private int mode = 0;
    public ActionOpenFile aOpenFile = new ActionOpenFile();
    public ActionOpenFolder aOpenFolder = new ActionOpenFolder();
    public ActionDelete aDelete = new ActionDelete();
    public ActionAutoSelect aAutoSelect = new ActionAutoSelect();
    public ActionMirrorSelection aMirror = new ActionMirrorSelection();
    public ActionDeselect aDeselect = new ActionDeselect();
    public ActionToggleTableContent aToggleTableContent = new ActionToggleTableContent();
    public ListenerDoubleClick dClickListener = new ListenerDoubleClick();

    /**Initialization method*/
    public void showList(ArrayList<Duplicate> dubList, ArrayList<Duplicate> badList, JTable tbl)
    {
        this.tbl = tbl;
        this.badList = badList;
        this.dubList = dubList;
        displayedList = this.dubList;       // display dubList by default.
        this.path = DuSettings.getPath();   // @path and @mode VARs separated from @DuSettings.path and @DuSettings.mode VARs
        this.mode = DuSettings.getMode();   // to prevent changes from the user's side at runtime.
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
        String[] colNames = {"", "Name", "Length", "Folder"};
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
        Object ret = null;
        switch (col)
        {
            case 0: ret = d.isSelected();                                           break;
            case 1: ret = d.getFile().getName();                                    break;
            case 2: long l = d.getFile().length()/1024;
            ret = (l < 1024) ? l + " KiB" : l/1024 + " MiB";                break;    
            case 3: ret = d.getFile().getParentFile().toString().replace(path, ""); break;
        }
        return ret;
    }
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return column == 0;
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
        return (columnIndex == 0) ? Boolean.class : String.class;   
    }
    private void open(File file)
    {
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
                    System.out.println("Can not open file: desktop is not supported.");
                }
            } 
            catch (IOException e1)
            {
                System.out.println(e1.getMessage() + 
                        "\nUsually it's not a program fault but something with your operation system.");
            }
        }
    }
    private void openFile()
    {
        open(displayedList.get(tbl.getSelectedRow()).getFile());
    }
    private void openFolder()
    {
        open(new File(displayedList.get(tbl.getSelectedRow()).getFile().getParent()));
    }
    private void updateRowColours() 
    {
        if (tbl != null)
        {
            DuTableCellRenderer renderer = new DuTableCellRenderer();
            tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
        }
    }
    private Color getRowColour(int row)
    {
        return (displayedList.get(row).getGroupId()%2 == 0) ? Color.GREEN : Color.ORANGE;
    }
    private void autoSelect()
    {
        if (mode != 2 && displayedList == dubList)
        {
            // keep one file from each group of duplicates, select other
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
        }
        else 
        {
            // select all
            for (Duplicate d : displayedList)
            {
                d.setSelected(true);
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
    private void toggleTableContent(boolean selected)
    {
        displayedList = selected ? badList : dubList;
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
    private class ActionOpenFile implements ActionListener
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
    private class ActionOpenFolder implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(tbl != null && tbl.getSelectedRowCount() > 0)
            {
                openFolder();
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
    private class DuTableCellRenderer extends DefaultTableCellRenderer
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
    private class ActionToggleTableContent implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            JToggleButton btn = (JToggleButton) e.getSource();
            toggleTableContent(btn.isSelected());
        }
    }
}
