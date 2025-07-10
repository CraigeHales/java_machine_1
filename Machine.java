package controller; // the (virtual) pathname to the student-written files
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    Selection[] selection;
    static Selection gCurrentSelection = null;
    boolean dispenserIsEmpty = true;
    Coinbox coinbox = null;
    final String caffeine = "Caffeine";
    final String ice = "Ice";
    final String sugar = "Sugar";
    final String lime = "Lime";
    final String lemon = "Lemon";
    final String chocolate = "chocolate";
    final String vanilla = "Vanilla";
 //   static Coins coins = null;
    public Machine(/*PostResult result*/){
        // there are more addon possibilities than buttons; each selection will
        // choose exactly three addons for the three buttons. Each addon remembers
        // its current state; if it is shared between two selections then the
        // state is shared as well.
        Addon addIce = new Addon(ice,40,"/",0);
        Addon addCaffeine = new Addon(caffeine,0,"/",10);
        Addon addSugar = new Addon(sugar,30,"/",0);
        Addon addLime = new Addon(lime,10,"/",0);
        Addon addLemon = new Addon(lemon,10,"/",0);
        Addon addChocolate = new Addon(chocolate,50,"/",0);
        Addon addVanilla = new Addon(vanilla,60,"/",0);
        selection = new Selection[6];
        selection[0] = new Selection("Coke","variety0",//button label, internal variety ID
            "#ffffff","#ff3333",// button text and background
            175,20,// price, nStock
            addIce,addCaffeine,addSugar,// possible addons 
            "#351313",".87");// liquid color and transparency
        selection[1] = new Selection("7-Up","variety1","#333333","#66ff66",
            165,20,addIce,addLime,addSugar,"#005500",".11");
        selection[2] = new Selection("Sprite","variety2","#ffffff","#33aa33",
            155,20,addIce,addLemon,addSugar,"#444400",".11");
        selection[3] = new Selection("Water","variety3","#ffffff","#3377ff",
            135,20,addIce,addLemon,addLime,"#111111",".06");
        selection[4] = new Selection("Milk","variety4","#000000","#ffffff",
            250,20,addIce,addChocolate,addVanilla,"#ffffff",".87");
        selection[5] = new Selection("Citrus","variety5","#000000","#ffff00",
            200,20,addLemon,addLime,addSugar,"#999944",".33");
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

        takeSelection(result,0); // clear leftovers, all lights on

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
        // else { // nothing selected, light them all (unless they are out?)
        //     for(Selection s: selection){
        //         s.on(result,(int)(1000*Math.random()));
        //     }
        // }
    }
    
    void makeSelection(PostResult result, String id){ // click on a variety button, Coke, for example

        if (!dispenserIsEmpty) {
            result.println("<<< dispenser is not empty!!! >>>");
            result.setAudio("groantick.mp3",0);
            return;
        }

        

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
            int delay = 0;
            result.setOpacity("idGlassCup", "1", delay);
            delay += 100;

            if ( gCurrentSelection.wants(ice) ) {
                result.setOpacity("idIceCubes", "1", delay);
                result.setAudio("ice.mp3",delay);
                delay += 300;
            }

            result.setTransform("idLiquidDrinkTransform", "matrix(1,0,0,1,0,0.0)", delay);
            result.setAudio("pour.mp3",delay);
            delay += 2000;

            if ( gCurrentSelection.wants(vanilla) ) {
                result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,1,0,0.0)", delay);
                result.setAudio("pour.mp3",delay);
                delay += 1000;
            }

            if ( gCurrentSelection.wants(chocolate) ) {
                result.setTransform("idChocolateAddinTransform", "matrix(1,0,0,1,0,0.0)", delay);
                result.setAudio("pour.mp3",delay);
                delay += 1000;
            }

            if ( gCurrentSelection.wants(lime) ) {
                result.setOpacity("idLimeSlice", "1", delay);
                result.setAudio("lime.mp3",delay);
                delay += 100;
            }

            if ( gCurrentSelection.wants(lemon) ) {
                result.setOpacity("idLemonSlice", "1", delay);
                result.setAudio("lemon.mp3",delay);
                delay += 100;
            }

            if ( gCurrentSelection.wants(sugar) ) {
                result.setOpacity("idSugarMolecule", "1", delay);
                result.setAudio("sugar.mp3",delay);
                delay += 500;
            }
            
            if ( gCurrentSelection.wants(caffeine) ) {
                result.setOpacity("idCaffeineMolecule", "1", delay);
                result.setAudio("caffeine.mp3",delay);
                delay += 500;
            }

            dispenserIsEmpty = false;    
            Addon.takeBelow(result,delay);

            // disable the selection buttons until drink removed
            gCurrentSelection = null;
            for(Selection s: selection){
                s.gray(result,0);
            }
        }
    }

    void takeSelection(PostResult result, int delay){ // click on the dispenser

        result.println("takeselection "+delay);
        result.setOpacity("idIceCubes", "0", 1500);
        result.setOpacity("idGlassCup", "0", 2000); // remove cup last
        result.setOpacity("idLimeSlice", "0", 500);
        result.setOpacity("idLemonSlice", "0", 1000);
        result.setOpacity("idCaffeineMolecule", "0", 200);
        result.setOpacity("idSugarMolecule", "0", 700);
        result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
        result.setTransform("idLiquidDrinkTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
        result.setTransform("idChocolateAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down

        dispenserIsEmpty = true;
        // enable the selection buttons after drink removed
        gCurrentSelection = null;
        for(Selection s: selection){
            s.on(result,1000+(int)(1000*Math.random()));
        }
        
        Addon.reset(result);

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
