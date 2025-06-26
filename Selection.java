package controller;
//import controller.PostResult;
public class Selection {
    String prodName;
    String id;
    String textColor;
    String backColor;
    Addon[] add;
    int pennies;
    int nStock;

    public Selection(String prodName,String id,String textColor,String backColor,int pennies,int nStock,Addon add0,Addon add1,Addon add2){
        this.prodName = prodName;
        this.id = id;
        this.textColor = textColor;
        this.backColor = backColor;
        this.add = new Addon[3];
        this.add[0] = add0; 
        this.add[1] = add1; 
        this.add[2] = add2; 
        this.pennies = pennies;
        this.nStock = nStock;    
    }

    public void init(PostResult result){
        result.println("Selection.init: "+prodName);
        result.setColor("rect_"+id, backColor);
        result.setColor("tspan_"+id, textColor);
        result.setText("tspan_"+id, prodName);
    }
    
    public void on(PostResult result){
        result.setColor("rect_"+id, backColor);
        result.setColor("tspan_"+id, textColor);
    }
    
    String dim(String c) { // cut the brightness in half. more or less.
        c.replace("1","0");
        c.replace("2","1");
        c.replace("3","1");
        c.replace("4","2");
        c.replace("5","2");
        c.replace("6","3");
        c.replace("7","3");
        c.replace("8","4");
        c.replace("9","4");
        c.replace("a","5");
        c.replace("b","5");
        c.replace("c","6");
        c.replace("d","6");
        c.replace("e","7");
        c.replace("f","7");
    }
    public void off(PostResult result){
        result.setColor("rect_"+id, dim(backColor));
        result.setColor("tspan_"+id, dim(textColor));
    }
    
    public void press(PostResult result){
        result.println("select "+prodName);
        result.println("addons:");
        for(int i=0; i<add.length; i+=1) {
            add[i].activateButton(result, "tspan_add"+i);
        }
        result.setAudio("plop.mp3");
        if (prodName.equals("Coke")) {
            result.setAudio("ice.mp3");
        }
    }

    public Addon getAddon(int i) {
        return add[i];
    }

    public int getPrice(PostResult result) {
        return pennies + add[0].getPrice(result) + add[1].getPrice(result) + add[2].getPrice(result);
    }
}
