package deque;
import edu.princeton.cs.algs4.Stopwatch;

public class TimeLinkedListDeque {
    private static void printTimingTable(LinkedListDeque<Integer> Ns, LinkedListDeque<Double> times, LinkedListDeque<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeLinkedListDequeConstruction();
    }

    public static void timeLinkedListDequeConstruction() {

        LinkedListDeque<Integer> Ns = new LinkedListDeque<>();
        LinkedListDeque<Double> times = new LinkedListDeque<>();

        for(int n = 1000; n <= 128000; n *= 2){
            LinkedListDeque<Integer> tempAList = new LinkedListDeque<>();

//            Stopwatch sw = new Stopwatch();
            for(int i = 1; i <= n; i++){
                tempAList.addFirst(i);
            }

            Stopwatch sw = new Stopwatch();
            for(int i = 1; i <= n; i++){
                tempAList.removeLast();
            }

            Ns.addLast(n);
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }

        printTimingTable(Ns, times, Ns);
    }
}
