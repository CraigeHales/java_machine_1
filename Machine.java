package controller; // the (virtual) pathname to the student-written files 
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    Selection[] selection;
    public Machine(){
        Addon addIce = new Addon();
        Addon addCaffeine = new Addon();
        Addon addSugar = new Addon();
        selection = new Selection[6];
        selection[0] = new Selection("Coke","variety1","#ffffff","#ff3333",
        175,20,addIce,addCaffeine,addSugar);
    }
    public void doClick(PostResult result, String id){
        result.println(id);
        if (id.startsWith("init")) {            
            result.setText("tspan_java_machine","Ice Cold Drinks");
            result.setColor("tspan_java_machine","#3366ff");

            result.setText("tspan_variety1","Coke");
            result.setColor("tspan_variety1","#ffffff");
            result.setColor("rect_variety1", "#ff3333");

            result.setText("tspan_variety2","7-Up");
            result.setColor("tspan_variety2","#333333");
            result.setColor("rect_variety2", "#66ff66");

            result.setText("tspan_variety3","Sprite");
            result.setColor("tspan_variety3","#ffffff");
            result.setColor("rect_variety3", "#33aa33");

            result.setText("tspan_variety4","Water");
            result.setColor("tspan_variety4","#ffffff");
            result.setColor("rect_variety4", "#3377ff");

            result.setText("tspan_variety5","Milk");
            result.setColor("tspan_variety5","#000000");
            result.setColor("rect_variety5", "#ffffff");

            result.setText("tspan_variety6","Lemonade");
            result.setColor("tspan_variety6","#000000");
            result.setColor("rect_variety6", "#ffff00");

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
