package controller;
public class Addon {

    String yesName;
    int yesPennies;
    String noName;
    int noPennies;

    boolean yes = false;

    public Addon(String yesName, int yesPennies, String noName, int noPennies) {
        this.yesName = yesName;
        this.yesPennies = yesPennies;
        this.noName = noName;
        this.noPennies = noPennies;
    }

    public static void reset(PostResult result){
        result.setText("tspan_add0","Pick a");
        result.setText("tspan_add1","Drink");
        result.setText("tspan_add2","Above");
    }

    public void activateButton(PostResult result, String id) {
        // result.println("⚫  "+name);
        if (yes) {
            result.setText(id, yesName);
        }
        else {
            result.setText(id, noName);
        }
    }

    public void toggleButton(PostResult result) {
        yes = !yes;
        activateButton(result,id);
    }

    public int getPrice(PostResult result) {
        // result.println("⚫  "+name);
        if (yes) {
            return yesPennies;
        }
        else {
            return noPennies;
        }
    }

}
