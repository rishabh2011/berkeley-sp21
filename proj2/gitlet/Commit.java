package gitlet;

import java.io.Serializable;
import java.util.*;

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
    Map<String, String> trackedFiles;
    /**
     * The SHA-1 ids of parents
     */
    private LinkedList<String> parentIDs;

    /**
     * Create a new commit object with the given values
     *
     * @param message the message associated with this commit
     * @param d       date of commit
     */
    public Commit(String message, Date d) {
        this.message = message;
        this.d = d;
        trackedFiles = new HashMap<>();
        parentIDs = new LinkedList<>();
    }

    /**
     * Track files being tracked by the parent and
     * store a reference to the parent ID
     *
     * @param parentCommits the commits that should be parents of this commit
     */
    public void trackParent(LinkedList<Commit> parentCommits) {
        for (Commit commit : parentCommits) {
            //Copy all map contents
            for (String file : commit.trackedFiles.keySet()) {
                trackedFiles.put(file, commit.trackedFiles.get(file));
            }
            //Set ParentID
            parentIDs.addLast(commit.getID());
        }
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
     * Returns the date of commit
     *
     * @return Date of commit
     */
    public Date getDate() {
        return d;
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
    public LinkedList<String> getParentIDs() {
        return parentIDs;
    }

    /**
     * Prints all relevant information about this commit
     */
    public void printCommitInfo() {
        System.out.println("===");
        System.out.println("commit " + id);

        //Merge commit
        if (parentIDs.size() > 1) {
            System.out.print("Merge: ");
            for (String parentID : parentIDs) {
                System.out.print(parentID.substring(0, 7) + " ");
            }
            System.out.println();
        }

        //Format Date
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        String formattedDate = String.format("%1$ta %1$tb %1$te %1$TT %1$TY %1$tz", c);
        //Print formatted date
        System.out.println("Date: " + formattedDate);

        System.out.println(message);
        System.out.println();
    }

    @Override
    public void dump() {
        System.out.println("message : " + message);
        System.out.println("date : " + d);
        System.out.println("firstParentID : " + parentIDs);

        for (String key : trackedFiles.keySet()) {
            System.out.println(key + " : " + trackedFiles.get(key));
        }
    }
}
