import java.util.LinkedList;

public class Stack {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(10);
        list.add(20);
        list.add(30);

        System.out.println(list);
        if (!list.isEmpty()) {
            list.removeFirst();
        }
        System.out.println("Lần xoá phần tử đầu: " + list);
        list.addFirst(100);
        System.out.println("Sau khi chèn 100 vào đầu danh sách: " + list);
    }
}
