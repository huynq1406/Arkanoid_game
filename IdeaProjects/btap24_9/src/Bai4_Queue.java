import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bai4_Queue {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            String s = sc.next();
            if (s.equals("enqueue")) {
                int x = sc.nextInt();
                q.add(x);
            }
            if (s.equals("dequeue")) {
                q.remove();
            }
        }
        sc.close();
        while (!q.isEmpty()) {
            int x = q.poll();
            System.out.print(x + " ");
        }
    }
}
