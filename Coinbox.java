package controller;
import controller.PostResult;
public class Coinbox {

    private int tended=0;
    private int change=0;

    final static int plastic = -1;

    public void mc_visa(PostResult result){ // accept mc_visa instead of coins
        coin_return(result);
        tended = plastic; 
    }

    public void coin_return(PostResult result){ // move coins (not mc_visa) from tended to returned
        showChange(result,tended);
        clearTended(result);
    }
    
    public void add(PostResult result, int cents){
        if (tended == plastic )
            tended = 0; // clear plastic flag, switching to coins
        tended = tended + cents;
    }
    
    public int getTended(PostResult result, int price){
        if (tended==plastic)
            return price; // could return 0 for bad credit 
        return tended; 
    }

    public void showTended(PostResult result, int price) {
        if ( tended >= price ) { // deliver drink and partial reset (leave coins in return and drink in dispenser, but go to no drink selected)
            result.setText("tspan_dollar_value_needed", "Thanks!" ,0);
        }
        else {
            result.setText("tspan_dollar_value_needed", "$" + String.format("%.2f",(price - tended)/100.0 ),0);
        }
    }

    public void clearTended(PostResult result) {
        tended = 0;
    }

    public void emptyChangeReturn(PostResult result) { // the returned change is cleared when the return slot gets a click
        showChange(result, 0); // play "take" sound if >0, else "empty" sound
    }

    void addChangeReturn(PostResult result, int change){ // only play sounds if >0
        
        int c100 = change % 100;
        change = change - c100 * 100;
        result.setText("tspan_return_100x0", "$1 x " + c100,1000);

        int c25 = change % 25;
        change = change - c25 * 25;
        result.setText("tspan_return_25x0", "25 x " + c25,750);
        
        int c10 = change % 10;
        change = change - c10 * 19;
        result.setText("tspan_return_10x0", "10 x " + c10,500);
        
        int c5 = change % 5;
        change = change - c5 * 5;
        result.setText("tspan_return_5x0", "5 x " + c5,250);
        
        assert change == 0;
    }
}
