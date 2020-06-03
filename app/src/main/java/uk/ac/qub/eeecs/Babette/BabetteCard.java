package uk.ac.qub.eeecs.Babette;

import android.graphics.Bitmap;
import java.util.ArrayList;
import uk.ac.qub.eeecs.gage.Game;

// Class to handle allocation and trade-in of User Babette Cards.
// Jake Smyth 40204021
public class BabetteCard {
    String bCardTerritory;
    Bitmap bCardBitmap;

    public BabetteCard(String bCardTerritory, Bitmap bCardBitmap) {
        this.bCardTerritory = bCardTerritory;
        this.bCardBitmap = bCardBitmap;
    }

    public String getbCardTerritory() {
        return bCardTerritory;
    }

    public void setbCardTerritory(String bCardTerritory) {
        this.bCardTerritory = bCardTerritory;
    }

    public Bitmap getBCardBitmap() {
        return bCardBitmap;
    }

    // Method for trade-in of matching cards.
    // Jake Smyth 40204021
    public static ArrayList<BabetteCard> tradeCardsForResources(ArrayList<BabetteCard> bCardList) {
        ArrayList<BabetteCard> bCardListToDelete = new ArrayList<>();
        BabetteCard bCardToDelete1, bCardToDelete2, bCardToDelete3;
        ArrayList<String> territoryList = new ArrayList<>();
        territoryList.add("CyberSecurity");
        territoryList.add("Development");
        territoryList.add("Hardware");
        territoryList.add("ECommerce");
        territoryList.add("Networking");
        territoryList.add("Programming");

        boolean threeIdenticalCards, twoIdenticalCardsAndWildCard, threeIndividualCards;
        String territoryToDelete;
        for (int i = 0; i < territoryList.size(); i++) {
            threeIdenticalCards = checkForThreeIdenticalCards(bCardList, territoryList.get(i));
            twoIdenticalCardsAndWildCard = checkForTwoIdenticalCardsAndWildCard(bCardList, territoryList.get(i));
            threeIndividualCards = checkForThreeIndividualCards(bCardList, territoryList);

            if (threeIdenticalCards || twoIdenticalCardsAndWildCard || threeIndividualCards) {
                territoryToDelete = territoryList.get(i);
                if (threeIdenticalCards)
                    bCardListToDelete = threeIdenticalCardsToDelete(bCardList, territoryToDelete);
                if (twoIdenticalCardsAndWildCard)
                    bCardListToDelete = twoIdenticalCardsAndWildCardToDelete(bCardList, territoryToDelete);
                if (threeIndividualCards)
                    bCardListToDelete = threeIndividualCardsToDelete(bCardList, territoryList);
                bCardToDelete1 = bCardListToDelete.get(0);
                bCardToDelete2 = bCardListToDelete.get(1);
                bCardToDelete3 = bCardListToDelete.get(2);
                bCardList.remove(bCardToDelete1);
                bCardList.remove(bCardToDelete2);
                bCardList.remove(bCardToDelete3);
            }
        }
        return bCardList;
    }

    // Methods to check for valid card combinations in the ArrayList.
    // Jake Smyth 40204021
    public static boolean checkForThreeIdenticalCards(ArrayList<BabetteCard> bCardList, String territory) {
        boolean threeIdenticalCardsFound = false;
        int counter = 0;
        for (BabetteCard bCard : bCardList)
            if (bCard.bCardTerritory == territory)
                counter++;
        if (counter >= 3)
            threeIdenticalCardsFound = true;
        return threeIdenticalCardsFound;
    }

    public static boolean checkForTwoIdenticalCardsAndWildCard(ArrayList<BabetteCard> bCardList, String territory) {
        boolean twoIdenticalCardsAndWildCard = false;
        boolean wildCardPresent = false;
        int counter = 0;
        for (BabetteCard bCard : bCardList)
            if (bCard.bCardTerritory == "WildCard")
                wildCardPresent = true;
        if (wildCardPresent)
            for (BabetteCard bCard : bCardList)
                if (bCard.bCardTerritory == territory || bCard.bCardTerritory == "WildCard")
                    counter++;
        if (counter >= 3) twoIdenticalCardsAndWildCard = true;
        return twoIdenticalCardsAndWildCard;
    }

    public static boolean checkForThreeIndividualCards(ArrayList<BabetteCard> bCardList, ArrayList<String> territoryList) {
        boolean threeIndividualCardsFound = false;
        BabetteCard bCard1 = null, bCard2 = null, bCard3 = null;
        int counter = 0;
        for (BabetteCard bCard : bCardList)
            for (String territory : territoryList)
                if (bCard.bCardTerritory == territory) {
                    if (bCard1 == null) {
                        bCard1 = bCard;
                        counter++;
                    }
                    if (bCard2 == null && bCard.bCardTerritory != bCard1.bCardTerritory) {
                        bCard2 = bCard;
                        counter++;
                    }
                    if (bCard3 == null && bCard.bCardTerritory != bCard1.bCardTerritory && bCard.bCardTerritory != bCard2.bCardTerritory) {
                        bCard3 = bCard;
                        counter++;
                    }
                }
        if (counter >= 3) threeIndividualCardsFound = true;
        return threeIndividualCardsFound;
    }

    // Each returns an array with the three cards to delete from the ArrayList.
    // Jake Smyth 40204021
    public static ArrayList<BabetteCard> threeIdenticalCardsToDelete(ArrayList<BabetteCard> bCardList, String territoryToDelete) {
        ArrayList<BabetteCard> threeIdenticalCardsToDelete = new ArrayList<>();
        int removalCounter = 3;
        for (int j = 0; j < bCardList.size(); j++)
            if (bCardList.get(j).bCardTerritory == territoryToDelete && removalCounter > 0) {
                BabetteCard newCard = bCardList.get(j);
                threeIdenticalCardsToDelete.add(newCard);
                removalCounter--;
            }
        return threeIdenticalCardsToDelete;
    }

    public static ArrayList<BabetteCard> twoIdenticalCardsAndWildCardToDelete(ArrayList<BabetteCard> bCardList, String territoryToDelete) {
        ArrayList<BabetteCard> twoIdenticalCardsAndWildCardToDelete = new ArrayList<>();
        int removalCounter = 3;
        for (BabetteCard bCard : bCardList)
            if ((bCard.bCardTerritory == territoryToDelete || bCard.bCardTerritory == "WildCard") && removalCounter > 0) {
                BabetteCard newCard = bCard;
                twoIdenticalCardsAndWildCardToDelete.add(newCard);
                removalCounter--;
            }
        return twoIdenticalCardsAndWildCardToDelete;
    }

    public static ArrayList<BabetteCard> threeIndividualCardsToDelete(ArrayList<BabetteCard> bCardList, ArrayList<String> territoryList) {
        ArrayList<BabetteCard> threeIndividualCardsToDelete = new ArrayList<>();
        BabetteCard bCardToDelete1 = null, bCardToDelete2 = null, bCardToDelete3 = null;
        int removalCounter = 3;
        for (BabetteCard bCard : bCardList)
            for (String territory : territoryList) {
                if ((bCard.bCardTerritory == territory) && removalCounter > 0) {
                    if (bCardToDelete1 == null) {
                        bCardToDelete1 = bCard;
                        removalCounter--;
                    }
                    if (bCardToDelete2 == null && bCard.bCardTerritory != bCardToDelete1.bCardTerritory) {
                        bCardToDelete2 = bCard;
                        removalCounter--;
                    }
                    if (bCardToDelete3 == null && bCard.bCardTerritory != bCardToDelete1.bCardTerritory && bCard.bCardTerritory != bCardToDelete2.bCardTerritory) {
                        bCardToDelete3 = bCard;
                        removalCounter--;
                    }
                }
            }
        threeIndividualCardsToDelete.add(bCardToDelete1);
        threeIndividualCardsToDelete.add(bCardToDelete2);
        threeIndividualCardsToDelete.add(bCardToDelete3);
        return threeIndividualCardsToDelete;
    }

    // Method for allocation of user resources.
    // Jake Smyth 40204021
    public static int resourcesToAssignToUser(int tradeCounter) {
        int resourcesToReturn = 0;
        if (tradeCounter == 1) resourcesToReturn = 4;
        if (tradeCounter == 2) resourcesToReturn = 6;
        if (tradeCounter == 3) resourcesToReturn = 8;
        if (tradeCounter == 4) resourcesToReturn = 10;
        if (tradeCounter == 5) resourcesToReturn = 12;
        if (tradeCounter == 6) resourcesToReturn = 15;
        if (tradeCounter >= 7) resourcesToReturn = 15 + ((tradeCounter - 6) * 5);
        return resourcesToReturn;
    }

    // Jake Smyth 40204021
    // Method to supply the User with a random Babette card.
    public static BabetteCard givePlayerRandomBabetteCard(Game game, ArrayList<BabetteCard> bCardList) {
        Bitmap bCardBitmap = null;
        ArrayList<String> territoryList = new ArrayList<>();
        territoryList.add("CyberSecurity");
        territoryList.add("Development");
        territoryList.add("Hardware");
        territoryList.add("Networking");
        territoryList.add("Programming");
        territoryList.add("ECommerce");
        territoryList.add("WildCard");

        boolean containsWildcard = false;
        for(BabetteCard bCard: bCardList)
            if(bCard.bCardTerritory == "WildCard")
                containsWildcard = true;

        int territoryNo;
        if(containsWildcard) {
            territoryNo = GameManager.generateNumber(0, 5);
        } else {
            territoryNo = GameManager.generateNumber(0, 6);
        }
        int resourceSize = GameManager.generateNumber(0, 2);
        String territoryName = territoryList.get(territoryNo);

        switch (territoryNo) {
            case 0:
                switch (resourceSize) {
                    case 0: bCardBitmap = game.getAssetManager().getBitmap("CyberSecurityTeam");break;
                    case 1: bCardBitmap = game.getAssetManager().getBitmap("CyberSecurityBusiness");break;
                    case 2: bCardBitmap = game.getAssetManager().getBitmap("CyberSecurityOrganisation");break;
                }break;
            case 1:
                switch (resourceSize) {
                    case 0: bCardBitmap = game.getAssetManager().getBitmap("DevelopmentTeam");break;
                    case 1: bCardBitmap = game.getAssetManager().getBitmap("DevelopmentBusiness");break;
                    case 2: bCardBitmap = game.getAssetManager().getBitmap("DevelopmentOrganisation");break;
                }break;
            case 2:
                switch (resourceSize) {
                    case 0: bCardBitmap = game.getAssetManager().getBitmap("HardwareTeam");break;
                    case 1: bCardBitmap = game.getAssetManager().getBitmap("HardwareBusiness");break;
                    case 2: bCardBitmap = game.getAssetManager().getBitmap("HardwareOrganisation");break;
                }break;
            case 3:
                switch (resourceSize) {
                    case 0: bCardBitmap = game.getAssetManager().getBitmap("NetworkingTeam");break;
                    case 1: bCardBitmap = game.getAssetManager().getBitmap("NetworkingBusiness");break;
                    case 2: bCardBitmap = game.getAssetManager().getBitmap("NetworkingOrganisation");break;
                }break;
            case 4:
                switch (resourceSize) {
                    case 0: bCardBitmap = game.getAssetManager().getBitmap("ProgrammingTeam");break;
                    case 1: bCardBitmap = game.getAssetManager().getBitmap("ProgrammingBusiness");break;
                    case 2: bCardBitmap = game.getAssetManager().getBitmap("ProgrammingOrganisation");break;
                }break;
            case 5:
                switch (resourceSize) {
                    case 0: bCardBitmap = game.getAssetManager().getBitmap("ECommerceTeam");break;
                    case 1: bCardBitmap = game.getAssetManager().getBitmap("ECommerceBusiness");break;
                    case 2: bCardBitmap = game.getAssetManager().getBitmap("ECommerceOrganisation");break;
                }break;
            case 6: bCardBitmap = game.getAssetManager().getBitmap("WildCard");break;
        }
        BabetteCard newCard = new BabetteCard(territoryName, bCardBitmap);
        return newCard;
    }

    //Christopher Jennings 40200418
    //Method to give the user a custom babette card if there are four babette cards in the arraylist.
    public static BabetteCard checkPlayerNeedsSpecificCard(ArrayList<BabetteCard> bCardList, ArrayList<String> territoryList) {
        boolean threeIdentical = false;
        boolean twoAndWildCard = false;
        boolean threeIndividual;

        if (bCardList.size() > 3) {
            for (int i = 0; i < territoryList.size(); i++) {
                if (!threeIdentical)
                    threeIdentical = checkForThreeIdenticalCards(bCardList, territoryList.get(i));
                if (!twoAndWildCard)
                    twoAndWildCard = checkForTwoIdenticalCardsAndWildCard(bCardList, territoryList.get(i));
            }
            threeIndividual = checkForThreeIndividualCards(bCardList, territoryList);

            if (!threeIdentical || !twoAndWildCard || !threeIndividual) {
                BabetteCard bcard1 = bCardList.get(0);
                BabetteCard bcard2 = null;

                int counter1 = 0, counter2 = 0;

                for (int i = 0; i < bCardList.size(); i++) {
                    if (bCardList.get(i) == bcard1) {
                        counter1++;
                    } else {
                        bcard2 = bCardList.get(i);
                        counter2++;
                    }

                    if (counter1 - counter2 == 0 && bcard2 != null) {
                        int cardToReturn = GameManager.generateNumber(1,2);
                        BabetteCard bcardToReturn = null;
                        switch(cardToReturn){
                            case 1: bcardToReturn = new BabetteCard(bcard1.getbCardTerritory(), bcard1.getBCardBitmap()); break;
                            case 2: bcardToReturn = new BabetteCard(bcard2.getbCardTerritory(), bcard2.getBCardBitmap()); break;
                        }
                        return bcardToReturn;
                    }
                }
            }
        }
        return null;
    }

    //Christopher Jennings 40200418
    public static ArrayList<BabetteCard> giveUserTailoredCard(ArrayList<BabetteCard> cardDeck, Game game, ArrayList<Territory> territories) {

        BabetteCard card;

        if (cardDeck.size() <= 3)
            cardDeck.add(givePlayerRandomBabetteCard(game, cardDeck));

        else {
            card = checkPlayerNeedsSpecificCard(cardDeck, territoryObjectToString(territories));
            if (card != null)
                cardDeck.add(card);
        }

        return cardDeck;
    }

    //Christopher Jennings 40200418
    //Creates the arraylist for converting an object
    private static ArrayList<String> territoryObjectToString(ArrayList<Territory> territories) {
        ArrayList<String> terr = new ArrayList<>();
        for (Territory t : territories)
            terr.add(t.getTerritoryName());
        return terr;
    }

    //Christopher Jennings 40200418
    //
    public static boolean checkTradePossible(ArrayList<BabetteCard> bCardList){
        ArrayList<String> territoryList = new ArrayList<>();
        territoryList.add("CyberSecurity");
        territoryList.add("Development");
        territoryList.add("Hardware");
        territoryList.add("ECommerce");
        territoryList.add("Networking");
        territoryList.add("Programming");

        boolean threeIdenticalCards = false, twoIdenticalCardsAndWildCard = false, threeIndividualCards = false;
        for (int i = 0; i < territoryList.size(); i++) {
            threeIdenticalCards = checkForThreeIdenticalCards(bCardList, territoryList.get(i));
            twoIdenticalCardsAndWildCard = checkForTwoIdenticalCardsAndWildCard(bCardList, territoryList.get(i));
            threeIndividualCards = checkForThreeIndividualCards(bCardList, territoryList);
            if(threeIdenticalCards || twoIdenticalCardsAndWildCard || threeIndividualCards) {
                return true;
            }
        }
            return false;
    }
}
