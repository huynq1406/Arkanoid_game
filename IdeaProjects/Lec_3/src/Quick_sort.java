public class Quick_sort {
        static int partition(int[] arr, int l, int h) {
            int pivot = arr[h];
            int i = l - 1;
            for (int j = l; j <= h - 1; j++) {
                if (arr[j] < pivot) {
                    i++;
                    swap(arr, i, j);
                }
            }
            swap(arr, i + 1, h);
            return i + 1;
        }

        // swap function
        static void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        static void quickSort(int[] arr, int l, int h) {
            if (l < h) {
                int pi = partition(arr, l, h);
                quickSort(arr, l, pi - 1);
                quickSort(arr, pi + 1, h);
            }
        }

        public static void main(String[] args) {
            int[] arr = {10, 7, 8, 9, 1, 5};
            int n = arr.length;
            quickSort(arr, 0, n - 1);
            for (int val : arr) {
                System.out.print(val + " ");
            }
        }
}

