package controller;
public class Addon {

    String name;
    int pricePennies;

    public Addon(String name, int pricePennies) {
        this.name = name;
        this.pricePennies = pricePennies;
    }

    public static void reset(PostResult result){
        result.setText("tspan_add0","Pick a");
        result.setText("tspan_add1","Drink");
        result.setText("tspan_add2","Above");
    }

    public void activateButton(PostResult result, String id) {
        result.println("⚫  "+name);
        result.setText(id, name);
    }
}
