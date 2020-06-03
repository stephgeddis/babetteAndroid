package uk.ac.qub.eeecs.Babette;

import java.util.ArrayList;

//Christopher Jennings 40200418
//Class to hold information about territories within the map

public class Territory {

    String territoryName;
    ArrayList<String> regions;
    int numResources;

    //Territory constructor
    public Territory(String territoryName, ArrayList<String> regions, int numResources) {
        this.territoryName = territoryName;
        this.regions = regions;
        this.numResources = numResources;
    }
    public String getTerritoryName() { return territoryName; }

    public void setTerritoryName(String territoryName) { this.territoryName = territoryName; }

    public ArrayList<String> getRegions() { return regions; }

    public void setRegions(ArrayList<String> regions) { this.regions = regions; }

    public int getNumResources() { return numResources; }

    public void setNumResources(int numResources) { this.numResources = numResources; }

}
