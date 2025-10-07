import java.util.Scanner;

public class baitap3th {
    public void main (String[] args){
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        while (a%b > 0){
            int n = a % b;
            a = b;
            b = n;
        }
        System.out.print(b);
    }
}
