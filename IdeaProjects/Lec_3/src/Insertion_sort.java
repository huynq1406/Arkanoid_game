public class Insertion_sort {
    void sort(int arr[]) {
        int N = arr.length;
        for (int i = 1; i < N; i++) {
            int start = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] >= start) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = start;
        }
    }

    public static void main(String args[]){
        int arr[] = { 12, 11, 13, 5, 6 };
        Insertion_sort is = new Insertion_sort();
        is.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
