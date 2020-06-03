package uk.ac.qub.eeecs.gage;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import uk.ac.qub.eeecs.Babette.BabetteCard;

public class BabetteCardUnitTest {
    BabetteCard bCard1 = new BabetteCard("CyberSecurity", null);
    BabetteCard bCard2 = new BabetteCard("CyberSecurity", null);
    BabetteCard bCard3 = new BabetteCard("CyberSecurity", null);
    BabetteCard bCard4 = new BabetteCard("Development", null);
    BabetteCard bCard5 = new BabetteCard("Development", null);
    BabetteCard bCard6 = new BabetteCard("Programming", null);
    BabetteCard bCard7 = new BabetteCard("WildCard", null);

    // Test to check no cards are removed for a non-matching BabetteCard ArrayList.
    // Jake Smyth 40204021
    @Test
    public void testNoCardsRemovedFromList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        ArrayList<BabetteCard> newCardList;
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard4);
        bCardList.add(bCard5);
        newCardList = BabetteCard.tradeCardsForResources(bCardList);
        assertTrue(newCardList.size() == bCardList.size());
    }

    // Test to check 3 cards are removed for an ArrayList containing 3 identical BabetteCards.
    // Jake Smyth 40204021
    @Test
    public void testThreeIdenticalCardsRemovedFromList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        ArrayList<BabetteCard> newCardList;
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard3);
        bCardList.add(bCard4);
        newCardList = BabetteCard.tradeCardsForResources(bCardList);
        assertTrue(newCardList.size() == 1);
    }

    // Test to check 3 cards are removed for an ArrayList containing 2 identical BabetteCards and a WildCard.
    // Jake Smyth 40204021
    @Test
    public void testTwoIdenticalCardsWithWildCardRemovedFromList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        ArrayList<BabetteCard> newCardList;
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard5);
        bCardList.add(bCard7);
        newCardList = BabetteCard.tradeCardsForResources(bCardList);
        assertTrue(newCardList.size()==1);
    }

    // Test to check 3 cards are removed for an ArrayList containing 3 identical BabetteCards.
    // Jake Smyth 40204021
    @Test
    public void testThreeIndividualCardsRemovedFromList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        ArrayList<BabetteCard> newCardList;
        bCardList.add(bCard1);
        bCardList.add(bCard4);
        bCardList.add(bCard6);
        bCardList.add(bCard7);
        newCardList = BabetteCard.tradeCardsForResources(bCardList);
        assertTrue(newCardList.size() == 1);
    }

    // Test to check 3 cards in an ArrayList have an identical territory.
    // Jake Smyth 40204021
    @Test
    public void testCheckThreeIdenticalCardsAreInList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard3);
        bCardList.add(bCard4);
        String territory = "CyberSecurity";
        assertTrue(BabetteCard.checkForThreeIdenticalCards(bCardList, territory));
    }

    // Test to check 3 cards in an ArrayList do not have an identical territory.
    // Jake Smyth 40204021
    @Test
    public void testCheckThreeIdenticalCardsAreNotInList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard4);
        bCardList.add(bCard7);
        String territory = "CyberSecurity";
        assertFalse(BabetteCard.checkForThreeIdenticalCards(bCardList, territory));
    }

    // Test to check 2 cards in an ArrayList have an identical territory, and a WildCard is present.
    // Jake Smyth 40204021
    @Test
    public void testCheckTwoIdenticalCardsWithWildCardAreInList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard4);
        bCardList.add(bCard7);
        String territory = "CyberSecurity";
        assertTrue(BabetteCard.checkForTwoIdenticalCardsAndWildCard(bCardList, territory));
    }

    // Test to check 2 cards in an ArrayList have an identical territory, and a WildCard is present.
    // Jake Smyth 40204021
    @Test
    public void testCheckTwoIdenticalCardsWithWildCardAreNotInList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard3);
        bCardList.add(bCard4);
        String territory = "CyberSecurity";
        assertFalse(BabetteCard.checkForTwoIdenticalCardsAndWildCard(bCardList, territory));
    }

    // Test to check 3 cards are removed for an ArrayList containing 3 identical BabetteCards.
    // Jake Smyth 40204021
    @Test
    public void testCheckThreeIndividualCardsAreInList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        bCardList.add(bCard1);
        bCardList.add(bCard4);
        bCardList.add(bCard6);
        bCardList.add(bCard7);
        ArrayList<String> territoryList = new ArrayList<>();
        territoryList.add("CyberSecurity");
        territoryList.add("Development");
        territoryList.add("Hardware");
        territoryList.add("Networking");
        territoryList.add("Programming");
        assertTrue(BabetteCard.checkForThreeIndividualCards(bCardList, territoryList));
    }

    // Test to check 3 cards are removed for an ArrayList containing 3 identical BabetteCards.
    // Jake Smyth 40204021
    @Test
    public void testCheckThreeIndividualCardsAreNotInList(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard3);
        bCardList.add(bCard4);
        ArrayList<String> territoryList = new ArrayList<>();
        territoryList.add("CyberSecurity");
        territoryList.add("Development");
        territoryList.add("Hardware");
        territoryList.add("Networking");
        territoryList.add("Programming");
        assertFalse(BabetteCard.checkForThreeIndividualCards(bCardList, territoryList));
    }

    // Test to return an ArrayList containing 3 identical BabetteCards.
    // Jake Smyth 40204021
    @Test
    public void testFindThreeIdenticalCardsToDelete(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        ArrayList<BabetteCard> newCardList;
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard3);
        bCardList.add(bCard4);
        String territory = "CyberSecurity";
        newCardList = BabetteCard.threeIdenticalCardsToDelete(bCardList, territory);
        assertTrue(newCardList.size() == 3);
    }

    // Test to return an ArrayList containing 2 identical BabetteCards and a WildCard.
    // Jake Smyth 40204021
    @Test
    public void testFindTwoIdenticalCardsWithWildCardToDelete(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        ArrayList<BabetteCard> newCardList;
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard5);
        bCardList.add(bCard7);
        String territory = "CyberSecurity";
        newCardList = BabetteCard.twoIdenticalCardsAndWildCardToDelete(bCardList, territory);
        assertTrue(newCardList.size() == 3);
    }

    // Test to check 3 cards are removed for an ArrayList containing 3 identical BabetteCards.
    // Jake Smyth 40204021
    @Test
    public void testFindThreeIndividualCardsToDelete(){
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        ArrayList<BabetteCard> newCardList;
        bCardList.add(bCard1);
        bCardList.add(bCard4);
        bCardList.add(bCard5);
        bCardList.add(bCard7);
        ArrayList<String> territoryList = new ArrayList<>();
        territoryList.add("CyberSecurity");
        territoryList.add("Development");
        territoryList.add("Hardware");
        territoryList.add("Networking");
        territoryList.add("Programming");
        newCardList = BabetteCard.threeIndividualCardsToDelete(bCardList, territoryList);
        assertTrue(newCardList.size() == 3);
    }

    // Tests to ensure correct user resources are allocated.
    // Jake Smyth 40204021
    @Test
    public void testFirstTrade(){
        int resourcesRewarded = BabetteCard.resourcesToAssignToUser(1);
        assertTrue(resourcesRewarded == 4);
    }

    @Test
    public void testFifthTrade(){
        int resourcesRewarded = BabetteCard.resourcesToAssignToUser(5);
        assertTrue(resourcesRewarded == 12);
    }

    @Test
    public void testNinthTrade(){
        int resourcesRewarded = BabetteCard.resourcesToAssignToUser(9);
        assertTrue(resourcesRewarded == 30);
    }
}