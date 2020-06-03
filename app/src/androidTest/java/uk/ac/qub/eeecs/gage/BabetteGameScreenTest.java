package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import uk.ac.qub.eeecs.Babette.BabetteGameScreen;
import uk.ac.qub.eeecs.Babette.DemoGame;
import uk.ac.qub.eeecs.Babette.GameManager;
import uk.ac.qub.eeecs.Babette.Player;
import uk.ac.qub.eeecs.Babette.User;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;

public class BabetteGameScreenTest {
    private DemoGame game;
    private Context context;
    private GameManager gameManager;

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
        AudioManager audioManager = new AudioManager(game);
        game.mAudioManager = audioManager;
        gameManager = new GameManager();
        addAssets();
        game.mScreenManager = new ScreenManager(game);
    }

    private void addAssets(){
        game.mAssetManager.loadAndAddBitmap("WorldMapBackground", "img/WorldMapColouredBackgroundOnly.png");
        game.mAssetManager.loadAndAddBitmap("WorldMapText", "img/WorldMapColouredTextOnly.png");
        game.mAssetManager.loadAndAddBitmap("WorldMap", "img/WorldMapColouredMapOnly.png");
        game.mAssetManager.loadAndAddBitmap("GameObjectiveBar", "img/GameObjectiveBar.png");

        game.mAssetManager.loadAndAddBitmap("BlueCircle1","img/BlueCircle1.png");
        game.mAssetManager.loadAndAddBitmap("GreenCircle1","img/GreenCircle1.png");
        game.mAssetManager.loadAndAddBitmap("RedCircle1","img/RedCircle1.png");
        game.mAssetManager.loadAndAddBitmap("YellowCircle1", "img/YellowCircle1.png");
        game.mAssetManager.loadAndAddBitmap("GreyCircle1", "img/GreyCircle1.png");

        game.mAssetManager.loadAndAddBitmap("Attack","img/Attack.png");
        game.mAssetManager.loadAndAddBitmap("Draft","img/Draft.png");
        game.mAssetManager.loadAndAddBitmap("Fortify","img/Fortify.png");
        game.mAssetManager.loadAndAddBitmap("FramedCircle","img/FramedCircle.png");

        game.mAssetManager.loadAndAddBitmap("AttackInvert","img/AttackInvert.png");
        game.mAssetManager.loadAndAddBitmap("DraftInvert","img/DraftInvert.png");
        game.mAssetManager.loadAndAddBitmap("FortifyInvert","img/FortifyInvert.png");

        game.mAssetManager.loadAndAddBitmap("Next", "img/Next.png");
        game.mAssetManager.loadAndAddBitmap("WildCard", "img/WildCard.png");

        //mGame.getAssetManager().loadAssets("txt/assets/BabetteAssets.JSON");
    }


    //Stephen Irons 40204625
    //Checking the nextTurnPhase method works as expected\
    @Test
    public  void testNextTurnPhase(){
        User testUser = new User("testUser", "Green","Avatar3","password1");
        ArrayList<Player> playerArrayList = new ArrayList<Player>();
        playerArrayList.add(testUser);
        gameManager.setStartingPlayers(playerArrayList);
        BabetteGameScreen testScreen = new BabetteGameScreen(game, gameManager);

        int turnPhaseBefore = testScreen.playerTurnPhase;
        testScreen.nextTurnPhase();
        int turnPhaseAfter = testScreen.playerTurnPhase;

        assertTrue(turnPhaseAfter == turnPhaseBefore+1);
    }
}
