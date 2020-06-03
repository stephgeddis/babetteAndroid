package uk.ac.qub.eeecs.gage;
import org.junit.Test;

import java.util.ArrayList;

import uk.ac.qub.eeecs.Babette.AI;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.Player;
import uk.ac.qub.eeecs.Babette.User;

import static org.junit.Assert.*;
import static uk.ac.qub.eeecs.Babette.PlayerTurnOrderScreen.sortPlayer2DArray;
import static uk.ac.qub.eeecs.Babette.PlayerTurnOrderScreen.sortPlayerTurn;

public class PlayerTurnOrderScreenUnitTest {
    AI testAI = new AI("testAi", "Blue","Avatar1");
    AI testAI2 = new AI("testAi2", "Yellow","Avatar2");
    AI testAI3 = new AI("testAi3", "Red","Avatar3");
    User testUser = new User("testUser", "Green","Avatar3","password1");
    ArrayList<Player> playerList = new ArrayList<Player>();
    GameManager gm = new GameManager();

    //Stephen Irons 40204625
    //Test to check that the method that sorts the 2D player array orders the players correctly
    @Test
    public void checkPlayersOrderedDescendingOrder(){
        playerList.add(testAI);
        playerList.add(testAI2);
        playerList.add(testAI3);
        playerList.add(testUser);

        String[][] player2DArray = new String[4][2];

        for(int i = 0; i< 4;i++){
            player2DArray[i][0]= playerList.get(i).getUsername();
            player2DArray[i][1]= Integer.toString(gm.generateNumber(0,6));
        }

        player2DArray = sortPlayer2DArray(player2DArray);

        boolean correctOrder = true;
        for(int i = 0;i<player2DArray.length-1;i++){

            if(Integer.parseInt(player2DArray[i][1])<Integer.parseInt(player2DArray[i+1][1])){
                correctOrder = false;
                break;
            }
        }
        assertTrue(correctOrder);
    }

    //Stephen Irons 40204625
    //Test to ensure that the sortPlayerTurn returns a player arrayList the same size as the initial arrayList
    @Test
    public void sortPlayerTurnSize(){
        playerList.add(testAI);
        playerList.add(testAI2);
        playerList.add(testAI3);
        playerList.add(testUser);

        ArrayList<Player> orderedPlayerList = sortPlayerTurn(playerList);

        assertTrue(orderedPlayerList.size()==playerList.size());
    }

    //Stephen Irons 40204625
    //Test to ensure sortPlayerTurn doesnt insert the same user more than once to the sorts ArrayList
    @Test
    public void sortPlayerTurnNoDuplicates(){
        playerList.add(testAI);
        playerList.add(testAI2);
        playerList.add(testAI3);
        playerList.add(testUser);

        ArrayList<Player> orderedPlayerList = sortPlayerTurn(playerList);

        boolean duplicatesFound = false;
        for(int i = 0; i<orderedPlayerList.size();i++){
            for(int j= 0; j<orderedPlayerList.size();j++){
                if(j!=i && orderedPlayerList.get(i).getUsername()==orderedPlayerList.get(j).getUsername()){
                    duplicatesFound = true;
                }
            }
        }
        assertFalse(duplicatesFound);
    }

}


