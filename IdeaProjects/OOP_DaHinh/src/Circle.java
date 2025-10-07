public class Circle extends Shape {
    protected double radius;

    /**
     * Description.
     */

    public Circle() {
        super();
    }

    /**
     * Description.
     */

    public Circle(double radius) {
        this.color = "null";
        this.filled = true;
        this.radius = radius;
    }

    /**
     * Description.
     */

    public Circle(double radius, String color, boolean filled) {
        super(color, filled);
        this.radius = radius;
    }

    /**
     * Description.
     */

    public double getRadius() {
        return radius;
    }

    /**
     * Description.
     */

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Description.
     */

    public double getArea() {
        return radius * radius * Math.PI;
    }

    /**
     * Description.
     */

    public double getPerimeter() {
        return 2 * radius * Math.PI;
    }

    /**
     * Description.
     */

    @Override
    public String toString() {
        return "Circle[radius=" + radius + ",color=" + color + ",filled=" + filled + ']';
    }
}
