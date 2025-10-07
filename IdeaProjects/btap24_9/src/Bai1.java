import java.util.Scanner;

public class Bai1 {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int [n];

        int maxVal = 0;
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
            if(arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        int[]freq = new int[maxVal + 1];
        for (int i : arr) {
            freq[i]++;
        }

        int count = 0;
        for (int f : arr) {
            if(f >= 2) {
                count += f * (f - 1) / 2;
            }
        }

        System.out.println(count);
    }
}