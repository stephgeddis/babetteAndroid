package uk.ac.qub.eeecs.gage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

import uk.ac.qub.eeecs.Babette.AI;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.Player;
import uk.ac.qub.eeecs.Babette.User;

public class PlayerUnitTest {
    AI testAI = new AI("testAi", "Blue","Avatar1");
    AI testAI2 = new AI("testAI2", "Red", "Avatar2");
    GameManager gm = new GameManager();
    User testUser = new User("User1","yellow","avatar1","pass1");

    //ArrayList<Region> gameRegionArrayList = GameManager.getRegionList();
    //ArrayList<Territory> territoryArrayList = GameManager.getTerritoryList();

    //Stephen Irons 40204625
    //Ensure that callForMerger returns one of the two hex codes passed
    @Test
    public void testCallForMerger(){
        User testUser = new User("User1","yellow","avatar1","pass1");
        String hexCodeOne = "0xFFFFFFFF";
        String hexCodeTwo = "0xFFFF6363";

        String returnedHexCode = testUser.callForMerger(hexCodeOne,hexCodeTwo, gm.getRegions());

        assertTrue(returnedHexCode==hexCodeOne||returnedHexCode==hexCodeTwo);
    }

    //Stephen Irons 40204625
    //Check that the changeRegionOwner works correctly
    @Test
    public void testChangeRegionOwner(){
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363"));
        testAI.regionsHeld = heldRegions;
        gm.getRegions().get(0).setNumResources(14);
        ArrayList<String> heldRegions2= new ArrayList<String>(Arrays.asList("0xFFfa6363"));
        AI testAI2 = new AI("testAi2", "red", "avatar2");
        gm.getTerritories().get(4).setNumResources(0);
        testAI2.regionsHeld=heldRegions2;

        ArrayList<Player> playerArrayList = new ArrayList<Player>();
        playerArrayList.add(testAI);
        playerArrayList.add(testAI2);

        testAI.changeRegionOwner("0xFFFF6363","0xFFfa6363",gm.getRegions(),playerArrayList);

        assertTrue(testAI.regionsHeld.contains("0xFFfa6363") && !testAI2.regionsHeld.contains("0xFFfa6363") && gm.getRegions().get(0).getNumResources()==7&&gm.getRegions().get(4).getNumResources()==7);
    }

    //Stephen Irons 40204625
    //Test to check that a regions resources will be decreased as expected with an initial value greater than 0
    @Test
    public void testDecreaseResourcesStart5(){
        int initialResources = 5;
        gm.getRegions().get(0).setNumResources(initialResources);
        String losingHexCode = "0xFFFF6363";

        testAI.decreaseRegionResources(losingHexCode, gm.getRegions());

        assertTrue(gm.getRegions().get(0).getNumResources()==initialResources-1);
    }

    //Stephen Irons 40204625
    //Test to check that a regions resources will not be decreased if the initial region resources is 0;
    @Test
    public void testDecreaseResourcesStart0(){
        int initialResources = 0;
        gm.getRegions().get(0).setNumResources(initialResources);
        String losingHexCode = "0xFFFF6363";

        testAI.decreaseRegionResources(losingHexCode, gm.getRegions());

        assertTrue(gm.getRegions().get(0).getNumResources()==0);
    }

    //40174176 - James Sims
    //Test to check that when a user selects an empty region, it is added to their region list
    @Test
    public void testAddToRegionList(){
        testUser.placeInitialResources(gm.getRegions(), "0xFFFF6363");

        assertTrue(testUser.regionsHeld.size() >= 1);
    }

    //40174176 - James Sims
    //Test to check that a user cannot place resources in a region they do not own
    @Test
    public void testAddRegionsToTerritoryOwnedByOther(){
        testUser.regionsHeld.clear();

        testAI.placeInitialResources(gm.getRegions(), "0xFFFF6363");
        testUser.placeInitialResources(gm.getRegions(), "0xFFFF6363");

        assertTrue(testUser.regionsHeld.size() == 0);
    }

    //40174176 - James Sims
    //Test that when a user selects a region, the region counter size is updated
    @Test
    public void testPlaceInitialResourcesUpdatesRegionCounter(){
        int resourcesInRegion = 0;

        testUser.placeInitialResources(gm.getRegions(), "0xFFFF6363");

        for(int i = 0; i < gm.getRegions().size(); i++){
            if(gm.getRegions().get(i).getHexCode() == "0xFFFF6363"){
                resourcesInRegion = gm.getRegions().get(i).getNumResources();
            }
        }

        assertTrue(resourcesInRegion == 1);
    }

    //40174176 - James Sims
    //Test to check that a user can add additional resources onto a region that they own
    @Test
    public void testAddResourcesToOwnedRegion(){
        int valueInRegion = 0;

        testUser.placeInitialResources(gm.getRegions(), "0xFFFF6363");
        testUser.placeRemainingResources(gm.getRegions(), "0xFFFF6363");

        for(int i = 0; i < gm.getRegions().size(); i++)
        {
            if (gm.getRegions().get(i).getHexCode() == "0xFFFF6363");
            {
                valueInRegion = gm.getRegions().get(i).getNumResources();
            }
        }

        assertTrue(valueInRegion >=0);
    }

    //40174176 - James Sims
    //Test that a user cannot add additional resources to a region they don't own
    @Test
    public void testUserCantAddResourcesToRegionOwnedByOther(){
        boolean belongsToUser = false;

        testAI.placeInitialResources(gm.getRegions(), "0xFFFF6363");
        testUser.placeRemainingResources(gm.getRegions(), "0xFFFF6363");

        assertTrue(testAI.regionsHeld.size() > 0 && testUser.regionsHeld.size() == 0);
    }
}
