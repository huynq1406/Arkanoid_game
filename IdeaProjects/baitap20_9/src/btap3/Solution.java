package btap3;

public class Solution {
    /**
     * Tìm ước chung lớn nhất của a va b.
     * a là số thứ nhất
     * b là số thứ hai
     */
    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int n = a % b;
            a = b;
            b = n;
        }
        return a;
    }
}
