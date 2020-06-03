package uk.ac.qub.eeecs.gage;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import uk.ac.qub.eeecs.Babette.AI;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.Region;
import uk.ac.qub.eeecs.Babette.Territory;

public class AIUnitTest {
    GameManager gm = new GameManager();

    AI testAI = new AI("testAi", "Blue","Avatar1");
    AI testAI2 = new AI("testAI2", "Red", "Avatar2");
    ArrayList<Region> gameRegionArrayList = gm.getRegions();
    ArrayList<Territory> territoryArrayList = gm.getTerritories();

    //Stephen Irons 40204625
    //Test to check getattackableRegions returns all valid regions only, it doesnt return a neighbouring region owned by the player
    @Test
    public void testGetAttackableRegions(){
        String antiVirusRegionHexCode = "0xFFFF6363";
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363","0xFFfa6363"));
        testAI.regionsHeld=heldRegions;

        ArrayList<String> attackableRegions = testAI.getAttackableRegions(gameRegionArrayList, antiVirusRegionHexCode);

        assertTrue(attackableRegions.contains("0xFFfb6363") && attackableRegions.contains("0xFFACDDFF") && attackableRegions.size()==2);
    }

    //Stephen Irons 40204625
    //Ensuring possibleAttack method returns false when no possible attacking regions are present
    @Test
    public void testPossibleAttackNotPossible(){
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363", "0xFFfd6363"));
        testAI.regionsHeld = heldRegions;
        gameRegionArrayList.get(0).setNumResources(1);
        gameRegionArrayList.get(1).setNumResources(1);
        boolean attackPossible = testAI.possibleAttack(gameRegionArrayList);

        assertTrue(!attackPossible);
    }

    //Stephen Irons 40204625
    //Ensuring possibleAttack method returns true when a region with more than 1 resource surrounded by regions not owned by the player
    @Test
    public void testPossibleAttackPossible(){
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363", "0xFFfd6363"));
        testAI.regionsHeld = heldRegions;
        gameRegionArrayList.get(0).setNumResources(2);
        gameRegionArrayList.get(1).setNumResources(1);
        boolean attackPossible = testAI.possibleAttack(gameRegionArrayList);

        assertTrue(attackPossible);
    }

    //Stephen Irons 40204625
    //Ensuring possibleAttack method returns false when a region has enough resources to attack but is surrounded by regions
    //owned by the same player
    @Test
    public void testPossibleAttackPossibleSurrounded(){
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363", "0xFFfd6363", "0xFFfa6363", "0xFFACDDFF"));
        testAI.regionsHeld = heldRegions;
        gameRegionArrayList.get(0).setNumResources(2);
        for(int i = 0; i<gameRegionArrayList.size();i++) {
            gameRegionArrayList.get(i).setNumResources(1);
        }
        boolean attackPossible = testAI.possibleAttack(gameRegionArrayList);

        assertTrue(!attackPossible);
    }

    //Stephen Irons 40204625
    //check that getPlayerTerritoryStatistics returns correct data for the player
    @Test
    public void testGetPlayerTerritoryStatistics(){
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFfd6363", "0xFFfc6363", "0xFFfb6363", "0xFFFFD078","0xFFA8DDFF", "0xFFACDDFF","0xFFFFD7DC", "0xFFFBD7DC", "0xFFFDD7DC", "0xFFFED7DC", "0xFFFCD7DC","0xFFFAFF7B", "0xFFFBFF7B", "0xFFF9FF7B", "0xFFF8FF7B"));

        testAI.regionsHeld = regionsHeld;

        for(Region region:gameRegionArrayList){
            if(region.getHexCode()=="0xFFfd6363"){
                region.setNumResources(11);
            }
            region.setNumResources(5);
        }

        int[][] testedPlayerTerritoryStatistics = testAI.getPlayerTerritoryStatistics(territoryArrayList,gameRegionArrayList);

        int[][] playerTerritoryStatistics = new int[territoryArrayList.size()][3];
        //checks if method can handle a number that has a remainder when calculationg percentage
        playerTerritoryStatistics[0][0]= 3;
        playerTerritoryStatistics[0][1]= 67;

        //checks can handle divide by zero
        playerTerritoryStatistics[1][0]= 0;
        playerTerritoryStatistics[1][1]= 0;

        playerTerritoryStatistics[2][0]= 1;
        playerTerritoryStatistics[2][1]= 20;

        playerTerritoryStatistics[3][0]= 2;
        playerTerritoryStatistics[3][1]= 40;

        playerTerritoryStatistics[4][0]= 5;
        playerTerritoryStatistics[4][1]= 100;

        playerTerritoryStatistics[5][0]= 4;
        playerTerritoryStatistics[5][1]= 80;

        boolean isIdentical = true;
        for(int i = 0; i<testedPlayerTerritoryStatistics.length;i++){
            for(int j = 0; j<1;j++){
                if(testedPlayerTerritoryStatistics[i][j]!= playerTerritoryStatistics[i][j]){
                    isIdentical = false;
                }
            }
        }

        assertTrue(isIdentical);
    }

    //Stephen Irons 40204625
    //Ensuring that the getBestTerritoryToAttack returns the index of the region with the highest percentage of player resources that
    //isnt 100%
    @Test
    public void testGetBestTerritoryToAttackFrom(){
        int[][] playerTerritoryStatistics = new int[territoryArrayList.size()][3];
        playerTerritoryStatistics[0][0]= 0;
        playerTerritoryStatistics[0][1]= 3;
        playerTerritoryStatistics[0][2]= 49;

        playerTerritoryStatistics[1][0]= 1;
        playerTerritoryStatistics[1][1]= 4;
        playerTerritoryStatistics[1][2]= 79;

        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFD087FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        testAI.regionsHeld = regionsHeld;

        playerTerritoryStatistics[2][0]= 2;
        playerTerritoryStatistics[2][1]= 2;
        playerTerritoryStatistics[2][2]= 35;

        playerTerritoryStatistics[3][0]= 3;
        playerTerritoryStatistics[3][1]= 1;
        playerTerritoryStatistics[3][2]= 56;

        playerTerritoryStatistics[4][0]= 4;
        playerTerritoryStatistics[4][1]= 0;
        playerTerritoryStatistics[4][2]= 0;


        playerTerritoryStatistics[5][0]= 5;
        playerTerritoryStatistics[5][1]= 5;
        playerTerritoryStatistics[5][2]= 100;

        int territoryIndex = testAI.getBestTerritoryToAttackFrom(playerTerritoryStatistics, territoryArrayList, gameRegionArrayList);

        assertTrue( territoryIndex==1);

    }

    //Stephen Irons 40204625
    //Ensuring that the getBestTerritoryToAttack returns the index of the region with 100% when the player doesnt own regions in any other territories
    @Test
    public void testGetBestTerritoryToAttackFromOneTerritoryWith100(){
        int[][] playerTerritoryStatistics = new int[territoryArrayList.size()][3];
        playerTerritoryStatistics[0][0]= 0;
        playerTerritoryStatistics[0][1]= 0;
        playerTerritoryStatistics[0][2]= 0;

        playerTerritoryStatistics[1][0]= 1;
        playerTerritoryStatistics[1][1]= 0;
        playerTerritoryStatistics[1][2]= 0;

        playerTerritoryStatistics[2][0]= 2;
        playerTerritoryStatistics[2][1]= 0;
        playerTerritoryStatistics[2][2]= 0;

        playerTerritoryStatistics[3][0]= 3;
        playerTerritoryStatistics[3][1]= 0;
        playerTerritoryStatistics[3][2]= 0;

        playerTerritoryStatistics[4][0]= 4;
        playerTerritoryStatistics[4][1]= 5;
        playerTerritoryStatistics[4][2]= 100;

        testAI.regionsHeld.clear();
        for(String regionHexCodeInTerritoryList: territoryArrayList.get(4).getRegions()){
            for(Region region: gameRegionArrayList){
                if(region.getHexCode()==regionHexCodeInTerritoryList){
                    testAI.regionsHeld.add(region.getHexCode());
                    region.setNumResources(5);
                }
            }
        }

        playerTerritoryStatistics[5][0]= 5;
        playerTerritoryStatistics[5][1]= 0;
        playerTerritoryStatistics[5][2]= 0;

        int territoryIndex = testAI.getBestTerritoryToAttackFrom(playerTerritoryStatistics, territoryArrayList, gameRegionArrayList);

        assertTrue( territoryIndex==4);

    }

    //Stephen Irons 40204625
    //Ensuring that the getBestTerritoryToAttack returns the index of the correct territory when 2 territories are controlled 100% by the player
    //but it is ponly possible to attack from 1 of them
    @Test
    public void testGetBestTerritoryToAttackFromTwoTerritoriesWith100(){
        int[][] playerTerritoryStatistics = new int[territoryArrayList.size()][3];
        playerTerritoryStatistics[0][0]= 5;
        playerTerritoryStatistics[0][1]= 100;

        testAI.regionsHeld.clear();
        for(String regionHexCodeInTerritoryList: territoryArrayList.get(0).getRegions()){
            for(Region region: gameRegionArrayList){
                if(region.getHexCode()==regionHexCodeInTerritoryList){
                    testAI.regionsHeld.add(region.getHexCode());
                    region.setNumResources(1);
                    if(region.getHexCode()=="0xFFfd6363"){
                        region.setNumResources(5);
                    }
                }
            }
        }

        playerTerritoryStatistics[1][0]= 0;
        playerTerritoryStatistics[1][1]= 0;

        playerTerritoryStatistics[2][0]= 0;
        playerTerritoryStatistics[2][1]= 0;

        playerTerritoryStatistics[3][0]= 0;
        playerTerritoryStatistics[3][1]= 0;

        playerTerritoryStatistics[4][0]= 5;
        playerTerritoryStatistics[4][1]= 100;

        for(String regionHexCodeInTerritoryList: territoryArrayList.get(4).getRegions()){
            for(Region region: gameRegionArrayList){
                if(region.getHexCode()==regionHexCodeInTerritoryList){
                    testAI.regionsHeld.add(region.getHexCode());
                    region.setNumResources(5);
                }
            }
        }

        playerTerritoryStatistics[5][0]= 0;
        playerTerritoryStatistics[5][1]= 0;

        int territoryIndex = testAI.getBestTerritoryToAttackFrom(playerTerritoryStatistics, territoryArrayList, gameRegionArrayList);

        assertTrue( territoryIndex==4);

    }

    //Stephen Irons 40204625
    //Checking getRegionsToAttackFrom returns valid region hex codes
    @Test
    public void testGetRegionsToAttackFrom(){
        int territoryPosition = 0;

        for(Region region: gameRegionArrayList){
            region.setNumResources(5);
        }
        //"0xFFfd6363" shouldnt be in offensive regions
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFfd6363", "0xFFfc6363", "0xFFfb6363", "0xFFfa6363", "0xFFFF6363"));
        testAI.regionsHeld = regionsHeld;

        ArrayList<String> offensiveRegions = testAI.getRegionsToAttackFrom( territoryPosition, territoryArrayList,gameRegionArrayList);

        assertTrue(offensiveRegions.contains("0xFFfc6363")&&offensiveRegions.contains("0xFFfb6363")&&offensiveRegions.contains("0xFFfa6363")&&offensiveRegions.contains("0xFFFF6363")&&offensiveRegions.size()==4);
    }

    //Stephen Irons 40204625
    //Checking getRegionsToAttackFrom returns valid region hex codes
    @Test
    public void testGetRegionsToAttackFromNoValidRegions(){
        int territoryPosition = 0;

        for(Region region: gameRegionArrayList){
            region.setNumResources(5);
        }
        //"0xFFfd6363" shouldnt be in offensive regions
        ArrayList<String> regionsHeld = new ArrayList<>();
        testAI.regionsHeld = regionsHeld;

        ArrayList<String> offensiveRegions = testAI.getRegionsToAttackFrom( territoryPosition, territoryArrayList,gameRegionArrayList);

        assertTrue(offensiveRegions.size() == 0);
    }

    //Stephen Irons 40204625
    //Test to check if the getPlayersRandomRegionHexCode method returns a hexcode within the players list of hexcodes
    @Test
    public void testGetRandomRegionHexCode(){
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363","0xFFACDDFF","0xFFFCD078"));
        testAI.regionsHeld = heldRegions;

        boolean containsValue = true;
        String returnedValue="";
        for(int i = 0; i< 5; i++) {
            returnedValue = testAI.getRandomRegionHexCode(0, testAI.regionsHeld);
            if(!testAI.regionsHeld.contains(returnedValue)){
                containsValue = false;
            }
        }

         assertTrue(containsValue);
    }

    //Stephen Irons 40204625
    //Test to check if the getPlayersRandomRegionHexCode method returns the first hex value if a number greater than the number of values is passed
    @Test
    public void testGetRandomRegionHexCodeAboveMax(){
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363","0xFFACDDFF","0xFFFCD078"));
        testAI.regionsHeld = heldRegions;

         String returnedValue = testAI.getRandomRegionHexCode(testAI.regionsHeld.size()-1, testAI.regionsHeld);

         //testAI.regionsHeld.contains(testAI.regionsHeld.get(0))
         assertTrue(returnedValue == "0xFFFCD078");
    }

    //Stephen Irons 40204625
    //Test to check if the getPlayersRandomRegionHexCode method returns the first hex value if a number less than 0 is passed
    @Test
    public void testGetRandomRegionHexCodeBelowMin(){
        ArrayList<String> heldRegions= new ArrayList<String>(Arrays.asList("0xFFFF6363","0xFFACDDFF","0xFFFCD078"));
        testAI.regionsHeld = heldRegions;

         String returnedValue = testAI.getRandomRegionHexCode(-1, testAI.regionsHeld);

         assertTrue(testAI.regionsHeld.contains(testAI.regionsHeld.get(0)));
    }

    //Jake Smyth 40204021
    //Test to check that resources are transferred from one region to another after passed into the fortify() method.
    @Test
    public void testFortifyFromOneToTwo() {
        int initialResourcesOne = 5;
        gameRegionArrayList.get(0).setNumResources(initialResourcesOne);
        String hexCodeOne = "0xFFFF6363";

        int initialResourcesTwo = 5;
        gameRegionArrayList.get(1).setNumResources(initialResourcesTwo);
        String hexCodeTwo = "0xFFfd6363";

        int regionOneInitialResources = gameRegionArrayList.get(0).getNumResources();
        int regionTwoInitialResources = gameRegionArrayList.get(1).getNumResources();

        testAI.fortify(hexCodeOne, hexCodeTwo, gameRegionArrayList);

        int regionOneFinalResources = gameRegionArrayList.get(0).getNumResources();
        int regionTwoFinalResources = gameRegionArrayList.get(1).getNumResources();

        assertTrue( (regionOneInitialResources + regionTwoInitialResources == regionOneFinalResources + regionTwoFinalResources) && regionOneInitialResources != regionOneFinalResources);
    }

    //Jake Smyth 40204021
    //Test to check that two regions can be found before passing the fortify() method.
    @Test
    public void testFindAndFortify() {
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFfd6363", "0xFFfc6363", "0xFFfb6363", "0xFFfa6363", "0xFFFF6363"));
        testAI.regionsHeld = regionsHeld;
        int sumBeforeFortify = 0;
        int sumAfterFortify = 0;
        for(Region r : gameRegionArrayList) {
            r.setNumResources(10);
            sumBeforeFortify += r.getNumResources();
        }

        testAI.selectFortifyTerritories(gameRegionArrayList);
        boolean resourcesChanged = false;
        for(Region r : gameRegionArrayList) {
            if(r.getNumResources() != 10)
                resourcesChanged = true;
            sumAfterFortify += r.getNumResources();
        }

        assertTrue( (sumAfterFortify == sumBeforeFortify) && resourcesChanged);
    }

    //Jake Smyth 40204021
    //Test to check that an array of regions with neighbours can be filled.
    @Test
    public void testFindRegionsWithNeighbours() {
        for(Region r : gameRegionArrayList) {
            r.setNumResources(10);
        }
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFD087FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        testAI.regionsHeld = regionsHeld;
        ArrayList<String> hexCodes = testAI.regionsHeld;
        ArrayList<String> regionsWithNeighbours = testAI.findAIRegionsWithNeighbours(hexCodes, gameRegionArrayList);

        assertTrue( regionsWithNeighbours.size() > 1);
    }

    //Jake Smyth 40204021
    //Test to check that a region to fortify from can be found.
    @Test
    public void testFindRegionOne() {
        for(Region r : gameRegionArrayList) {
            r.setNumResources(10);
        }
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFD087FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        testAI.regionsHeld = regionsHeld;
        ArrayList<String> hexCodes = testAI.regionsHeld;
        ArrayList<String> regionsWithNeighbours = testAI.findAIRegionsWithNeighbours(hexCodes, gameRegionArrayList);
        String regionOne = testAI.getRandomAIRegionToFortifyFrom(regionsWithNeighbours, gameRegionArrayList);

        assertTrue( regionOne != "");
    }

    //Jake Smyth 40204021
    //Test to check that region one's adjacent regions can be found.
    @Test
    public void testFindRegionOneAdjacentRegions() {
        for(Region r : gameRegionArrayList) {
            r.setNumResources(10);
        }
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFD087FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        testAI.regionsHeld = regionsHeld;
        ArrayList<String> hexCodes = testAI.regionsHeld;
        ArrayList<String> regionsWithNeighbours = testAI.findAIRegionsWithNeighbours(hexCodes, gameRegionArrayList);
        String regionOne = testAI.getRandomAIRegionToFortifyFrom(regionsWithNeighbours, gameRegionArrayList);
        ArrayList<String> adjacentOwnedRegions = testAI.getRegionOneListOfAdjacentOwnedRegions(regionOne, hexCodes, gameRegionArrayList);

        assertTrue( adjacentOwnedRegions.size() != 0);
    }

    //Jake Smyth 40204021
    //Tests to ensure that the checkFortifyPossible method is working.
    @Test
    public void testCheckFortifyIsPossible() {
        for(Region r : gameRegionArrayList) {
            r.setNumResources(10);
        }
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFD087FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        testAI.regionsHeld = regionsHeld;
        ArrayList<String> hexCodes = testAI.regionsHeld;
        boolean fortifyPossible = testAI.checkFortifyPossible(hexCodes, gameRegionArrayList);
        assertTrue(fortifyPossible);
    }

    @Test
    public void testCheckFortifyIsNotPossibleNoRegions() {
        for(Region r : gameRegionArrayList) {
            r.setNumResources(10);
        }
        ArrayList<String> hexCodes = testAI.regionsHeld;
        boolean fortifyPossible = testAI.checkFortifyPossible(hexCodes, gameRegionArrayList);
        assertFalse(fortifyPossible);
    }

    @Test
    public void testCheckFortifyIsNotPossibleNoResources() {
        for(Region r : gameRegionArrayList) {
            r.setNumResources(0);
        }
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFD087FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        testAI.regionsHeld = regionsHeld;
        ArrayList<String> hexCodes = testAI.regionsHeld;
        boolean fortifyPossible = testAI.checkFortifyPossible(hexCodes, gameRegionArrayList);
        assertFalse(fortifyPossible);
    }

    //Test to check a hex code is added to a players list
    //Stephen Irons 40204625 & James Sims 40174176 & Adam Higgins 40212255
    @Test
    public void testplaceResourcesInEmptyRegions(){
        testAI.placeResourcesInEmptyRegions(gameRegionArrayList);
        assertTrue(testAI.regionsHeld.size()>0);
    }

    //40174176 - James Sims
    //Test to check that the AI method for placing resources will actually increase at least one of the regions
    @Test
    public void testPlaceRemainingResources(){
        GameManager gameManager = new GameManager();
        ArrayList<AI> AIs= new ArrayList<AI>(Arrays.asList(testAI,testAI2));
        for(int i=0; i<15; i++) {
            for (AI ai : AIs) {
                ai.placeResourcesInEmptyRegions(gameRegionArrayList);
            }
        }

        /*for(Region region: gameRegionArrayList){
            if(region.getNumResources() != 1){
                region.setNumResources(1);
            }
        }*/
        testAI.placeRemainingResources(gameRegionArrayList, territoryArrayList, gameManager);
        boolean regionWithMoreThanOne = false;
        for(Region region:gameRegionArrayList){
            if(region.getNumResources() > 1){
                regionWithMoreThanOne = true;
                break;
            }
        }
        assertTrue(regionWithMoreThanOne);
    }

    //40174176 - James Sims
    //Test to check that when an AI places resources in a region initially, their held resources list is updated.
    @Test
    public void testPlaceInitialResources(){
        ArrayList<AI> AIs= new ArrayList<AI>(Arrays.asList(testAI,testAI2));
        for(int i=0; i<15; i++) {
            for (AI ai : AIs) {
                ai.placeResourcesInEmptyRegions(gameRegionArrayList);
            }
        }
        assertTrue(testAI.regionsHeld.size() == 15 && testAI2.regionsHeld.size() == 15);
    }

    //Christopher Jennings
    //40200418 - Test to get the held region resources within a territory
    @Test
    public void testGetHeldRegionResources(){
        ArrayList<String> regionsHeld = new ArrayList<>(Arrays.asList("0xFFfd6363", "0xFFfc6363", "0xFFfb6363", "0xFFFFD078","0xFFA8DDFF", "0xFFACDDFF","0xFFFFD7DC", "0xFFFBD7DC", "0xFFFDD7DC", "0xFFFED7DC", "0xFFFCD7DC","0xFFFAFF7B", "0xFFFBFF7B", "0xFFF9FF7B", "0xFFF8FF7B"));

        testAI.regionsHeld = regionsHeld;

        int returnedvalue = testAI.getHeldRegionResources();

        assertTrue(returnedvalue >= 1);
    }

    //Christopher Jennings
    //40200418 - Test to get the held territory resources taken from all the regions held.
    @Test
    public void testGetHeldTerritoryResources(){

        testAI.regionsHeld = new ArrayList<>(Arrays.asList("0xFFfd6363", "0xFFfc6363", "0xFFfb6363", "0xFFfa6363", "0xFFFF6363"));
        int returnResources = testAI.getHeldRegionResources();

        assertTrue(returnResources==territoryArrayList.get(0).getNumResources());
    }

    //Christopher Jennings
    //40200418 - Test to get the held territory resources within a region
    @Test
    public void testGetHeldTerritoryResourcesNoTerritoryHeld(){
        testAI.regionsHeld = new ArrayList<>(Arrays.asList("0xFFfb6363", "0xFFfa6363", "0xFFFF6363"));
        int returnResources = testAI.getHeldTerritoryResources(territoryArrayList);

        assertTrue(returnResources==0);
    }

    //Christopher Jennings 40200418
    //
    @Test
    public void testPlaceTurnResources(){
        boolean moreThanOne = false;
        for (int i =0; i < gm.getRegions().size(); i++){
            testAI.placeResourcesInEmptyRegions(gm.getRegions());
        }
        testAI.placeTurnResources(gm.getTerritories(), gm.getRegions());

        for (int i =0; i < gm.getRegions().size(); i++){
            if (gm.getRegions().get(i).getNumResources() > 1){
                moreThanOne = true;
            }
        }
        assertTrue(moreThanOne);
    }

    //40174176 - James Sims
    //Test to check that when the game is loaded up, all regions are empty
    @Test
    public void testCheckForEmptyRegions(){
        for(int i = 0; i < 10; i++){
            testAI.placeResourcesInEmptyRegions(gameRegionArrayList);
        }
        for(int i = 0; i < 10; i++){
            testAI2.placeResourcesInEmptyRegions(gameRegionArrayList);
        }
        assertTrue(AI.checkForEmptyRegions(gameRegionArrayList));
    }

    //40174176 - James Sims
    //Test that an AI cannot place resources in a region held by another Player
    @Test
    public void testAICantPlaceInAnother(){
        for(int i = 0; i < gm.getRegions().size(); i++){
            testAI.placeResourcesInEmptyRegions(gm.getRegions());
        }
        testAI2.placeRemainingResources(gm.getRegions(), gm.getTerritories(), gm);
        assertTrue(testAI2.regionsHeld.size() == 0);
    }
}
