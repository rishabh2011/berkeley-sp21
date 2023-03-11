package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.LatestCommonAncestor.findLCA;
import static gitlet.Utils.*;
import static gitlet.Helper.*;
import static gitlet.StagingOperations.*;

/**
 * Represents a gitlet repository.
 * Provides functionality for all gitlet commands.
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
     * (Contains refs such as current head commit, current branch commit
     * and the branch directory)
     */
    static final File REF_DIR = join(GITLET_DIR, "refs");
    /**
     * The Branch Directory
     */
    static final File BRANCH_DIR = join(REF_DIR, "branches");
    /**
     * Tracks the current branch
     */
    static String currentBranch = "master";

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

        //Initialize repo with first commit
        Commit newCommit = new Commit("initial commit", new Date(0));
        saveCommit(newCommit);
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

        File file = new File(join(CWD, fileName).toString());
        if (!file.exists()) {
            message("File does not exist.");
            System.exit(0);
        }

        //Calculate given file ID
        String fileContent = readContentsAsString(file);
        String fileID = sha1(fileContent);

        //HEAD commit file ID
        Commit headCommit = loadCurrentHead();
        String commitFileID = headCommit.getFileID(fileName);

        //File has been modified / is new
        if (!fileID.equals(commitFileID)) {
            stageFileForAddition(fileName, fileID);
        } else { //Remove file from the staging area (Condition 3)
            removeFromStagingArea(fileName);
        }
    }

    // ------------------------------- COMMIT ------------------------------ //

    /**
     * Creates a new commit
     *
     * @param message the message associated with this commit
     */
    public void commit(String message) {

        //if message is empty abort
        if (message.equals("")) {
            message("Please enter a commit message.");
            System.exit(0);
        }

        //no files staged
        if (getFilesStagedForAddition().size() == 0
                && getFilesStagedForRemoval().size() == 0) {
            message("No changes added to the commit");
            System.exit(0);
        }

        LinkedList<Commit> parents = new LinkedList<>();
        parents.addLast(loadCurrentHead());
        commit(message, parents);
    }

    /**
     * Create a new commit that tracks files that have been
     * created/modified/removed as reflected in the staging area
     *
     * @param message the message associated with this commit
     * @param parents the commits that should be parents of the new commit
     */
    private void commit(String message, LinkedList<Commit> parents) {

        //Create and save commit
        Commit newCommit = new Commit(message, new Date());
        newCommit.trackParent(parents);
        newCommit.trackStagedFiles(getFilesStagedForAddition());
        newCommit.untrackRemovedFiles(getFilesStagedForRemoval());
        saveCommit(newCommit);

        //Save staged files to the repository
        saveFiles(getFilesStagedForAddition());
    }

    // ---------------------------------- CHECKOUT ------------------------------ //

    /**
     * Checkout:
     * <br>1. Branch</br>
     * <br>2. file in head commit</br>
     * <br>3. file in given commit</br>
     *
     * @param args <br>1. (branch)</br>
     *             <br>2. (file) </br>
     *             <br>3. (commit, file)</br>
     */
    public void checkout(String... args) {

        switch (args.length - 1) {
            case 1:
                checkoutBranch(args[1]);
                break;
            case 2:
                Commit headCommit = loadCurrentHead();
                checkoutFileInCommit(headCommit, args[2]);
                break;
            case 3:
                Commit commit = loadCommitWithID(args[1]);
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
        //Get ID of file from tracked files
        String fileID = commit.getFileID(fileName);

        //File doesn't exist
        if (fileID == null) {
            message("File does not exist in that commit.");
            System.exit(0);
        }

        //Load file data into string from disk
        File file = new File(join(FILE_DIR, fileID.substring(0, 6),
                fileID.substring(6)).toString());
        String fileContents = readContentsAsString(file);

        //Replace CWD with checked out file contents / create new
        writeContents(join(CWD, fileName), fileContents);
    }

    /**
     * Checkout given branch
     *
     * @param branch the branch to check out
     */
    private void checkoutBranch(String branch) {

        //Check Branch exists
        List<String> branchList = plainFilenamesIn(BRANCH_DIR);
        if (!branchList.contains(branch)) {
            message("No such branch exists.");
            System.exit(0);
        }

        //Check if branch is different from current branch
        loadCurrentBranchVar();
        if (currentBranch.equals(branch)) {
            message("No need to checkout the current branch.");
            System.exit(0);
        }

        Commit currentHead = loadCurrentHead();
        File branchFile = new File(BRANCH_DIR, branch);
        String branchCommitID = readContentsAsString(branchFile);
        Commit branchHead = loadCommitWithID(branchCommitID);

        //Check any working untracked files exist
        checkUntrackedFiles(currentHead, branchHead);

        checkoutCommit(currentHead, branchHead);

        //Make checked out branch, the current branch
        currentBranch = branch;
        saveCurrentBranchVar();
    }

    /**
     * Check out all files in the given commit
     *
     * @param currentHead the current head commit
     * @param givenCommit the commit to check out
     */
    private void checkoutCommit(Commit currentHead, Commit givenCommit) {
        //Replace files in the CWD with versions tracked
        // by the checked out branch
        for (String fileName : givenCommit.trackedFiles.keySet()) {
            checkoutFileInCommit(givenCommit, fileName);
        }

        //Delete files from the CWD tracked by the current branch
        //but not tracked by the checked out branch
        for (String fileName : currentHead.trackedFiles.keySet()) {
            if (!givenCommit.trackedFiles.containsKey(fileName)) {
                File file = new File(CWD, fileName);
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        //Clear staging area
        clearStagingArea();
    }

    // ------------------------------- FIND ------------------------------ //

    /**
     * Finds all commits in repository containing the given message
     */
    public void find(String message) {
        boolean foundCommit = false;
        List<String> commitFolders = plainFolderNamesIn(COMMIT_DIR);
        for (String commitFolder : commitFolders) {
            List<String> commitFile = plainFilenamesIn(join(COMMIT_DIR, commitFolder));
            Commit commit = loadCommitWithID(commitFolder + commitFile);
            if (message.equals(commit.getMessage())) {
                foundCommit = true;
                System.out.println(commit.getID());
            }
        }

        if (!foundCommit) {
            message("Found no commit with that message.");
            System.exit(0);
        }
    }

    // ------------------------------- RM ------------------------------ //

    /**
     * Removes the given file from the CWD if it exists
     * and stops tracking file in further commits
     *
     * @param fileName the file that should be removed and untracked in the repo
     */
    public void rm(String fileName) {

        boolean fileStaged = getFilesStagedForAddition().containsKey(fileName);
        boolean fileTracked = loadCurrentHead().trackedFiles.containsKey(fileName);
        //File is neither staged nor tracked in the head commit
        if (!fileStaged && !fileTracked) {
            message("No reason to remove the file.");
            System.exit(0);
        }

        //Unstage file if it has been staged for addition
        if (fileStaged) {
            removeFromStagingArea(fileName);
        }

        //Stage file for removal if tracked by latest commit
        if (fileTracked) {
            stageFileForRemoval(fileName);
            //Remove file from CWD if not removed by user already
            File file = new File(join(CWD, fileName).toString());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    // ------------------------------- RESET ------------------------------ //

    /**
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node.
     *
     * @param commitID the id of the commit to reset to
     */
    public void reset(String commitID) {
        Commit currentHead = loadCurrentHead();
        Commit givenCommit = loadCommitWithID(commitID);
        checkUntrackedFiles(currentHead, givenCommit);
        checkoutCommit(currentHead, givenCommit);

        loadCurrentHead();
        saveBranch(currentBranch, commitID);
    }

    // ------------------------------- BRANCH ------------------------------ //

    /**
     * Creates a new branch with the given name
     *
     * @param branch the name of the branch to be created
     */
    public void branch(String branch) {
        //Branch with given name already exists
        List<String> branches = plainFilenamesIn(BRANCH_DIR);
        if (branches.contains(branch)) {
            message("A branch with that name already exists.");
            System.exit(0);
        }

        Commit headCommit = loadCurrentHead();
        saveBranch(branch, headCommit.getID());
    }

    // ------------------------------- RM BRANCH ------------------------------ //

    /**
     * Removes branch with the given name from the repository
     * <p> This only deletes the pointer associated with the branch;
     * Does not delete any commits created under the branch</p>
     *
     * @param branch the name of the branch to be removed
     */
    public void removeBranch(String branch) {
        List<String> branches = plainFilenamesIn(BRANCH_DIR);
        if (!branches.contains(branch)) {
            message("A branch with that name does not exist.");
            System.exit(0);
        }

        loadCurrentBranchVar();
        if (branch.equals(currentBranch)) {
            message("Cannot remove the current branch.");
            System.exit(0);
        }

        //Delete branch from branch dir
        File file = new File(join(BRANCH_DIR, branch).toString());
        file.delete();
    }

    // ------------------------------- MERGE ------------------------------ //

    /**
     * Merges given branch with the current branch
     *
     * @param mergeBranch branch that needs to be merged with current branch
     */
    public void merge(String mergeBranch) {

        //Failure 1: Uncommited changes
        if (getFilesStagedForAddition().size() != 0
                || getFilesStagedForRemoval().size() != 0) {
            message("You have uncommited changes.");
            System.exit(0);
        }

        //Failure 2: Branch doesn't exist
        List<String> branches = plainFilenamesIn(BRANCH_DIR);
        if (!branches.contains(mergeBranch)) {
            message("A branch with that name does not exist.");
            System.exit(0);
        }

        //Failure 3: Merge branch is the same as current branch
        loadCurrentBranchVar();
        if (currentBranch.equals(mergeBranch)) {
            message("Cannot merge a branch with itself.");
            System.exit(0);
        }

        //Find the latest common ancestor (split point)
        Commit currentHead = loadCurrentHead();
        Commit mergeHead = loadBranchHead(mergeBranch);
        Commit split = findLCA(currentHead, mergeHead);

        //Failure 4: Untracked files
        checkUntrackedFiles(currentHead, mergeHead);

        //Failure 5: Split point is the same as merge branch
        if (split.getID().equals(mergeHead.getID())) {
            message("Given branch is ancestor of the current branch.");
            System.exit(0);
        }

        // Split point is the same as current branch
        if (split.getID().equals(currentHead.getID())) {
            checkoutBranch(mergeBranch);
            message("Current branch fast-forwarded.");
            System.exit(0);
        }

        //Do Merge
        int conflictCount = 0;
        conflictCount += cmpSplitFiles(currentHead, mergeHead, split);
        conflictCount += cmpMergeFiles(currentHead, mergeHead);
        if (conflictCount > 0) {
            System.out.println("Encountered a merge conflict.");
        }

        //Commit merge
        LinkedList<Commit> parents = new LinkedList<>();
        parents.addLast(currentHead);
        parents.addLast(mergeHead);
        commit("Merged " + mergeBranch + " into " + currentBranch + ".", parents);
    }

    /**
     * Compares files in split commit with that of the current head and merge head commits
     *
     * @param currentHead the current head commit
     * @param mergeHead   the merge head commit
     * @param split       the split commit
     * @return total number of conflicted files
     */
    private int cmpSplitFiles(Commit currentHead, Commit mergeHead, Commit split) {

        int conflictCount = 0;
        for (String file : split.trackedFiles.keySet()) {
            String splitID = split.trackedFiles.get(file);
            String currID = currentHead.trackedFiles.get(file);
            String mergeID = mergeHead.trackedFiles.get(file);

            switch (mergeFileStates(splitID, currID, mergeID)) {
                case 1:
                    rm(file);
                    break;
                case 2:
                    ++conflictCount;
                    processConflictedFile(file, currID, mergeID, currentHead, mergeHead);
                    break;
                case 3:
                    checkoutFileInCommit(mergeHead, file);
                    stageFileForAddition(file, mergeID);
                    break;
                default:
                    break;
            }
            mergeHead.trackedFiles.remove(file);
        }
        return conflictCount;

    }

    /**
     * Compares files in merge commit that are absent in the split commit
     * with current commit files
     *
     * @param currentHead the current head commit
     * @param mergeHead   the merge head commit
     * @return total number of conflicted files
     */
    private int cmpMergeFiles(Commit currentHead, Commit mergeHead) {

        int conflictCount = 0;
        for (String file : mergeHead.trackedFiles.keySet()) {
            String currID = currentHead.trackedFiles.get(file);
            String mergeID = mergeHead.trackedFiles.get(file);

            switch (mergeFileStates(null, currID, mergeID)) {
                case 2:
                    ++conflictCount;
                    processConflictedFile(file, currID, mergeID, currentHead, mergeHead);
                    break;
                case 4:
                    checkoutFileInCommit(mergeHead, file);
                    stageFileForAddition(file, mergeID);
                    break;
                default:
                    break;
            }
        }
        return conflictCount;

    }

    /**
     * Compares file states of all three commits involved in the merging process
     * and returns an int value accordingly.
     *
     * <p>
     * <strong>Condition 1:</strong>
     * <br>-File absent in merge branch and unmodified in current branch</br>
     *
     * <br><strong>Condition 2: Conflicts</strong></br>
     * <br>-File absent in current branch and modified in merge branch</br>
     * <br>-File absent in merge branch and modified in current branch</br>
     * <br>-File present in split point and modified differently in both branches</br>
     * <br>-File absent in split point and modified differently in both branches</br>
     *
     * <br><strong>Condition 3:</strong></br>
     * <br>-File modified in merge branch and unmodified in current branch</br>
     *
     * <br><strong>Condition 4:</strong></br>
     * <br>-File absent in split point and present only in merge branch</br>
     * </p>
     *
     * @param splitID the split commit version of a file
     * @param currID  the current head commit version of a file
     * @param mergeID the merge head commit version of a file
     * @return value corresponding to the appropriate file states | 0 as default
     */
    private int mergeFileStates(String splitID, String currID, String mergeID) {

        if (splitID != null) {
            //File absent in current branch and present in merge branch
            if (currID == null && mergeID != null) {
                //8. Modified in given branch
                if (!splitID.equals(mergeID)) {
                    return 2;
                }
            }

            //File absent in merge branch and present in current branch
            if (mergeID == null && currID != null) {
                //6. Unmodified in current branch
                if (splitID.equals(currID)) {
                    return 1;
                }

                //8. Modified in current branch
                return 2;
            }

            //File modified in merge branch
            if (mergeID != null && !splitID.equals(mergeID)) {
                //1. Unmodified in current branch
                if (splitID.equals(currID)) {
                    return 3;
                }

                //8. Modified differently in both branches
                return 2;
            }
        } else {
            //5. File present only in given branch
            if (currID == null) {
                return 4;
            }

            //8. File present in both branches and modified differently
            if (!mergeID.equals(currID)) {
                return 2;
            }
        }

        return 0;
    }

    /**
     * Processes a conflicted file encountered during a merge operation
     *
     * @param fileName    the conflicted file
     * @param currID      the id of the conflicted file in the current head
     * @param mergeID     the id of the conflicted file in the merge head
     * @param currentHead the current head commit
     * @param mergeHead   the merge head commit
     */
    private void processConflictedFile(String fileName, String currID, String mergeID,
                                       Commit currentHead, Commit mergeHead) {
        String currentFileContents = "";
        String mergeFileContents = "";

        if (currID != null) {
            checkoutFileInCommit(currentHead, fileName);
            File cwdFile = new File(CWD, fileName);
            currentFileContents = readContentsAsString(cwdFile);
        }
        if (mergeID != null) {
            checkoutFileInCommit(mergeHead, fileName);
            File cwdFile = new File(CWD, fileName);
            mergeFileContents = readContentsAsString(cwdFile);
        }

        File file = join(CWD, fileName);
        String output = "<<<<<<< HEAD\n" + currentFileContents + "=======\n"
                + mergeFileContents + ">>>>>>>\n";
        writeContents(file, output);
        add(fileName);
    }

    // ------------------------------- LOG ------------------------------ //

    /**
     * Displays commit history of the currently active branch
     */
    public void log() {
        Commit headCommit = loadCurrentHead();
        log(headCommit);
    }

    /**
     * Recursively iterate through all commits and print relevant info
     */
    private void log(Commit commit) {
        if (commit == null) {
            return;
        }

        //Display commit info
        commit.printCommitInfo();
        if (!commit.getParentIDs().isEmpty()) {
            String commitID = commit.getParentIDs().get(0);
            //Load first parent commit file from disk
            File commitFile = new File(join(COMMIT_DIR, commitID.substring(0, 6),
                    commitID.substring(6)).toString());
            log(readObject(commitFile, Commit.class));
        }
    }

    // ------------------------------- GLOBAL LOG ------------------------------ //

    /**
     * Displays commit history of the entire repository across all branches
     * in no particular order
     */
    public void globalLog() {
        List<String> commitFolders = plainFolderNamesIn(COMMIT_DIR);
        for (String commitFolder : commitFolders) {
            List<String> commitFile = plainFilenamesIn(join(COMMIT_DIR, commitFolder));
            Commit commit = loadCommitWithID(commitFolder + commitFile);
            commit.printCommitInfo();
        }
    }

    // ------------------------------- STATUS ------------------------------ //

    /**
     * Display the current status of the git repository
     */
    public void status() {
        System.out.println("=== Branches ===");
        List<String> branches = plainFilenamesIn(BRANCH_DIR);
        loadCurrentBranchVar();
        for (String branch : branches) {
            if (branch.equals(currentBranch)) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        List<String> sortedFiles = new ArrayList<>(getFilesStagedForAddition().keySet());
        Collections.sort(sortedFiles);
        for (String file : sortedFiles) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        sortedFiles = new ArrayList<>(getFilesStagedForRemoval());
        Collections.sort(sortedFiles);
        for (String file : sortedFiles) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        SortedSet<String> modifiedFiles = new TreeSet<>();
        Map<String, String> stagedFiles = getFilesStagedForAddition();
        Commit headCommit = loadCurrentHead();
        for (String file : headCommit.trackedFiles.keySet()) {
            File cwdFile = new File(CWD, file);
            if (cwdFile.exists()) {
                String fileContent = readContentsAsString(cwdFile);
                String fileID = sha1(fileContent);
                //File tracked by current commit, modified in CWD and not staged
                if (!fileID.equals(headCommit.trackedFiles.get(file))
                        && !stagedFiles.containsKey(file)) {
                    modifiedFiles.add(file + " (modified)");
                }
                //File tracked by current commit, removed from CWD and not staged for removal
            } else if (!getFilesStagedForRemoval().contains(file)) {
                modifiedFiles.add(file + " (deleted)");
            }
        }
        for (String file : stagedFiles.keySet()) {
            File cwdFile = new File(CWD, file);
            if (cwdFile.exists()) {
                String fileContent = readContentsAsString(cwdFile);
                String fileID = sha1(fileContent);
                //File staged and then modified differently in CWD
                if (!fileID.equals(stagedFiles.get(file))) {
                    modifiedFiles.add(file + " (modified)");
                }
                //File staged and then removed from CWD
            } else {
                modifiedFiles.add(file + " (deleted)");
            }
        }
        for (String file : modifiedFiles) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        List<String> cwdFiles = plainFilenamesIn(CWD);
        if (cwdFiles != null) {
            for (String file : cwdFiles) {
                if (!headCommit.trackedFiles.containsKey(file) && !stagedFiles.containsKey(file)) {
                    System.out.println(file);
                }
            }
        }
        System.out.println();
    }

    /**
     * Checks for any untracked files in current head commit
     * that can potentially be overwritten or removed by
     * a checkout branch or reset or merge operation
     *
     * @param currentHead head commit of the current branch
     * @param givenHead   the commit to check with
     */
    private void checkUntrackedFiles(Commit currentHead, Commit givenHead) {
        List<String> cwdFiles = plainFilenamesIn(CWD);
        if (cwdFiles != null) {
            for (String file : cwdFiles) {
                if (!currentHead.trackedFiles.containsKey(file)
                        && givenHead.trackedFiles.containsKey(file)) {
                    message("There is an untracked file in the way; delete it, "
                            + "or add and commit it first.");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Checks if a gitlet repository has been made
     * and informs the user to make one first if it doesn't exist
     */
    public void checkRepoExists() {
        File gitletDir = new File(GITLET_DIR.toString());
        //Check repo exists
        if (!gitletDir.exists()) {
            message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
