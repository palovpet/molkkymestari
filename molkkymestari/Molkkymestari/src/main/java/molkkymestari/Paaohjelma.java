/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package molkkymestari;
import domain.Game;
import domain.Player;
import static javafx.application.Application.launch;
import ui.MolkkyApplication;


/**
 *
 * @author palovpet
 */
public class Paaohjelma {
    
    Game molkky = new Game();
    Player player = new Player("Placeholder");
    
    
    
    public static void main(String[] args){
        launch(MolkkyApplication.class);
    }
    
    
    public static void changePointLimit(){
        
    }
    
    public static void changeWhatHappensIfPoinLimitIsPassed(){
        
    }
    
    public static void addPlayers(){
    
    }
    
}
    

