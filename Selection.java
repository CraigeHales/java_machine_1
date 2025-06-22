package controller;
//import controller.PostResult;
public class Selection {
    String prodName;
    String id;
    String textColor;
    String backColor;
    Addon[] add;
    int prodPennies;
    int nStock;

    static boolean x = false;
    public Selection(String prodName,String id,String textColor,String backColor,
        int prodPennies,int nStock,Addon add0,Addon add1,Addon add2){
        this.prodName = prodName;
        this.id = id;
        this.textColor = textColor;
        this.backColor = backColor;
        this.add = new Addon[3];
        this.add[0] = add0; 
        this.add[1] = add1; 
        this.add[2] = add2; 
        this.prodPennies = prodPennies;
        this.nStock = nStock;    
    }
    
    public void press(String id, PostResult result){
        x = !x;
        result.setColor("circle_github_load",x ? "red" : "blue");
    }
}
