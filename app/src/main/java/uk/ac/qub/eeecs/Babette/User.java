package uk.ac.qub.eeecs.Babette;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;

/**
 *
 * This class is used to implement methods for user
 *
 * @author  Stephen Irons 40204625, James Sims 40174176
 * @version 6.0
 *
 */

public class User extends Player { // Created by Adam Higgins 40212255

    private String salt;
    private String password;
    private int numberOfWins;
    private final static String PATH_NAME = "User.txt"; // Permanent user storage location
    private final static String SALT_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // String to salt passwords before they're hashed and stored
    private final static char[] SALT_ARRAY = SALT_STRING.toCharArray();

    // Constructors
    // Normal Constructor: 40212255 Adam Higgins
    public User(String name, String col, String ava, String pass)
    {
        super(name, col, ava);
        salt = generateUniqueSalt();
        password = hash(salt(pass));
        numberOfWins = 0;
    }


    // Constructor for data from text Files: 40212255 Adam Higgins
    private User(String[] arr) {
        super(arr[0], arr[1], arr[2]);
        this.salt = arr[3];
        this.password = arr[4];
        this.numberOfWins = Integer.parseInt(arr[5]);

        /*if (super.getColour() != "green" ||
                super.getColour() != "yellow" ||
                super.getColour() != "red" ||
                super.getColour() != "blue")
            super.setColour("green");*/

    }

    private static String[] stringToArr(String s) {
        String[] arr = s.split(":");
        return arr;
    }

    // Getters and setters ------------------- Adam Higgins: 40212255
    public void setPassword(String pass) {
        password = hash(salt(pass));
    }

    public String getPassword() {
        return password;
    }

    // Method used to check if the user has input a correct password
    public boolean correctPassword(String pass) {
        return password.equals(hash(salt(pass)));
    }

    public void win() {
        numberOfWins++;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public String getSalt() {
        return salt;
    }

    // -----------------------------------------------------

    //James Sims 40174176 & Stephen Irons 40204625
    //Overloading placeInitialResources method to allow the user to place resources during game setup
    public void placeInitialResources(ArrayList<Region> gameRegionList,String hexCode) {

        for (int i = 0; i < gameRegionList.size(); i++) {
            if (gameRegionList.get(i).hexCode == hexCode && gameRegionList.get(i).numResources == 0) {
                gameRegionList.get(i).setNumResources(gameRegionList.get(i).getNumResources() + 1);
                addRegion(hexCode);
                break;
            }
        }
    }

    //40174176 - James Sims
    //Method for user to place remaining resources
    public void placeRemainingResources(ArrayList<Region> gameRegionList, String hexCode){
        for (int i = 0; i < gameRegionList.size(); i++) {
            if (gameRegionList.get(i).hexCode == hexCode) {
                int numResources = gameRegionList.get(i).getNumResources() + 1;
                gameRegionList.get(i).setNumResources(numResources);
                break;
            }
        }
    }

    //Stephen Irons 40204625
    //Method allows user to attack another region
    public boolean callForMerger(String attackingRegionHexCode, String defendingRegionHexCode, ArrayList<Region> gameRegionList, ArrayList<Player> playerArrayList, Game game, GameManager gm){
        String losingRegionHexCode = callForMerger(attackingRegionHexCode,defendingRegionHexCode, gameRegionList);
        decreaseRegionResources(losingRegionHexCode,gameRegionList);

        for(int i = 0;i<gameRegionList.size();i++){
            if(gameRegionList.get(i).getHexCode() == defendingRegionHexCode && gameRegionList.get(i).getNumResources()==0){
                changeRegionOwner(attackingRegionHexCode,defendingRegionHexCode,gameRegionList, playerArrayList);
                //Christopher Jennings 40200418
                //add babette Cards
                if (!gm.getCardThisTurn()) {
                    cards = BabetteCard.giveUserTailoredCard(cards, game, gm.getTerritories());
                    return true;
                }
            }
        }

        return false;
    }

    //Stephen Irons 40204625
    //This method calculates the number of resources a user has to place at the start of their turn
    public int placeTurnResources(ArrayList<Territory> territoryArrayList) {

        int numResources = 0;
        numResources+=this.getHeldRegionResources();
        numResources+=this.getHeldTerritoryResources(territoryArrayList);

        return numResources;
    }

    // Transfer resources from one region to another for the User.
    // Jake Smyth 40204021
    public void fortify(String regionFrom, String regionTo, ArrayList<Region> gameRegionList, int fortifyValue){
        for(int i = 0; i< gameRegionList.size();i++){
            if(gameRegionList.get(i).hexCode == regionFrom){
                gameRegionList.get(i).setNumResources(gameRegionList.get(i).getNumResources() - fortifyValue);
                for(int j = 0; j< gameRegionList.size();j++){
                    if(gameRegionList.get(j).hexCode == regionTo){
                        gameRegionList.get(j).setNumResources(gameRegionList.get(j).getNumResources() + fortifyValue);
                        break;
                    }
                }
            }
        }
    }

    // Adam Higgins 40212255:
    // File IO Methods -----------------------------------------------------------------------------
    // The code inside the try block for the methods readFromFile() and writeToFile() was based on a
    // youtube video: https://www.youtube.com/watch?v=CNoj9vzAYiQ
    // The code has been interpreted to fit into our project

    // Writes a list of user objects to a file
    public static void writeToFile(ArrayList<User> users, Context c) {
        try {
            FileOutputStream fos = c.openFileOutput(PATH_NAME, Context.MODE_PRIVATE);
            fos.write(getFileString(users).getBytes());
            fos.close();
        }
        catch (Exception e) {
            return;
        }
    }

    // Reads a list of user objects from a file
    public static ArrayList<User> readFromFile(Context c) throws Exception {
        FileInputStream fis = c.openFileInput(PATH_NAME);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        return getUserList(new String(buffer));
    }

    // Writing a blank string into the file
    public static void clearUserFile(Context c) {
        try {
            FileOutputStream fos = c.openFileOutput(PATH_NAME, Context.MODE_PRIVATE);
            fos.write("".getBytes());
            fos.close();
        }
        catch (Exception e) {
            return;
        }
    }

    // File IO Helper Methods
    // Method to return a list of users from the string taken from a file
    public static ArrayList<User> getUserList(String fileString) {

        ArrayList<User> users = new ArrayList<>();
        String[] arr = fileString.split("£");

        for (String s : arr)
            users.add(new User(stringToArr(s)));

        return users;
    }

    // Method to return a String to write to the file built up of a list of users
    public static String getFileString(ArrayList<User> users) {
        String fileStr = "";

        for (User u : users) {
            fileStr += u.toTextFormat() + "£";
        }

        return fileStr;
    }

    // Method that returns a single user object in the form of plain text
    public String toTextFormat() {

        return this.getUsername() + ":" +
                this.getColour() + ":" +
                this.getAvatar() + ":" +
                this.salt + ":" +
                this.password + ":" +
                Integer.toString(this.numberOfWins);
    }

    // Password encryption ----------------------------------- 40212255: Adam Higgins
    private String salt(String pass) {
        return pass + salt;
    }

    private String generateUniqueSalt() {

        String slt = "";
        for (int i = 0; i < 16; i++)
            slt += SALT_ARRAY[GameManager.generateNumber(0, SALT_ARRAY.length - 1)];

        return slt;
    }

    // Hashing algorithm based on:
    // https://veerasundar.com/blog/2010/09/storing-passwords-in-java-web-application/
    private String hash(String pass) {

        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(pass.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (byte b : hashedBytes) {
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            return "-1";
        }

        return hash.toString();
    }
}
