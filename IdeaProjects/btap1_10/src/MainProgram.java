public class MainProgram {
    public static void main(String[] args) {
        boolean appleTaxFree = true;
        boolean orangeTaxFree = false;
        Apple[] apples = new Apple[2];
        apples[0] = new Apple(0.2f, appleTaxFree);
        apples[1] = new Apple(0.2f, appleTaxFree);

        Orange[] oranges = new Orange[2];
        oranges[0] = new Orange(0.2f, orangeTaxFree);
        oranges[1] = new Orange(0.2f, orangeTaxFree);

        for (Apple a : apples) {
            a.printInfo();
        }

        for (Orange a : oranges) {
            a.printInfo();
        }

    }
}

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

public class Orange {
    private float weight;
    private boolean isTaxFree = false;

    public Orange(float weight,  boolean isTaxFree) {
        this.weight = weight;
        this.isTaxFree = isTaxFree;
    }

    public void printInfo() {
        System.out.println("Cân nặng của cam: " + weight + " kg");
        System.out.println("Miễn thuế: " + (isTaxFree ? "Có" : "Không"));
    }
}

