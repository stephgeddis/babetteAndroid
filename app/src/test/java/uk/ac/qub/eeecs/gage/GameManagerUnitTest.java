package uk.ac.qub.eeecs.gage;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import uk.ac.qub.eeecs.Babette.AI;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.Player;
import uk.ac.qub.eeecs.Babette.User;

public class GameManagerUnitTest {
    //Stephen Iroms 40204625
    //Checking the generate number returns a number between min and max
    @Test
    public void testGenerateNumber(){
        int min = 0;
        int max = 6;
        int returnedValue;
        boolean valid = true;

        for(int i = 0; i < 10; i++){
             returnedValue = GameManager.generateNumber(min,max);
             if(returnedValue>max||returnedValue<min){
                 valid=false;
             }
        }
        assertTrue(valid);
    }

    //Stephen Iroms 40204625
    //Check that generate number can handle min < max
    @Test
    public void testGenerateNumberInvalidInput(){
        int min = 6;
        int max = 0;
        int returnedValue = GameManager.generateNumber(min,max);
        assertTrue(returnedValue==0);
    }

    //Stephen Iroms 40204625
    //Checking create enemy AI list returns a list of AI players
    @Test
    public void testCreateEnemyAIListreturnsAI(){
        GameManager gameManager = new GameManager();
        ArrayList<Player> AIArrayList = gameManager.createEnemyAIList();

        boolean isAI = true;
        for(Player player: AIArrayList){
            if(!(player instanceof AI)){
                isAI = false;
            }
        }
        assertTrue(isAI);
    }

    //Stephen Iroms 40204625
    //Ensuring create enemy AI List returns an array list with at least one AI player
    @Test
    public void testCreateEnemyAIListSizeGreaterThanEqualToOne() {
        GameManager gameManager = new GameManager();
        ArrayList<Player> AIArrayList = gameManager.createEnemyAIList();

        assertTrue(AIArrayList.size() >= 1);
    }

    //Stephen Iroms 40204625
    //Checking create enemy AI list returns An array list of AI with different colour to the current user
    @Test
    public void testCreateEnemyAIListDoesntReturnPlayersWithSameColour(){
        GameManager gameManager = new GameManager();
        User testUser = new User("User1","yellow","avatar1","pass1");
        gameManager.setCurrentUser(testUser);
        ArrayList<Player> AIArrayList = gameManager.createEnemyAIList();

        boolean differentColour = true;
        for(Player player: AIArrayList){
            if(player.getColour() == testUser.getColour()){
                differentColour = false;
            }
        }
        assertTrue(differentColour);
    }
}
