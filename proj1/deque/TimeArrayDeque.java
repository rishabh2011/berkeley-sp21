package deque;

import edu.princeton.cs.algs4.Stopwatch;

public class TimeArrayDeque {

    private final double C = 1e6;

    private static void printTimingTable(ArrayDeque<Integer> ns, ArrayDeque<Double> times,
                                         ArrayDeque<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < ns.size(); i += 1) {
            int N = ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeArrayDequeConstruction();
    }

    public static void timeArrayDequeConstruction() {

        ArrayDeque<Integer> ns = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();

        for (int n = 1000; n <= 128000; n *= 2) {
            ArrayDeque<Integer> tempAList = new ArrayDeque<>();

            Stopwatch sw = new Stopwatch();
            for (int i = 1; i <= n; i++) {
                tempAList.addFirst(i);
            }

//            Stopwatch sw = new Stopwatch();
            for (int i = 1; i <= n; i++) {
                tempAList.removeLast();
            }

            ns.addLast(n);
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }

        printTimingTable(ns, times, ns);
    }
}
