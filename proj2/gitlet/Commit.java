package gitlet;

import java.io.Serializable;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author Rishabh Choudhury
 */
public class Commit implements Serializable, Dumpable {
    /**
     * The message of this Commit.
     */
    private String message;
    /**
     * The SHA-1 id of the current commit
     */
    private String id;
    /**
     * The date and time the commit was made
     */
    private Date d;
    /**
     * Maps tracked file names to their SHA-1 id's <br><br>
     * Key - File Name <br>
     * Value - SHA-1 ID <br>
     */
    Map<String, String> trackedFiles = new HashMap<>();
    /**
     * The SHA-1 id of the first parent
     */
    private String firstParentID;
    /**
     * The SHA-1 id of the second parent (if exists)
     */
    private String secondParentID;

    /**
     * Create a new commit object with the given values
     *
     * @param message the message associated with this commit
     * @param d       date of commit
     */
    public Commit(String message, Date d) {
        this.message = message;
        this.d = d;
    }

    /**
     * Track files being tracked by the parent and
     * store a reference to the parent ID
     *
     * @param parentCommit the commit that should be a parent of this commit
     */
    public void trackParent(Commit parentCommit) {
        //Copy all map contents
        trackedFiles = parentCommit.trackedFiles;

        //Set ParentID
        firstParentID = parentCommit.getID();
    }

    /**
     * Tracks the files added in the staging area
     *
     * @param filesStagedForAddition Map representing files staged for addition
     */
    public void trackStagedFiles(Map<String, String> filesStagedForAddition) {
        for (String fileName : filesStagedForAddition.keySet()) {
            trackedFiles.put(fileName, filesStagedForAddition.get(fileName));
        }
    }

    /**
     * Untracks the files deleted by gitlet.Main rm command
     *
     * @param filesStagedForRemoval List representing files staged for removal
     */
    public void untrackRemovedFiles(List<String> filesStagedForRemoval) {
        for (String fileName : filesStagedForRemoval) {
            trackedFiles.remove(fileName);
        }
    }

    /**
     * Set the SHA-1 id of this commit object
     *
     * @param commitID SHA-1 id of this object
     */
    public void setID(String commitID) {
        this.id = commitID;
    }

    /**
     * Returns the SHA-1 ID of the given fileName
     *
     * @param fileName the file whose SHA id is required
     * @return SHA id of given file
     */
    public String getFileID(String fileName) {
        return trackedFiles.get(fileName);
    }

    /**
     * Returns the SHA-1 ID of this commit
     *
     * @return SHA-1 ID
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the first parent SHA-1 ID of this commit
     *
     * @return SHA-1 ID
     */
    public String getFirstParentID() {
        return firstParentID;
    }
    
    /**
     * Prints all relevant information about this commit
     */
    public void printCommitInfo() {
        System.out.println("===");
        System.out.println("commit " + id);

        //Format Date
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        String formattedDate = String.format("%1$ta %1$tb %1$te %1$TT %1$TY %1$tz", c);
        //Print formatted date
        System.out.println("Date: " + formattedDate);

        System.out.println(message);
        System.out.println();

        //TODO: Add Merge info functionality
    }

    @Override
    public void dump() {
        System.out.println("message : " + message);
        System.out.println("date : " + d);
        System.out.println("firstParentID : " + firstParentID);

        for (String key : trackedFiles.keySet()) {
            System.out.println(key + " : " + trackedFiles.get(key));
        }
    }
}
