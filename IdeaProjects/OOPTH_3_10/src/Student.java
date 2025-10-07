public class Student extends Person {
    private String program;
    private int year;
    private double fee;
    /**
     * Description.
     */

    public Student(String name, String address, String program, int year, double fee) {
        super(name, address);
        this.program = program;
        this.year = year;
        this.fee = fee;
    }
    /**
     * Description.
     */

    public String getProgram() {
        return program;
    }
    /**
     * Description.
     */

    public void setProgram(String program) {
        this.program = program;
    }
    /**
     * Description.
     */

    public int getYear() {
        return year;
    }
    /**
     * Description.
     */

    public void setYear(int year) {
        this.year = year;
    }
    /**
     * Description.
     */

    public double getFee() {
        return fee;
    }
    /**
     * Description.
     */

    public void setFee(double fee) {
        this.fee = fee;
    }
    /**
     * Description.
     */

    public String toString() {
        return "Student[" + super.toString()
               + ",program=" + program + ",year="
                + year + ",fee=" + fee + "]";
    }
}