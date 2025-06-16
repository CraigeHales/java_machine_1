package controller; // the (virtual) pathname to the student-written files 
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use
// import controller.Aa; // other student-written
// import controller.Bb; // classes that are needed
// import controller.Cc; // by the Machine class
import controller.Buttons;
public class Machine implements Executer {
    public void doClick(PostResult result, String id){
        System.out.println("machine running 333");
        try {
            System.out.println("machine running 4");
            Buttons b = new Buttons();
            System.out.println("machine running 5");
            b.hello(result);
            System.out.println("machine running 6");
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
            
        result.setText("some_id","some_texttttt");
    }
}
