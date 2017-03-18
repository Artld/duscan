package logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JProgressBar;

import structure.DuImg;
import structure.Duplicate;

public class DuImgSearch extends DuSearch
{    
    public DuImgSearch(JProgressBar progress)
    {
        super(progress);

        System.out.println("Start hashing...");
        progress.setString("hashing...");

        ArrayList<DuImg> hl = getHashList();

        System.out.println("Hashing completed. \nStart comparison...");
        progress.setValue(0);
        progress.setString("comparison...");

        imgComparison(hl);  // fills final dubList

        System.out.println(dubList.size()+" similar pictures found.\nDone.");
        progress.setValue(progress.getMaximum());
        progress.setString("Done.");
    }

    private void imgComparison (ArrayList<DuImg> hashList)
    {
        for (int i=0; i<hashList.size(); i++)
        {
            int[] data1 = hashList.get(i).getHash();
            boolean added = false;
            for (int j=i+1; j<hashList.size(); j++)
            {
                if (hashesEquals(data1, hashList.get(j).getHash()))
                {
                    added = true;
                    Duplicate d = hashList.get(j);
                    d.setGroupId(i);
                    dubList.add(d);
                    hashList.remove(j);
                    j--;
                }
            }
            if (added)
            {
                Duplicate d = hashList.get(i);
                d.setGroupId(i);
                dubList.add(d);
            }
            progress.setValue(i+1);
        }
    }

    private ArrayList<DuImg> getHashList()
    {
        ArrayList<DuImg> hashList = new ArrayList<>();
        for (File file : getFileList())
        {
            if (isImage(file))
            {
                BufferedImage image = readImage(file);

                if (image == null)
                {
                    badList.add(new Duplicate(file,0));
                }
                else
                {
                    hashList.add(new DuImg(file,getHash(image)));
                }
            }
            progress.setValue(progress.getValue()+1);
        }
        return hashList;
    }

    /** How it works: int RGB values of pixels are taken along spiral
     *  that starts from center of image*/
    private int[] getHash (BufferedImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] hash = new int[points];
        int b = (Math.min(width, height)/points)/2; // distance between the coils

        for (int i=0; i<points; i++)
        {
            int r = b*i;                    // Archimedean spiral in polar coordinates: r = a + b*phi.
            int x = (int) (r*Math.cos(i));  // Converting to Cartesian coordinates.
            int y = (int) (r*Math.sin(i));
            hash[i] = image.getRGB(x+width/2, y+height/2);
        }
        return hash;
    }

    /**Hashes equals function.
     * Caution! data1 and data2 must be the same length,
     * this method doesn't check it!*/
    private boolean hashesEquals(int[] data1, int[] data2)
    {
        boolean flag = true;
        for (int i=0; i<data1.length; i++)
        {
            if (data1[i]!=data2[i])
            {
                flag = false;
            }
        }
        return flag;
    }
}