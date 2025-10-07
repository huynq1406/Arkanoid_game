public class Apple {
    private float weight;
    private boolean isTaxFree = true;

    public Apple(float weight, boolean isTaxFree) {
        this.weight = weight;
        this.isTaxFree = isTaxFree;
    }

    public void printInfo() {
        System.out.println("Cân nặng của táo: " + weight + " kg");
        System.out.println("Miễn thuế: " + (isTaxFree ? "Có" : "Không"));
    }
}