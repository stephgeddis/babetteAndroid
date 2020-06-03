package uk.ac.qub.eeecs.gage;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import uk.ac.qub.eeecs.Babette.AI;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.Player;
import uk.ac.qub.eeecs.Babette.User;

public class BabetteGameScreenUnitTest{
    AI testAI = new AI("testAi", "Blue","Avatar1");
    AI testAI2 = new AI("testAi2", "Yellow","Avatar2");
    AI testAI3 = new AI("testAi3", "Red","Avatar3");
    User testUser = new User("testUser", "Green","Avatar3","password1");
    ArrayList<Player> playerList = new ArrayList<Player>();
    GameManager gameManager = new GameManager();

    //Stephen Irons 40204625
    //Test to check that the generateRandomNumber method returns a value between the limits
    @Test
    public void testGenerateRandomNumber(){
        int min = 0;
        int max = 6;
        int result = gameManager.generateNumber(min,max);

        assertTrue(result>=min&&result<=max);
    }

    //Stephen Irons 40204625
    //Checking an AI cannot be assigned the same colour as the logged in user
    @Test
    public void testCreateEnemyAIList(){
        User testUser = new User("testUser", "blue","Avatar1", "pass1");
        gameManager.setCurrentUser(testUser);

        ArrayList<Player> AIPlayerList = gameManager.createEnemyAIList();

        boolean colourRepeated = false;
        for(Player AIPlayer: AIPlayerList){
            if(testUser.getColour()==AIPlayer.getColour()){
                colourRepeated = true;
            }
        }

        assertFalse(colourRepeated);
    }

    //Test checkNotWinner()
    //Christopher Jennings 40200418
    @Test
    public void testcheckNotWinnerMethod(){
        ArrayList<Player> playerList = new ArrayList<Player>();
        //Adding in two users to the playerList
        User testUser1 = new User("testUser1", "Green","Avatar1","password1");
        User testUser2 = new User("testUser2", "Red","Avatar2","password2");
        playerList.add(testUser1);
        playerList.add(testUser2);

        boolean Winner = false;
            if (playerList.size()<2){
                Winner = true;
            }
        assertTrue(!Winner);
    }

    //Test checkWinner()
    //Christopher Jennings 40200418
    @Test
    public void testcheckWinnerMethod(){
        ArrayList<Player> playerList = new ArrayList<Player>();
        //Adding in one user to the playerList
        User testUser1 = new User("testUser1", "Green","Avatar1","password1");
        playerList.add(testUser1);

        boolean Winner = false;
        if (playerList.size()<2){
            Winner = true;
        }
        assertTrue(Winner);
    }
}
