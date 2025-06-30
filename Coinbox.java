package controller;
import controller.PostResult;
public class Coinbox {

    private int tendedCents=0;
    private int coinReturnCents=0;

    final static int plastic = -1;

    public void pay_with_mc_visa(PostResult result){ // accept mc_visa instead of coins
        move_tended_to_coin_return(result);
        tendedCents = plastic; 
        result.println("tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
   }

    public void move_tended_to_coin_return(PostResult result){ // move coins (not mc_visa) from tended to returned
        result.println("move_tended_to_coin_return1 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
        addCentsToCoinReturn(result,tendedCents);
        tendedCents = 0;
        result.println("move_tended_to_coin_return2 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
    }
    
    public void addCentsToTended(PostResult result, int cents){
        if (tendedCents == plastic )
            tendedCents = 0; // clear plastic flag, switching to coins
        tendedCents = tendedCents + cents;
        result.println("tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
    }
    
    public int getTended(PostResult result, int price){
        if (tendedCents==plastic)
            return price; // could return 0 for bad credit 
        return tendedCents; 
    }

    public void showTended(PostResult result, int price) {
        if ( tendedCents >= price ) { // deliver drink and partial reset (leave coins in return and drink in dispenser, but go to no drink selected)
            result.setText("tspan_dollar_value_needed", "Thanks!" ,0);
        }
        else {
            result.setText("tspan_dollar_value_needed", "$" + String.format("%.2f",(price - tendedCents)/100.0 ),0);
        }
    }

    public void emptyChangeReturn(PostResult result) { // the returned change is cleared when the return slot gets a click
        // play "take" sound if coinReturnCents>0, else "empty" sound
        result.println("emptyChangeReturn1 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
        coinReturnCents = 0; // jam to zero
        addCentsToCoinReturn(result, 0); // and display with 0 more added
        result.println("emptyChangeReturn2 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
    }

    void addCentsToCoinReturn(PostResult result, int addCents){ 
        result.println("addCentsToCoinReturn adding addCents="+addCents+" to coinReturnCents="+coinReturnCents);
        // only play sounds if addCents>0

        coinReturnCents = coinReturnCents + addCents; // change can accumulate if not emptied
        
        int c100 = coinReturnCents / 100;
        coinReturnCents = coinReturnCents % 100;
        result.println("c100="+c100+" coinReturnCents="+coinReturnCents);
        result.setText("tspan_return_100x0", "$1 x " + c100,1000);

        int c25 = coinReturnCents / 25;
        coinReturnCents = coinReturnCents % 25;
        result.println("c25="+c25+" coinReturnCents="+coinReturnCents);
        result.setText("tspan_return_25x0", "25 x " + c25,750);
        
        int c10 = coinReturnCents / 10;
        coinReturnCents = coinReturnCents % 10;
        result.println("c10="+c10+" coinReturnCents="+coinReturnCents);
        result.setText("tspan_return_10x0", "10 x " + c10,500);
        
        int c5 = coinReturnCents / 5;
        coinReturnCents = coinReturnCents % 5;
        result.println("c5="+c5+" coinReturnCents="+coinReturnCents);
        result.setText("tspan_return_5x0", "5 x " + c5,250);
        
        assert coinReturnCents == 0;
        result.println("tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
    }
}
