package logic;

public class DuSettings
{
    private static String path = System.getProperty("user.home");
    private static int mode = 0;
    private static int points = 10;  // number of bytes or pixels to check file equivalence
    private static int[] ResolutionBorders = {600, 500};  // used to search images by filter (mode == 2)

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
    public static int getMode()
    {
        return mode;
    }
    public static void setMode(int mode)
    {
        DuSettings.mode = mode;
    }
    public static int[] getResolutionBorders()
    {
        return DuSettings.ResolutionBorders;
    }
    public static void setResolutionBorders(int maxWidth, int maxHeight)
    {
        if (maxWidth >= 0 & maxHeight >= 0)
        {
            DuSettings.ResolutionBorders = new int[]{maxWidth, maxHeight};
        }
    }
}
