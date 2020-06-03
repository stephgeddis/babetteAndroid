package uk.ac.qub.eeecs.Babette;

import android.content.Context;

/**
 *
 * @author  Stephen Irons 40204625
 * @version 6.0
 *
 */

//import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


// Created and written by Adam Higgins 40212255: An interface for different screens to interact with one another and store data
// Contributed to by: Stephen Irons 40204625, Christopher Jennings 40200418, Jake Smyth 40204021 and Stephen Geddis
public class GameManager {

    // Members
    private ArrayList<User> existingUsers;
    public Context ctx;
    private GameState gameState;
    private User currentUser;
    private ArrayList<Region> regions;
    private ArrayList<Territory> territories;
    private final String NOT_FOUND = "";
    private int tradeCounter;
    private boolean cardThisTurn;

    // Constructor
    // Adam Higgins 40212255
    public GameManager() {

        gameState = new GameState();
        updateExistingUsers();
        resetRegionList();
        resetTerritoryList();
        currentUser = new User("adam_higgins", "green", "Ava1", "pass");
        tradeCounter = 1;
        cardThisTurn = false;
    }

    // Adam Higgins 40212255
    // This could be put in the constructor but for testing it is easier to not require a context
    public void setContext(Context c) {
        ctx = c;
    }

    //----------------------------------------------------------------------------------------------
    // Getters and setters

    // Adam Higgins 40212255
    public User getCurrentUser() {
        return currentUser;
    }

    // Adam Higgins 40212255
    public void setCurrentUser(User user) {
        currentUser = user;
        boolean found = false;
        for (User u : existingUsers) {
            if (u.getUsername().equals(user.getUsername()))
                found = true;
        }
        if (!found)
            existingUsers.add(user);
    }

    // Game play methods
    // Adam Higgins 40212255
    public void setStartingPlayers(ArrayList<Player> players) {
        gameState.setStartingPlayers(players);
    }

    // Adam Higgins 40212255
    public ArrayList<Player> getCurrentPlayers() {
        return gameState.getPlayers();
    }

    // Adam Higgins 40212255
    public void addPlayerToGame(Player player) {
        gameState.addPlayerToGame(player);
    }

    // Adam Higgins 40212255
    public void playerLoses(String username) {
        gameState.kickPlayerFromGame(username);
    }

    // Adam Higgins 40212255
    public void endGame(Player winner) {
        if (winner instanceof User)
            ((User) winner).win();
        nextTurn();
    }

    // Adam Higgins 40212255
    public int getCurrentPlayer() {
        return gameState.getCurrentPlayer();
    }

    // Adam Higgins 40212255
    public void nextTurn() {
        gameState.nextTurn();
    }

    // Adam Higgins 40212255
    public void nextStage() {
        gameState.nextStage();
    }

    // Adam Higgins 40212255
    public int getGameStage() {
        return gameState.getGameStage();
    }

    // Adam Higgins 40212255
    public ArrayList<Region> getRegions() {
        return regions;
    }

    // Adam Higgins 40212255
    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public int getTradeCounter() {
        return tradeCounter;
    }

    public void incrementTradeCounter() {
        if (tradeCounter < 7)
            tradeCounter++;
    }

    public boolean getCardThisTurn() {
        return cardThisTurn;
    }

    public void setCardThisTurn(boolean bool) {
        cardThisTurn = bool;
    }

    //----------------------------------------------------------------------------------------------
    // Methods

    // Adam Higgins 40212255: Moving James Sims' Method from out of the user class to the game screen
    // I did not write this code
    public boolean checkEmptyRegionsExist() {
        for (int i = 0; i < getRegions().size(); i++) {
            if (getRegions().get(i).getNumResources() == 0) {
                return true; //checks that there are empty regions.
            }
        }

        return false;
    }

    // Changes message on game screen to show which region has been selected by user
    // Jake Smyth 40204021 and Stephen Geddis
    public String regionSelectedByUser(int colour) {

        String message;
        switch (colour) {
            // Expanded initial switch statement made by Stephen Geddis to
            // return values for all 30 regions. Also returns if the user fails to
            // click a region on the map.
            case 0xFFFFFFFF: message = "White";break;
            case 0xFFFF6363: message = "Anti-Virus";break;
            case 0xFFfd6363: message = "Phishing";break;
            case 0xFFfc6363: message = "Hacking";break;
            case 0xFFfb6363: message = "IdentityTheft";break;
            case 0xFFfa6363: message = "Malware";break;
            case 0xFFACDDFF: message = "Java";break;
            case 0xFFABDDFF: message = "HTML";break;
            case 0xFFAADDFF: message = "C++";break;
            case 0xFFA9DDFF: message = "Python";break;
            case 0xFFA8DDFF: message = "SQL";break;
            case 0xFFFFD078: message = "Servers";break;
            case 0xFFFED078: message = "Laptops";break;
            case 0xFFFDD078: message = "Printers";break;
            case 0xFFFCD078: message = "Desktops";break;
            case 0xFFFBD078: message = "Peripherals";break;
            case 0xFFD187FF: message = "Business";break;
            case 0xFFD087FF: message = "Video Sharing";break;
            case 0xFFCF87FF: message = "Photo Sharing";break;
            case 0xFFCE87FF: message = "Live Streaming";break;
            case 0xFFCD87FF: message = "Social";break;
            case 0xFFFFD7DC: message = "Delivery Services";break;
            case 0xFFFED7DC: message = "e-Currency";break;
            case 0xFFFDD7DC: message = "Digital Downloads";break;
            case 0xFFFCD7DC: message = "Online Store";break;
            case 0xFFFBD7DC: message = "Market Place";break;
            case 0xFFFBFF7B: message = "Government Systems";break;
            case 0xFFFAFF7B: message = "AI Development";break;
            case 0xFFF9FF7B: message = "Mobile Development";break;
            case 0xFFF8FF7B: message = "Web Development";break;
            case 0xFFF7FF7B: message = "Game Development";break;
            default: message = "";break;
        }

        return message;
    }

    // Adam Higgins 40212255
    public String regionNameToHexCode(String regionName) {
        for (Region r : getRegions()) {
            if (r.getRegionName().equals(regionName))
                return r.getHexCode();
        }
        return NOT_FOUND;
    }

    // Adam Higgins 40212255
    public String hexCodeToRegionName(String hexCode) {
        for (Region r : getRegions()) {
            if (r.getHexCode().equals(hexCode))
                return r.getRegionName();
        }
        return NOT_FOUND;
    }

    // User file methods----------------------------------------------------------------------------
    // Adam Higgins 40212255
    public void updateFile() {
        User.writeToFile(existingUsers, ctx);
    }

    public void updateExistingUsers() {
        try {
            existingUsers = User.readFromFile(ctx);
        }
        catch (Exception e) {
            if (existingUsers == null)
                existingUsers = new ArrayList<>();
        }
    }

    private ArrayList<User> readFromFile() {
        return existingUsers;
    }

    // Adam Higgins 40212255
    public void clearUserFile() {
        User.clearUserFile(ctx);
    }

    // Adam Higgins 40212255
    public void addNewUser(User u) {
        existingUsers.add(u);
        updateFile();
    }

    // Adam Higgins 40212255
    public void removeExisingUser(String username) {

        for (int i = 0; i < existingUsers.size(); i++) {
            if (existingUsers.get(i).getUsername().equals(username))
                existingUsers.remove(i);
        }

        updateFile();
    }

    public void updateCurrentUser() {
        for (int i = 0; i < existingUsers.size(); i++) {
            if (existingUsers.get(i).getUsername().equals(currentUser.getUsername())) {
                existingUsers.remove(i);
                existingUsers.add(i, currentUser);
            }
        }
    }

    // Game Set Up methods--------------------------------------------------------------------------

    // Adam Higgins 40212255 && Stephen Irons 40204625:
    // Assigning AI players a game colour that is no the same as the logged in player colour
    public ArrayList<Player> createEnemyAIList() {

        ArrayList<String> colours = new ArrayList<>(Arrays.asList("yellow", "red", "blue", "green"));
        ArrayList<String> avatars = new ArrayList<>(Arrays.asList("Avatar2", "Avatar3", "Avatar4"));
        ArrayList<Player> AIList = new ArrayList<>();

        for (int i = 0; i < colours.size() - 1; i++) {
            if (colours.get(i) == getCurrentUser().getColour()) {
                colours.remove(i);
                break;
            }
        }

        for (int i = 0; i < avatars.size(); i++)
            AIList.add(new AI(colours.get(i), colours.get(i), avatars.get(i)));

        return AIList;
    }

    //Stephen Irons 40204625
    //Creating an instance of each individual region and adding them to a regions array list
    public void resetRegionList() {
        ArrayList<Region> RegionsList = new ArrayList<Region>();
        ArrayList<String> AntiVirusAdjRegions = new ArrayList<>(Arrays.asList("0xFFfb6363", "0xFFfa6363", "0xFFACDDFF"));
        Region Antivirus = new Region("Anti-Virus", "0xFFFF6363", 122, 367, 0, AntiVirusAdjRegions);
        RegionsList.add(Antivirus);
        ArrayList<String> PhishingAdjRegions = new ArrayList<>(Arrays.asList("0xFFfc6363", "0xFFfb6363"));
        Region Phishing = new Region("Phishing", "0xFFfd6363", 205, 65, 0, PhishingAdjRegions);
        RegionsList.add(Phishing);
        ArrayList<String> HackingAdjRegions = new ArrayList<>(Arrays.asList("0xFFfd6363", "0xFFfb6363", "0xFFFFD078", "0xFFD187FF", "0xFFD087FF"));
        Region Hacking = new Region("Hacking", "0xFFfc6363", 389, 122, 0, HackingAdjRegions);
        RegionsList.add(Hacking);
        ArrayList<String> IdentityTheftAdjRegions = new ArrayList<>(Arrays.asList("0xFFfd6363", "0xFFfc6363", "0xFFFFD078", "0xFFA8DDFF", "0xFFfa6363", "0xFFFF6363"));
        Region IdentityTheft = new Region("IdentityTheft", "0xFFfb6363", 207, 167, 0, IdentityTheftAdjRegions);
        RegionsList.add(IdentityTheft);

        ArrayList<String> MalwareAdjRegions = new ArrayList<>(Arrays.asList("0xFFfb6363", "0xFFA8DDFF", "0xFFACDDFF", "0xFFFF6363"));
        Region Malware = new Region("Malware", "0xFFfa6363", 215, 322, 0, MalwareAdjRegions);
        RegionsList.add(Malware);
        ArrayList<String> JavaAdjRegions = new ArrayList<>(Arrays.asList("0xFFFF6363", "0xFFfa6363", "0xFFA8DDFF", "0xFFABDDFF"));
        Region Java = new Region("Java", "0xFFACDDFF", 290, 496, 0, JavaAdjRegions);
        RegionsList.add(Java);
        ArrayList<String> HTMLAdjRegions = new ArrayList<>(Arrays.asList("0xFFACDDFF", "0xFFA8DDFF", "0xFFFED078", "0xFFFDD078", "0xFFAADDFF"));
        Region HTML = new Region("HTML", "0xFFABDDFF", 416, 500, 0, HTMLAdjRegions);
        RegionsList.add(HTML);
        ArrayList<String> CppAdjRegions = new ArrayList<>(Arrays.asList("0xFFABDDFF", "0xFFFED078", "0xFFFDD078", "0xFFA9DDFF"));
        Region Cpp = new Region("C++", "0xFFAADDFF", 518, 574, 0, CppAdjRegions);
        RegionsList.add(Cpp);
        ArrayList<String> PythonAdjRegions = new ArrayList<>(Arrays.asList("0xFFAADDFF", "0xFFFDD078", "0xFFFCD7DC", "0xFFF7FF7B"));
        Region Python = new Region("Python", "0xFFA9DDFF", 619, 622, 0, PythonAdjRegions);
        RegionsList.add(Python);
        ArrayList<String> SQLAdjRegions = new ArrayList<>(Arrays.asList("0xFFACDDFF", "0xFFfa6363", "0xFFfb6363", "0xFFFFD078", "0xFFFED078", "0xFFABDDFF"));
        Region SQL = new Region("SQL", "0xFFA8DDFF", 320, 348, 0, SQLAdjRegions);
        RegionsList.add(SQL);

        ArrayList<String> ServersAdjRegions = new ArrayList<>(Arrays.asList("0xFFA8DDFF", "0xFFfb6363", "0xFFfc6363", "0xFFD087FF", "0xFFD187FF", "0xFFFBD078", "0xFFFED078"));
        Region Servers = new Region("Servers", "0xFFFFD078", 417, 242, 0, ServersAdjRegions);
        RegionsList.add(Servers);
        ArrayList<String> LaptopsAdjRegions = new ArrayList<>(Arrays.asList("0xFFA8DDFF", "0xFFFFD078", "0xFFFBD078", "0xFFFCD078", "0xFFFDD078", "0xFFAADDFF", "0xFFABDDFF"));
        Region Laptops = new Region("Laptops", "0xFFFED078", 446, 366, 0, LaptopsAdjRegions);
        RegionsList.add(Laptops);
        ArrayList<String> PrintersAdjRegions = new ArrayList<>(Arrays.asList("0xFFAADDFF", "0xFFFED078", "0xFFFCD078", "0xFFFED7DC", "0xFFFCD7DC", "0xFFA9DDFF", "0xFFABDDFF"));
        Region Printers = new Region("Printers", "0xFFFDD078", 615, 484, 0, PrintersAdjRegions);
        RegionsList.add(Printers);
        ArrayList<String> DesktopsAdjRegions = new ArrayList<>(Arrays.asList("0xFFFED078", "0xFFFBD078", "0xFFFED7DC", "0xFFFDD078"));
        Region Desktops = new Region("Desktops", "0xFFFCD078", 605, 391, 0, DesktopsAdjRegions);
        RegionsList.add(Desktops);
        ArrayList<String> PeripheralsAdjRegions = new ArrayList<>(Arrays.asList("0xFFFED078", "0xFFFFD078", "0xFFD187FF", "0xFFCF87FF", "0xFFFED7DC", "0xFFFCD078"));
        Region Peripherals = new Region("Peripherals", "0xFFFBD078", 569, 299, 0, PeripheralsAdjRegions);
        RegionsList.add(Peripherals);
                                                                                                                       
        ArrayList<String> BusinessAdjRegions = new ArrayList<>(Arrays.asList("0xFFFFD078", "0xFFD087FF", "0xFFCD87FF", "0xFFFBD078"));
        Region Business = new Region("Business", "0xFFD187FF", 559, 223, 0, BusinessAdjRegions);
        RegionsList.add(Business);
        ArrayList<String> VideoSharingAdjRegions = new ArrayList<>(Arrays.asList("0xFFfc6363", "0xFFFFD078", "0xFFD187FF", "0xFFCD87FF"));
        Region VideoSharing = new Region("Video Sharing", "0xFFD087FF", 557, 136, 0, VideoSharingAdjRegions);
        RegionsList.add(VideoSharing);
        ArrayList<String> PhotoSharingAdjRegions = new ArrayList<>(Arrays.asList("0xFFFBD078", "0xFFD187FF", "0xFFCD87FF", "0xFFCE87FF", "0xFFFDD7DC", "0xFFFED7DC"));
        Region PhotoSharing = new Region("Photo Sharing", "0xFFCF87FF", 694, 283, 0, PhotoSharingAdjRegions);
        RegionsList.add(PhotoSharing);
        ArrayList<String> LiveStreamingAdjRegions = new ArrayList<>(Arrays.asList("0xFFCD87FF", "0xFFD087FF", "0xFFFDD7DC", "0xFFCF87FF"));
        Region LiveStreaming = new Region("Live Streaming", "0xFFCE87FF", 759, 191, 0, LiveStreamingAdjRegions);
        RegionsList.add(LiveStreaming);
        ArrayList<String> SocialAdjRegions = new ArrayList<>(Arrays.asList("0xFFD087FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        Region Social = new Region("Social", "0xFFCD87FF", 670, 158, 0, SocialAdjRegions);
        RegionsList.add(Social);

        ArrayList<String> DeliveryServicesAdjRegions = new ArrayList<>(Arrays.asList("0xFFFBD7DC", "0xFFFAFF7B"));
        Region DeliveryServices = new Region("Delivery Services", "0xFFFFD7DC", 1053, 242, 0, DeliveryServicesAdjRegions);
        RegionsList.add(DeliveryServices);
        ArrayList<String> ECurrencyAdjRegions = new ArrayList<>(Arrays.asList("0xFFFCD078", "0xFFFBD078", "0xFFCF87FF", "0xFFFDD7DC", "0xFFFBFF7B", "0xFFFCD7DC", "0xFFFDD078"));
        Region ECurrency = new Region("e-Currency", "0xFFFED7DC", 727, 399, 0, ECurrencyAdjRegions);
        RegionsList.add(ECurrency);
        ArrayList<String> DigitalDownloadsAdjRegions = new ArrayList<>(Arrays.asList("0xFFFED7DC", "0xFFCF87FF", "0xFFCE87FF", "0xFFFBD7DC", "0xFFFBFF7B"));
        Region DigitalDownloads = new Region("Digital Downloads", "0xFFFDD7DC", 781, 331, 0, DigitalDownloadsAdjRegions);
        RegionsList.add(DigitalDownloads);
        ArrayList<String> OnlineStoreAdjRegions = new ArrayList<>(Arrays.asList("0xFFA9DDFF", "0xFFFDD078", "0xFFFED7DC", "0xFFFBFF7B"));
        Region OnlineStore = new Region("Online Store", "0xFFFCD7DC", 724, 532, 0, OnlineStoreAdjRegions);
        RegionsList.add(OnlineStore);
        ArrayList<String> MarketPlaceAdjRegions = new ArrayList<>(Arrays.asList("0xFFFDD7DC", "0xFFFBFF7B", "0xFFFAFF7B", "0xFFFFD7DC"));
        Region MarketPlace = new Region("Market Place", "0xFFFBD7DC", 880, 301, 0, MarketPlaceAdjRegions);
        RegionsList.add(MarketPlace);

        ArrayList<String> GovSystemsRegionsAdjRegions = new ArrayList<>(Arrays.asList("0xFFFED7DC", "0xFFFCD7DC", "0xFFF7FF7B", "0xFFF8FF7B", "0xFFF9FF7B", "0xFFFAFF7B", "0xFFFBD7DC", "0xFFFDD7DC"));
        Region GovSystems = new Region("Government Systems", "0xFFFBFF7B", 864, 434, 0, GovSystemsRegionsAdjRegions);
        RegionsList.add(GovSystems);
        ArrayList<String> AIAdjRegions = new ArrayList<>(Arrays.asList("0xFFFDD7DC", "0xFFFBD7DC", "0xFFFBFF7B", "0xFFF9FF7B", "0xFFFFD7DC"));
        Region AI = new Region("AI Development", "0xFFFAFF7B", 1003, 369, 0, AIAdjRegions);
        RegionsList.add(AI);
        ArrayList<String> MobileAdjRegions = new ArrayList<>(Arrays.asList("0xFFFAFF7B", "0xFFFBFF7B", "0xFFF8FF7B"));
        Region Mobile = new Region("Mobile Development", "0xFFF9FF7B", 996, 472, 0, MobileAdjRegions);
        RegionsList.add(Mobile);
        ArrayList<String> WebAdjRegions = new ArrayList<>(Arrays.asList("0xFFF7FF7B", "0xFFFBFF7B", "0xFFF9FF7B"));
        Region Web = new Region("Web Development", "0xFFF8FF7B", 906, 560, 0, WebAdjRegions);
        RegionsList.add(Web);
        ArrayList<String> GameAdjRegions = new ArrayList<>(Arrays.asList("0xFFA9DDFF", "0xFFFCD7DC", "0xFFFBFF7B", "0xFFF8FF7B"));
        Region Game = new Region("Game Development", "0xFFF7FF7B", 781, 582, 0, GameAdjRegions);
        RegionsList.add(Game);

        regions = RegionsList;
    }

    //Christopher Jennings 40200418
    //Creating method for the territories within an array list
    public void resetTerritoryList() {
        ArrayList<Territory> territoryList = new ArrayList<Territory>();

        ArrayList<String> CyberSecurityRegions = new ArrayList<String>(Arrays.asList("0xFFfd6363", "0xFFfc6363", "0xFFfb6363", "0xFFfa6363", "0xFFFF6363"));
        Territory CyberSecurity = new Territory("CyberSecurity", CyberSecurityRegions, 5);
        territoryList.add(CyberSecurity);

        ArrayList<String> SocialMediaRegions = new ArrayList<String>(Arrays.asList("0xFFD087FF", "0xFFCD87FF", "0xFFD187FF", "0xFFCF87FF", "0xFFCE87FF"));
        Territory SocialMedia = new Territory("SocialMedia", SocialMediaRegions, 7);
        territoryList.add(SocialMedia);

        ArrayList<String> HardwareRegions = new ArrayList<String>(Arrays.asList("0xFFFFD078", "0xFFFBD078", "0xFFFED078", "0xFFFCD078", "0xFFFDD078"));
        Territory Hardware = new Territory("Hardware", HardwareRegions, 15);
        territoryList.add(Hardware);

        ArrayList<String> LanguagesRegions = new ArrayList<String>(Arrays.asList("0xFFA8DDFF", "0xFFACDDFF", "0xFFABDDFF", "0xFFAADDFF", "0xFFA9DDFF"));
        Territory Languages = new Territory("Languages", LanguagesRegions, 7);
        territoryList.add(Languages);

        ArrayList<String> eCommerceRegions = new ArrayList<String>(Arrays.asList("0xFFFFD7DC", "0xFFFBD7DC", "0xFFFDD7DC", "0xFFFED7DC", "0xFFFCD7DC"));
        Territory eCommerce = new Territory("eCommerce", eCommerceRegions, 12);
        territoryList.add(eCommerce);

        ArrayList<String> DevelopmentRegions = new ArrayList<String>(Arrays.asList("0xFFFAFF7B", "0xFFFBFF7B", "0xFFF9FF7B", "0xFFF8FF7B", "0xFFF7FF7B"));
        Territory Development = new Territory("Development", DevelopmentRegions, 5);
        territoryList.add(Development);

        territories = territoryList;
    }

    // STATIC METHODS ------------------------------------------------------------------------------

    //Stephen Irons 40204625
    //Method used to generate a random number
    //Code taken from stackoverflow to generage the random number
    public static int generateNumber(int min, int max){
        int size = 0;
        if(min <= max)
            size = ThreadLocalRandom.current().nextInt(min, max + 1);
        return size;
    }
}
