package uk.ac.qub.eeecs.Babette;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;

/**
 *
 * This class is used to create AI functionality
 * to enable AI to play on the Babette Game Screen
 *
 * @author  Stephen Irons 40204625, James Sims 40174176
 * @version 6.0
 *
 */

public class AI extends Player implements Behaviours {

    public AI(String name, String col, String ava) {
        super(name, col, ava);
    }

    //40174176 - James Sims
    //Called to check if any regions are still empty
    public static boolean checkForEmptyRegions(ArrayList<Region> gameRegionList){
        for (int i = 0; i<gameRegionList.size();i++){
            if(gameRegionList.get(i).getNumResources()==0){
                return true;
            }
        }
        return false;
    }

    //40174176 James Sims & 40204625 Stephen Irons pair programming
    //When there is one or more regions with no resources in them this method will be called
    public void placeResourcesInEmptyRegions(ArrayList<Region> gameRegionList){
        ArrayList<String> emptyRegions = new ArrayList<>(0);
        for(Region region: gameRegionList){
            if(region.getNumResources()==0){
                emptyRegions.add(region.getHexCode());
            }
        }
        int randomNumber = GameManager.generateNumber(0, emptyRegions.size()-1);

        String hexCode = emptyRegions.get(randomNumber);
        for(Region region: gameRegionList){
            if(region.getHexCode()==hexCode){
                region.setNumResources(region.getNumResources()+1);
                this.regionsHeld.add(hexCode);
            }
        }
    }

    //40174176 James Sims & 40204625 Stephen Irons pair programming
    //when all regions have at least 1 resource in them this method will called to place remaining resources
    //40174176 James Sims later updated to make AI's selection smarter
    public void placeRemainingResources(ArrayList<Region> gameRegionList, ArrayList<Territory> gameTerritoryList, GameManager gameManager){
        ArrayList<Region> playerRegions = new ArrayList<Region>(0);

        for(String playerRegion: this.regionsHeld){
            for(Region gameRegion: gameRegionList){
                if(playerRegion == gameRegion.regionName){      //changed from .getHexCode
                    playerRegions.add(gameRegion);
                }
            }
        }

        ArrayList<String> playerRegionHexCodes = this.regionsHeld;
        int randomRegionPosition = gameManager.generateNumber(0,playerRegionHexCodes.size()-1);
        String hexCode = getRandomRegionHexCode(randomRegionPosition,playerRegionHexCodes);

        for(Region region: gameRegionList){
            if(region.getHexCode()==hexCode){
                int numResources = region.getNumResources()+1;
                region.setNumResources(numResources);
            }
        }

        //James Sims 40174176 - added code to get best region to add resources to instead of keeping it random.
        int [] [] territoryStatistics = getPlayerTerritoryStatistics(gameTerritoryList, gameRegionList);
        int bestTerritory = getBestTerritoryToAttackFrom(territoryStatistics, gameTerritoryList, gameRegionList);
        ArrayList<String> regionsInBestTerritory = new ArrayList<String>();

        for(int i = 0; i < gameTerritoryList.size(); i++){
            if(gameTerritoryList.indexOf(i) == bestTerritory){                  //check if the for loop ever enters
                regionsInBestTerritory = gameTerritoryList.get(i).getRegions();
            }
        }
        String hexCode2 = "";
        for(Region region: playerRegions) {
            for (String regionName : regionsInBestTerritory) {
                if (region.getRegionName() == regionName){
                    hexCode2 = region.getHexCode();
                }
            }
        }
        //int randomNumber = gameManager.generateNumber(0, playerRegions.size()-1);
        //String hexCode = playerRegions.get(randomNumber).getHexCode();
        for(Region region: gameRegionList){
            if(region.getHexCode()==hexCode2){
                int numResources = region.getNumResources()+1;
                region.setNumResources(numResources);
            }
        }
    }

    //Stephen Irons 40204625
    //Calling all the methods required for an AI call for merger
    public void callForMerger(ArrayList<Region> gameRegionList, ArrayList<Player> playerArrayList, ArrayList<Territory> territoryArrayList, Game game, GameManager gm){
            if (possibleAttack(gameRegionList)) {
                int[][] playerTerritoryStatistics = getPlayerTerritoryStatistics(territoryArrayList, gameRegionList);

                int territoryToAttackFrom = getBestTerritoryToAttackFrom(playerTerritoryStatistics, territoryArrayList, gameRegionList);

                ArrayList<String> validAttackingRegionHexCodes = getRegionsToAttackFrom(territoryToAttackFrom, territoryArrayList, gameRegionList);

                String attackingRegionHexCode = getRandomRegionHexCode(GameManager.generateNumber(0, validAttackingRegionHexCodes.size() - 1), validAttackingRegionHexCodes);

                ArrayList<String> defendingRegionHexCodes = getAttackableRegions(gameRegionList, attackingRegionHexCode);

                performAttack(defendingRegionHexCodes, gameRegionList, playerArrayList, attackingRegionHexCode, game, gm);
            }else{
                return;
            }
    }

    //Stephen Irons 40204625
    //Carries out the attack for an AI
    public void performAttack(ArrayList<String> defendingRegionHexCodes, ArrayList<Region> gameRegionList, ArrayList<Player> playerArrayList, String attackingRegionHexCode, Game game, GameManager gm){
    String defendingRegionHexCode = getRandomRegionHexCode(GameManager.generateNumber(0, defendingRegionHexCodes.size() - 1), defendingRegionHexCodes);
    String losingRegionHexCode = callForMerger(attackingRegionHexCode, defendingRegionHexCode, gameRegionList);
    decreaseRegionResources(losingRegionHexCode, gameRegionList);

    for (int i = 0; i < gameRegionList.size(); i++) {
        if (gameRegionList.get(i).getHexCode() == defendingRegionHexCode && gameRegionList.get(i).getNumResources() <=0) {
            changeRegionOwner(attackingRegionHexCode, defendingRegionHexCode, gameRegionList, playerArrayList);
            // Christopher Jennings 40200418
            //Possible expansion to have babette cards work with the AI
            //this.cards.add(BabetteCard.giveUserTailoredCard(cards, game, this.getPlayerTerritories(gm)));
        }
    }

}

    //Stephen Irons 40204625
    //Generate statistics, number of regions held and percentage of resources owned, for each territory for the AI
    public int[][] getPlayerTerritoryStatistics(ArrayList<Territory> territoryArrayList, ArrayList<Region> gameRegionArrayList){
        int[][] playerTerritoryStatistics = new int[territoryArrayList.size()][3];
        int territoryIndex = 0;
            for(Territory territory: territoryArrayList){
                int numRegions = 0;
                int numPlayerResourcesInTerritory = 0;
                int totalResourcesInTerritory = 0;

                for(String regionInTerritoryHexCode: territory.getRegions()){
                    for(Region regionInRegionArrayList: gameRegionArrayList){
                        if(regionInRegionArrayList.getHexCode()==regionInTerritoryHexCode){
                            totalResourcesInTerritory+=regionInRegionArrayList.getNumResources();
                            for(String regionHexCodeInPlayerArrayList: this.regionsHeld){
                                if(regionHexCodeInPlayerArrayList==regionInRegionArrayList.getHexCode()){
                                    numPlayerResourcesInTerritory+= regionInRegionArrayList.getNumResources();
                                    numRegions++;
                                }
                            }
                        }
                    }
                }

                playerTerritoryStatistics[territoryIndex][0] = numRegions;
                try {
                    playerTerritoryStatistics[territoryIndex][1] = numPlayerResourcesInTerritory / totalResourcesInTerritory * 100; //percentageOfRegionHeld
                }catch(Exception e){
                    e.printStackTrace();
                    playerTerritoryStatistics[territoryIndex][1] = 0;
                }
                territoryIndex++;
            }
        return playerTerritoryStatistics;
    }

    //Stephen Irons 40204625
    //Getting the index of the best territory to attack for the AI based on the player territory statistics 2D array
    public int getBestTerritoryToAttackFrom(int[][] playerTerritoryStatistics, ArrayList<Territory> territoryArrayList, ArrayList<Region> gameRegionArrayList){
        int territoryPosition = 0;
        int resourcesInTerritory = 0;

        //choosing a territory where the AI has the most resources in it that isnt 100% controlled by that AI
        for(int i = 0; i < playerTerritoryStatistics.length; i++){
            if(playerTerritoryStatistics[i][1]>resourcesInTerritory && playerTerritoryStatistics[i][1]!=100){
                ArrayList<String> playerRegionsInTerritory = new ArrayList<>();
                for(String territoryHexCode: territoryArrayList.get(i).getRegions()){
                    if(this.regionsHeld.contains(territoryHexCode)){
                        playerRegionsInTerritory.add(territoryHexCode);
                    }
                }
                for(String playerHexCodeInRegion: playerRegionsInTerritory){
                    for(Region region: gameRegionArrayList){
                        if(region.getHexCode()==playerHexCodeInRegion && getAttackableRegions(gameRegionArrayList,playerHexCodeInRegion).size()>1){
                            territoryPosition=i;
                            resourcesInTerritory = playerTerritoryStatistics[i][1];
                        }
                    }
                }
            }
        }

        String region ="";

        //if a territory has not been chosen to attack at this point, choose the first territory where an attack is possible
        //this may happen if an AI only controls territories where they own 100% of the resources in them
        if(resourcesInTerritory==0) {
            outerloop:
            for (String regionHexCode : this.regionsHeld) {
                for(Region region1 : gameRegionArrayList) {
                    if(region1.getHexCode()==regionHexCode&&region1.getNumResources()>1) {
                        if (getAttackableRegions(gameRegionArrayList, regionHexCode).size() > 0) {
                            region = regionHexCode;
                            break outerloop;
                        }
                    }
                }
            }

            for (int i = 0; i < territoryArrayList.size(); i++) {
                for (String territoryRegion : territoryArrayList.get(i).getRegions()) {
                    if (territoryRegion == region) {
                        territoryPosition = i;
                        break;
                    }
                }
            }
        }
        return  territoryPosition;
    }

    //Stephen Irons 40204625
    //Getting an array list of valid regions the AI can attack from based on the best Territory to attack
    public ArrayList<String> getRegionsToAttackFrom(int territoryPosition, ArrayList<Territory> territoryArrayList,ArrayList<Region> gameRegionList){
        ArrayList<String> territoryRegionArrayList = new ArrayList<>();
        ArrayList<String> regionsToAttackFrom = new ArrayList<>();

        //finding the territory in the territory array list and getting the regions within it
        for(int i = 0; i<territoryArrayList.size();i++){
            if(i==territoryPosition){
                territoryRegionArrayList = territoryArrayList.get(i).getRegions();
                break;
            }
        }

        //getting an array list of valid regions to attack from within the region
        for(String region:territoryRegionArrayList) {
            for(Region regionInRegionList:gameRegionList) {
                if(region==regionInRegionList.getHexCode() && regionInRegionList.getNumResources()>1 && this.regionsHeld.contains(region)&& getAttackableRegions(gameRegionList, region).size()>0){
                    regionsToAttackFrom.add(region);
                }
            }
        }
        return regionsToAttackFrom;
    }

    //Stephen Irons 40204625
    //returning valid regions an AI can attack
    public ArrayList<String> getAttackableRegions(ArrayList<Region> gameRegionArrayList, String attackingRegionHexCode){
        //getting the regions hex codes surrounding the attacking region
        ArrayList<String> surroundingHexCodes = new ArrayList<>();

        for(Region region: gameRegionArrayList){
            if(region.getHexCode()==attackingRegionHexCode){
                surroundingHexCodes=region.getAjacentRegions();
                break;
            }
        }

        //getting valid regions hex codes the AI can attack
        ArrayList<String> attackableRegionHexCodes = new ArrayList<>();
        for(String surroundingRegionHexCode: surroundingHexCodes){
            boolean regionOwned = false;
            for(String playerRegionHexCode: this.regionsHeld){
                if(surroundingRegionHexCode==playerRegionHexCode){
                    regionOwned=true;
                    break;
                }
            }
            if(!regionOwned){
                attackableRegionHexCodes.add(surroundingRegionHexCode);
            }
        }
        return attackableRegionHexCodes;
    }

    //Stephen Irons 40204625
    //determining whether an AI can attack this turn or not
    public boolean possibleAttack(ArrayList<Region> gameRegionList){
        for(String playerRegionHexCode: this.regionsHeld){
            for(Region region: gameRegionList){
                if(playerRegionHexCode==region.getHexCode()&& region.getNumResources()>1 && getAttackableRegions(gameRegionList,region.getHexCode()).size()>0){
                    return true;
                }
            }
        }
        return false;
    }

    //Jake Smyth 40204021
    //Method to find two connected regions that can be passed into the fortify method.
    public void selectFortifyTerritories(ArrayList<Region> gameRegionList) {
        String randomRegionToFortifyFrom;
        ArrayList<String> hexCodesForAIRegions = new ArrayList<>(this.regionsHeld);

        boolean fortifyPossible = checkFortifyPossible(hexCodesForAIRegions, gameRegionList);

        if(fortifyPossible) {
            ArrayList<String> regionsWithNeighbours = findAIRegionsWithNeighbours(hexCodesForAIRegions, gameRegionList);
            ArrayList<String> adjacentOwnedRegions;
            if (regionsWithNeighbours.size() > 0) {
                randomRegionToFortifyFrom = getRandomAIRegionToFortifyFrom(regionsWithNeighbours, gameRegionList);
                adjacentOwnedRegions = getRegionOneListOfAdjacentOwnedRegions(randomRegionToFortifyFrom, hexCodesForAIRegions, gameRegionList);

                if (adjacentOwnedRegions.size() > 0) {
                    int randomAdjacentRegionNumber = GameManager.generateNumber(0, adjacentOwnedRegions.size() - 1);
                    fortify(randomRegionToFortifyFrom, adjacentOwnedRegions.get(randomAdjacentRegionNumber), gameRegionList);
                }
            }
        }
    }

    //Jake Smyth 40204021
    // Method to check if the AI is able to fortify on this turn.
    public boolean checkFortifyPossible(ArrayList<String> aiRegions, ArrayList<Region> gameRegionList) {
        boolean fortifyPossible = false;
        int fortifyCounter = 0;
        for (Region region : gameRegionList) {
            String regionHex = region.getHexCode();
            for (String r : aiRegions)
                if (r == regionHex && region.getNumResources() > 1)
                    fortifyCounter++;
        }
        if (fortifyCounter > 1)
            fortifyPossible = true;

        return fortifyPossible;
    }

    //Jake Smyth 40204021
    //Finds regions owned by the AI that has at least one of it's own regions directly beside it.
    public ArrayList<String> findAIRegionsWithNeighbours(ArrayList<String> aiRegionHexCodes, ArrayList<Region> gameRegionList) {
            //Empty array to hold AI regions which has another of its own regions beside it
            ArrayList<String> regionsWithNeighbours = new ArrayList<String>();

            for (int i = 0; i < aiRegionHexCodes.size() - 1; i++) {
                //Getting the hex codes for the surrounding regions of hexCodesForAIRegions(i)
                ArrayList<String> adjacentRegionHexCodes = new ArrayList<>();
                for (int j = 0; j < gameRegionList.size(); j++)
                    if (gameRegionList.get(j).hexCode == aiRegionHexCodes.get(i))
                        adjacentRegionHexCodes = gameRegionList.get(i).getAjacentRegions();

                //Getting all the hex codes for surrounding regions that are controlled by AI
                for (int k = 0; k < adjacentRegionHexCodes.size(); k++)
                    for (int x = 0; x < this.regionsHeld.size(); x++)
                        if (adjacentRegionHexCodes.get(k) == this.regionsHeld.get(x)) {
                            regionsWithNeighbours.add(aiRegionHexCodes.get(i));
                            break;
                        }
            }
            return regionsWithNeighbours;
        }

    //Jake Smyth 40204021
    //Returns a random AI hex code to later be used as region one for fortify.
    public String getRandomAIRegionToFortifyFrom(ArrayList<String> regionsWithNeighbours, ArrayList<Region> gameRegionList) {
            String randomRegionToFortifyFrom = "";
            boolean validFirstRegion = false;
            //Will loop until an AI region with at least 2 resources is found.
            while(!validFirstRegion) {
                int randomRegionNumber = GameManager.generateNumber(0, regionsWithNeighbours.size() - 1);
                randomRegionToFortifyFrom = regionsWithNeighbours.get(randomRegionNumber);
                for(int i = 0; i < gameRegionList.size(); i++) {
                    if(gameRegionList.get(i).getHexCode() == randomRegionToFortifyFrom && gameRegionList.get(i).getNumResources() > 0)
                        validFirstRegion = true;
                }
            }
            return randomRegionToFortifyFrom;
        }

    //Finds a list of adjacent regions for the AI's region one that is owned by the AI.
    //Jake Smyth 40204021
    public ArrayList<String> getRegionOneListOfAdjacentOwnedRegions(String regionOne, ArrayList<String> aiHexCodes, ArrayList<Region> gameRegionList) {
            ArrayList<String> adjacentOwnedRegions = new ArrayList<>();
            int numberOfAdjacentRegions;
            //Loops through game list.
            for (int j = 0; j < gameRegionList.size(); j++) {
                //If region one is found
                if (gameRegionList.get(j).hexCode == regionOne && gameRegionList.get(j).numResources >= 1) {
                    numberOfAdjacentRegions = gameRegionList.get(j).adjacentRegions.size();

                    //Loops through region one's adjacent regions
                    for (int y = 0; y < numberOfAdjacentRegions - 1; y++)
                        //Loops through AI's owned regions
                        for (int z = 0; z < aiHexCodes.size(); z++)
                            //Adds hex codes of AI's owned adjacent regions to array.
                            if (gameRegionList.get(j).getAjacentRegions().get(y) == aiHexCodes.get(z) && aiHexCodes.get(z) != regionOne)
                                adjacentOwnedRegions.add(gameRegionList.get(j).getAjacentRegions().get(y));
                }
            }
            return adjacentOwnedRegions;
        }

    //Jake Smyth 40204021
    //Transfer resources from one region to another.
    public void fortify(String regionFrom, String regionTo, ArrayList<Region> gameRegionList){
        int fortifyValue = 0;
        for(int i = 0; i< gameRegionList.size();i++)
            if (gameRegionList.get(i).hexCode == regionFrom) {
                fortifyValue = GameManager.generateNumber(1, gameRegionList.get(i).numResources - 1);
                gameRegionList.get(i).setNumResources(gameRegionList.get(i).getNumResources() - fortifyValue);
                break;
            }
        for(int j = 0; j< gameRegionList.size();j++)
            if (gameRegionList.get(j).hexCode == regionTo) {
                gameRegionList.get(j).setNumResources(gameRegionList.get(j).getNumResources() + fortifyValue);
                break;
            }
    }

    //Christopher Jennings 40200418
    public void placeTurnResources(ArrayList<Territory> territoriesList, ArrayList<Region> gameRegionList){

        int numOfResources = getHeldRegionResources();

        numOfResources+=getHeldTerritoryResources(territoriesList);

        //int numRegions = this.regionsHeld.size();
        ArrayList<String> playerRegionHexCodes = new ArrayList<String>(this.regionsHeld);

        //40174176 James Sims
        //Placing resources changed from random to weakest region
        ArrayList<Region> playerRegions = new ArrayList<>();
        for(Region region: gameRegionList) {
            String checkedHex = region.getHexCode();
            for(String regionHex: playerRegionHexCodes){
                if(checkedHex == regionHex){
                    playerRegions.add(region);
                }
            }
        }
        while(numOfResources>0) {
            Region regionToPlace = playerRegions.get(0);
            for(Region region: playerRegions){
                if(region.getNumResources() < regionToPlace.getNumResources()){
                    regionToPlace = region;
                }
            }

            int placeThisTurn = GameManager.generateNumber(1,numOfResources);
            for(int k = 0; k<gameRegionList.size();k++){
                if(gameRegionList.get(k).hexCode==regionToPlace.getHexCode()){
                    gameRegionList.get(k).setNumResources(gameRegionList.get(k).numResources+=placeThisTurn);
                    numOfResources-=placeThisTurn;
                }
            }
        }
    }

    //Christopher Jennings 40200418
    //Methods returns held regions up to placeTurnResources
    public int getHeldRegionResources(){
        int numOfResources = this.regionsHeld.size();
        if (numOfResources >10) {
            numOfResources = numOfResources/2;
        }
        if(numOfResources==0){
         numOfResources=1;
        }
        return numOfResources;
    }

    //Christopher Jennings 40200418
    //Methods returns held resources up to placeTurnResources
    public int getHeldTerritoryResources(ArrayList<Territory> territoriesList){
        int numOfResources =0;
        //for each territory
        for(Territory territory: territoriesList){
            if(this.regionsHeld.containsAll(territory.getRegions())){
                numOfResources+=territory.getNumResources();
            }
        }

        return  numOfResources;
    }

    //Stephen Irons 40204625
    //Return a random region hexcode from an array list
    public String getRandomRegionHexCode(int regionNumber, ArrayList<String> hexCodeList){
      String regionHexCode = "";
      if(!(regionNumber<0 && regionNumber>this.regionsHeld.size())) {
          for (int i = 0; i < hexCodeList.size(); i++) {
              if (i == regionNumber) {
                  regionHexCode = hexCodeList.get(i);
                  break;
              }
          }
      }
      return regionHexCode;
    }

    public void placeInitialResources(ArrayList<Region> gameRegionList,String hexCode){
        for (int i = 0; i < gameRegionList.size(); i++) {
            if (gameRegionList.get(i).hexCode == hexCode) {
                gameRegionList.get(i).setNumResources(gameRegionList.get(i).getNumResources() + 1);
                addRegion(hexCode);
                break;
            }
        }
    }

    public void fortify(){}
    public void selectFortifyTerritories(){}
}