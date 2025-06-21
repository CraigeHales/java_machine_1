package controller;
import controller.PostResult;
public class Selection {
    public void press(String id, PostResult result){
      result.setText("some_other_id","some_other_textx");
        result.println("---------before---------");
        String x=null;System.out.println(x);
        result.println("---------after---------");
    }
}
