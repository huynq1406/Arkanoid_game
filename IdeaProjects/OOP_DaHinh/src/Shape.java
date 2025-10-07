public abstract class Shape {
    protected String color;
    protected boolean filled;

    public Shape() {
        color = "null";
        filled = false;
    }

    /**
     * Description.
     */

    public Shape(String color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }

    /**
     * Description.
     */

    public String getColor() {
        return color;
    }

    /**
     * Description.
     */

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Description.
     */

    public boolean isFilled() {
        return filled;
    }

    /**
     * Description.
     */

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    /**
     * Description.
     */

    public abstract double getArea();

    /**
     * Description.
     */

    public abstract double getPerimeter();

    /**
     * Description.
     */

    @Override
    public String toString() {
        return "Shape[" + "color=" + color + ",filled=" + filled + ']';
    }
}
