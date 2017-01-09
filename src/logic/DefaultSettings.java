package logic;

public class DefaultSettings
{
	private String path = System.getProperty("user.home");
	private int minFileLength = 1024*5;
	private boolean imageSearchMode = true;
	
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
