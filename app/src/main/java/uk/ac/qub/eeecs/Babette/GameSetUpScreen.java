package uk.ac.qub.eeecs.Babette;

import android.graphics.Color;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.Game;

public class GameSetUpScreen extends GameScreen { // Created by 40212255: Adam Higgins

    private PushButton backBtn, left, right, startGame;
    private GameObject mBackground, map;
    public static int selectedMap;
    private AssetManager assetMan;
    private int viewWidth, viewHeight;
    private boolean mapChanged;
    private GameManager gameManager;

    public GameSetUpScreen(Game game, GameManager gm) {
        super("GameSetUpScreen", game);
        gameManager = gm;

        // Setting up the core aspects of the screen
        loadAssets();
        selectedMap = 0;

        viewWidth = (int)mDefaultLayerViewport.getWidth();
        viewHeight = (int)mDefaultLayerViewport.getHeight();

        setUpObjects();
        setUpButtons();
    }

    private void loadAssets() {
        assetMan = mGame.getAssetManager();
        assetMan.loadAssets("txt/assets/BabetteAssets.JSON");
        assetMan.loadAndAddBitmap("BackButtonBabette", "img/BackButtonBabette.png");
        assetMan.loadAndAddBitmap("BabetteBgScreen", "img/BabetteBgScreen.png");
        assetMan.loadAndAddBitmap("LeftBtn", "img/Left.png");
        assetMan.loadAndAddBitmap("RightBtn", "img/Right.png");
        assetMan.loadAndAddBitmap("StartButton", "img/StartButton.png");
        assetMan.loadAndAddBitmap("GameMap", "img/GameMap.png");
        assetMan.loadAndAddBitmap("WorldMap", "img/WorldMapColouredMapOnly.png");
        //inverted button
        assetMan.loadAndAddBitmap("StartButtoninvert", "img/StartButtoninvert.png");
        assetMan.loadAndAddBitmap("Backinvert", "img/Backinvert.png");
    }

    private void setUpObjects() {
        //Setting the background using the default layer viewport which is already a suitable 3:2 aspect ratio
        mBackground = new GameObject(viewWidth / 2, viewHeight /2,
                viewWidth, viewHeight,
                getGame().getAssetManager().getBitmap("BabetteBgScreen"), this);

        loadMap();
    }

    // Only one map available currently but this allows opportunity for expansion.
    private void loadMap() {
        switch(selectedMap) {
            case 0:
                map = new GameObject(viewWidth / 2, (viewHeight / 2) + (viewHeight / 10),
                        viewHeight / 2, viewHeight / 3,
                        getGame().getAssetManager().getBitmap("WorldMap"), this);
                break;
            default:
                map = new GameObject(viewWidth / 2, (viewHeight / 2) + (viewHeight / 10),
                        viewHeight / 2, viewHeight / 3,
                        getGame().getAssetManager().getBitmap("WorldMap"), this);
        }

        mapChanged = false;
    }

    private void setUpButtons() {

        // Setting up the placement and images for the buttons
        backBtn = new PushButton(viewHeight / 12, viewHeight / 12,
                viewHeight / 6, viewHeight / 6,
                "BackButtonBabette", "Backinvert", this);
        backBtn.setPlaySounds(true, true);


        left = new PushButton((viewWidth / 2) - (viewHeight / 3), (viewHeight / 2) + (viewHeight / 10),
                viewHeight / 8, viewHeight / 8,
                "LeftBtn", "LeftBtn", this);
        left.setPlaySounds(true, true);


        right = new PushButton((viewWidth / 2) + (viewHeight / 3), (viewHeight / 2) + (viewHeight / 10),
                viewHeight / 8, viewHeight / 8,
                "RightBtn", "RightBtn", this);
        right.setPlaySounds(true, true);


        startGame = new PushButton(viewWidth / 2, (viewHeight / 2) - (viewHeight / 4),
                viewHeight / 3, viewHeight / 5,
                "StartButton", "StartButtoninvert", this);
        startGame.setPlaySounds(true, true);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            backBtn.update(elapsedTime);
            left.update(elapsedTime);
            right.update(elapsedTime);
            startGame.update(elapsedTime);

            if (backBtn.isPushTriggered())
                mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
            else if (startGame.isPushTriggered())
                mGame.getScreenManager().addScreen(new PlayerTurnOrderScreen(mGame, gameManager));
            /*else if (left.isPushTriggered()) {
                selectedMap++;
                mapChanged = true;
            }
            else if (right.isPushTriggered()) {
                selectedMap--;
                mapChanged = true;
            }*/
        }

        if (mapChanged)
            loadMap();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.BLACK);

        mBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        map.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        backBtn.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        left.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        right.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        startGame.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}
