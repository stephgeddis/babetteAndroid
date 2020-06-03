package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import uk.ac.qub.eeecs.Babette.BabetteCard;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.Babette.DemoGame;
import uk.ac.qub.eeecs.Babette.GameManager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class BabetteCardInstrumentTest {
    BabetteCard bCard1 = new BabetteCard("CyberSecurity", null);
    BabetteCard bCard2 = new BabetteCard("CyberSecurity", null);
    BabetteCard bCard3 = new BabetteCard("CyberSecurity", null);
    BabetteCard bCard4 = new BabetteCard("Development", null);
    BabetteCard bCard5 = new BabetteCard("Development", null);
    BabetteCard bCard6 = new BabetteCard("Programming", null);
    BabetteCard bCard7 = new BabetteCard("WildCard", null);

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
        gameManager = gameManager;
        game.mScreenManager = new ScreenManager(game);
        assetManager.loadAssets("txt/assets/BabetteAssets.JSON");
    }

    //Jake Smyth 40204021
    //Test to ensure that the givePlayerRandomBabetteCard method is working.
    @Test
    public void CheckGivePlayerRandomBabetteCard() {
        ArrayList<BabetteCard> bCardList = new ArrayList<>();
        bCardList.add(bCard1);
        bCardList.add(bCard2);
        bCardList.add(bCard4);
        bCardList.add(bCard7);

        ArrayList<BabetteCard> newCardList = bCardList;

        newCardList.add(BabetteCard.givePlayerRandomBabetteCard(game, bCardList));
        boolean wildCardFound = false;
        boolean secondWildCardFound = false;
        for(BabetteCard card: newCardList) {
            if (card.getbCardTerritory() == "WildCard" && wildCardFound == true)
                secondWildCardFound = true;
            if (card.getbCardTerritory() == "WildCard" && wildCardFound == false)
                wildCardFound = true;
        }
        assertTrue(newCardList.size() == 5 || !secondWildCardFound);
    }
}
