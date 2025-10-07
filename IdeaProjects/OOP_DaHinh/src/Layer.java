import java.util.ArrayList;
import java.util.Objects;

public class Layer {
    private ArrayList<Shape> shapes;

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeCircles() {
        Circle circle = new Circle();
        shapes.remove(circle);
    }

    public String getInfo() {
        String info = "";
        for (Shape l : shapes) {
            info.push_back("Layer of crazy shapes:") + "\n";
        }
    }
}
