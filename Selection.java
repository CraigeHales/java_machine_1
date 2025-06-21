package controller;
import controller.PostResult;
public class Selection {
    public void ddd press(String id, PostResult result){
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
