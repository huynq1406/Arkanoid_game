public class Staff extends Person {
    private String school;
    private double pay;
    /**
     * Description.
     */

    public Staff(String name, String address, String school, double pay) {
        super(name, address);
        this.school = school;
        this.pay = pay;
    }
    /**
     * Description.
     */

    public String getSchool() {
        return school;
    }
    /**
     * Description.
     */

    public void setSchool(String school) {
        this.school = school;
    }
    /**
     * Description.
     */

    public double getPay() {
        return pay;
    }
    /**
     * Description.
     */

    public void setPay(double pay) {
        this.pay = pay;
    }
    /**
     * Description.
     */

    public String toString() {
        return "Staff[" + super.toString() + ",school=" + school + ",pay=" + pay + "]";
    }
}
