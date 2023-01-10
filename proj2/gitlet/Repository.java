package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author Rishabh Choudhury
 */
public class Repository {
    /**
     * The current working directory.
     */
    static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The commits directory
     */
    static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    /**
     * The files directory
     */
    static final File FILE_DIR = join(GITLET_DIR, "files");
    /**
     * The Staging Area Directory
     */
    static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /**
     * The refs directory
     * (Contains refs such as current head commit, branch commit refs such as master)
     */
    static final File REF_DIR = join(GITLET_DIR, "refs");
    /**
     * Points to the latest commit in the current branch
     */
    private String head;
    /**
     * Maps staged file names to their SHA-1 id's <br><br>
     * Key - File Name <br>
     * Value - SHA-1 ID <br>
     */
    private Map<String, String> stagedFiles;
    /**
     * Tracks the current branch
     */
    private String currentBranch = "master";

    /**
     * Creates a new Gitlet version-control system ion the current directory.
     */
    public void init() {
        //Create gitlet directory and all required subdirectories
        File gitletDir = new File(GITLET_DIR.toString());
        if (gitletDir.exists()) {
            message("A Gitlet version-control system already "
                    + "exists in the current directory.");
            System.exit(0);
        }
        setupPersistence();

        //Initialize repo with first commit //
        Date d = new Date(0);
        Commit newCommit = new Commit("Initial message", null, d, null, null);
        storeCommitObject(newCommit);
    }

    /**
     * Add given file to the staging area <br><br>
     * <p>
     * Function adds given file to the staging area if it has been modified as
     * compared to its existing repo version <br>
     * <p>
     * Condition 1: Add file if it is newly created <br>
     * Condition 2: Add file if it already exists in repo and has been modified <br>
     * Condition 3: Remove file from staging area if
     * it has been reset to match its existing repo version
     *
     * @param fileName the name of the file to be added
     */
    public void add(String fileName) {

        File givenFile = new File(join(CWD, fileName).toString());
        if(!givenFile.exists()) {
            message("File does not exist.");
            System.exit(0);
        }

        String givenFileSHAID = sha1(givenFile.toString());
        Commit lastCommit = getLastCommitObject();
        String commitFileSHAID = lastCommit.getFileSHAID(fileName);

        File stagedFile = new File(join(STAGING_DIR, fileName).toString());
        //File has not been staged
        if (!stagedFile.exists()) {
            try { //File has been modified
                if (!givenFileSHAID.equals(commitFileSHAID)) {
                    stagedFile.createNewFile();
                    writeContents(stagedFile, givenFileSHAID);
                }
            } catch (IOException e) {
                message("An error occured while staging file " + fileName + " : ", e);
            }
        } else { //File has already been staged
            //File has been modified
            if (!givenFileSHAID.equals(commitFileSHAID)) {
                writeContents(stagedFile, givenFileSHAID);
            } else { //Remove file from the staging area (Condition 3)
                restrictedDelete(stagedFile);
            }
        }
    }

    public void commit(String message) {



//        storeCommitObject();
    }

    // ==================================== HELPER FUNCTIONS =================================== //

    /**
     * Creates all the required gitlet repo directories
     */
    private void setupPersistence() {
        File gitletDir = new File(GITLET_DIR.toString());
        gitletDir.mkdir();

        File commitsDir = new File(COMMIT_DIR.toString());
        commitsDir.mkdir();

        File stagingDir = new File(STAGING_DIR.toString());
        stagingDir.mkdir();

        File filesDir = new File(FILE_DIR.toString());
        filesDir.mkdir();

        File refsDir = new File(REF_DIR.toString());
        refsDir.mkdir();
    }

    /**
     * Updates the head ref and
     * the currently tracked branch
     * to point to the current commit
     *
     * @param commitID the id of the latest commit to which head should point
     */
    private void updateRefs(String commitID) {
        head = commitID;
        File headFile = new File(join(REF_DIR, "HEAD").toString());
        writeContents(headFile, head);

        // Update the currently tracked branch
        File branchFile = new File(join(REF_DIR, currentBranch).toString());
        writeContents(branchFile, head);
    }

    /**
     * Creates a new commit file and saves the latest commit object to it
     *
     * @param newCommit the commit object that needs to be saved
     */
    private void storeCommitObject(Commit newCommit) {

        //Calculate commit SHA-1 id
        String commitID = sha1(serialize(newCommit));
        newCommit.setID(commitID);

        //Make a new commit directory using the first 6 characters of the commitID
        File commitDir = new File(join(COMMIT_DIR, commitID.substring(0, 6)).toString());
        commitDir.mkdir();

        //Save the commit to a new file in the new commit dir
        //named using the remaining characters in the commitID
        String commitFileName = commitID.substring(7);
        File commitFile = new File(join(commitDir, commitFileName).toString());
        writeObject(commitFile, newCommit);

        updateRefs(commitID);
    }

    /**
     * Loads the last committed object in the repo i.e. the latest
     *
     * @return Last commited object
     */
    private Commit getLastCommitObject() {
        File headFile = new File(join(REF_DIR, "HEAD").toString());
        head = readContentsAsString(headFile);

        File commitPath = join(COMMIT_DIR, head.substring(0, 6), head.substring(7));
        return readObject(commitPath, Commit.class);
    }

//    /**
//     * Returns the SHA-1 ID of the given file
//     *
//     * @param fileName the file whose SHA-1 ID is required
//     * @return SHA-1 ID of the given file
//     */
//    private String getFileSHAID(String fileName) {
//        return Utils.sha1(Utils.serialize(Utils.join(CWD, fileName).toString()));
//    }

//    /** Creates a new branch
//     *
//     * @param name the name of the new branch to be created
//     * */
//    private void createBranch(String name){
//        File branchFile = new File(Utils.join(REF_DIR, name).toString());
//        Utils.writeObject(branchFile, head);
//    }
}
