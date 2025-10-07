import java.util.Deque;
import java.util.LinkedList;



public class queue {
    public static void main(String[] args) {
        Deque<Integer> queue = new LinkedList<>();

        // Thêm phần tử vào hàng đợi (enqueue)
        queue.add(10);
        queue.add(20);
        queue.add(30);
        queue.add(40);

        System.out.println(queue);
        if (!queue.isEmpty()) {
            int removed = queue.removeFirst();  // removeFirst() = lấy và xoá phần tử đầu
            System.out.println("Phần tử đầu bị xóa: " + removed);
        }
        System.out.println("Hàng đợi sau khi xoá: " + queue);

        // Chèn phần tử vào đầu hàng đợi
        queue.addFirst(100);
        System.out.println("Hàng đợi sau khi chèn 99 vào đầu: " + queue);
    }
}
