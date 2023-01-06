package gitlet;

import java.io.File;
import java.util.Date;

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
     * The refs directory
     * (Contains refs such as current head commit, branch commit refs such as master)
     */
    static final File REF_DIR = join(GITLET_DIR, "refs");
    /**
     * Points to the latest commit in the current branch
     */
    private String head;

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
            Utils.message("A Gitlet version-control system already "
                    + "exists in the current directory.");
            System.exit(0);
        }
        setupPersistence();

        //Initialize repo with first commit //
        Date d = new Date(0);
        Commit newCommit = new Commit("Initial message", null, d, null, null);
        commit(newCommit);
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
        Utils.writeObject(headFile, head);

        // Update the currently tracked branch
        File branchFile = new File(Utils.join(REF_DIR, currentBranch).toString());
        Utils.writeObject(branchFile, head);
    }

    /**
     * Creates a new commit file and saves the latest commit to it
     *
     * @param newCommit the commit object that needs to be saved
     */
    private void commit(Commit newCommit) {

        //Calculate commit SHA-1 id
        String commitID = Utils.sha1(Utils.serialize(newCommit));
        newCommit.setID(commitID);

        //Make a new commit directory using the first 6 characters of the commitID
        File commitDir = new File(join(COMMIT_DIR, commitID.substring(0, 6)).toString());
        commitDir.mkdir();

        //Save the commit to a new file in the new commit dir
        //named using the remaining characters in the commitID
        String commitFileName = commitID.substring(7);
        File commitFile = new File(join(commitDir, commitFileName).toString());
        Utils.writeObject(commitFile, newCommit);

        updateRefs(commitID);
    }

//    /** Creates a new branch
//     *
//     * @param name the name of the new branch to be created
//     * */
//    private void createBranch(String name){
//        File branchFile = new File(Utils.join(REF_DIR, name).toString());
//        Utils.writeObject(branchFile, head);
//    }
}
