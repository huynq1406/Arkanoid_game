public class Shape {
    protected String color;
    protected boolean filled;

    public Shape(String color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public double getArea() {
        double area = 0.0;
        if (filled) {
            area = 0.0;
        }
        return area;
    }

    public double getPerimeter() {
        double perimeter = 0.0;
        if (filled) {
            perimeter = 0.0;
        }
        return perimeter;
    }

    public String toString() {
        return "Shape[" + color + ", " +  filled + "]";
    }

}
