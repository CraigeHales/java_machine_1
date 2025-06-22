package controller; // the (virtual) pathname to the student-written files 
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    Selection[] selection;
    public Machine(/*PostResult result*/){
        //System.out.println("machine ctor x");
        Addon addIce = new Addon("Ice",40);
        Addon addCaffeine = new Addon("Caffeine",20);
        Addon addSugar = new Addon("Sugar",30);
        Addon addLime = new Addon("Lime",10);
        Addon addLemon = new Addon("Lemon",10);
        Addon addChocolate = new Addon("Chocolate",50);
        Addon addVanilla = new Addon("Vanilla",60);
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
    boolean x=false;
    public void init(PostResult result){
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
        result.println("machine.doclick("+id+")");
        if (id.startsWith("init")) {            
            
        }
        else if ( id.startsWith("rect_variety") ){ //  || id.startsWith("circle_add")
            int idi = Integer.parseInt(id.subString(12));
            selection[idi].press(result);
        }
        x = !x;
        result.setColor("circle_github_load",x ? "red" : "blue");         
    }
}
