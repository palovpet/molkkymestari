package logic;

import molkkymestari.logic.MolkkyService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MolkkyServiceTest {
    MolkkyService service = new MolkkyService();
       
    @Before
    public void setUp() {
        service.addNewPlayer("Testi-Timjami");
        service.addNewPlayer("Testi-Tatiana");
    }
    
    @Test
    public void getPlayersPointsWithIndexDoesThat() {
        service.documentThrow("3");
        assertEquals(service.getPlayersPointsWithIndex(0), "3");
    }
    
    @Test
    public void getPointLimitReturnsPointLimit() {
        assertEquals(service.getPointLimit(), 50);
    }
    
    @Test
    public void setPointLimitChangesPointLimit() {
        service.setPointLimit(30);
        assertEquals(service.getPointLimit(), 30);
    }
    
    @Test
    public void getPointsToZeroWhenPointLimitPassedReturnsThat() {
        assertEquals(service.getPointsToZeroWhenPointLimitPassed(), false);
    }
    
    @Test
    public void getHowManyPlayersReturnsCorrectInformation() {
        assertEquals(service.getHowManyPlayers(), 2);
    }
    
    @Test
    public void documentThrowWontDocumentNegativeNumbers() {
        service.documentThrow("-1");
        assertEquals(service.getPlayersPointsWithIndex(service.getWhosTurnIndex()), "0");
    }
    
    @Test
    public void documentThrowWontDocumentNumbersHigherThanTwelwe() {
        service.documentThrow("13");
        assertEquals(service.getPlayersPointsWithIndex(service.getWhosTurnIndex()), "0");
    }
    
    @Test
    public void checkIfIntegerReturnsFalseForNonInteger() {
        assertEquals(service.checkIfInteger("testi"), false);
    }
    
    @Test
    public void checkIfIntegerReturnsTrueForInteger() {
        assertEquals(service.checkIfInteger("5"), true);
    }
    
    @Test
    public void getWhosTurnNameReturnsCorrectName() {
        assertEquals(service.getWhosTurnName(), "Testi-Timjami");
    }
    
    @Test
    public void getWhosNextNameReturnsCorrectName() {
        assertEquals(service.getWhosNextName(), "Testi-Tatiana");
    }
    
    @Test
    public void getWhosTurnNameReturnsCorrectNameAfterThrow() {
        service.documentThrow("5");
        assertEquals(service.getWhosTurnName(), "Testi-Tatiana");
    }
    
    @Test
    public void getWhosNextNameReturnsCorrectNameAfterThrow() {
        service.documentThrow("6");
        assertEquals(service.getWhosNextName(), "Testi-Timjami");
    }
    
    @Test
    public void checkIValidNumberReturnsFalseForNonIntegers() {
        assertEquals(service.checkIfValidNumber("piste"), false);
    }
    
    @Test
    public void getPlayersNameWithIndexReuturnsThat() {
        assertEquals(service.getPlayersNameWithIndex(1), "Testi-Tatiana");
    }
    
    @Test
    public void getPlayersToPrintReturnsPlayersSeparatedOnOwnRows() {
        assertEquals(service.getPlayersToPrint(), "Testi-Timjami\nTesti-Tatiana\n");
    }
    
    @Test
    public void checkIfNameIsAlreadyAddedReturnsTrueIfPlayerIsFound() {
        assertEquals(service.checkIfNameIsAlreadyAdded("Testi-Timjami"), true);
    }
    
    @Test
    public void checkIfNameIsAlreadyAddedReturnsFalseIfPlayerIsNotFound() {
        assertEquals(service.checkIfNameIsAlreadyAdded("Testi-Tauno"), false);
    }
    
    @Test
    public void createPointTableCreatesPointTable() {
        service.documentThrow("3");
        service.documentThrow("5");
        
        assertEquals(service.createPlayerPointTable(), "\n" +
            " Pelaaja: Testi-Timjami, pisteitä:  3\n" +
            " Pelaaja: Testi-Tatiana, pisteitä:  5");
    }
}
