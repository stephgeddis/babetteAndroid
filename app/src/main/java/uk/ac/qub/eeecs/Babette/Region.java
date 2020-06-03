package uk.ac.qub.eeecs.Babette;

import java.util.ArrayList;

/**
 *
 *Region class created to store information about regions
 * on the map,including the colour of the region in hex,
 * x and y co ordinate to show number of resources in a region,
 * number of resources in a region and an arraylist of
 * regions that can be attacked from a region
 *
 * @author  Stephen Irons 40204625
 * @version 6.0
 *
 */

public class Region {
    String regionName, hexCode;
    int xCoOrdinate, yCoOrdinate, numResources;
    ArrayList<String> adjacentRegions;
    Counter regionCounter;

    public Region(String regionName, String hexCode, int xCoOrdinate, int yCoOrdinate, int numResources, ArrayList<String> adjacentRegions) {
        this.regionName = regionName;
        this.hexCode = hexCode;
        this.xCoOrdinate = xCoOrdinate;
        this.yCoOrdinate = yCoOrdinate;
        this.numResources = numResources;
        this.adjacentRegions = adjacentRegions;
    }

    public String getRegionName(){ return regionName; }

    public void setRegionName(String regionName){ this.regionName=regionName; }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public int getxCoOrdinate() {
        return xCoOrdinate;
    }

    public void setxCoOrdinate(int xCoOrdinate) {
        this.xCoOrdinate = xCoOrdinate;
    }

    public int getyCoOrdinate() {
        return yCoOrdinate;
    }

    public void setyCoOrdinate(int yCoOrdinate) {
        this.yCoOrdinate = yCoOrdinate;
    }

    public int getNumResources() {
        return numResources;
    }

    public void setNumResources(int numResources) {
        this.numResources = numResources;
    }

    public ArrayList<String> getAjacentRegions() {
        return adjacentRegions;
    }

    public void setAjacentRegions(ArrayList<String> ajacentRegions) {
        this.adjacentRegions = ajacentRegions;
    }

    public void setRegionCounter(Counter counter){
        this.regionCounter = counter;
    }

    public Counter getRegionCounter()
    {
        return regionCounter;
    }
}



