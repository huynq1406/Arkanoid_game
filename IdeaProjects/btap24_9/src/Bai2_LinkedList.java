import java.util.Scanner;

public class Bai2_LinkedList {
        public static void main(String[] args) {
            java.util.LinkedList<Integer> linkedList = new java.util.LinkedList();
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            for (int i = 1; i <= n; i++) {
                String s = sc.next();
                int p = sc.nextInt();
                if (s.equals("insert")) {
                    int x = sc.nextInt();
                    linkedList.add(p, x);
                }
                if (s.equals("delete")) {
                    linkedList.remove(p);
                }
            }

            for (int i = 0; i < linkedList.size(); i++) {
                System.out.print(linkedList.get(i) + " ");
            }
        }
}

