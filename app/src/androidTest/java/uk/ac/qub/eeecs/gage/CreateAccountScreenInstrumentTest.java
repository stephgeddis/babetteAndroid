package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.Babette.CreateAccountScreen;
import uk.ac.qub.eeecs.Babette.DemoGame;
import uk.ac.qub.eeecs.Babette.GameManager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class CreateAccountScreenInstrumentTest {

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
    }

    //Tests to ensure that the checkForOverlappingAvatars method is working.
    @Test
    public void CheckAvatarsAreOverlapping() {
        CreateAccountScreen demoScreen = new CreateAccountScreen(game, gameManager);

        GameObject avatarOneGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar1"), demoScreen);
        GameObject avatarTwoGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar2"), demoScreen);
        GameObject avatarThreeGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar3"), demoScreen);
        GameObject avatarFourGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar4"), demoScreen);
        GameObject avatarFiveGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar5"), demoScreen);

        ArrayList<GameObject> avatars = new ArrayList<>();

        avatars.add(avatarOneGO);
        avatars.add(avatarTwoGO);
        avatars.add(avatarThreeGO);
        avatars.add(avatarFourGO);
        avatars.add(avatarFiveGO);
        assertTrue(CreateAccountScreen.checkForOverlappingAvatars(avatarOneGO, avatars));
    }

    @Test
    public void CheckAvatarsAreNotOverlapping() {
        CreateAccountScreen demoScreen = new CreateAccountScreen(game, gameManager);

        GameObject avatarOneGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar1"), demoScreen);
        GameObject avatarTwoGO = new GameObject(
                1000, 1000, 100, 100,
                game.getAssetManager().getBitmap("Avatar2"), demoScreen);
        GameObject avatarThreeGO = new GameObject(
                1000, 1000, 100, 100,
                game.getAssetManager().getBitmap("Avatar3"), demoScreen);
        GameObject avatarFourGO = new GameObject(
                1000, 1000, 100, 100,
                game.getAssetManager().getBitmap("Avatar4"), demoScreen);
        GameObject avatarFiveGO = new GameObject(
                1000, 1000, 100, 100,
                game.getAssetManager().getBitmap("Avatar5"), demoScreen);

        ArrayList<GameObject> avatars = new ArrayList<>();

        avatars.add(avatarOneGO);
        avatars.add(avatarTwoGO);
        avatars.add(avatarThreeGO);
        avatars.add(avatarFourGO);
        avatars.add(avatarFiveGO);
        assertFalse(CreateAccountScreen.checkForOverlappingAvatars(avatarOneGO, avatars));
    }

    //Test that checks Avatars 2-5 are hidden by checking if any of them overlap with Avatar 1.
    @Test
    public void CheckAvatarsTwoToFiveAreMovedOffScreen() {
        CreateAccountScreen demoScreen = new CreateAccountScreen(game, gameManager);

        GameObject avatarOneGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar1"), demoScreen);
        GameObject avatarTwoGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar2"), demoScreen);
        GameObject avatarThreeGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar3"), demoScreen);
        GameObject avatarFourGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar4"), demoScreen);
        GameObject avatarFiveGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar5"), demoScreen);

        ArrayList<GameObject> avatars = new ArrayList<>();

        avatars.add(avatarTwoGO);
        avatars.add(avatarThreeGO);
        avatars.add(avatarFourGO);
        avatars.add(avatarFiveGO);

        CreateAccountScreen.hideAvatarsOffScreen(avatars);

        avatars.add(avatarOneGO);

        assertTrue(!CreateAccountScreen.checkForOverlappingAvatars(avatarOneGO, avatars));
    }

    //Tests to ensure that the nextAvatarInSequence method is working.
    @Test
    public void CheckNextAvatarIsShownOneToTwo() {
        CreateAccountScreen demoScreen = new CreateAccountScreen(game,gameManager);

        GameObject avatarOneGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar1"), demoScreen);
        GameObject avatarTwoGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar2"), demoScreen);
        GameObject avatarThreeGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar3"), demoScreen);
        GameObject avatarFourGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar4"), demoScreen);
        GameObject avatarFiveGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar5"), demoScreen);

        int avCounter = 1;
        ArrayList<GameObject> avatarList = new ArrayList<>();
        avatarList.add(avatarOneGO);
        avatarList.add(avatarTwoGO);
        avatarList.add(avatarThreeGO);
        avatarList.add(avatarFourGO);
        avatarList.add(avatarFiveGO);

        ArrayList<GameObject> avatarsToHide = new ArrayList<>();
        avatarsToHide.add(avatarTwoGO);
        avatarsToHide.add(avatarThreeGO);
        avatarsToHide.add(avatarFourGO);
        avatarsToHide.add(avatarFiveGO);
        CreateAccountScreen.hideAvatarsOffScreen(avatarsToHide);

        CreateAccountScreen.nextAvatarInSequence(avatarList, avCounter);

        assertTrue(!CreateAccountScreen.checkForOverlappingAvatars(avatarTwoGO, avatarList));
    }

    @Test
    public void CheckNextAvatarIsShownFiveToOne() {
        CreateAccountScreen demoScreen = new CreateAccountScreen(game,gameManager);

        GameObject avatarOneGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar1"), demoScreen);
        GameObject avatarTwoGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar2"), demoScreen);
        GameObject avatarThreeGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar3"), demoScreen);
        GameObject avatarFourGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar4"), demoScreen);
        GameObject avatarFiveGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar5"), demoScreen);

        int avCounter = 5;
        ArrayList<GameObject> avatarList = new ArrayList<>();
        avatarList.add(avatarOneGO);
        avatarList.add(avatarTwoGO);
        avatarList.add(avatarThreeGO);
        avatarList.add(avatarFourGO);
        avatarList.add(avatarFiveGO);

        ArrayList<GameObject> avatarsToHide = new ArrayList<>();
        avatarsToHide.add(avatarOneGO);
        avatarsToHide.add(avatarTwoGO);
        avatarsToHide.add(avatarThreeGO);
        avatarsToHide.add(avatarFourGO);
        CreateAccountScreen.hideAvatarsOffScreen(avatarsToHide);

        CreateAccountScreen.nextAvatarInSequence(avatarList, avCounter);

        assertTrue(!CreateAccountScreen.checkForOverlappingAvatars(avatarOneGO, avatarList));
    }

    //Tests to ensure that the previousAvatarInSequence method is working.
    @Test
    public void CheckPreviousAvatarIsShownFourToThree() {
        CreateAccountScreen demoScreen = new CreateAccountScreen(game, gameManager);

        GameObject avatarOneGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar1"), demoScreen);
        GameObject avatarTwoGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar2"), demoScreen);
        GameObject avatarThreeGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar3"), demoScreen);
        GameObject avatarFourGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar4"), demoScreen);
        GameObject avatarFiveGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar5"), demoScreen);

        int avCounter = 4;
        ArrayList<GameObject> avatarList = new ArrayList<>();
        avatarList.add(avatarOneGO);
        avatarList.add(avatarTwoGO);
        avatarList.add(avatarThreeGO);
        avatarList.add(avatarFourGO);
        avatarList.add(avatarFiveGO);

        ArrayList<GameObject> avatarsToHide = new ArrayList<>();
        avatarsToHide.add(avatarOneGO);
        avatarsToHide.add(avatarTwoGO);
        avatarsToHide.add(avatarThreeGO);
        avatarsToHide.add(avatarFiveGO);

        CreateAccountScreen.hideAvatarsOffScreen(avatarsToHide);

        CreateAccountScreen.previousAvatarInSequence(avatarList, avCounter);

        assertFalse(CreateAccountScreen.checkForOverlappingAvatars(avatarThreeGO, avatarList));
    }

    @Test
    public void CheckPreviousAvatarIsShownOneToFive() {
        CreateAccountScreen demoScreen = new CreateAccountScreen(game, gameManager);

        GameObject avatarOneGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar1"), demoScreen);
        GameObject avatarTwoGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar2"), demoScreen);
        GameObject avatarThreeGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar3"), demoScreen);
        GameObject avatarFourGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar4"), demoScreen);
        GameObject avatarFiveGO = new GameObject(
                100, 100, 100, 100,
                game.getAssetManager().getBitmap("Avatar5"), demoScreen);

        int avCounter = 1;
        ArrayList<GameObject> avatarList = new ArrayList<>();
        avatarList.add(avatarOneGO);
        avatarList.add(avatarTwoGO);
        avatarList.add(avatarThreeGO);
        avatarList.add(avatarFourGO);
        avatarList.add(avatarFiveGO);

        ArrayList<GameObject> avatarsToHide = new ArrayList<>();
        avatarsToHide.add(avatarTwoGO);
        avatarsToHide.add(avatarThreeGO);
        avatarsToHide.add(avatarFourGO);
        avatarsToHide.add(avatarFiveGO);

        CreateAccountScreen.hideAvatarsOffScreen(avatarsToHide);

        CreateAccountScreen.previousAvatarInSequence(avatarList, avCounter);

        assertFalse(CreateAccountScreen.checkForOverlappingAvatars(avatarFiveGO, avatarList));
    }
}
