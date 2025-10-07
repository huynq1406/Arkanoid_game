import java.util.Scanner;

public class baitap1th {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next();
        int n = a.length();

        for (int i = n - 1; i >= 0; i--) {
            System.out.print(a.charAt(i));
        }
    }
}
