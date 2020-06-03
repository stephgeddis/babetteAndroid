package uk.ac.qub.eeecs.Babette;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;

public interface Behaviours {
    void callForMerger(ArrayList<Region> gameRegionList, ArrayList<Player> playerArrayList, ArrayList<Territory> territoryArrayList, Game game, GameManager gm);
    void placeTurnResources(ArrayList<Territory> territoriesList, ArrayList<Region> gameRegionList);
}
