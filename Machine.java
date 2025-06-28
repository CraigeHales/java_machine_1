package controller; // the (virtual) pathname to the student-written files
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

/* how to run a timed sequence?

    use a list of timed operations; this will make drink delivery and coinbox delivery
    simpler (and not another statemachine that callbacks would want.)

    that will change the result block to be a list of timed results.
 */

public class Machine implements Executer {
    Selection[] selection;
    static Selection gCurrentSelection = null;
    Coinbox coinbox = null;
 //   static Coins coins = null;
    public Machine(/*PostResult result*/){
        // there are more addon possibilities than buttons; each selection will
        // choose exactyl three addons for the three buttons. Each addon remembers
        // its current state; if it is shared between two selections then the
        // state is shared as well.
        Addon addIce = new Addon("Ice",40,"No Ice",0);
        Addon addCaffeine = new Addon("Caf",0,"Decaf",10);
        Addon addSugar = new Addon("Sugar",30,"Diet",0);
        Addon addLime = new Addon("Lime",10,"No Lime",0);
        Addon addLemon = new Addon("Lemon",10,"No Lemon",0);
        Addon addChocolate = new Addon("Choc",50,"No Choc",0);
        Addon addVanilla = new Addon("Vanilla",60,"No Van",0);
        selection = new Selection[6];
        selection[0] = new Selection("Coke","variety0","#ffffff","#ff3333",
            175,20,addIce,addCaffeine,addSugar);
        selection[1] = new Selection("7-Up","variety1","#333333","#66ff66",
            165,20,addIce,addLime,addSugar);
        selection[2] = new Selection("Sprite","variety2","#ffffff","#33aa33",
            155,20,addIce,addLemon,addSugar);
        selection[3] = new Selection("Water","variety3","#ffffff","#3377ff",
            135,20,addIce,addLemon,addLime);
        selection[4] = new Selection("Milk","variety4","#000000","#ffffff",
            250,20,addIce,addChocolate,addVanilla);
        selection[5] = new Selection("Citrus","variety5","#000000","#ffff00",
            200,20,addLemon,addLime,addSugar);
        coinbox = new Coinbox(); 
    }
    boolean x=false;
    public void init(PostResult result){
        result.println("Machine.init()");
        // give the machine a name
        result.setText("tspan_java_machine","Ice Cold Drinks",0);
        result.setColor("tspan_java_machine","#3366ff",0);
        // initialize the rectangular selection buttons
        for(int i = 0; i<selection.length; i=i+1){
            selection[i].init(result); 
        }

        Addon.reset(result);
        result.setAudio("startup.mp3",0);
    }
    public void doClick(PostResult result, String id){
        if (id.startsWith("init")) {            
            
        }
        else if ( id.startsWith("rect_variety") ){ //  || id.startsWith("circle_add")
            result.println("machine.doclick("+id+")");
            int idi = Integer.parseInt(id.substring(12));
            selection[idi].press(result);
            gCurrentSelection = selection[idi];
            int demodelay =0;
            for(Selection s: selection){
                if ( s == gCurrentSelection ) {
                    s.on(result,100);
                }
                else {
                    demodelay += 1;
                    s.off(result,100*demodelay);
                }
            }
        }
        else if ( id.startsWith("circle_add") ) {
            result.println("machine.doclick("+id+")");
            int idi = Integer.parseInt(id.substring(10));

            if (gCurrentSelection != null){
                gCurrentSelection.getAddon(idi).press(result);
            }
            else {
                result.setAudio("groantick.mp3",0);
            }
        }
        else if ( id.startsWith("circle_coin_") ) {
            if (id.endsWith("mc_visa")) {
                coinbox.mc_visa(result);
            }
            else if (id.endsWith("coin_return")) {
                coinbox.coin_return(result);
            }
            else {
                coinbox.add(result, Integer.parseInt(id.substring(12)));
            }
        }

        if ( gCurrentSelection != null ) {
            int needed = gCurrentSelection.getPrice(result);
            int tended = coinbox.tended(result);
            String thanks = null;
            if (needed > 0) {
                if (tended > needed) {
                    thanks = "Change below";
                }
                else if (tended==needed) {
                    thanks = "You're cool";
                }
                else if (tended == -1) { // covered by mc/visa
                    thanks = "MC/Visa billed";
                    tended = needed;
                }
                else {
                    assert tended < needed;
                    thanks = "still needed";
                }
            }
            else{
                thanks = "Pick a Drink";
            }
            result.setText("tspan_still_needed",thanks,0);

            if ( tended >= needed ) { // deliver drink and partial reset (leave coins in return and drink in dispenser, but go to no drink selected)
                int change = tended - needed;
                result.setText("tspan_dollar_value_needed", "Thanks!" ,0);
                int c100 = change % 100;
                change = change - c100;
                result.setText("tspan_return_5x0", "$1 x " + c100,0);
                int c25 = change % 25;
                change = change - c25;
                result.setText("tspan_return_5x0", "25 x " + c25,0);
                int c10 = change % 10;
                change = change - c10;
                result.setText("tspan_return_5x0", "10 x " + c10,0);
                int c5 = change % 5;
                change = change - c5;
                result.setText("tspan_return_5x0", "5 x " + c5,0);
                assert change == 0;

                // run all the dispenser audio and animation

                // and reset the lights
            }
            else {
                result.setText("tspan_dollar_value_needed", "$" + String.format("%.2f",(needed - tended)/100.0 ),0);
            }
        }
        else { // nothing selected, light them all (unless they are out?)
            for(Selection s: selection){
                s.on(result,(int)(1000*Math.random()));
            }
        }
    }
}
