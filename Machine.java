package controller; // the (virtual) pathname to the student-written files 
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    Selection selection = null;
    public Machine(){
        selection = new Selection();
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
        }
        else if ( id.startsWith("rect_variety") || id.startsWith("circle_add") ){
            result.setText("tspan_github",id.substring(0,id.indexOf("_")));
            result.setText("tspan_load",id.substring(1+id.indexOf("_")));
            selection.press(id, result);
        }
        while(true) result.println("loop");
    }
}
