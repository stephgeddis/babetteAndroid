package uk.ac.qub.eeecs.Babette;
import java.util.ArrayList;

/**
 *
 * This class is used to define methods for
 * Ai and user
 *
 * @author  Stephen Irons 40204625
 * @version 6.0
 *
 */

public abstract class Player { // Create by Adam Higgins 40212255

    // Member variables
    private String username, colour, avatar;
    public ArrayList<String> regionsHeld; //Stephen irons changed this to public for testing
    public boolean initialResourcesPlaced;

    public ArrayList<BabetteCard> cards;


    // Default constructor protected as it is solely to be used by the subclass
    protected Player() { }

    // Constructor
    public Player(String name, String col, String ava)
    {
        username = name;
        colour = col;
        avatar = ava;
        regionsHeld = new ArrayList<>(); // Adam Higgins 40212255 fixed bug with null pointer exception for regions held
        initialResourcesPlaced = false;
        cards = new ArrayList<>();
    }

    // Getters and setters
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String name)
    {
        username = name;
    }

    public String getColour()
    {
        return colour;
    }

    public void setColour(String col)
    {
        colour = col;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String ava)
    {
        avatar = ava;
    }

    public void addRegion(String hexCode)
    {
        regionsHeld.add(hexCode);
    }

    // Methods
    //Stephen Irons 40204625
    //Abstract methods required in both AI and User Player
    abstract void placeInitialResources(ArrayList<Region> gameRegionList,String hexCode);

    //Stephen Irons 40204625 and Adam Higgins 40212255
    //callForMerger method to determine a winner from a call for merger action
    public String callForMerger(String attackerHexCode, String defenderHexCode, ArrayList<Region> regions){

        int min = 0;
        int max = 6;
        int attackerDiceRoll = GameManager.generateNumber(min,max);
        int defenderDiceRoll = GameManager.generateNumber(min,max);
        int attackerResources = 0, defenderResources = 0;

        if(attackerDiceRoll>defenderDiceRoll)
            return defenderHexCode;

        else if(defenderDiceRoll>attackerDiceRoll)
            return attackerHexCode;

        // Adam Higgins
        // If there is a draw, the region resources are compared
        // If the region resources are equal the attacker is favoured
        else {

            for (int i = 0; i < regions.size(); i++) {
                if (regions.get(i).getHexCode().equals(attackerHexCode))
                    attackerResources = regions.get(i).getNumResources();
                else if (regions.get(i).getHexCode().equals(defenderHexCode))
                    defenderResources = regions.get(i).getNumResources();
            }

            if (attackerResources >= defenderResources)
                return defenderHexCode;
            else
                return attackerHexCode;
        }
    }

    //Stephen Irons 40204625
    //Updating the region resources for the losing region of a call for merger
    public void decreaseRegionResources(String losingHexCode, ArrayList<Region> gameRegionList){
        for(int i = 0; i< gameRegionList.size();i++){
            if(gameRegionList.get(i).hexCode == losingHexCode && gameRegionList.get(i).numResources!=0){
                gameRegionList.get(i).setNumResources(gameRegionList.get(i).getNumResources()-1);
            }
        }
    }

    //Stephen Irons 40204625
    //Changing the owner of a region if a region has been taken over
    public void changeRegionOwner(String attackingRegionHexCode, String defendingRegionHexCode, ArrayList<Region> gameRegionList, ArrayList<Player> playerArrayList){
        for(Player player: playerArrayList){
            for(int i = 0; i<player.regionsHeld.size();i++){
                if(player.regionsHeld.get(i) == defendingRegionHexCode){
                    player.regionsHeld.remove(i);
                }
            }
        }
        this.regionsHeld.add(defendingRegionHexCode);

        int numResources = 0;

        for(int i = 0;i<gameRegionList.size();i++){
            if(gameRegionList.get(i).getHexCode()==attackingRegionHexCode){
                numResources = gameRegionList.get(i).getNumResources();
            }
        }

        int resourcesMoved = numResources/2;

        for(int i = 0;i<gameRegionList.size();i++){
            if(gameRegionList.get(i).getHexCode()==attackingRegionHexCode){
                int newNumResources = gameRegionList.get(i).numResources;
                newNumResources = newNumResources - resourcesMoved;
                gameRegionList.get(i).setNumResources(newNumResources);
            }
            if(gameRegionList.get(i).getHexCode()==defendingRegionHexCode){
                gameRegionList.get(i).setNumResources(resourcesMoved);
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

    //Christopher Jennings 40200418
    public ArrayList<Territory> getPlayerTerritories(GameManager gm) {

        ArrayList<Territory> terr = new ArrayList<>();

        for (int i =0; i< gm.getTerritories().size(); i++){
            if (regionsHeld.containsAll(gm.getTerritories().get(i).getRegions()))
                terr.add(gm.getTerritories().get(i));
        }

        return terr;
    }
}
