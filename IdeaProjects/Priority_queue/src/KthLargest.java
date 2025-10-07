import java.util.Scanner;
import java.util.Arrays;

class KthLargest {
    private int k;
    private int[] arr;
    private int size;

    static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }

        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(arr, n, largest);
        }
    }

    static void MaxHeap(int[] arr, int n) {
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {

            // Move current root to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    public KthLargest(int k, int[] nums) {
        this.k = k;
        this.arr = nums;
        int n = nums.length;
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        MaxHeap(nums, n);
    }

    public int getKthLargest() {
        if (k > size) {
            throw new IllegalArgumentException("Chưa có đủ phần tử để lấy kth largest");
        }
        return arr[size - k];
    }


// int     add(10)
// void    add(10)

    // thêm một phần tử và trả về phần tử lớn thứ k
    public int add(int val) {
        if (size < arr.length) {
            arr[size++] = val; // thêm phần tử mới
        } else {
            // nếu mảng đã đầy thì ghi đè lên (tùy yêu cầu, ở đây mình cứ giữ nguyên)
            arr[size - 1] = val;
        }

        // copy mảng hiện tại để sort
        int[] temp = Arrays.copyOf(arr, size);
        MaxHeap(temp, size); // sort tăng dần

        // kth largest là phần tử size - k
        if (k <= size) {
            return temp[size - k];
        } else {
            return -1; // nếu chưa có đủ k phần tử
        }
    }

    public static void main(String[] args) {
        KthLargest kth =  new KthLargest(3, new int[] {1, 2, 3, 4, 5});
        System.out.println(kth.add(8));
    }
}

/** class KthLargest {
 private int k;
 private int[] heap;
 private int size;

 public KthLargest(int k, int[] nums) {
 this.k = k;
 this.heap = new int[k];
 this.size = 0;

 for (int num : nums) {
 add(num);
 }
 }

 public int add(int val) {
 if (size < k) {
 heap[size] = val;
 size++;
 heapifyUp(size - 1);
 } else if (val > heap[0]) {
 heap[0] = val;
 heapifyDown(0);
 }
 return heap[0];
 }

 // đẩy phần tử mới lên đúng chỗ (heapify up)
 private void heapifyUp(int i) {
 while (i > 0) {
 int parent = (i - 1) / 2;
 if (heap[i] < heap[parent]) {
 swap(i, parent);
 i = parent;
 } else break;
 }
 }

 // đẩy phần tử xuống đúng chỗ (heapify down)
 private void heapifyDown(int i) {
 while (true) {
 int left = 2 * i + 1, right = 2 * i + 2;
 int smallest = i;

 if (left < size && heap[left] < heap[smallest]) smallest = left;
 if (right < size && heap[right] < heap[smallest]) smallest = right;

 if (smallest != i) {
 swap(i, smallest);
 i = smallest;
 } else break;
 }
 }

 private void swap(int i, int j) {
 int tmp = heap[i];
 heap[i] = heap[j];
 heap[j] = tmp;
 }
 }
*/

 /**
 * Your KthLargest object will be instantiated and called as such:
 * KthLargest obj = new KthLargest(k, nums);
 * int param_1 = obj.add(val);
 */
