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

        takeSelection(result,0); // clear leftovers

        Addon.reset(result);
        result.setAudio("startup.mp3",0);
    }
    public void doClick(PostResult result, String id){
        //result.println(id);
        
        if (id.startsWith("init")) {            
            
        }
        else {
            if ( id.startsWith("rect_variety") ){ //  || id.startsWith("circle_add")
                makeSelection(result,id);
            }
            else if ( id.startsWith("circle_add") ) {
                result.setAudio("keypress.mp3",0);
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
                if (!id.equals("circle_coin_return") && !id.equals("circle_coin_mc_visa")){
                    result.setAudio("keypress.mp3",0); // only the coin buttons
                }
                addMoney(result, id.substring(12));
            }
            else if ( id.equals("rect_coin_change") ) {
                result.println("change removed from slot");
                coinbox.emptyChangeReturn(result);
            }
            else if ( id.equals("idDispenserBackground") ) {
                takeSelection( result, 0);
                result.setAudio("plop.mp3",0);
            }
        }
        if ( gCurrentSelection != null ) {
            int needed = gCurrentSelection.getPrice(result);
            int tended = coinbox.getTended(result,needed);
            String thanks = null;
            if (needed > 0) {
                if (tended > needed) {
                    thanks = "Change below";
                    serveSelection(result);
                }
                else if (tended==needed) {
                    thanks = "You're cool";
                    serveSelection(result);
                }
                else if (tended == -1) { // covered by mc/visa
                    thanks = "MC/Visa billed";
                    tended = needed;
                    serveSelection(result);
                }
                else {
                    assert tended < needed;
                    thanks = "still needed";
                }
            }
            else{
                thanks = "Pick a Drink";
                coinbox.move_tended_to_coin_return(result);
            }
            result.setText("tspan_still_needed",thanks,0);
            coinbox.showTended(result,needed);
        }
        else { // nothing selected, light them all (unless they are out?)
            for(Selection s: selection){
                s.on(result,(int)(1000*Math.random()));
            }
        }
    }
    
    void makeSelection(PostResult result, String id){ // click on a variety button, Coke, for example
        result.setAudio("keypress.mp3",0);
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

    void serveSelection(PostResult result){ // tended >= price
        // ice first, if needed
        int needed = gCurrentSelection.getPrice(result);
        coinbox.payFromTended(result,needed);
        if (coinbox.getTended(result, 0) > 0) {
            coinbox.move_tended_to_coin_return(result);
        }
        if (gCurrentSelection != null){
            result.setOpacity("idGlassCup", "1", 0);
            if ( gCurrentSelection.has("Ice") ) {
                result.setOpacity("idIceCubes", "1", 200);
                result.setAudio("ice.mp3",200);
            }
            if ( gCurrentSelection.has("Vanilla") ) {
              //  result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.0,0,274)", 0);
              //  result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.2,0,250)", 50);
              //  result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.3,0,200)", 100);
              //  result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.4,0,150)", 150);
                result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.5,0,137)", 200);
                //result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.6,0, 50)", 250);
              //  result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.7,0, 25)", 300);
              //  result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,1,0,0.0)", 350);
                //result.setAudio("ice.mp3",200);
            }
        }
        // this animation sequence
        // o  drop change, play sound
        // o  add cup to dispenser, play plop sound
        // o  wipes in the ice cubes, reveal up, and plays sound
        // o  add lemon slice to rim, squish sound
        // o  add lime slice to rim, squish sound
        // o  add sugar specs, reveal down, play dust sound
        // o  chooses fluid color with transparency over ice, color by choc, van, caf   reveal up
        // o  raises cover to take drink, play up sound

     //   takeSelection(result,999); // the thirsty person should have to click, but for now...
    }

    void takeSelection(PostResult result, int delay){ // click on the dispenser
        // o  remove all items from dispenser
        // o  closes drink cover, play down sound
        // o  nothing selected. change remains if not taken.
        // result.setOpacity("idIceCubes", "1", 0);
        // result.setOpacity("idGlassCup", "1", 0);
        // result.setOpacity("idLimeSlice", "1", 0);
        // result.setOpacity("idLemonSlice", "1", 0);
        // result.setOpacity("idCaffeineMolecule", "1", 0);
        // result.setOpacity("idSugarMolecule", "1", 0);

        result.setOpacity("idIceCubes", "0", delay);
        result.setOpacity("idGlassCup", "0", delay);
        result.setOpacity("idLimeSlice", "0", delay);
        result.setOpacity("idLemonSlice", "0", delay);
        result.setOpacity("idCaffeineMolecule", "0", delay);
        result.setOpacity("idSugarMolecule", "0", delay);
        result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.001,0,274)", delay);

    }

    void addMoney(PostResult result, String idsuffix){ // includes mc/visa and coin return

        if (idsuffix.equals("return")) {
            result.println("add money running coin return");
            coinbox.move_tended_to_coin_return(result);
        }
        else if (idsuffix.equals("mc_visa")) {
            coinbox.pay_with_mc_visa(result);
        }
        else {
            coinbox.addCentsToTended(result, Integer.parseInt(idsuffix));
        }
    }

}
