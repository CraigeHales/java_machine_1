package controller; // the (virtual) pathname to the student-written files 
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    Selection[] selection;
    public Machine(PostResult result){
        result.println("machine ctor x");
        Addon addIce = new Addon();
        Addon addCaffeine = new Addon();
        Addon addSugar = new Addon();
        Addon addLime = new Addon();
        Addon addLemon = new Addon();
        Addon addChocolate = new Addon();
        Addon addVanilla = new Addon();
        selection = new Selection[6];
        selection[0] = new Selection("Coke","variety1","#ffffff","#ff3333",
            175,20,addIce,addCaffeine,addSugar);
        selection[1] = new Selection("7-Up","variety2","#333333","#66ff66",
            165,20,addIce,addLime,addSugar);
        selection[2] = new Selection("Sprite","variety3","#ffffff","#33aa33",
            155,20,addIce,addLemon,addLime);
        selection[3] = new Selection("Water","variety4","#ffffff","#3377ff",
            135,20,addIce,addLemon,addLime);
        selection[4] = new Selection("Milk","variety5","#000000","#ffffff",
            250,20,addIce,addChocolate,addVanilla);
        selection[5] = new Selection("Citrus","variety6","#000000","#ffff00",
            200,20,addLemon,addLime,addSugar);
    }
    public void doClick(PostResult result, String id){
        result.println(id);
        if (id.startsWith("init")) {            
            result.setText("tspan_java_machine","Ice Cold Drinks");
            result.setColor("tspan_java_machine","#3366ff");

            for(int i = 0; i<selection.length; i=i+1){
                //result.setText 
            }

            result.setText("tspan_add1","Ice");
            result.setText("tspan_add2","Decaf");
            result.setText("tspan_add3","Diet");
            
        }
        else if ( id.startsWith("rect_variety") || id.startsWith("circle_add") ){
            result.setText("tspan_github",id.substring(0,id.indexOf("_")));
            result.setText("tspan_load",id.substring(1+id.indexOf("_")));
            selection[0].press(id, result);
        }
        
    }
}
