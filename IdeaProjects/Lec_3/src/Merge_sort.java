public class Merge_sort {
    static void merge(int arr[], int l, int m, int r){

        // size của hai mảng chia ra từ 1 mảng
        int s1 = m - l + 1;
        int s2 = r - m;

        // tạo ra các array mới
        int L[] = new int[s1];
        int R[] = new int[s2];

        // copy giá trị từ 2 mảng cũ vào các array mới
        for (int i = 0; i < s1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < s2; ++j)
            R[j] = arr[m + 1 + j];

        // sắp xếp mảng

        int i = 0, j = 0;

        int k = l;
        while (i < s1 && j < s2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < s1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < s2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    static void mergeSort(int arr[], int l, int r){
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    public static void main(String args[]){

        int arr[] = {38, 27, 43, 10};

        mergeSort(arr, 0, arr.length - 1);

        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}
