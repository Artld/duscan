package logic;

import java.io.File;

public class Duplicate
{
	private File file;
	private int groupId;
	private boolean selected;
	
	Duplicate(File file, int groupId)
	{
		setFile(file);
		setGroupId(groupId);
		setSelected(false);
	}
	public File getFile()
	{
		return file;
	}
	public void setFile(File file)
	{
		this.file = file;
	}
	public int getGroupId()
	{
		return groupId;
	}
	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}
	public boolean isSelected()
	{
		return selected;
	}
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
}
