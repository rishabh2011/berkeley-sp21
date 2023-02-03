package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
    static final File COPY_DIR = new File(join(STAGING_DIR,
            "File Copies").toString());

    /**
     * References files that should be tracked next commit
     * onwards
     */
    private Map<String, String> trackFiles = new HashMap<>();

    /**
     * References files that should be untracked
     * next commit onwards
     */
    private Map<String, String> untrackFiles = new HashMap<>();

    /**
     * Creates a new staging file
     */
    public static void createStagingArea() {
        //Create staging_file
        StagingOperations sa = new StagingOperations();
        saveStagedFile(sa);

        //Create directory for storing copies of CWD files
        COPY_DIR.mkdir();
    }

    /**
     * Copies given file to the staging area and marks
     * it for tracking
     *
     * @param fileName  the file that should be staged
     * @param fileSHAID sha-id corresponding to the given file
     */
    public static void stageFile(String fileName, String fileSHAID) {
        StagingOperations stageOps = loadStagedFile();
        stageOps.trackFiles.put(fileName, fileSHAID);
        saveStagedFile(stageOps);

        //Copy CWD file to Staged Files Directory
        String fileContents = readContentsAsString(join(CWD, fileName));
        writeContents(new File(COPY_DIR, fileName), fileContents);
    }

    /**
     * Removes given file from the staging area
     * if it has been staged
     *
     * @param fileName the staged file that should be removed
     */
    public static void removeStagedFile(String fileName) {
        //Load data
        StagingOperations stageOps = loadStagedFile();
        //Modify
        stageOps.trackFiles.remove(fileName);
        //Save to disk
        saveStagedFile(stageOps);
        //Delete File
        File file = new File(COPY_DIR, fileName);
        file.delete();
    }

    /**
     * Returns a map representing all staged files and their corresponding SHA-ids
     *
     * @return staged files
     */
    public static Map<String, String> getStagedFiles() {
        StagingOperations stageOps = loadStagedFile();
        return stageOps.trackFiles;
    }

    /**
     * Deletes files in the staging area and
     * resets the staging_file
     */
    public static void clearStagingArea() {
        StagingOperations stageOps = loadStagedFile();
        stageOps.trackFiles.clear();
        saveStagedFile(stageOps);

        //Delete Files in staging area
        List<String> filesInStagingArea = plainFilenamesIn(COPY_DIR);
        if (filesInStagingArea.size() > 0) {
            for (String fileName : filesInStagingArea) {
                File file = new File(COPY_DIR, fileName);
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
        for (String key : trackFiles.keySet()) {
            System.out.println(key + " : " + trackFiles.get(key));
        }
    }
}
