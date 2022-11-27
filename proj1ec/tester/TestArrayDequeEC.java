package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {

    @Test
    public void randomizedTest(){
        ArrayDequeSolution<Integer> corrDeque = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> stDeque = new StudentArrayDeque<>();
        StringBuilder operationHistory = new StringBuilder();

        int n = 500;
        int randVal;
        for(int i = 0; i < n; ++i){
            int operation = StdRandom.uniform(0, 4);
            switch(operation){
                case 0: //addFirst()
                    randVal = StdRandom.uniform(0, 100);
                    operationHistory.append("addFirst(" + randVal + ")\n");

                    corrDeque.addFirst(randVal);
                    stDeque.addFirst(randVal);
                    assertEquals(corrDeque.size(), stDeque.size());
                    break;
                case 1: //addLast()
                    randVal = StdRandom.uniform(0, 100);
                    operationHistory.append("addLast(" + randVal + ")\n");

                    corrDeque.addLast(randVal);
                    stDeque.addLast(randVal);
                    assertEquals(corrDeque.size(), stDeque.size());
                    break;
                case 2: //removeFirst()
                    if(corrDeque.size() != 0 && stDeque.size() != 0){
                        Integer x = corrDeque.removeFirst();
                        Integer y = stDeque.removeFirst();
                        operationHistory.append("removeFirst()\n");

                        assertEquals(operationHistory.toString(), x, y);
                        assertEquals(corrDeque.size(), stDeque.size());
                    }
                    break;
                case 3: //removeLast()
                    if(corrDeque.size() != 0 && stDeque.size() != 0) {
                        Integer x = corrDeque.removeLast();
                        Integer y = stDeque.removeLast();
                        operationHistory.append("removeLast()\n");

                        assertEquals(operationHistory.toString(), x, y);
                        assertEquals(corrDeque.size(), stDeque.size());
                    }
                    break;
            }
        }
    }
}
