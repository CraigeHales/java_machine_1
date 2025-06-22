package controller;
public class Addon {

    String name;
    int pricePennies;

    Public Addon(String name, int pricePennies) {
        this.name = name;
        this.pricePennies = pricePennies;
    }

    static void reset(PostResult result){
        result.setText("tspan_add1","Pick a");
        result.setText("tspan_add2","Drink");
        result.setText("tspan_add3","Above");
    }

    public void activateButton(PostResult result, String id) {
        result.setText(id, name);
    }
}
