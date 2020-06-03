package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
import uk.ac.qub.eeecs.Babette.PlayerTurnOrderScreen;
import uk.ac.qub.eeecs.Babette.User;


import static org.junit.Assert.assertEquals;
/**
 * This class has been created in order to test
 * the player turn order screen functionality
 *
 * @author  Stephen Geddis 40210068
 * @version 6.0
 *
 */
@RunWith(AndroidJUnit4.class)
public class PlayerTurnOrderInstrumentTest {

    private DemoGame game;
    private GameManager gameManager;
    private Context context;
    private GameObject gameObjectDice;


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
        gameManager = new GameManager();
        addAssetsToAssetManager();
        game.mAudioManager = new AudioManager(game);
        gameManager.setCurrentUser(new User("STEPHEN","green","Avatar1","SUPERSECUREPASSWORD"));
    }

    private void addAssetsToAssetManager(){


        game.mAssetManager.loadAndAddBitmap("WorldMapBackground", "img/WorldMapColouredBackgroundOnly.png");


        game.mAssetManager.loadAndAddBitmap("Avatar1", "img/av1.png");
        game.mAssetManager.loadAndAddBitmap("Avatar2", "img/av2.png");
        game.mAssetManager.loadAndAddBitmap("Avatar3", "img/av3.png");
        game.mAssetManager.loadAndAddBitmap("Avatar4", "img/av4.png");

        game.mAssetManager.loadAndAddBitmap("BabetteBgScreen", "img/BabetteBgScreen.png");
        game.mAssetManager.loadAndAddBitmap("PLAY", "img/PLAY.png");
        game.mAssetManager.loadAndAddBitmap("PLAYinvert", "img/PLAYinvert.png");

        game.mAssetManager.loadAndAddBitmap("SpinningWheelBeforeActive", "img/SpinningWheelBeforeActive.png");
        game.mAssetManager.loadAndAddBitmap("SpinningWheel1", "img/SpinningWheel1.png");
        game.mAssetManager.loadAndAddBitmap("SpinningWheel2", "img/SpinningWheel2.png");
        game.mAssetManager.loadAndAddBitmap("SpinningWheel3", "img/SpinningWheel3.png");
        game.mAssetManager.loadAndAddBitmap("SpinningWheel4", "img/SpinningWheel4.png");
        game.mAssetManager.loadAndAddBitmap("SpinningWheel5", "img/SpinningWheel5.png");
        game.mAssetManager.loadAndAddBitmap("SpinningWheel6", "img/SpinningWheel6.png");
        game.mAssetManager.loadAndAddBitmap("InitalInvisible", "img/InitalInvisible.png");

    }
    //Tests to see if SpinningWheel Images Are Accessible
    //40210068 Stephen Geddis
    @Test
    public void testToAccessSpinngWheel1Image(){

        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);
        gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1"));
        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1");
        assertEquals(bitmap,gameObjectDice.getBitmap());

    }

    @Test
    public void testToAccessSpinngWheel2Image(){
        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);
        gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2"));
        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2");
        assertEquals(bitmap,gameObjectDice.getBitmap());

    }

    @Test
    public void testToAccessSpinngWheel3Image(){
        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);
        gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3"));
        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3");
        assertEquals(bitmap,gameObjectDice.getBitmap());

    }

    @Test
    public void testToAccessSpinngWheel4Image(){
        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);
        gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4"));
        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4");
        assertEquals(bitmap,gameObjectDice.getBitmap());

    }

    @Test
    public void testToAccessSpinngWheel5Image(){
        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);
        gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5"));
        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5");
        assertEquals(bitmap,gameObjectDice.getBitmap());

    }
    @Test
    public void testToAccessSpinngWheel6Image(){
        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);
        gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6"));
        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6");
        assertEquals(bitmap,gameObjectDice.getBitmap());

    }

    //Methods to test if random number displays correct dice panel
    // Stephen Geddis 40204021
    @Test
    public void testToSeeIfNumberOneDisplaysCorrectDicePanel(){

        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);

        int userPlayerDiceNumber = 1;

        switch (userPlayerDiceNumber){
            case 1:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1"));
                break;
            case 2:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2"));
                break;
            case 3:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3"));
                break;
            case 4:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4"));
                break;
            case 5:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5"));
                break;
            case 6:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6"));
                break;
        }

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1");
        assertEquals(bitmap,gameObjectDice.getBitmap());
    }

    @Test
    public void testToSeeIfNumberTwoDisplaysCorrectDicePanel(){

        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);

        int userPlayerDiceNumber = 2;

        switch (userPlayerDiceNumber){
            case 1:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1"));
                break;
            case 2:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2"));
                break;
            case 3:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3"));
                break;
            case 4:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4"));
                break;
            case 5:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5"));
                break;
            case 6:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6"));
                break;
        }

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2");
        assertEquals(bitmap,gameObjectDice.getBitmap());
    }

    @Test
    public void testToSeeIfNumberThreeDisplaysCorrectDicePanel(){

        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);

        int userPlayerDiceNumber = 3;

        switch (userPlayerDiceNumber){
            case 1:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1"));
                break;
            case 2:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2"));
                break;
            case 3:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3"));
                break;
            case 4:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4"));
                break;
            case 5:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5"));
                break;
            case 6:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6"));
                break;
        }

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3");
        assertEquals(bitmap,gameObjectDice.getBitmap());
    }

    @Test
    public void testToSeeIfNumberFourDisplaysCorrectDicePanel(){

        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);

        int userPlayerDiceNumber = 4;

        switch (userPlayerDiceNumber){
            case 1:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1"));
                break;
            case 2:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2"));
                break;
            case 3:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3"));
                break;
            case 4:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4"));
                break;
            case 5:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5"));
                break;
            case 6:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6"));
                break;
        }

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4");
        assertEquals(bitmap,gameObjectDice.getBitmap());
    }

    @Test
    public void testToSeeIfNumberFiveDisplaysCorrectDicePanel(){

        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);

        int userPlayerDiceNumber = 5;

        switch (userPlayerDiceNumber){
            case 1:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1"));
                break;
            case 2:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2"));
                break;
            case 3:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3"));
                break;
            case 4:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4"));
                break;
            case 5:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5"));
                break;
            case 6:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6"));
                break;
        }

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5");
        assertEquals(bitmap,gameObjectDice.getBitmap());
    }

    @Test
    public void testToSeeIfNumberSixDisplaysCorrectDicePanel(){

        PlayerTurnOrderScreen demoScreen = new PlayerTurnOrderScreen(game, gameManager);
        gameObjectDice = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("SpinningWheelBeforeActive"), demoScreen);

        int userPlayerDiceNumber = 6;

        switch (userPlayerDiceNumber){
            case 1:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel1"));
                break;
            case 2:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel2"));
                break;
            case 3:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel3"));
                break;
            case 4:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel4"));
                break;
            case 5:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel5"));
                break;
            case 6:
                gameObjectDice.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6"));
                break;
        }

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("SpinningWheel6");
        assertEquals(bitmap,gameObjectDice.getBitmap());
    }

}
