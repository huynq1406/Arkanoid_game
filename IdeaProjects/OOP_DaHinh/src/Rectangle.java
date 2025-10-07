public class Rectangle extends Shape {
    protected double width;
    protected double length;

    /**
     * Description.
     */

    public Rectangle() {
        super();
    }

    /**
     * Description.
     */

    public Rectangle(double width, double length) {
        this.width = width;
        this.length = length;
    }

    /**
     * Description.
     */

    public Rectangle(double width, double length, String color, boolean filled) {
        this.width = width;
        this.length = length;
        this.color = color;
        this.filled = filled;
    }

    /**
     * Description.
     */

    public double getWidth() {
        return width;
    }

    /**
     * Description.
     */

    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Description.
     */

    public double getLength() {
        return length;
    }

    /**
     * Description.
     */

    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Description.
     */

    public double getArea() {
        return width * length;
    }

    /**
     * Description.
     */

    public double getPerimeter() {
        return (width + length) * 2;
    }

    /**
     * Description.
     */

    @Override
    public String toString() {
        return "Rectangle[width=" + width
                + ",length=" + length + ",color="
                + color + ",filled=" + filled + "]";
    }
}
