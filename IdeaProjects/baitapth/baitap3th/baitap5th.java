import java.util.Scanner;

public class baitap5th {
    //hàm sắp xếp vị trí
    public static void swap(int[] arr){
        int n = arr.length;
        for(int i=0;i<n;i++){
            for (int j = i + 1; j < n; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    //hàm main
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        swap(arr);
        System.out.print(arr[0] + arr[n - 1]);
    }
}
