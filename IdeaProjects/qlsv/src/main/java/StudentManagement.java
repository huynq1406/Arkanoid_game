public class StudentManagement {
    private Student[] students = new Student[100];
    private int stuSize = 0;

    /**
     * So sánh hai sinh viên có cùng lớp hay không.
     *
     * @param s1 sinh viên thứ nhất
     * @param s2 sinh viên thứ hai
     * @return true nếu cùng group, ngược lại false
     */
    public static boolean sameGroup(Student s1, Student s2) {
        return s1.getGroup().equals(s2.getGroup());
    }

    /**
     * Thêm một sinh viên mới vào danh sách.
     *
     * @param newStudent sinh viên cần thêm
     */

    public void addStudent(Student newStudent) {
        students[stuSize] = newStudent;
        stuSize++;
    }

    /**
     * Xóa một sinh viên dựa trên mã số id.
     *
     * @param id mã số sinh viên cần xóa
     */

    public void removeStudent(String id) {
        for (int i = 0; i < stuSize; i++) {
            if (students[i].getId().equals(id)) {
                for (int j = i; j < stuSize - 1; j++) {
                    students[j] = students[j + 1];
                }
                students[stuSize - 1] = null;
                stuSize--;
            }
        }
    }

    /**
     * Liệt kê danh sách sinh viên theo từng lớp.
     *
     * @return chuỗi chứa danh sách sinh viên theo group
     */


    public String studentsByGroup() {
        String s = "";
        boolean[] visited = new boolean[stuSize]; //tham khảo chatgpt

        for (int i = 0; i < stuSize; i++) {
            if (!visited[i]) {
                String group = students[i].getGroup();
                s += group + "\n";
                for (int j = 0; j < stuSize; j++) {
                    if (!visited[j] && students[j].getGroup().equals(group)) {
                        s += students[j].getName() + " - "
                                + students[j].getId() + " - "
                                + students[j].getGroup() + " - "
                                + students[j].getEmail() + "\n";
                        visited[j] = true;
                    }
                }
            }
        }
        return s.toString();
    }
}