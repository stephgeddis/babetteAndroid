package uk.ac.qub.eeecs.gage;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import uk.ac.qub.eeecs.Babette.Region;

public class RegionUnitTest {
    //Stephen Irons 40204625
    //Check that the constructor for a region works properly
    @Test
    public void testRegionConstructor(){
        ArrayList<Region> RegionsList = new ArrayList<Region>();
        ArrayList<String> AntiVirusAdjRegions = new ArrayList<>(Arrays.asList("0xFFfa6363", "0xFFfa6363", "0xFFACDDFF"));
        Region Antivirus = new Region("Anti-Virus", "0xFFFF6363", 122, 367, 0, AntiVirusAdjRegions);

        assertTrue(Antivirus instanceof Region);
    }

    //Stephen Irons 40204625
    //Check that the getAdjacentRegions method returns the correct values
    @Test
    public void testGetAjacentRegions(){
        ArrayList<Region> RegionsList = new ArrayList<Region>();
        ArrayList<String> AntiVirusAdjRegions = new ArrayList<>(Arrays.asList("0xFFfa6363", "0xFFfa6363", "0xFFACDDFF"));
        Region Antivirus = new Region("Anti-Virus", "0xFFFF6363", 122, 367, 0, AntiVirusAdjRegions);

        ArrayList<String> returnedRegions = new ArrayList<>();
        returnedRegions = Antivirus.getAjacentRegions();

        boolean containsRegions = true;
        for (String regionHexCode: AntiVirusAdjRegions) {
            boolean regionFound = false;
            for (String returnedRegionsHexcode:returnedRegions) {
                if(regionHexCode.contains(returnedRegionsHexcode)){
                    regionFound = true;
                }
            }
            if(!regionFound){
                containsRegions = false;
            }
        }
        assertTrue(containsRegions);
    }

}
