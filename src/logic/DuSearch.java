package logic;

import java.awt.color.CMMException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

import structure.Duplicate;

public abstract class DuSearch
{
    private ArrayList<File> fileList;           // raw list of all files
    protected ArrayList<Duplicate> dubList;     // main duplicate list
    protected ArrayList<Duplicate> badList;     // list of images with some problems detected
    protected int points;
    protected JProgressBar progress;

    protected DuSearch(JProgressBar progress)
    {
        this.progress = progress;
        progress.setVisible(true);
        fileList = new ArrayList<>();
        dubList = new ArrayList<>();
        badList = new ArrayList<>();
        points = DuSettings.getPoints();

        //---Making file list---
        progress.setString("file list creation...");
        System.out.println("Start making file list...");

        makeFileList(DuSettings.getPath());

        System.out.println("File list has "+fileList.size()+" points");
        progress.setValue(0);
        progress.setMaximum(fileList.size());
    }
    protected ArrayList<File> getFileList()
    {
        return fileList;
    }
    public ArrayList<Duplicate> getDubList()
    {
        return dubList;
    }
    public ArrayList<Duplicate> getBadList()
    {
        return badList;
    }
    /**Finds all files by path and fills fileList
     * @param path to folder with files */
    protected void makeFileList(String path) 
    {
        String[] array = new File(path).list();
        File file = null;
        for (String str : array)
        {
            file = new File(path + "/" + str);
            if (file.isFile())                      // file
            {
                fileList.add(file);                 // adding to common list
            }
            else if (file.isDirectory())            // folder
            {
                makeFileList(file.getPath());       // recursive scanning of folders
            }
        }
    }
    protected BufferedImage readImage (File file)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(file);
        }
        catch (IOException | IllegalArgumentException | CMMException | OutOfMemoryError e)
        {
            System.out.println("Error reading image " + file + "\n" + e.getMessage()) ;
            return null;
        }
        return image;
    }
    private final String[] EXTENSIONS = { ".jpg", ".jpeg", ".png", ".bmp", ".JPG", ".JPEG", ".PNG", ".BMP"};
    protected boolean isImage(File file)
    {
        String fname = file.getName();
        for (String ext : EXTENSIONS)
        {
            if (fname.endsWith(ext))
            {
                return true;
            }
        }     
        return false;
    }
}
