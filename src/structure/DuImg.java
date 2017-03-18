package structure;

import java.io.File;

public class DuImg extends Duplicate
{
    private int[] hash;
    
    public DuImg(File file, int[] hash)
    {
        super(file);
        this.hash = hash;
    }
    public int[] getHash()
    {
        return hash;
    }
}
