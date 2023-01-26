package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.join;
import static gitlet.Utils.plainFilenamesIn;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Rishabh Choudhury
 */
public class Commit implements Serializable, Dumpable {
    /** The message of this Commit. */
    private String message;
    /** The SHA-1 id of the current commit */
    private String id;
    /** The name of the commiter */
    private String authorName;
    /** The date and time the commit was made */
    private Date d;
    /** Maps tracked file names to their SHA-1 id's <br><br>
     * Key - File Name <br>
     * Value - SHA-1 ID <br>
     * */
    private Map<String, String> trackedFiles = new HashMap<>();
    /** The SHA-1 id of the first parent */
    private String firstParentID;
    /** The SHA-1 id of the second parent (if exists) */
    private String secondParentID;

    /** Create a new commit object with the given values
     *
     * @param message the message associated with this commit
     * @param authorName name of the author commiting
     * @param d date of commit
     * */
    public Commit(String message, String authorName, Date d){
        this.message = message;
        this.authorName = authorName;
        this.d = d;
    }

    /** Copies existing files tracked by the parent and
     * stores a reference to the parent ID
     *
     * @param parentCommit the commit that should be a parent of this commit
     */
    public void setParentValues(Commit parentCommit) {
        //Copy all map contents
        trackedFiles = parentCommit.getTrackedFiles();

        //Set ParentID
        firstParentID = parentCommit.getID();
    }

    /** Tracks the files added in the staging area
     *
     * @param stagedFiles Map of files and their respective SHA-1 id staged for tracking
     * */
    public void trackStagedFiles(Map<String, String> stagedFiles) {
        for(String fileName : stagedFiles.keySet()) {
            trackedFiles.put(fileName, stagedFiles.get(fileName));
        }
    }

    //TODO: Write the function
    /** Untracks the files deleted by gitlet.Main rm command
     *
     * @param stagingDir staging area directory path
     * */
    public void untrackRemovedFiles(File stagingDir) {

    }

    /** Set the SHA-1 id of this commit object
     *
     * @param id SHA-1 id of this object
     * */
    public void setID(String id){
        this.id = id;
    }

    /** Returns the SHA-1 ID of the given fileName
     *
     * @param fileName the file whose SHA id is required
     * @return SHA id of given file
     */
    public String getFileSHAID(String fileName){
        return trackedFiles.get(fileName);
    }

    /** Returns the SHA-1 ID of this commit
     *
     * @return SHA-1 ID
     * */
    public String getID() {
        return id;
    }

    /** Returns the trackedFiles map ds of this commit
     *
     * @return trackedFiles map ds
     * */
    public Map<String, String> getTrackedFiles() {
        return trackedFiles;
    }

    @Override
    public void dump() {
        System.out.println("message : " + message);
        System.out.println("date : " + d);
        System.out.println("firstParentID : " + firstParentID);

        for(String key : trackedFiles.keySet()) {
            System.out.println(key + " : " + trackedFiles.get(key));
        }
    }
}
