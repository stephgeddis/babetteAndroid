package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import uk.ac.qub.eeecs.Babette.DemoGame;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.SplashScreen;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameObject;

import static junit.framework.Assert.assertEquals;

/**
 * This class is used to create a slot machine game screen
 * as a bonus feature in order to comply with the
 * accompanying module (CSC 2045) for a Queen's themed feature
 *
 * @author  Stephen Geddis 40210068
 * @version 6.0
 *
 */
@RunWith(AndroidJUnit4.class)
public class SplashScreenInstrumentTest {

    private DemoGame game;
    private GameManager gameManager;
    private Context context;

    private GameObject gameObjectAceLogo, gameObjectCard,gameObjectAce;


    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setUpGameManager();
    }

    private void setUpGameManager() {
        game = new DemoGame();
        game.mFileIO = new FileIO(context);
        AssetManager assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;
        game.mScreenManager = new ScreenManager(game);
        gameManager = gameManager;
        loadAndAddBitmapToAssetManager();
        game.mAudioManager = new AudioManager(game);

    }

    private void loadAndAddBitmapToAssetManager() {
        game.mAssetManager.loadAndAddBitmap("LoadingAce", "img/LoadingAce.png");
        game.mAssetManager.loadAndAddBitmap("LoadingCard", "img/LoadingCard.png");
        game.mAssetManager.loadAndAddBitmap("LoadingLogo", "img/LoadingLogo.png");
    }

    //Test to see if the Ace logo can be accessed
    //Stephen Geddis 40210068
    @Test
    public void testToAccessAceLogoBitmap() {

        SplashScreen demoScreen = new SplashScreen(game,gameManager);

        gameObjectAceLogo = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("LoadingLogo"), demoScreen);
        gameObjectAceLogo.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("LoadingLogo"));

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("LoadingLogo");

        assertEquals(bitmap,gameObjectAceLogo.getBitmap());

    }


    //Test to see if the ace bitmap can be accessed
    //Stephen Geddis 40210068
    @Test
    public void testToAccessAceBitmap() {

        SplashScreen demoScreen = new SplashScreen(game,gameManager);

        gameObjectAce = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("LoadingAce"), demoScreen);
        gameObjectAce.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("LoadingAce"));

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("LoadingAce");

        assertEquals(bitmap,gameObjectAce.getBitmap());

    }


    //Test to see if the card bitmap can be accessed
    //Stephen Geddis 40210068
    @Test
    public void testToAccessCardBitmap() {

        SplashScreen demoScreen = new SplashScreen(game,gameManager);

        gameObjectCard = new GameObject(1,1,0 , 0,game.mAssetManager.getBitmap("LoadingCard"), demoScreen);
        gameObjectCard.setBitmap(demoScreen.getGame().getAssetManager().getBitmap("LoadingCard"));

        Bitmap bitmap = demoScreen.getGame().getAssetManager().getBitmap("LoadingCard");

        assertEquals(bitmap,gameObjectCard.getBitmap());

    }
}
