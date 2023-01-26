package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.*;

/**
 * Performs all gitlet file staging operations
 *
 * @author Rishabh Choudhury
 */

public class StagingOperations implements Serializable, Dumpable {
    /**
     * Maps staged file names to their respective SHA-1 ids
     */
    private Map<String, String> stagedFilesMap = new HashMap<>();
    /**
     * Stores a reference to the staging file on disk
     */
    private static final File STAGED_FILES = new File(join(System.getProperty("user.dir"),
            ".gitlet", "staging", "staged_files").toString());

    /**
     * Creates a new staging file in memory if it doesn't already exist
     */
    public static void createStagingFile() {
        try {
            if (!(STAGED_FILES.exists())) {
                STAGED_FILES.createNewFile();
                StagingOperations sa = new StagingOperations();
                saveStagedFile(sa);
            }
        } catch (IOException e) {
            message("An error occured while creating staging file : " + e);
        }
    }

    /** Stages given file
     *
     * @param fileName the file that should be staged
     * @param fileSHAID sha-id corresponding to the given file
     * */
    public static void stageFile(String fileName, String fileSHAID) {
        StagingOperations stageOps = loadStagedFile();
        stageOps.stagedFilesMap.put(fileName, fileSHAID);
        //Save to disk
        saveStagedFile(stageOps);
    }

    /** Removes given file if it has been staged
     *
     * @param fileName the staged file that should be removed
     * */
    public static void removeStagedFile(String fileName) {
        //Load data
        StagingOperations stageOps = loadStagedFile();
        //Modify
        stageOps.stagedFilesMap.remove(fileName);
        //Save to disk
        saveStagedFile(stageOps);
    }

    /** Returns a map representing all staged files and their corresponding SHA-ids
     *
     * @return staged files
     * */
    public static Map<String, String> getStagedFiles() {
        StagingOperations stageOps = loadStagedFile();
        return stageOps.stagedFilesMap;
    }

    /** Clear the staging area */
    public static void clearStagingArea() {
        StagingOperations stageOps = loadStagedFile();
        stageOps.stagedFilesMap.clear();
        saveStagedFile(stageOps);
    }

    // ==================================== HELPER FUNCTIONS =================================== //

    /** Loads staged files from disk
     *
     * @return Object containing map of all staged files
     * */
    private static StagingOperations loadStagedFile() {
        return readObject(STAGED_FILES, StagingOperations.class);
    }

    /** Saves staged files to disk
     *
     * @param stageOps Staging operations object that should be saved
     * */
    private static void saveStagedFile(StagingOperations stageOps) {
        writeObject(STAGED_FILES, stageOps);
    }

    @Override
    public void dump() {
        for (String key : stagedFilesMap.keySet()) {
            System.out.println(key + " : " + stagedFilesMap.get(key));
        }
    }
}
