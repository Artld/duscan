package structure;

import java.io.File;

public class Duplicate
{
    protected File file;
    private int groupId;
    private boolean selected;

    protected Duplicate(File file)
    {
        setFile(file);
        setSelected(false);
    }
    public Duplicate(File file, int groupId)
    {
        this(file);
        setGroupId(groupId);
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
