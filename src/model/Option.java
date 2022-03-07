package model;

class Option implements java.io.Serializable {
    private static final long serialVersionUID = 2272307185575003314L;
    private String optionName;
    private double price;

    /* Constructor */
    protected Option() {
        optionName = "";
        price = 0;
    }

    protected Option(String name, double price_) {
        optionName = name;
        price = price_;
    }

    /* Getter */
    protected String getName() {
        return optionName;
    }

    protected double getPrice() {
        return price;
    }

    /* Setter */
    protected void setName(String name) {
        optionName = name;
    }

    protected void setPrice(double price_) {
        price = price_;
    }

    /* print() and toString() */
    public void print() {
        System.out.print(toString());
    }

    public String toString() {
        StringBuffer stringBufferObject;
        stringBufferObject = new StringBuffer("");
        stringBufferObject.append(getName()).append(" for $").append(getPrice());
        return stringBufferObject.toString();
    }
}