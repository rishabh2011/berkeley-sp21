package deque;

import java.util.Iterator;

public class Solution {

    public static void main(String[] args) {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
//        ad.addLast(5);
//        ad.addFirst(3);
//        ad.addLast(10);
//        ad.addFirst(6);
//        ad.addLast(8);

        for (Integer item : ad) {
            System.out.println(item);
        }

        Iterator<Integer> seer = ad.iterator();
        while (seer.hasNext()) {
            System.out.println(seer.next());
        }

        LinkedListDeque<String> lld = new LinkedListDeque<>();
//        lld.addLast("name");
//        lld.addFirst("my");
//        lld.addLast("is");
//        lld.addFirst("Hello");
//        lld.addLast("Rishabh");
//
//        for (String item : lld) {
//            System.out.println(item);
//        }

        Iterator<String> lldSeer = lld.iterator();
        while (lldSeer.hasNext()) {
            System.out.println(lldSeer.next());
        }
    }

}
