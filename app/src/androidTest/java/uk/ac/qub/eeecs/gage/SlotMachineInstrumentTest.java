package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.Babette.DemoGame;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.SlotMachine;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
/**
 * This class has been created in order to test
 * the slot machine's functionality
 *
 * @author  Stephen Geddis 40210068
 * @version 6.0
 *
 */
@RunWith(AndroidJUnit4.class)
public class SlotMachineInstrumentTest {

    private DemoGame game;
    private GameManager gameManager;
    private Context context;
    private GameObject gameObjectToTestSlotMachine;
    private GameObject gameObjectToTestSlotMachineSlot;
    private GameObject gameObjectToTestSlotMachineSlot2;
    private GameObject gameObjectToTestSlotMachineSlot3;
    private int seedForRandom = 1234;


    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
        setUpGameManager();
    }

    private void setUpGameManager(){
        game = new DemoGame();
        game.mFileIO = new FileIO(context);
        AssetManager assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;
        game.mScreenManager = new ScreenManager(game);
        gameManager = gameManager;
        loadAndAddBitmapToAssetManager();
        game.mAudioManager = new AudioManager(game);
    }


    //Adds and loads the bitmap image file to the asset manager
    public void loadAndAddBitmapToAssetManager(){

        game.mAssetManager.loadAndAddBitmap("SpinningWheelBeforeActive", "img/SpinningWheelBeforeActive.png");
        game.mAssetManager.loadAndAddBitmap("BabetteBgScreen", "img/BabetteBgScreen.png");
        game.mAssetManager.loadAndAddBitmap("BackButtonBabette", "img/BackButtonBabette.png");
        game.mAssetManager.loadAndAddBitmap("Backinvert", "img/Backinvert.png");
        game.mAssetManager.loadAndAddBitmap("SlotMachine", "img/SlotMachineTemplate.png");
        game.mAssetManager.loadAndAddBitmap("SlotMachineStart", "img/SlotMachineStart.png");
        game.mAssetManager.loadAndAddBitmap("SlotMachineFail", "img/SlotMachineFail.png");
        game.mAssetManager.loadAndAddBitmap("SlotMachineQueens", "img/SlotMachineQueensLogo.png");


    }


    //Test to see if Slot Machine Image Is Accessible
    //40210068 Stephen Geddis
    @Test
    public void testToAccessSlotMachineBitmap(){

        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        gameObjectToTestSlotMachine = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachine"), demoScreen);
        gameObjectToTestSlotMachine.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SlotMachine"));
        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SlotMachine");

       assertEquals(bitmap,gameObjectToTestSlotMachine.getBitmap());

    }

    //Test to see if Slot Machine Start Image Is Accessible
    //40210068 Stephen Geddis
    @Test
    public void testToAccessSlotStartBitmap(){

        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineStart"), demoScreen);

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineStart");

        assertEquals(bitmap,gameObjectToTestSlotMachineSlot.getBitmap());

    }

    //Test to see if Slot Machine Fail Image Is Accessible
    //40210068 Stephen Geddis
    @Test
    public void testToAccessSlotFailBitmap(){

        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineFail"), demoScreen);

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineFail");

        assertEquals(bitmap,gameObjectToTestSlotMachineSlot.getBitmap());

    }

    //Test to see if Slot Machine Queens Image Is Accessible
    //40210068 Stephen Geddis
    @Test
    public void testToAccessSlotQueensBitmap(){

        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens");

        assertEquals(bitmap,gameObjectToTestSlotMachineSlot.getBitmap());

    }


    //Test to see if slot image is set to fail if random number is 2
    //40210068 Stephen Geddis
    @Test
    public void testToSeeIfSlotImageChangesToFailIfRandomNumberIsTwo(){

        int slotRandomNumber =2;
        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineStart"), demoScreen);

        Bitmap bitmapToCompareWithResult = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineFail");


        switch(slotRandomNumber) {
            case 1:
                gameObjectToTestSlotMachineSlot.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens"));
                break;
            case 2:
                gameObjectToTestSlotMachineSlot.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SlotMachineFail"));
                break;
            default: break;
        }



        assertEquals(bitmapToCompareWithResult,gameObjectToTestSlotMachineSlot.getBitmap());

    }

    //Test to see if slot image is set to Queens logo if random number is 1
    //40210068 Stephen Geddis
    @Test
    public void testToSeeIfSlotImageChangesToQueensLogoIfRandomNumberIsOne(){

        int slotRandomNumber =1;
        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineStart"), demoScreen);

        Bitmap bitmapToCompareWithResult = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens");


        switch(slotRandomNumber) {
            case 1:
                gameObjectToTestSlotMachineSlot.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens"));
                break;
            case 2:
                gameObjectToTestSlotMachineSlot.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SlotMachineFail"));
                break;
            default: break;
        }

        assertEquals(bitmapToCompareWithResult,gameObjectToTestSlotMachineSlot.getBitmap());

    }

    //Test to see if Slot Machine message displays win
    //40210068 Stephen Geddis
    @Test
    public void testToCheckWinMessageForWin() {
        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        String message;

        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);
        gameObjectToTestSlotMachineSlot2 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);
        gameObjectToTestSlotMachineSlot3 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);

        Bitmap bitmapForWin = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens");

        if(((gameObjectToTestSlotMachineSlot.getBitmap()== bitmapForWin) &&(gameObjectToTestSlotMachineSlot2.getBitmap()== bitmapForWin)&& (gameObjectToTestSlotMachineSlot3.getBitmap()== bitmapForWin))){
            message = "Win";
        }else {
            message = "lose";
        }

        assertEquals("Win",message);
    }


    //Test to see if Slot Machine message for winning is set to display lose
    //40210068 Stephen Geddis
    @Test
    public void testToCheckWinMessageForLost() {
        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        String message;

        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);
        gameObjectToTestSlotMachineSlot2 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineFail"), demoScreen);
        gameObjectToTestSlotMachineSlot3 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);

        Bitmap bitmapForWin = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens");

        if(((gameObjectToTestSlotMachineSlot.getBitmap()== bitmapForWin) &&(gameObjectToTestSlotMachineSlot2.getBitmap()== bitmapForWin)&& (gameObjectToTestSlotMachineSlot3.getBitmap()== bitmapForWin))){
            message = "Win";
        }else {
            message = "Lose";
        }

        assertEquals("Lose",message);
    }

    //Test to see if Slot Machine coins are reduced when lost
    //40210068 Stephen Geddis
    @Test
    public void testToCheckIfCoinsReduceWhenLose() {
        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        int coins =2;

        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);
        gameObjectToTestSlotMachineSlot2 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineFail"), demoScreen);
        gameObjectToTestSlotMachineSlot3 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);

        Bitmap bitmapForWin = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens");

        if(((gameObjectToTestSlotMachineSlot.getBitmap()== bitmapForWin) &&(gameObjectToTestSlotMachineSlot2.getBitmap()== bitmapForWin)&& (gameObjectToTestSlotMachineSlot3.getBitmap()== bitmapForWin))){
            coins = increaseCoins(coins);
        }else {
            coins = reduceCoins(coins);
        }

        Assert.assertTrue(coins < 2);
    }


    //Test to see if Slot Machine  coins are increased if won
    //40210068 Stephen Geddis
    @Test
    public void testToCheckIfCoinsIncreaseWhenWin() {
        SlotMachine demoScreen = new SlotMachine(game,gameManager);
        int coins =2;

        gameObjectToTestSlotMachineSlot = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);
        gameObjectToTestSlotMachineSlot2 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);
        gameObjectToTestSlotMachineSlot3 = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SlotMachineQueens"), demoScreen);

        Bitmap bitmapForWin = demoScreen.getGame().getAssetManager().getBitmap("SlotMachineQueens");

        if(((gameObjectToTestSlotMachineSlot.getBitmap()== bitmapForWin) &&(gameObjectToTestSlotMachineSlot2.getBitmap()== bitmapForWin)&& (gameObjectToTestSlotMachineSlot3.getBitmap()== bitmapForWin))){
            coins = increaseCoins(coins);
        }else {
            coins = reduceCoins(coins);
        }

        Assert.assertTrue(coins > 2);
    }


    private int reduceCoins(int coins){
        return coins-=1;
    }
    private int increaseCoins(int coins){
        return coins+=1;
    }



    //Test to see  if seed changed to a new value after a random number is generated
    //40210068 Stephen Geddis
    @Test
    public void testToSeeIfSeedForRandomNumberUpdatesWhenANumberIsGenerated(){


        int seedbefore = seedForRandom;
        int number = getRandomNumberUsingMiddleSquare();


        //Seed should change from the original value
        Assert.assertTrue(seedbefore !=seedForRandom);

    }


    //Test to see if middle square method generates a number between 1 and 2
    //40210068 Stephen Geddis
    @Test
    public void testToSeeIfRandomNumberGeneratedUsingMiddleSquareIsBetween1and2(){

       int number = getRandomNumberUsingMiddleSquare();

       //number should be either 1 or 2 when generated
        Assert.assertTrue((number < 2) && !(number==0));

    }


    //Test to see if middle square method generates a number between 1 and 2
    //40210068 Stephen Geddis
    @Test
    public void testToSeeIfRandomNumberGeneratedUsingMiddleSquareIsNotGreaterthan2(){

        int number = getRandomNumberUsingMiddleSquare();

        //number should be either 1 or 2 when generated
        Assert.assertTrue(number <2);

    }


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
