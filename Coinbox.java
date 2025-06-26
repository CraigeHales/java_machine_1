package controller;
import controller.PostResult;
public class Coinbox {

    private int tended=0;


    public void mc_visa(PostResult result){}
    public void coin_return(PostResult result){}
    public void add(PostResult result, int pennies){
        tended = tended + pennies;
    }
    public int tended(PostResult result){ return tended; }




}
