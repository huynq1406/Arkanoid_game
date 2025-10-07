import java.util.Objects;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getPointX() {
        return x;
    }

    public double getPointY() {
        return y;
    }

    public void setPointX(double x) {
        this.x = x;
    }

    public void setPointY(double y) {
        this.y = y;
    }

    public double distance(Point newPoint) {
        return Math.sqrt(Math.pow(newPoint.x - x, 2) + Math.pow(newPoint.y - y, 2));
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            if (p.x == x && p.y == y) {
                return true;
            }
            return false;
        }
        return false;
    }

    public String toString() {
        return "Point[" + "x=" + x + ", y=" + y + ']';
    }
}
