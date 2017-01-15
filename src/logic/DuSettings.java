package logic;

public class DuSettings
{
	private String path = System.getProperty("user.home");
	private int minFileLength = 1024*5;
	private int points = 10;
	private boolean imageSearchMode = false;
	
	
	public int getPoints()
	{
		return points;
	}
	public void setPoints(int points)
	{
		this.points = points;
	}	
	public boolean isImageSearchMode()
	{
		return imageSearchMode;
	}
	public void setImageSearchMode(boolean imageSearchMode)
	{
		this.imageSearchMode = imageSearchMode;
	}
	public String getPath()
	{
		return path;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	public int getMinFileLength()
	{
		return minFileLength;
	}
	public void setMinFileLength(int minFileLength)
	{
		this.minFileLength = minFileLength;
	}
}
