package logic;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JProgressBar;

import structure.DuFile;
import structure.Duplicate;

public class DuFileSearch extends DuSearch
{
    public DuFileSearch(JProgressBar progress)
    {
        super(progress);

        // 1st comparison stage: by file length
        System.out.println("Start file comparison...");
        progress.setString("comparison by file size");

        ArrayList<ArrayList<DuFile>> groupList = groupByLength();  // list of file groups

        // hash generation
        System.out.println("List has "+groupList.size()+" groups of similar files\nStart hashing...");
        progress.setString("hashing");
        progress.setValue(0);
        progress.setMaximum(groupList.size());

        doHash(groupList);

        // 2nd comparison stage: by file hash
        System.out.println("Start byte to byte comparison...");
        progress.setString("comparison by hash");
        progress.setValue(0);

        hashComparison(groupList);  // fills final dubList

        System.out.println(dubList.size()+" similar files found.\nDone.");
        progress.setValue(progress.getMaximum());
        progress.setString("Done.");
    }

    /** Finds files with identical length */
    private ArrayList<ArrayList<DuFile>> groupByLength ()
    {
        ArrayList<ArrayList<DuFile>> outputList = new ArrayList<>();
        ArrayList<DuFile> inputList = getListWithLengths();
        for (int i=0; i<inputList.size(); i++)
        {
            long l1 = inputList.get(i).getLength();
            ArrayList<DuFile> smallOutputList = new ArrayList<>();
            for (int j=i+1; j<inputList.size(); j++) 
            {
                if (l1 == inputList.get(j).getLength())     // if files are the same length
                {
                    smallOutputList.add(inputList.get(j));
                    inputList.remove(j);
                    j--;
                }
            }
            if (!smallOutputList.isEmpty())                 // if at least one duplicate added
            {
                smallOutputList.add(inputList.get(i));
                outputList.add(smallOutputList);
            }
            progress.setValue(i+1);
        }
        return outputList;
    }

    /** Makes a list of DuFile structures from list of files */
    private ArrayList<DuFile> getListWithLengths()
    {
        ArrayList<DuFile> filesNLengths = new ArrayList<>();
        for (File file : getFileList())
        {
            filesNLengths.add(new DuFile(file));
        }
        return filesNLengths;
    }

    /** Generates hashes for all files in list */
    private void doHash (ArrayList<ArrayList<DuFile>> bigList)
    {
        for (ArrayList<DuFile> list : bigList)
        {
            for (DuFile d : list)
            {
                d.hashing(points);
            }
            progress.setValue(progress.getValue()+1);
        }
    }

    /** Compares file hashes */
    private void hashComparison(ArrayList<ArrayList<DuFile>> bigInputList)
    {
        int groups = 0;
        for (ArrayList<DuFile> smallInputList : bigInputList)
        {
            for (int j=0; j<smallInputList.size(); j++)
            {
                byte[] hash1 = smallInputList.get(j).getHash();     // hash of file1
                boolean added = false;
                for (int k=j+1; k<smallInputList.size(); k++)
                {
                    if (hashesEquals(hash1, smallInputList.get(k).getHash()))   // comparison of hash1 and hash2
                    {
                        added = true;
                        Duplicate d = smallInputList.get(k);        // file2
                        d.setGroupId(groups);
                        dubList.add(d);                             // add file2
                        smallInputList.remove(k);
                        k--;
                    }
                }
                if (added)                                  // if at least one file was added
                {
                    Duplicate d = smallInputList.get(j);    // file1
                    d.setGroupId(groups);
                    dubList.add(d);                         // add file1
                    groups++;
                }
            }
            progress.setValue(progress.getValue() + 1);
        }
    }

    /**File hashes equals function.
     * Caution! data1 and data2 must be the same length,
     * this method doesn't check it! */
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
