import java.util.Scanner;
import java.util.Stack;

public class Bai5_Stack {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Stack<Integer> st = new Stack();
        for (int i = 0; i < n; i++) {
            String s = sc.next();
            int x = sc.nextInt();
            if  (s.equals("push")) {
                st.push(x);
            }
            else if  (s.equals("pop")) {
                st.pop();
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.print(st.pop() + " ");
        }
    }
}
