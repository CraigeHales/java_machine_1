package controller; // the (virtual) pathname to the student-written files 
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    Selection[] selection;
    static Selection gCurrentSelection = null;
 //   static Coins coins = null;
    public Machine(/*PostResult result*/){
        //System.out.println("machine ctor x");
        Addon addIce = new Addon("Ice",40,"No Ice",0);
        Addon addCaffeine = new Addon("Caf",0,"Decaf",10);
        Addon addSugar = new Addon("Sugar",30,"Diet",0);
        Addon addLime = new Addon("Lime",10,"No Lime",0);
        Addon addLemon = new Addon("Lemon",10,"No Lemon",0);
        Addon addChocolate = new Addon("Choc",50,"No Choc",0);
        Addon addVanilla = new Addon("Vanilla",60,"No Van",0);
        selection = new Selection[6];
        selection[0] = new Selection("Coke","variety0","#ffffff","#ff3333",
            175,20,addIce,addCaffeine,addSugar);
        selection[1] = new Selection("7-Up","variety1","#333333","#66ff66",
            165,20,addIce,addLime,addSugar);
        selection[2] = new Selection("Sprite","variety2","#ffffff","#33aa33",
            155,20,addIce,addLemon,addSugar);
        selection[3] = new Selection("Water","variety3","#ffffff","#3377ff",
            135,20,addIce,addLemon,addLime);
        selection[4] = new Selection("Milk","variety4","#000000","#ffffff",
            250,20,addIce,addChocolate,addVanilla);
        selection[5] = new Selection("Citrus","variety5","#000000","#ffff00",
            200,20,addLemon,addLime,addSugar);
     //   coins = new Coins();
    }
    boolean x=false;
    public void init(PostResult result){
        result.println("Machine.init()");
        // give the machine a name
        result.setText("tspan_java_machine","Ice Cold Drinks");
        result.setColor("tspan_java_machine","#3366ff");
        // initialize the rectangular selection buttons
        for(int i = 0; i<selection.length; i=i+1){
            selection[i].init(result); 
        }

        Addon.reset(result);
    }
    public void doClick(PostResult result, String id){
        if (id.startsWith("init")) {            
            
        }
        else if ( id.startsWith("rect_variety") ){ //  || id.startsWith("circle_add")
            result.println("machine.doclick("+id+")");
            int idi = Integer.parseInt(id.substring(12));
            selection[idi].press(result);
            gCurrentSelection = selection[idi];
        }
        else if ( id.startsWith("circle_add") ) {
            result.println("machine.doclick("+id+")");
            int idi = Integer.parseInt(id.substring(10));
            selection[idi].press(result);
        }
    /*    else if ( id.startsWith("circle_coin_") ) {
            String coin = id.substring(12); // "5" ... "100" and "return" and "mc_visa"
            if (coin.equals("return"))
                coins.return(result);
            else if (coin.equals("mc_visa"))
                coins.mc_visa(result);
            else {
                coins.deposit(Integer.parseInt(coin));
            }
        } */
        if ( gCurrentSelection != null ) {
            result.setText("tspan_dollar_value_needed", "$" + String.format("%.2f",gCurrentSelection.getPrice(result)/100.0 ) );
        }
    }
}
