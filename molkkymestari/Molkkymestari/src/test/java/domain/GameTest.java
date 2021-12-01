/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import molkkymestari.domain.Game;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author palovpet
 */
public class GameTest {   
    
    Game game;
    
    @Before
    public void setUp() {
        game = new Game();
    }
    
    @Test
    public void getPointLimitReturnsPointLimit() {
        assertEquals(game.getPointLimit(), 50);
    }
    
    @Test
    public void getPointsToZeroWhenPointLimitPassedWithValueReturnsThat() {
        assertEquals(game.getPointsToZeroWhenPointLimitPassedWithValue(), false);
    }
    
    @Test
    public void getWhosTurnIndexReturnsThat() {
        assertEquals(game.getWhosTurnIndex(), 0);
    }
    
    @Test
    public void constructorWithIntValueGeneratesGameWithThatAsPointLimit(){
        Game newGame = new Game(25);
        assertEquals(newGame.getPointLimit(), 25);
    }
    
    @Test
    public void setPointLimitSetsThePointLimit() {
        game.setPointLimit(100);
        assertEquals(game.getPointLimit(), 100);
    }
    
    @Test
    public void setPointsToZeroWhenPointLimitPassedWithValueDoesThat() {
        game.setPointsToZeroWhenPointLimitPassedWithValue(true);
        assertEquals(game.getPointsToZeroWhenPointLimitPassedWithValue(), true);
    }
    
}
