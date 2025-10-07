public class Orange {
    private float weight;
    private boolean isTaxFree;

    public Orange(float weight,  boolean isTaxFree) {
        this.weight = weight;
        this.isTaxFree = isTaxFree;
    }

    public void printInfo() {
        System.out.println("Cân nặng của cam: " + weight + " kg");
        System.out.println("Miễn thuế: " + (isTaxFree ? "Có" : "Không"));
    }
}
