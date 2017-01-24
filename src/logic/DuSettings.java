package logic;

public class DuSettings
{
	private static String path = System.getProperty("user.home");
	private static int minFileLength = 1024*5;
	private static int points = 10;
	private static boolean imageSearchMode = false;
	
	public static String getPath()
	{
		return path;
	}
	public static void setPath(String path)
	{
		if (!path.equals(""))
		{
			DuSettings.path = path;
		}
	}
	public static int getMinFileLength()
	{
		return minFileLength;
	}
	public static void setMinFileLength(int minFileLength)
	{
		DuSettings.minFileLength = minFileLength;
	}
	public static int getPoints()
	{
		return points;
	}
	public static void setPoints(int points)
	{
		if (points >= 1)
		{
			DuSettings.points = points;
		}
	}
	public static boolean isImageSearchMode()
	{
		return imageSearchMode;
	}
	public static void setImageSearchMode(boolean imageSearchMode)
	{
		DuSettings.imageSearchMode = imageSearchMode;
	}
}
