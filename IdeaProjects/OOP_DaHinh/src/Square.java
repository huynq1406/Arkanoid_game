public class Square extends Rectangle {
    /**
     * Description.
     */
    public Square() {
        super();
    }

    /**
     * Description.
     */

    public Square(double side) {
        super(side, side);
    }

    /**
     * Description.
     */

    public Square(double side, String color, boolean filled) {
        super(side, side);
        this.color = color;
        this.filled = filled;
    }

    /**
     * Description.
     */

    public void setSide(double side) {
        super.setWidth(side);
        super.setLength(side);
    }

    /**
     * Description.
     */
    public double getSide() {
        return super.getWidth();
    }

    /**
     * Description.
     */

    @Override

    public void setWidth(double side) {
        super.setWidth(side);
        super.setLength(side);
    }

    /**
     * Description.
     */
    @Override

    public void setLength(double side) {
        super.setWidth(side);
        super.setLength(side);
    }

    /**
     * Description.
     */
    @Override

    public String toString() {
        return "Square[" + "side=" + getSide() +  ",color=" + color + ",filled=" + filled + ']';
    }

}
