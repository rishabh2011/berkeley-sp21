package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> corrAList = new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();

        corrAList.addLast(3);
        buggyAList.addLast(3);
        corrAList.addLast(4);
        buggyAList.addLast(4);
        corrAList.addLast(5);
        buggyAList.addLast(5);

        assertEquals(corrAList.size(), buggyAList.size());

        assertEquals(corrAList.removeLast(), buggyAList.removeLast());
        assertEquals(corrAList.removeLast(), buggyAList.removeLast());
        assertEquals(corrAList.removeLast(), buggyAList.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> corrAList = new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; ++i) {
            int operationNumber = StdRandom.uniform(0, 4);
            //Add Last
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                corrAList.addLast(randVal);
                buggyAList.addLast(randVal);
            }
            //Size
            else if (operationNumber == 1) {
                assertEquals(corrAList.size(), buggyAList.size());
            }
            //Get Last
            else if (operationNumber == 2) {
                if(corrAList.size() > 0){
                    assertEquals(corrAList.getLast(), buggyAList.getLast());
                }
            }
            //Remove Last
            else if (operationNumber == 3) {
                if(corrAList.size() > 0){
                    assertEquals(corrAList.removeLast(), buggyAList.removeLast());
                }
            }
        }
    }
}
