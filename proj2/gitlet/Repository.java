package gitlet;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;
import static gitlet.StagingOperations.STAGED_COPY_DIR;

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
     * Tracks the current branch
     */
    private String currentBranch = "master";

    // ----------------------------- INIT ------------------------------ //

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
        Commit newCommit = new Commit("initial commit", new Date(0));
        saveCommitObject(newCommit);
    }

    // ------------------------------ ADD ------------------------------ //

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
        if (!givenFile.exists()) {
            message("File does not exist.");
            System.exit(0);
        }

        //Calcuate given file content SHA-1 id;
        String givenFileContents = readContentsAsString(givenFile);
        String givenFileSHAID = sha1(serialize(givenFileContents));
        //Get the same file's previous content SHA-1 id if it exists
        Commit lastCommit = getLatestCommitObject();
        String commitFileSHAID = lastCommit.getFileSHAID(fileName);

        //File has been modified / is new
        if (!givenFileSHAID.equals(commitFileSHAID)) {
            StagingOperations.stageFileForAddition(fileName, givenFileSHAID);
        } else { //Remove file from the staging area (Condition 3)
            StagingOperations.removeStagedFile(fileName);
        }
    }

    // ------------------------------- COMMIT ------------------------------ //

    /**
     * Create a new commit that tracks files that have been
     * created/modified/removed as reflected in the staging area
     *
     * @param message the message associated with this commit
     */
    public void commit(String message) {

        //if message is null abort
        if (message == null) {
            message("Please enter a commit message.");
            System.exit(0);
        }

        Map<String, String> filesStagedForAddition = StagingOperations.getFilesStagedForAddition();
        List<String> filesStagedForRemoval = StagingOperations.getFilesStagedForRemoval();
        //no files staged
        if (filesStagedForAddition.size() == 0 && filesStagedForRemoval.size() == 0) {
            message("No changes added to the commit");
            System.exit(0);
        }

        //Create and save commit
        Commit parentCommit = getLatestCommitObject();
        Commit newCommit = new Commit(message, new Date());
        newCommit.setParentValues(parentCommit);
        newCommit.trackStagedFiles(filesStagedForAddition);
        newCommit.untrackRemovedFiles(filesStagedForRemoval);
        saveCommitObject(newCommit);

        //Save staged files to the repository
        saveFiles(filesStagedForAddition);
    }

    // ---------------------------------- CHECKOUT ------------------------------ //

    /**
     * Checkout:
     * <br>1. file in head commit</br>
     * <br>2. file in given commit</br>
     * <br>3. Branch</br>
     *
     * @param args <br>1. (file) </br>
     *             <br>2. (commit, file)</br>
     *             <br>3. (branch)</br>
     */
    public void checkout(String... args) {

        switch (args.length - 1) {
            case 1:
                checkoutBranch(args[1]);
                break;
            case 2:
                Commit latestCommit = getLatestCommitObject();
                checkoutFileInCommit(latestCommit, args[2]);
                break;
            case 3:
                //Check folder exists
                File commitFolder = new File(join(COMMIT_DIR, args[1].substring(0, 6)).toString());
                if (!commitFolder.exists()) {
                    message("No commit with that id exists.");
                    System.exit(0);
                }
                //Get the commit file in commit folder
                List<String> files = plainFilenamesIn(commitFolder);
                File commitFile = new File(join(commitFolder, files.get(0)).toString());
                //Load commit object in commitFile
                Commit commit = readObject(commitFile, Commit.class);
                checkoutFileInCommit(commit, args[3]);
                break;
            default:
                break;
        }
    }

    /**
     * Checkout given file in given commit
     *
     * @param commit   the commit from which to check out given file
     * @param fileName file to check out
     */
    private void checkoutFileInCommit(Commit commit, String fileName) {
        //Get hash id of file from tracked files
        String fileSHAID = commit.getFileSHAID(fileName);

        //If file doesn't exist
        if (fileSHAID == null) {
            message("File does not exist in that commit.");
            System.exit(0);
        }

        //Load file data into string from disk
        File file = new File(join(FILE_DIR, fileSHAID.substring(0, 6),
                fileSHAID.substring(6)).toString());
        String fileContents = readContentsAsString(file);

        //Replace CWD with checked out file contents
        writeContents(join(CWD, fileName), fileContents);
    }

    /**
     * Checkout given branch
     *
     * @param branchName the branch to check out
     */
    private void checkoutBranch(String branchName) {

        //TODO: Finish function

        //Check Branch exists
        List<String> branchList = plainFilenamesIn(REF_DIR);
        if (!branchList.contains(branchName)) {
            message("File does not exist in that commit");
            System.exit(0);
        }

        //Check Branch is different from current branch


        //Replace files in the CWD with versions tracked
        // by the checked out branch

        //Delete files from the CWD tracked by the current branch
        //but are not tracked by the checked out branch

        //Make checked out branch, the current branch

    }

    // ------------------------------- LOG ------------------------------ //

    /**
     * Display information on all commits made so far
     */
    public void log() {
        Commit latestCommit = getLatestCommitObject();
        latestCommit.printCommitInfo();
        log(latestCommit.getFirstParentID());
    }

    /**
     * Recursively iterate through all commits and print relevant info
     */
    private void log(String commitID) {
        if (commitID == null) {
            return;
        }
        //Load commit file from disk
        File commitFile = new File(join(COMMIT_DIR, commitID.substring(0, 6),
                commitID.substring(6)).toString());
        Commit commit = readObject(commitFile, Commit.class);

        //Display commit info
        commit.printCommitInfo();
        log(commit.getFirstParentID());
    }

    // ------------------------------- RM ------------------------------ //

    public void rm(String fileName) {

        boolean fileStaged = StagingOperations.getFilesStagedForAddition().containsKey(fileName);
        boolean fileTracked = getLatestCommitObject().getTrackedFiles().containsKey(fileName);
        //File is neither staged nor tracked in the latest commit
        if (!fileStaged && !fileTracked) {
            message("No reason to remove the file.");
            System.exit(0);
        }

        //Unstage file if it has been staged for addition
        if (fileStaged) {
            StagingOperations.removeStagedFile(fileName);
        }

        //Stage file for removal if tracked by latest commit
        if (fileTracked) {
            StagingOperations.stageFileForRemoval(fileName);
        }

        //Remove file from CWD if not removed by user already
        File file = new File(join(CWD, fileName).toString());
        if (file.exists()) {
            file.delete();
        }
    }

    // ==================================== HELPER FUNCTIONS =================================== //

    /**
     * Checks if a gitlet repository has been made
     * and informs the user to make one first if it doesn't exist
     */
    public void checkRepoExists() {
        File gitletDir = new File(GITLET_DIR.toString());
        //Check repo exists
        if (!gitletDir.exists()) {
            message("No gitlet repository exists. Please create a repository first using"
                    + " the \"java gitlet.Main init\" command");
            System.exit(0);
        }
    }

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
        StagingOperations.createStagingArea();

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
    private void saveCommitObject(Commit newCommit) {

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

        updateRefs(commitID);
    }

    /**
     * Loads the latest committed object in the repo
     *
     * @return Latest commited object
     */
    private Commit getLatestCommitObject() {
        File headFile = new File(join(REF_DIR, "HEAD").toString());
        head = readContentsAsString(headFile);

        File commitPath = join(COMMIT_DIR, head.substring(0, 6), head.substring(6));
        return readObject(commitPath, Commit.class);
    }

    /**
     * Saves all staged working directory files to the repo
     * and clears the staging area
     *
     * @param stagedFiles list of files added to the staging area
     */
    private void saveFiles(Map<String, String> stagedFiles) {

        for (String fileName : stagedFiles.keySet()) {
            String fileSHAID = stagedFiles.get(fileName);
            //Make a new folder using the first 6 characters of the fileSHAID
            File newFolder = new File(join(FILE_DIR, fileSHAID.substring(0, 6)).toString());
            newFolder.mkdir();

            //Save the staged copy of the working directory file to a new file in a new folder
            //named using the remaining characters in the fileSHAID
            String saveFileName = fileSHAID.substring(6);
            File saveFile = new File(join(newFolder, saveFileName).toString());
            String fileContents = readContentsAsString(join(STAGED_COPY_DIR, fileName));
            writeContents(saveFile, fileContents);
        }
        //Clear staging area
        StagingOperations.clearStagingArea();
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
