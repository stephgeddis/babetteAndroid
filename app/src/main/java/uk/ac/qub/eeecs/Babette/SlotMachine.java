package uk.ac.qub.eeecs.Babette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * This class is used to create a slot machine game screen
 * as a bonus feature in order to comply with the
 * accompanying module (CSC 2045) for a Queen's themed feature
 *
 * @author  Stephen Geddis 40210068
 * @version 6.0
 *
 */
public class SlotMachine extends GameScreen{

    private GameManager gameManager;
    private AssetManager assetManager = mGame.getAssetManager();
    private AudioManager slotMachineWinOrLoseAudio = getGame().getAudioManager();


    //Bitmap variables used to store various slot images
    //Stephen Geddis 40210068
    private Bitmap bitmapSlotMachineStart;
    private Bitmap bitmapSlotMachineFail;
    private Bitmap bitmapSlotMachineQueens;

    //Used to create a coins object setting the amount to 3
    //Stephen Geddis 40210068
    private Coins slotmachineCoins = new Coins(3);

    //List to create the slot machine's slots as game objects
    //Stephen Geddis 40210068
    private List<GameObject> listGameObjectsSlotMachineSlots = new ArrayList<>();


    private GameObject gameObjectSlotMachine;
    private GameObject gameObjectBackground;

    private Paint paintToDisplayWinOrLose = new Paint();
    private Paint paintToDisplayCoinAmount = new Paint();
    private PushButton btnStartSlots;
    private PushButton btnBackToMenu;

    //String to display win or loose above the slot machine
    //Stephen Geddis 40210068
    private String stringToDisplayWinOrLose = "???";


    //Seed is used as starting point for the middle square random number method
    //Stephen Geddis 40210068
    private static int seedForRandom;


    //Used as the main variable to change the text paint colour of the Win/Loose string
    //The number -16777216 represents black and is used to initalise
    //the text paint colour
    //Stephen Geddis 40210068
    private int intToDetermineTextPaintColour = -16777216;

    //Used to change the colour of the text paint depending on win (green) or loose (red)
    //Stephen Geddis 40210068
    private final int INTTOTURNTEXTRED = -65536;
    private final int INTTOTURNTEXTGREEN = -16711936;


    public SlotMachine(Game game, GameManager gm) {
            super("SlotMachine", game);
            gameManager = gm;

            loadAndAddBitmapToAssetManager();
            defineBitmaps();
            createBackgroundObject(game);
            createSlotMachineGameObject(game);

            constructSlotGameObjects("txt/assets/SlotMachineSlotLayout",listGameObjectsSlotMachineSlots);

            createStartSlotsButton();
            createBtnBackToMenu();



            //Creates a starting value for the seed for the random number
            //Stephen Geddis 40210068
            seedForRandom = 1234;

        }

    @Override
    public void update(ElapsedTime elapsedTime) {

        btnStartSlots.update(elapsedTime);

        if(btnStartSlots.isPushTriggered()){
            if (slotmachineCoins.getCoinAmount() >0) {
                reduceCoins();
                runSlotChange();
                checkForWin();
            }else{
                noCoinsLeft();
            }

        }

        btnBackToMenu.update(elapsedTime);
        if(btnBackToMenu.isPushTriggered()){
            btnBackToMenuPressed(elapsedTime);
        }

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.BLACK);

        paintToDisplayWinOrLose.setColor(intToDetermineTextPaintColour);
        paintToDisplayWinOrLose.setTypeface(Typeface.MONOSPACE);
        paintToDisplayWinOrLose.setTextSize(80.0f);
        paintToDisplayWinOrLose.setTextAlign(Paint.Align.CENTER);

        paintToDisplayCoinAmount.setColor(Color.WHITE);
        paintToDisplayCoinAmount.setTypeface(Typeface.MONOSPACE);
        paintToDisplayCoinAmount.setTextSize(50.0f);
        paintToDisplayCoinAmount.setTextAlign(Paint.Align.CENTER);

       gameObjectBackground.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);



       for(GameObject slot:listGameObjectsSlotMachineSlots)
           slot.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

       gameObjectSlotMachine.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);


       graphics2D.drawText(stringToDisplayWinOrLose, gameObjectSlotMachine.getWidth()+120, gameObjectSlotMachine.getHeight()-80, paintToDisplayWinOrLose);

        graphics2D.drawText("Coin Amount: " + slotmachineCoins.getCoinAmount(), gameObjectSlotMachine.getWidth()+400, gameObjectSlotMachine.getHeight()+425, paintToDisplayCoinAmount);



        btnStartSlots.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
       btnBackToMenu.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);


    }

    //Adds and loads the bitmap image file to the asset manager
    //Stephen Geddis 40210068
    public void loadAndAddBitmapToAssetManager(){

        assetManager.loadAndAddBitmap("BabetteBgScreen", "img/BabetteBgScreen.png");
        assetManager.loadAndAddBitmap("SlotMachine", "img/SlotMachineTemplate.png");
        assetManager.loadAndAddBitmap("SlotMachineStart", "img/SlotMachineStart.png");
        assetManager.loadAndAddBitmap("SlotMachineFail", "img/SlotMachineFail.png");
        assetManager.loadAndAddBitmap("SlotMachineQueens", "img/SlotMachineQueensLogo.png");
        assetManager.loadAndAddBitmap("BtnPlaySlot", "img/btnPlaySlot.png");
        assetManager.loadAndAddBitmap("Backinvert", "img/Backinvert.png");
        assetManager.loadAndAddMusic("SlotMachineWin","sound/SlotMachineWinSFX.mp3");
        assetManager.loadAndAddMusic("SlotMachineLose","sound/SlotMachineLoseSFX.mp3");

    }

    //Defines the bitmaps for the various slot machine's slot possibilities
    //Stephen Geddis 40210068
    public void defineBitmaps(){

        bitmapSlotMachineStart = Bitmap.createScaledBitmap(assetManager.getBitmap("SlotMachineStart"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);


        bitmapSlotMachineFail = Bitmap.createScaledBitmap(assetManager.getBitmap("SlotMachineFail"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);

        bitmapSlotMachineQueens = Bitmap.createScaledBitmap(assetManager.getBitmap("SlotMachineQueens"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);

    }

    //Method to define the slot machine's game object properties
    // Stephen Geddis 40204021
    private void createSlotMachineGameObject(Game game){
        gameObjectSlotMachine = new GameObject(
                mDefaultLayerViewport.x , mDefaultLayerViewport.y, mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(), game.getAssetManager().getBitmap("SlotMachine"), this);

    }

    //Method to create a background object
    // Stephen Geddis 40204021
    private void createBackgroundObject(Game game){
        gameObjectBackground = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y,
                mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("BabetteBgScreen"), this);
    }


    //Method to create a start slot button
    // Stephen Geddis 40204021
    private void createBtnBackToMenu() {
      btnBackToMenu = new PushButton(mDefaultLayerViewport.x-150, mDefaultLayerViewport.y -130,
                mDefaultLayerViewport.getWidth() / 6, mDefaultLayerViewport.getHeight() / 8,
                "BackButtonBabette", "Backinvert", this);
    }


    //Method to return the user to the menu screen once back button is pressed
    // Stephen Geddis 40204021
    private void btnBackToMenuPressed(ElapsedTime elapsedTime){

        btnBackToMenu.update(elapsedTime);
        if (btnBackToMenu.isPushTriggered()) {
            mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
        }

    }


    //Method to create a start slot button
    // Stephen Geddis 40204021
    private void createStartSlotsButton() {
        btnStartSlots = new PushButton(mDefaultLayerViewport.x + 190, mDefaultLayerViewport.y +90,
                mDefaultLayerViewport.getWidth() / 12, mDefaultLayerViewport.getHeight() / 8,
                "BtnPlaySlot", "BtnPlaySlot", this);
    }


    //Method used in order to construct the slot bitmaps using a JSON file based on Phil's tutorial
    //Stephen Geddis 40210068
    private void constructSlotGameObjects(String slotBitmapsToConstructJSON, List<GameObject> gameObjectsSlot){

        String loadedJSONForSlotGameObjects;

        try{
            loadedJSONForSlotGameObjects = mGame.getFileIO().loadJSON(slotBitmapsToConstructJSON);

        }catch(IOException e){
            throw new RuntimeException("SlotMachine screen Cannot Load JSON [" + slotBitmapsToConstructJSON+ "] to construct slot game  objects" );
        }

        try{
            JSONObject jsonObjectBitmapSettings = new JSONObject(loadedJSONForSlotGameObjects);
            JSONArray jsonArrayBitmapSlots = jsonObjectBitmapSettings.getJSONArray("slotBitmaps");


            //Sets the size of each slot game object
            float gameLayerWidth = mDefaultLayerViewport.getWidth();
            float gameLayerHeight = mDefaultLayerViewport.getHeight();

            //Sets the x and y position of the slot game object on the screen
            float gameLayerX = mDefaultLayerViewport.x;
            float gameLayerY = mDefaultLayerViewport.y;

            //Creates the slot game objects based on the number of slots defined in the JSON (jsonArrayBitmapSlots.length)
            //Taking their values for the x and y position as wells as the height and width
            //And using these values to create a game object for the slot
            for(int i = 0; i < jsonArrayBitmapSlots.length();i++){
                float x = (float)jsonArrayBitmapSlots.getJSONObject(i).getDouble("x");
                float y = (float)jsonArrayBitmapSlots.getJSONObject(i).getDouble("y");
                float width = (float)jsonArrayBitmapSlots.getJSONObject(i).getDouble("width");
                float height = (float)jsonArrayBitmapSlots.getJSONObject(i).getDouble("height");


                GameObject gameObjectSlot = new GameObject(
                        gameLayerX+x, gameLayerY+y,
                        gameLayerWidth/width, gameLayerHeight/height,
                        bitmapSlotMachineStart, this);

                //Adds the newly created object to the slot machine slots object list
                listGameObjectsSlotMachineSlots.add(gameObjectSlot);

            }

        }catch(JSONException | IllegalArgumentException e){
            throw new RuntimeException("Slot Machine.constructSlotGameObjects: JSON Parsing Error [" + e.getMessage()+"]");
        }



    }


    //Method used to change the slot machine's slot when the slot
    //machine's button is pressed
    //Stephen Geddis 40210068
    private void runSlotChange(){

        int intToDecideSlot;
        Random rand = new Random();
        int endOfLoopCheck=0;

        //Used to cycle through the  slot game objects and set a random number value
        //to determine which slot image will appear
        for(GameObject slot:listGameObjectsSlotMachineSlots){
            //Used to determine the slot image for the first two slots using built in random
            intToDecideSlot =rand.nextInt(2) + 1;

            //Once the last slot has been reached in the for each loop
            //The final slot's random image is set using the middle square method
            //the reason why not all are set using middle square methdd as its too difficult to win
            if (endOfLoopCheck++ == listGameObjectsSlotMachineSlots.size()-1) {
                intToDecideSlot = getRandomNumberUsingMiddleSquare();
            }

            //Once the number has been generate for the slot
            //the image is changed based to either a queen's logo for win or an x for loose
            switch(intToDecideSlot) {
                case 1:
                    slot.setBitmap(bitmapSlotMachineQueens);
                    break;
                case 2: slot.setBitmap(bitmapSlotMachineFail);
                    break;
                default: break;
            }

        }

    }

    //Method used to check to see it the user has won
    //Stephen Geddis 40210068
    private void checkForWin() {

        boolean checkWin = false;

        //For each go through the slot game object's list and check to see if the slots are all the queens logo
        //in order to win
        for(int i = 0; i < listGameObjectsSlotMachineSlots.size();i++){

            if(listGameObjectsSlotMachineSlots.get(i).getBitmap()== bitmapSlotMachineQueens){
                checkWin= true;
                continue;
                }else{
                checkWin =false;
                break;
            }

        }

         if(checkWin){
            //turns the paint text to green before displaying win
           turnTextGreen();
            stringToDisplayWinOrLose = "WIN";
            playSlotMachineWin();
            increaseCoins();

        } else {
           playSlotMachineLose();
           //turns the paint text to red before displaying loose
            turnTextRed();
            stringToDisplayWinOrLose = "LOSE";
        }
    }

    //Methods to play sounds for win or lose
    //Stephen Geddis 40210068
    private void playSlotMachineWin(){ slotMachineWinOrLoseAudio.play(getGame().getAssetManager().getSound("SlotMachineWin")); }
    private void playSlotMachineLose(){ slotMachineWinOrLoseAudio.play(getGame().getAssetManager().getSound("SlotMachineLose")); }

    //Method to turn text green for winning text
    //Stephen Geddis 40210068
    private void turnTextGreen(){
        intToDetermineTextPaintColour = INTTOTURNTEXTGREEN;
    }

    //Method to turn text red for loosing text
    //Stephen Geddis 40210068
    private void turnTextRed(){
        intToDetermineTextPaintColour = INTTOTURNTEXTRED;
    }

    //Methods to increase/decrease the coin amount
    //Stephen Geddis 40210068
    private void reduceCoins(){ slotmachineCoins.reduceCoins(); }
    private void increaseCoins(){ slotmachineCoins.increaseCoins(); }

    //Methods to inform the user that they have no coins left
    //Stephen Geddis 40210068
    private void noCoinsLeft(){
        turnTextRed();
        stringToDisplayWinOrLose ="No Coins Left";
    }

    //Code to generate random number using middle square method based upon youtube tutorial in javascript
    // https://www.youtube.com/watch?v=4sYawx70iP4&t=
    //Stephen Geddis 402101068
    private int getRandomNumberUsingMiddleSquare(){
        int digits = 4;
        double randomNumber = 0;


        String stringPaddedSeed = Integer.toString(seedForRandom*seedForRandom);

        //adds a 3 to the padded string if it is less than 8 in length
        while(stringPaddedSeed.length() < digits * 2){
            stringPaddedSeed = "3" + stringPaddedSeed;
        }

        //math.floor gives largest int that is <= argument
        double startOfNewSeed = Math.floor(digits/2);
        double endOfNewSeed = startOfNewSeed + digits;

        //creates new seed based on the padded string
        seedForRandom = Integer.parseInt(stringPaddedSeed.substring((int)startOfNewSeed,(int)endOfNewSeed));


        //divides by 1000 to create a random number that is a single digit
        randomNumber = seedForRandom /1000;

        //if the number isn't 1 or 2 regenerates the number
        if((randomNumber>2)||(randomNumber==0))return getRandomNumberUsingMiddleSquare();

        else return (int)randomNumber;

    }

}
