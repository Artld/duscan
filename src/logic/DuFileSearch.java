package logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JProgressBar;

public class DuFileSearch implements DuSearch
{
	private ArrayList<ArrayList<File>> bigClassList; 	// list of file groups
	private ArrayList<File> fileList;					// list of all files
	private int points;
	private JProgressBar progress;

	public DuFileSearch(JProgressBar progress)
	{
		this.progress = progress;
		progress.setVisible(true);
		fileList = new ArrayList <File> ();	
		points = DuSettings.getPoints();
		
		//---Making file list---
		progress.setString("file list creation...");
		System.out.println("Start making file list...");
		
		makeFileList(DuSettings.getPath(), DuSettings.getMinFileLength());
		
		System.out.println("File list has "+fileList.size()+" points. \nStart file comparison...");
		progress.setString("comparison by file size");
		progress.setMaximum(fileList.size());
		
		bigClassList = lengthChecking();
		
		System.out.println("List has "+bigClassList.size()+" groups of similar files");

		//2nd comparison stage
		this.progress.setString("comparison by hash");
		progress.setValue(0);
		progress.setMaximum(bigClassList.size());
		System.out.println("Start byte to byte comparison...");

		bigClassList = hashComparison(bigClassList);

		System.out.println("List has "+bigClassList.size()+" groups of equal files");
		progress.setValue(progress.getMaximum());
		this.progress.setVisible(false);
		System.out.println("Done.");
	}

	@Override
	public ArrayList<ArrayList<File>> getDubList()
	{
		return bigClassList;
	}

	/**Finds all files by path and fills fileList
	 * @param path	path to folder with files
	 * @param minFileLength minimal	length for file to enter in a list*/
	private void makeFileList(String path, int minFileLength) 
	{
		File file = null;
		String[] array = new File(path).list();
		for (String str : array)
		{
			file = new File(path + "/" + str);
			if (file.isFile())						// файл
			{
				if (file.length() > minFileLength)	// этим числом ограничиваем минимальный размер добавляемых файлов
				{
					fileList.add(file);				// добавление в общий список
				}
			}
			else if (file.isDirectory())			// папка
			{
				makeFileList(file.getPath(),minFileLength);		// рекурсивная проверка папок
			}
		}
	}
	
	/** Finds files with same length */
	private ArrayList<ArrayList<File>>  lengthChecking ()
	{
		ArrayList<ArrayList<File>> bigList = new ArrayList<ArrayList<File>>();
		ArrayList<Long> lengthList = getLengthList();
		for (int i=0; i<lengthList.size(); i++)
		{
			long l1 = lengthList.get(i);
			ArrayList <File> smalList = new ArrayList<File>();	//список дубликатов этого файла
			for (int j=i+1; j<lengthList.size(); j++) 
			{
				long l2 = lengthList.get(j);
				if (l1 == l2)	//если файлы одинаковой длины
				{
					smalList.add(fileList.get(j));	//добавить файл2 в список дубликатов файла1
					fileList.remove(j);				//убрать файл2 из списка fileList
					lengthList.remove(j);
					j--;
				}
			}
			if (!smalList.isEmpty())			//если хотя бы один дубликат найден
			{
				smalList.add(fileList.get(i));	//добавить также file1
				bigList.add(smalList);			//добавить список в список дубликатов
			}
			progress.setValue(i+1);				//отображение прогресса
		}
		return bigList;
	}
	
	private ArrayList<Long> getLengthList ()
	{
		ArrayList<Long> lengthList = new ArrayList<Long>();
		for (File file : fileList)
		{
			lengthList.add(file.length());
		}
		return lengthList;
	}

	/**Compares file hashes */
	private ArrayList<ArrayList<File>> hashComparison(ArrayList<ArrayList<File>> bigInputList)
	{
		ArrayList<ArrayList<File>> bigOutputList = new ArrayList<ArrayList<File>>();
		for (int i=0; i<bigInputList.size(); i++)
		{
			ArrayList <File> smallInputList = bigInputList.get(i);			//parent file list
					
			ArrayList <byte[]> hashList = getHashList(smallInputList);		//file hashes of parent list

			for (int j=0; j<hashList.size(); j++)
			{
				ArrayList <File> smallOutputList = new ArrayList<File>();	//result list
				byte[] hash1 = hashList.get(j);
				for (int k=j+1; k<hashList.size(); k++)
				{
					if (hashesEquals(hash1, hashList.get(k)))
					{
						smallOutputList.add(smallInputList.get(k));
						smallInputList.remove(k);
						hashList.remove(k);
						k--;
					}
				}
				if (!smallOutputList.isEmpty())
				{
					smallOutputList.add(smallInputList.get(j));
					bigOutputList.add(smallOutputList);
				}
			}
			progress.setValue(i+1);
		}
		return bigOutputList;
	}

	private ArrayList<byte[]> getHashList (ArrayList<File> fileList)
	{
		ArrayList<byte[]> hashList = new ArrayList<byte[]>();
		for (File file : fileList)
		{
			hashList.add(hashing(file));
		}
		return hashList;
	}

	/**Builds file hash*/
	private byte[] hashing(File file)
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
		for (int i=0; i<points; i++)			//points == 1: first byte
		{										//points == 2: first and 1/2 of length byte
			hash[i] = data[i*distance];			//points == 3: first, 1/3 and 2/3 of length byte
		}
		return hash;
	}

	/**File hashes equals function.
	 * Caution! data1 and data2 must be the same length,
	 * this method doesn't check it!*/
	private boolean hashesEquals(byte[] data1, byte[] data2)
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
