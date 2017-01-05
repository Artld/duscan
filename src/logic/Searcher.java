package logic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

public class Searcher
{
	private ArrayList <File> fileList;				// список всех найденных файлов
	private ArrayList <File> filesZeroSize;			// список файлов c нулевым размером
	private ArrayList <ArrayList<File>> bigList;	// список списков дубликатов
	private int minFileLength;
	private JProgressBar progress;

	public Searcher(DefaultSettings settings, JProgressBar progress)
	{
		this.progress = progress;
		this.progress.setVisible(true);
		
		fileList = new ArrayList <File> ();
		filesZeroSize = new ArrayList <File> ();
		minFileLength = settings.getMinFileLength();
		
		getFileList(settings.getPath());
		System.out.println("File list has "+fileList.size()+" points");
		lengthChecking();
		if (settings.isImageSearchMode())
		{
			imageChecking();
		}
	}
	
	public ArrayList <ArrayList<File>> getDubList()
	{
		return bigList;
	}
	
	public ArrayList <File> getZeroList()
	{
		return filesZeroSize;
	}
	
	/**Находит все файлы по пути path и заполняет ими fileList 
	 * @param path	путь к папке с файлами */
	private void getFileList(String path) 
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
				else if (file.length() == 0)
				{
					filesZeroSize.add(file);		// добавление в список нулевых файлов
				}
			}
			else									// папка
			{
				getFileList(file.getPath());		// рекурсивная проверка папок
			}
		}
	}

	/** Находит файлы одинакового размера и расширения*/
	public void lengthChecking ()
	{
		progress.setMaximum(fileList.size());		// установка шкалы прогресса
		
		bigList = new ArrayList<ArrayList<File>>();	//список всех дубликатов (по размеру)
		
		for (int i=0; i<fileList.size(); i++)
		{
			File file1 = fileList.get(i);						//файл1 для сравнения
			ArrayList <File> smalList = new ArrayList<File>();	//список дубликатов этого файла
			String fname = file1.getName();						//имя файла1
			if (fname.lastIndexOf(".")==-1)						//нет расширения
			{
				continue;
			}
			String extens1 = fname.substring(fname.lastIndexOf("."), fname.length());		//расширение1
			for (int j=i+1; j<fileList.size(); j++) 
			{
				File file2 = fileList.get(j);		//файл2 для сравнения
				
				fname = file2.getName();			//имя файла2
				if (fname.lastIndexOf(".")==-1)		//нет расширения
				{
					fileList.remove(j);				//убрать файл2 из списка fileList
					j--;
					continue;
				}
				String extens2 = fname.substring(fname.lastIndexOf("."), fname.length());	//расширение2
				if (!ext_equals(extens1, extens2))	//расширения не совпадают
				{
					continue;
				}
				if (file1.length()==file2.length())	//если файлы одинаковой длины
				{
					smalList.add(file2);			//добавить файл2 в список дубликатов файла1
					fileList.remove(j);				//убрать файл2 из списка fileList
					j--;
				}
			}
			
			if (!smalList.isEmpty())		//если хотя бы один дубликат найден
			{
				smalList.add(file1);		//добавить также file1
				bigList.add(smalList);		//добавить список в список дубликатов
			}
			progress.setValue(i+1);			//отображение прогресса
		}
		System.out.println("List has "+bigList.size()+" similar files");
	}
	
	/**Среди уже найденных файлов с одинаковым размером и расширением находит
	 * изображения jpg, png и bmp одного разрешения и с одинаковыми нулевыми и центральными пикселями.
	 * Эта функция используется только в Image Search Mode*/
	public void imageChecking()
	{
		progress.setValue(0);
		progress.setMaximum(bigList.size());		// установка шкалы прогресса по-новой
		
		ArrayList<ArrayList<File>> bigImgList = new ArrayList<ArrayList<File>>();
		for (int i=0; i<bigList.size(); i++)
		{
			ArrayList <File> smalList = bigList.get(i);		
			File file1 = smalList.get(0);					
			String fname = file1.getName();			//имя файла
			if (fname.endsWith(".jpg") || fname.endsWith(".jpeg") || fname.endsWith(".png") || fname.endsWith(".bmp") || fname.endsWith(".JPG") || fname.endsWith(".JPEG") || fname.endsWith(".PNG") || fname.endsWith(".BMP"))
			{
				ArrayList <File> smalImgList = new ArrayList<File>();
				BufferedImage image1 = null;
				BufferedImage image2 = null;
				try
				{
					image1 = ImageIO.read(file1);
					if (image1==null)				//anti-nullpointerexception
					{
						System.out.println("Loaded image "+file1+" is null");
						continue;
					}
				} 
				catch (IOException e)
				{
					e.printStackTrace();
					continue;
				}
				for (int j=1; j<smalList.size(); j++)
				{
					File file2 = smalList.get(j);
					try
					{
						System.out.println(""+file2);
						image2 = ImageIO.read(file2);
						if (image2==null)
						{
							continue;
						}
					} 
					catch (IOException | IllegalArgumentException e)
					{
						System.out.println("Error processing file "+file2);
						e.printStackTrace();
						continue;
					}
					if (image1.getWidth()==image2.getWidth())				//по ширине
					{
						if (image1.getHeight()==image2.getHeight())			//по высоте
						{
							if (image1.getRGB(0, 0)==image2.getRGB(0, 0))	//по нулевому пикселю
							{
								int centerX = image1.getWidth()/2;
								int centerY = image1.getHeight()/2;
								if (image1.getRGB(centerX, centerY)==image2.getRGB(centerX, centerY))	//по центральному пикселю
								{
									smalImgList.add(file2);
									smalList.remove(j);
									j--;
								}
							}
						}
					}
				}
				if (!smalImgList.isEmpty())
				{
					smalImgList.add(file1);
					bigImgList.add(smalImgList);
				}
			}
			progress.setValue(i+1);
		}
		bigList = bigImgList;	// замена для простоты вывода
		System.out.println("List has "+bigList.size()+" similar pictures");
	}
	
	/**File extensions equals function*/
	private boolean ext_equals(String s1, String s2)
	{
		if (s1.equals(s2) || s1.equals(s2.toLowerCase()) || s1.equals(s2.toUpperCase()))
		{
			return true;
		}
		if (s1.equals("jpg")||s1.equals("jpeg")||s1.equals("JPG")||s1.equals("JPEG"))
		{
			if (s2.equals("jpg")||s2.equals("jpeg")||s2.equals("JPG")||s2.equals("JPEG"))
			{
				return true;
			}
		}
		return false;
	}
}
