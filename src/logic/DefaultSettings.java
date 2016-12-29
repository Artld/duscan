package logic;

public class DefaultSettings
{
	private String path = System.getProperty("user.home");
	private int minFileLength = 1024*5;
	private int frameWidth = 900;
	private int frameHeigth = 500;
	private boolean ImageSearchMode = true;
	
	public boolean isImageSearchMode()
	{
		return ImageSearchMode;
	}
	public void setImageSearchMode(boolean imageSearchMode)
	{
		ImageSearchMode = imageSearchMode;
	}
	public int getFrameWidth()
	{
		return frameWidth;
	}
	public void setFrameWidth(int frameWidth)
	{
		this.frameWidth = frameWidth;
	}
	public int getFrameHeigth()
	{
		return frameHeigth;
	}
	public void setFrameHeigth(int frameHeigth)
	{
		this.frameHeigth = frameHeigth;
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
