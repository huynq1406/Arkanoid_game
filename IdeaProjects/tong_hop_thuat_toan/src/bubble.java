import java.util.Arrays;

class bubble {
    public static void bubblesort(int[] arr){
    int n = arr.length;
    for (int i = 0; i < n; i++){
        for (int j = i + 1; j < n; j++){
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    public static void main (String[] args) {

        }
}
