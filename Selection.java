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

    public Selection(String prodName,String id,String textColor,String backColor,int prodPennies,int nStock,Addon add0,Addon add1,Addon add2){
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

    public void init(PostResult result){
        result.setColor("rect_"+id, backColor);
        result.setColor("tspan_"+id, textColor);
        result.setText("tspan_"+id, prodName);
    }
    
    public void press(PostResult result){
        for(int i=0; i<add.length; i+=1) {
            add[i].activateButton(result, "tspan_add"+i+1)
        }
    }
}
