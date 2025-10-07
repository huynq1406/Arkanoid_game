public class Person {
    private String name;
    private String address;
    /**
     * Description.
     */

    public Person() {
        this.name = "Huy";
        this.address = "Dân tổ Quan Hoa";
    }

    /**
     * Description.
     */

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }
    /**
     * Description.
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Description.
     */

    public String getAddress() {
        return this.address;
    }
    /**
     * Description.
     */

    public void setName(String name) {
        this.name = name;
    }
    /**
     * Description.
     */

    public String getName() {
        return name;
    }
    /**
     * Description.
     */

    public String toString() {
        return "Person[name=" + name + ",address=" + address + "]";
    }
}
