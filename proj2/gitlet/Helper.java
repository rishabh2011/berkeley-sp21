package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

/**
 * Provides various helper functions for saving and loading operations on
 * the gitlet repository
 */
public class Helper {

    /**
     * Creates all the required gitlet repo directories
     */
    static void setupPersistence() {
        File gitletDir = new File(GITLET_DIR.toString());
        gitletDir.mkdir();

        File commitsDir = new File(COMMIT_DIR.toString());
        commitsDir.mkdir();

        File stagingDir = new File(STAGING_DIR.toString());
        stagingDir.mkdir();
        StagingOperations.createStagingArea();

        File filesDir = new File(FILE_DIR.toString());
        filesDir.mkdir();

        File refsDir = new File(REF_DIR.toString());
        refsDir.mkdir();
        saveCurrentBranchVar();

        File branchDir = new File(BRANCH_DIR.toString());
        branchDir.mkdir();
    }

    // ------------ Loading head commit --------------- //

    /**
     * Loads the current HEAD commit in the repo i.e. the head commit of the
     * currently active branch
     *
     * @return head commit
     */
    static Commit loadCurrentHead() {
        loadCurrentBranchVar();
        return loadBranchHead(currentBranch);
    }

    // ------------ Saving and Loading Branches --------------- //

    /**
     * Updates current branch variable to point to the active branch and saves it
     */
    static void saveCurrentBranchVar() {
        File currentBranchFile = new File(REF_DIR, "current branch");
        writeContents(currentBranchFile, currentBranch);
    }

    /**
     * Loads the current branch value into the currentBranch variable
     */
    static void loadCurrentBranchVar() {
        File currentBranchFile = new File(join(REF_DIR, "current branch").toString());
        currentBranch = readContentsAsString(currentBranchFile);
    }

    /**
     * Updates and saves the given branch ref
     * to point to the given commit
     *
     * @param branch the branch to save
     * @param commitID the commit id to save as given branch head
     */
    static void saveBranch(String branch, String commitID) {
        File branchFile = new File(join(BRANCH_DIR, branch).toString());
        writeContents(branchFile, commitID);
    }

    /**
     * Loads the head commit of the given branch
     *
     * @param branch the branch to load from memory
     */
    static Commit loadBranchHead(String branch) {
        File branchFile = new File(BRANCH_DIR, branch);
        String branchCommitID = readContentsAsString(branchFile);
        return loadCommitWithID(branchCommitID);
    }

    // ------------ Saving and Loading Commits --------------- //

    /**
     * Creates a new commit file and saves it
     *
     * @param newCommit the commit that needs to be saved
     */
    static void saveCommit(Commit newCommit) {

        //Calculate commit SHA-1 id
        String commitID = sha1(serialize(newCommit));
        newCommit.setID(commitID);

        //Make a new commit directory using the first 6 characters of the commitID
        File commitDir = new File(join(COMMIT_DIR, commitID.substring(0, 6)).toString());
        commitDir.mkdir();

        //Save the commit to a new file in the new commit dir
        //named using the remaining characters in the commitID
        String commitFileName = commitID.substring(6);
        File commitFile = new File(join(commitDir, commitFileName).toString());
        writeObject(commitFile, newCommit);

        loadCurrentBranchVar();
        saveBranch(currentBranch, commitID);
    }

    /**
     * Loads the commit defined by the commit id
     *
     * @param commitID the id of the commit that needs to be loaded
     */
    static Commit loadCommitWithID(String commitID) {
        //Check folder exists
        File commitFolder = new File(join(COMMIT_DIR, commitID.substring(0, 6)).toString());
        if (!commitFolder.exists()) {
            message("No commit with that id exists.");
            System.exit(0);
        }
        //Get the commit file in commit folder
        List<String> files = plainFilenamesIn(commitFolder);
        File commitFile = new File(join(commitFolder, files.get(0)).toString());
        //Load commit object in commitFile
        Commit commit = readObject(commitFile, Commit.class);
        return commit;
    }

    // ------------ Saving CWD Files --------------- //

    /**
     * Saves all staged working directory files to the repo
     * and clears the staging area
     *
     * @param stagedFiles list of files added to the staging area
     */
    static void saveFiles(Map<String, String> stagedFiles) {

        for (String fileName : stagedFiles.keySet()) {
            String fileSHAID = stagedFiles.get(fileName);
            //Make a new folder using the first 6 characters of the fileSHAID
            File newFolder = new File(join(FILE_DIR, fileSHAID.substring(0, 6)).toString());
            newFolder.mkdir();

            //Save the staged copy of the working directory file to a new file in a new folder
            //named using the remaining characters in the fileSHAID
            String saveFileName = fileSHAID.substring(6);
            File saveFile = new File(join(newFolder, saveFileName).toString());
            String fileContents = readContentsAsString(join(StagingOperations.STAGED_COPY_DIR,
                    fileName));
            writeContents(saveFile, fileContents);
        }
        //Clear staging area
        StagingOperations.clearStagingArea();
    }
}
