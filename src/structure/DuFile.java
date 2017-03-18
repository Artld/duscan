package structure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DuFile extends Duplicate
{
    private byte[] hash = null;
    private long fLength;
    public DuFile(File file)
    {
        super(file);
        this.fLength = file.length();
    }
    public long getLength()
    {
        return fLength;
    }
    public byte[] getHash()
    {
        return hash;
    }
    /**Builds file hash*/
    public void hashing(int points)
    {
        //---Reading the file in byte array---
        byte[] data = null;
        try
        {
            data = Files.readAllBytes(Paths.get(file.getPath()));
        } 
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        //---Choosing some bytes---
        byte[] hash = new byte[points];
        int distance = data.length/points;
        for (int i=0; i<points; i++)            // points == 1: first byte
        {                                       // points == 2: first and 1/2 of length byte
            hash[i] = data[i*distance];         // points == 3: first, 1/3 and 2/3 of length byte
        }
        this.hash = hash;
    }
}
