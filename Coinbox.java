package controller;
import controller.PostResult;
public class Coinbox {

    private int tendedCents=0;
    private int coinReturnCents=0;
    // you can add more coins to the return slot without removing the old ones; keep track of 5,10,25,100 totals
    private int coinReturn5=0;
    private int coinReturn10=0;
    private int coinReturn25=0;
    private int coinReturn100=0;
    final static int plastic = -1;

    public void pay_with_mc_visa(PostResult result){ // accept mc_visa instead of coins
        move_tended_to_coin_return(result);
        tendedCents = plastic; 
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        result.println("tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
   }

    public void move_tended_to_coin_return(PostResult result){ // move coins (not mc_visa) from tended to returned
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        result.println("move_tended_to_coin_return1 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
        if (tendedCents<=0){
            result.setAudio("groantick.mp3",0);
        }
        else {
            addCentsToCoinReturn(result,tendedCents);
            tendedCents = 0;
        }
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        result.println("move_tended_to_coin_return2 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
    }
    
    public void addCentsToTended(PostResult result, int cents){
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        if (tendedCents == plastic )
            tendedCents = 0; // clear plastic flag, switching to coins
        tendedCents = tendedCents + cents;
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        result.println("tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
    }
    
    public int getTended(PostResult result, int price){
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        if (tendedCents==plastic)
            return price; // could return 0 for bad credit 
        return tendedCents; 
    }

    public void showTended(PostResult result, int price) {
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        if ( tendedCents==plastic || tendedCents >= price ) { // deliver drink and partial reset (leave coins in return and drink in dispenser, but go to no drink selected)
            result.setText("tspan_dollar_value_needed", "Thanks!" ,0);
        }
        else {
            result.setText("tspan_dollar_value_needed", "$" + String.format("%.2f",(price - tendedCents)/100.0 ),0);
        }
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
    }

    public void payFromTended(PostResult result, int price) {
        if (tendedCents==plastic){
            assert false; // handled earlier
            tendedCents = 0;
        } else if (tendedCents >= price) {
            tendedCents -= price;
        }
        else {
            assert false; // handled earlier
        }
    }

    public void emptyChangeReturn(PostResult result) { // the returned change is cleared when the return slot gets a click
        // play "take" sound if coinReturnCents>0, else "empty" sound
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        if (coinReturnCents == 0){
            result.setAudio("TakeNoChange.mp3",0);
        }
        else {
            result.setAudio("TakeChange.mp3",0);
        }
        result.println("emptyChangeReturn1 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
        coinReturnCents = 0; // jam to zero
        coinReturn5 = 0; // jam to zero
        coinReturn10 = 0; // jam to zero
        coinReturn25 = 0; // jam to zero
        coinReturn100 = 0; // jam to zero
        addCentsToCoinReturn(result, 0); // and display with 0 more added
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        result.println("emptyChangeReturn2 tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
    }

    void addCentsToCoinReturn(PostResult result, int addCents){ 
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
        result.println("addCentsToCoinReturn adding addCents="+addCents+" to coinReturnCents="+coinReturnCents);
        // only play sounds if addCents>0

        coinReturnCents = coinReturnCents + addCents; // change can accumulate if not emptied
        
        int balance = addCents; // balance gets destroyed as the coin values are taken out of it
        int delay = 0;

        int c100 = balance / 100;
        balance = balance % 100;
        for(int i = 0; i<c100; i+=1){
            result.setAudio("DropDollar.mp3",delay);
            delay+=500;
        }
        coinReturn100 += c100;
        result.println("c100="+c100+" balance="+balance);
        result.setText("tspan_return_100x0", "$1 x " + coinReturn100,delay);

        int c25 = balance / 25;
        balance = balance % 25;
        for(int i = 0; i<c25; i+=1){
            result.setAudio("DropQuarter.mp3",delay);
            delay+=500;
        }
        coinReturn25 += c25;
        result.println("c25="+c25+" balance="+balance);
        result.setText("tspan_return_25x0", "25 x " + coinReturn25,delay);
        
        int c10 = balance / 10;
        balance = balance % 10;
        for(int i = 0; i<c10; i+=1){
            result.setAudio("DropDime.mp3",delay);
            delay+=500;
        }
        coinReturn10 += c10;
        result.println("c10="+c10+" balance="+balance);
        result.setText("tspan_return_10x0", "10 x " + coinReturn10,delay);
        
        int c5 = balance / 5;
        balance = balance % 5;
        for(int i = 0; i<c5; i+=1){
            result.setAudio("DropNickel.mp3",delay);
            delay+=500;
        }
        coinReturn5 += c5;
        result.println("c5="+c5+" balance="+balance);
        result.setText("tspan_return_5x0", "5 x " + coinReturn5,delay);
        
        assert balance == 0;
        result.println("tendedCents="+tendedCents+" coinReturnCents="+coinReturnCents);
        assert coinReturn5 * 5 + coinReturn10 * 10 + coinReturn25 * 25 + coinReturn100 * 100 == coinReturnCents;
    }
}
