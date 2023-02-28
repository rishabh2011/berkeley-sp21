package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Repository.CWD;
import static gitlet.Repository.STAGING_DIR;
import static gitlet.Utils.*;

/**
 * Performs all gitlet file staging operations
 *
 * @author Rishabh Choudhury
 */

public class StagingOperations implements Serializable, Dumpable {
    /**
     * File stores references to files marked for tracking / removal
     */
    static final File STAGED_FILES = new File(join(STAGING_DIR,
            "staged_files").toString());

    /**
     * Copy directory for storing copies of CWD files
     */
    static final File STAGED_COPY_DIR = new File(join(STAGING_DIR,
            "File Copies").toString());

    /**
     * References files that should be tracked next commit
     * onwards
     */
    private Map<String, String> addFiles = new HashMap<>();

    /**
     * References files that should be untracked
     * next commit onwards
     */
    private List<String> removeFiles = new LinkedList<>();

    /**
     * Creates a new staging file
     */
    public static void createStagingArea() {
        //Create staging_file
        StagingOperations sa = new StagingOperations();
        saveStagedFile(sa);

        //Create directory for storing copies of CWD files
        STAGED_COPY_DIR.mkdir();
    }

    /**
     * Copies given file to the staging area and marks
     * it for tracking
     *
     * @param fileName  the file that should be staged for addition
     * @param fileID sha-id corresponding to the given file
     */
    public static void stageFileForAddition(String fileName, String fileID) {
        StagingOperations stageOps = loadStagedFile();
        stageOps.addFiles.put(fileName, fileID);
        saveStagedFile(stageOps);

        //Copy CWD file to Staged Files Directory
        String fileContents = readContentsAsString(join(CWD, fileName));
        writeContents(new File(STAGED_COPY_DIR, fileName), fileContents);
    }

    /**
     * Stages the given file for removal next commit onwards
     *
     * @param fileName the file that should be staged for removal
     */
    public static void stageFileForRemoval(String fileName) {
        StagingOperations stageOps = loadStagedFile();
        stageOps.removeFiles.add(fileName);
        saveStagedFile(stageOps);
    }

    /**
     * Removes given file from the staging area
     * if it has been staged
     *
     * @param fileName the staged file that should be removed
     */
    public static void removeFromStagingArea(String fileName) {
        StagingOperations stageOps = loadStagedFile();
        stageOps.addFiles.remove(fileName);
        saveStagedFile(stageOps);
        File file = new File(STAGED_COPY_DIR, fileName);
        file.delete();
    }

    /**
     * Returns all files staged for addition
     *
     * @return files staged for addition
     */
    public static Map<String, String> getFilesStagedForAddition() {
        StagingOperations stageOps = loadStagedFile();
        return stageOps.addFiles;
    }

    /**
     * Returns all files staged for removal
     *
     * @return files staged for removal
     */
    public static List<String> getFilesStagedForRemoval() {
        StagingOperations stageOps = loadStagedFile();
        return stageOps.removeFiles;
    }

    /**
     * Deletes files in the staging area and
     * resets the staging_file
     */
    public static void clearStagingArea() {
        StagingOperations stageOps = loadStagedFile();
        stageOps.addFiles.clear();
        saveStagedFile(stageOps);

        //Delete Files in staging area
        List<String> filesInStagingArea = plainFilenamesIn(STAGED_COPY_DIR);
        if (filesInStagingArea.size() > 0) {
            for (String fileName : filesInStagingArea) {
                File file = new File(STAGED_COPY_DIR, fileName);
                file.delete();
            }
        }
    }

    // ==================================== HELPER FUNCTIONS =================================== //

    /**
     * Loads staged files from disk
     *
     * @return Object containing map of all staged files
     */
    private static StagingOperations loadStagedFile() {
        return readObject(STAGED_FILES, StagingOperations.class);
    }

    /**
     * Saves staged files to disk
     *
     * @param stageOps Staging operations object that should be saved
     */
    private static void saveStagedFile(StagingOperations stageOps) {
        writeObject(STAGED_FILES, stageOps);
    }

    @Override
    public void dump() {
        for (String key : addFiles.keySet()) {
            System.out.println(key + " : " + addFiles.get(key));
        }
    }
}
