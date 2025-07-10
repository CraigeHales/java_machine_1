package controller;
public class Addon {

    String yesName;
    int yesCents;
    String noName;
    int noCents;

    String myId;

    boolean yes = false;

    public Addon(String yesName, int yesCents, String noName, int noCents) {
        this.yesName = yesName;
        this.yesCents = yesCents;
        this.noName = noName;
        this.noCents = noCents;
    }

    public static void reset(PostResult result){
        result.setText("tspan_add0","Pick a",0);
        result.setText("tspan_add1","Drink",0);
        result.setText("tspan_add2","Above",0);
    }

    public void activateButton(PostResult result, String id) {
        myId = id;
        if (yes) {
            result.setText(id, yesName,0);
            result.println("Addon.activateButton: " + yesName + " " + id + " " + id);
        }
        else {
            result.setText(id, noName,0);
            result.println("Addon.activateButton: " + noName + " " + id + " " + id);
        }
    }

    public void press(PostResult result) {
        yes = !yes;
        activateButton(result,myId);
    }

    public int getPrice(PostResult result) {
        if (yes) {
            return yesCents;
        }
        else {
            return noCents;
        }
    }

    public String setting() {
        if (yes) {
            return yesName;
        }
        else {
            return noName;
        }
    }

}
