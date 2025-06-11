package controller; // the (virtual) pathname to the student-written files
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use
// import controller.Aa; // other student-written
// import controller.Bb; // classes that are needed
// import controller.Cc; // by the Machine class
public class Machine implements Executer {
    public void doClick(PostResult result, String id){
        // process the click from the svg "id"
//        Aa.dosomething();
        result.setText("some_id","some_text");
    }
}
