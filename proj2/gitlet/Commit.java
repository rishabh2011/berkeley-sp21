package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
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
    private Map<String, String> trackedFiles;
    /** The SHA-1 id of the first parent */
    private String firstParentID;
    /** The SHA-1 id of the second parent (if exists) */
    private String secondParentID;

    /** Create a new commit object with the given values
     *
     *
     * */
    public Commit(String message, String authorName, Date d, String... parentID){
        this.message = message;
        this.authorName = authorName;
        this.d = d;
        this.firstParentID = parentID[0];
        this.trackedFiles = new HashMap<>();
        if(parentID[1] != null){
            this.secondParentID = parentID[1];
        }
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
}
