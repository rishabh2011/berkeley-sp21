package gitlet;

import java.util.HashSet;

import static gitlet.Helper.*;

/**
 * Class finds the latest common ancestor of two given branches i.e their common split point
 */
class LatestCommonAncestor {

    /**
     * hash set to store all parent commits of branch 1
     */
    static HashSet<String> branch1Commits = new HashSet<>();

    /**
     * Stores the latest common ancestor of branch1 and branch2
     */
    static Commit lca;

    /**
     * Returns the latest common ancestor of given branches
     *
     * @param branch1Head head commit of branch 1
     * @param branch2Head head commit of branch 2
     * @return the latest common ancestor commit of both branches
     */
    static Commit findLCA(Commit branch1Head, Commit branch2Head) {
        dfsCurrent(branch1Head);
        dfsMerge(branch2Head);
        return lca;
    }

    /**
     * Performs depth first search on the commit tree
     * starting from branch1 head adding found
     * commits to {@code branch1Commits} set
     */
    private static void dfsCurrent(Commit commit) {
        branch1Commits.add(commit.getID());
        for (String parentCommitID : commit.getParentIDs()) {
            if (!branch1Commits.contains(parentCommitID)) {
                dfsCurrent(loadCommitWithID(parentCommitID));
            }
        }
    }

    /**
     * Performs depth first search on the commit tree
     * starting from branch2 head. Returns as soon
     * as the latest common ancestor is found
     */
    private static void dfsMerge(Commit commit) {

        //Common commit
        if (branch1Commits.contains(commit.getID())) {
            //Already found a LCA
            if (lca != null) {
                //this commit is more recent than lca i.e. latest
                if (commit.getDate().compareTo(lca.getDate()) > 0) {
                    lca = commit;
                }
            } else {
                lca = commit;
            }
            return;
        }

        for (String parentCommitID : commit.getParentIDs()) {
            Commit parentCommit = loadCommitWithID(parentCommitID);
            dfsMerge(parentCommit);
        }
    }
}
