package baitap_tuan2;

import java.util.Scanner;

public class count_lap {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int[] arr;
        int n = arr.length;

        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        int count = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i] == arr[j]) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }
}
