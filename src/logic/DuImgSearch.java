package logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

public class DuImgSearch implements DuSearch
{
	private ArrayList<ArrayList<File>> bigClassList;	// list of file groups
	private ArrayList <File> fileList;					// list of all files
	private JProgressBar progress;
	private int points;

	public DuImgSearch(JProgressBar progress)
	{
		points = DuSettings.getPoints();
		this.progress = progress;
		progress.setVisible(true);
		progress.setString("file list creation...");
		System.out.println("Start making file list...");

		fileList = new ArrayList <File> ();	
		makeFileList(DuSettings.getPath(), DuSettings.getMinFileLength());

		System.out.println("File list has "+fileList.size()+" points. \nStart hashing...");
		progress.setValue(0);
		progress.setMaximum(fileList.size());
		progress.setString("hashing...");

		ArrayList<int[]> hl = getHashList();
		
		System.out.println("Hashing completed. \nStart comparison...");
		progress.setValue(0);
		progress.setString("comparison...");
		
		imgComparison(hl);

		System.out.println("List has "+bigClassList.size()+" groups of similar pictures");
		progress.setValue(progress.getMaximum());
		progress.setVisible(false);
		System.out.println("Done.");
	}

	public ArrayList<ArrayList<File>> getDubList()
	{
		return bigClassList;
	}

	/**Finds image files by path and fills fileList
	 * @param path	path to folder with files
	 * @param minFileLength minimal length for file to enter in the list*/
	private void makeFileList(String path, int minFileLength) 
	{
		File file = null;
		String[] array = new File(path).list();
		for (String str : array)
		{
			file = new File(path + "/" + str);
			if (file.isFile())
			{
				if (file.length() > minFileLength)
				{
					String fname = file.getName();			//имя файла
					if (fname.endsWith(".jpg") || fname.endsWith(".jpeg") || fname.endsWith(".png") || fname.endsWith(".bmp")|| 
							fname.endsWith(".JPG") || fname.endsWith(".JPEG") || fname.endsWith(".PNG") || fname.endsWith(".BMP"))
					{
						fileList.add(file);
					}
				}
			}
			else if (file.isDirectory())
			{
				makeFileList(file.getPath(),minFileLength);		// recursive check of folders
			}
		}
	}

	private void imgComparison (ArrayList<int[]> hashList)
	{
		bigClassList = new ArrayList<ArrayList<File>> ();
		for (int i=0; i<hashList.size(); i++)
		{
			ArrayList<File> sList = new ArrayList<File> ();
			int[] data1 = hashList.get(i);
			for (int j=i+1; j<hashList.size(); j++)
			{
				if (hashesEquals(data1, hashList.get(j)))
				{
					sList.add(fileList.get(j));
					fileList.remove(j);
					hashList.remove(j);
					j--;
				}
			}
			if (!sList.isEmpty())
			{
				sList.add(fileList.get(i));
				bigClassList.add(sList);
			}
			progress.setValue(i+1);
		}
	}
	
	private ArrayList<int[]> getHashList ()
	{
		ArrayList<int[]> hashList = new ArrayList<int[]> ();
		for (int i=0; i<fileList.size(); i++)
		{
			int[] hash = hashing(fileList.get(i));
			if (hash!=null)
			{
				hashList.add(hash);
			}
			progress.setValue(i+1);
		}
		return hashList;
	}
	
	private int[] hashing (File file)
	{
		//---Image loading---
		
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(file);
			if (image==null)
			{
				System.out.println("Loaded image "+file+" is null");
				return null;
			}
		} 
		catch (IOException | IllegalArgumentException e)	
		{
			System.out.println("Error reading image "+file);
			System.out.println(e.getMessage());
			return null;
		}

		//---Hashing---
		//int rgb values of pixels
		//are taken along spiral
		//that starts from center of image
		
		int width = image.getWidth();
		int height = image.getHeight();
		int[] hash = new int[points];
		int b = (Math.min(width, height)/points)/2; //distance between the coils
		
		for (int i=0; i<points; i++)
		{
			int r = b*i;						//Archimedean spiral in polar coordinates: r = a + b*phi;
			int x = (int) (r*Math.cos(i));		//converting to Cartesian coordinates
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