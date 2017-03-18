package logic;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JProgressBar;

import structure.Duplicate;

public class DuImgFilter extends DuSearch
{
    public DuImgFilter(JProgressBar progress)
    {
        super(progress);
        int[] resolutionBorders = DuSettings.getResolutionBorders();

        System.out.println("Start search...");
        progress.setString("search...");
        
        for (File file : getFileList())
        {     
            if (isImage(file))
            {
                BufferedImage image = readImage(file);
                if (image == null)
                {
                    badList.add(new Duplicate(file, 0));
                }
                else if (image.getWidth() < resolutionBorders[0] && image.getHeight() < resolutionBorders[1])
                {
                    dubList.add(new Duplicate(file, 0));
                }
            }
            progress.setValue(progress.getValue()+1);
        }
        progress.setString("Done!");
        progress.setValue(progress.getMaximum());
        System.out.println(dubList.size() + " images are smaller then " + resolutionBorders[0] + " x " + resolutionBorders[1]);
    }
}
