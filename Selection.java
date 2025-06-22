package controller;
import controller.PostResult;
public class Selection {
    String prodName;
    String id;
    String textColor;
    String backColor;
    Addon add[];
    int prodPennies;
    int nStock;
    public Selection(String prodName,String id,String textColor,String backColor,
        int prodPennies,int nStock,Addon add0,Addon add1,Addon add2){
    this.prodName = prodName;
    this.id = id;
    this.textColor = textColor;
    this.backColor = backColor;
    this.add = new Addon()[3];
    this.add[0] = add0; 
    this.add[1] = add1; 
    this.add[2] = add2; 
    this.prodPennies = prodPennies;
    this.nStock = nStock;
        
    }
    public void press(String id, PostResult result){
        result.setText("some_other_id","some_other_textx");
        if (id.startsWith("rect")) {
            result.println("---------before---------");
            String x=null;
            System.out.println("aaa"+x.toString());
            x=x+1;
            System.out.println("bbb"+x.toString());
            result.println("---------after---------");
        }
    }
}
