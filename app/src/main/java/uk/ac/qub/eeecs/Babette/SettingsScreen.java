package uk.ac.qub.eeecs.Babette;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;

/**
 *
 * This class is used to create a settings screen
 * where the user can edit sound level and counter colour
 *
 * @author  James Sims
 * @version 6.0
 *
 */

public class SettingsScreen extends GameScreen {
    private PushButton personAvatar;
    private PushButton BabetteLogo;
    private PushButton X;
    private PushButton leftArrow1;
    private PushButton leftArrow2;
    private PushButton leftArrow3;
    private PushButton rightArrow1;
    private PushButton rightArrow2;
    private PushButton rightArrow3;
    private GameObject gBabetteLogo;
    private Bitmap bitmapBackground;
    private GameManager gameManager;
    private GameObject redCounter;
    private GameObject blueCounter;
    private GameObject yellowCounter;
    private GameObject greenCounter;

    private int masterVolume;
    private int gameVolume;
    private int musicVolume;
    private Paint textPaint = new Paint();
    private Paint labelText = new Paint();
    int spacingX = (int)mDefaultLayerViewport.getWidth() / 5;
    int spacingY = (int)mDefaultLayerViewport.getHeight() / 3;

    private ArrayList<GameObject> counters = new ArrayList<>();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public SettingsScreen(Game game, GameManager gm) {
        super("BabetteMainMenu", game);
        gameManager = gm;
        masterVolume = 5;
        loadAssets();
        createButtons();
        createGameObjects(game);
        //ArrayList<GameObject> counters = new ArrayList<>();
        counters.add(redCounter);
        counters.add(blueCounter);
        counters.add(yellowCounter);
        counters.add(greenCounter);
        switch(gameManager.getCurrentUser().getColour()) {
            case "red": redCounter.position.add(-5000, +0); break;
            case "blue": blueCounter.position.add(-5000, +0); break;
            case "yellow": yellowCounter.position.add(-5000, +0);  break;
            case "green": greenCounter.position.add(-5000, +0); break;
            default: break;
        }
    }

// /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        leftArrow1.update(elapsedTime);
        rightArrow1.update(elapsedTime);
        leftArrow3.update(elapsedTime);
        rightArrow3.update(elapsedTime);
        redCounter.update(elapsedTime);
        blueCounter.update(elapsedTime);
        yellowCounter.update(elapsedTime);
        greenCounter.update(elapsedTime);

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            // Update each button and transition if needed
            BabetteLogo.update(elapsedTime);
            X.update(elapsedTime);
            if (BabetteLogo.isPushTriggered()) {
                mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
                gameManager.updateCurrentUser();
                gameManager.updateFile();
            }
            else if (X.isPushTriggered()) {
                mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
                gameManager.updateCurrentUser();
                gameManager.updateFile();
            }
            else if(leftArrow1.isPushed()){
                if(masterVolume >0)
                    masterVolume--;
            }
            else if(rightArrow1.isPushed()){
                if(masterVolume <10)
                masterVolume++;
            }
            else if(leftArrow3.isPushed()){
                previousCounter(counters);
            }
            else if(rightArrow3.isPushed()){
                nextCounter(counters);
            }
        }
    }

    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen and draw the buttons
        graphics2D.clear(Color.BLACK);
        gBabetteLogo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        BabetteLogo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        X.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        personAvatar.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        leftArrow1.draw(elapsedTime,graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        rightArrow1.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        leftArrow3.draw(elapsedTime,graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        rightArrow3.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(75.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.WHITE);
        graphics2D.drawText(Integer.toString(masterVolume), spacingX * 6.4f, spacingY * 4, textPaint);
        redCounter.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        blueCounter.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        yellowCounter.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        greenCounter.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        labelText.setTypeface(Typeface.MONOSPACE);
        labelText.setTextSize(25.0f);
        labelText.setTextAlign(Paint.Align.LEFT);
        labelText.setColor(Color.WHITE);
        graphics2D.drawText("Volume: (Out of 10)", spacingX * 4f, spacingY * 3.1f, labelText);
        graphics2D.drawText("Player's Counter Colour:", spacingX * 4f, spacingY * 5.3f, labelText);
    }

    public void loadAssets() {
        //Load in Bitmaps for accounts. Will later be changed to get bitmap for specific player
        mGame.getAssetManager().loadAssets("txt/assets/BabetteAssets.JSON");

        AssetManager assetManager = mGame.getAssetManager();                            //Might move to above line 42
        assetManager.loadAndAddBitmap("Play", "img/PLAYcurved.png");
        assetManager.loadAndAddBitmap("Avatar1", "img/av1.png");
        assetManager.loadAndAddBitmap("babetteLogoV2", "img/babetteLogoV2.png");
        assetManager.loadAndAddBitmap("X", "img/X.png");
        //InvertedButtons
        assetManager.loadAndAddBitmap("Xinvert", "img/Xinvert.png");
        assetManager.loadAndAddBitmap("CardDemoIconSelected", "img/CardDemoIconSelected.png");
        assetManager.loadAndAddBitmap("LeftArrow", "img/LeftArrow.png");
        assetManager.loadAndAddBitmap("RightArrow", "img/RightArrow.png");
        assetManager.loadAndAddBitmap("LeftArrowSelected", "img/LeftArrowSelected.png");
        assetManager.loadAndAddBitmap("RightArrowSelected", "img/RightArrowSelected.png");

        assetManager.loadAndAddBitmap("RedCounter", "img/RedCircle1.png");
        assetManager.loadAndAddBitmap("BlueCounter", "img/BlueCircle1.png");
        assetManager.loadAndAddBitmap("YellowCounter", "img/YellowCircle1.png");
        assetManager.loadAndAddBitmap("GreenCounter", "img/GreenCircle1.png");
    }

    public void createGameObjects(Game game) {
        AssetManager assetManager = mGame.getAssetManager();

        bitmapBackground = Bitmap.createScaledBitmap(assetManager.getBitmap("BabetteBgScreen"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        gBabetteLogo = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y,
                mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("BabetteBgScreen"), this);

        redCounter = new GameObject(
                spacingX * 2.5f + 5000, spacingY * 0.6f,
                50, 50,
                game.getAssetManager().getBitmap("RedCounter"), this);
        blueCounter = new GameObject(
                spacingX * 2.5f + 5000, spacingY * 0.6f,
                50, 50,
                game.getAssetManager().getBitmap("BlueCounter"), this);
        yellowCounter = new GameObject(
                spacingX * 2.5f + 5000, spacingY * 0.6f,
                50, 50,
                game.getAssetManager().getBitmap("YellowCounter"), this);
        greenCounter = new GameObject(
                spacingX * 2.5f + 5000, spacingY * 0.6f,
                50, 50,
                game.getAssetManager().getBitmap("GreenCounter"), this);
    }

    public void createButtons() {
        BabetteLogo = new PushButton(
                spacingX * 4.3f, spacingY * 2.8f, 250.0f, 120.0f,
                "babetteLogoV2", "babetteLogoV2", this);
        BabetteLogo.setPlaySounds(true, true);
        personAvatar = new PushButton(
                spacingX * 2.50f, spacingY * 2.3f, 80.0f, 80.0f,
                gameManager.getCurrentUser().getAvatar(), "CardDemoIconSelected", this);
        personAvatar.setPlaySounds(true, true);

        X = new PushButton(
                spacingX * 0.20f, spacingY * 2.8f, 30.0f, 30.0f,
                "X", "Xinvert", this);
        X.setPlaySounds(true, true);

        //40174176 - James Sims
        //Created Arrow buttons to be used in volume control
        leftArrow1 = new PushButton(
                spacingX * 1.60f, spacingY *1.5f, 30.0f, 30.0f,
                "LeftArrowSelected", "LeftArrow",this);

        rightArrow1 = new PushButton(
                spacingX * 3.40f, spacingY * 1.5f, 30.0f, 30.0f,
                "RightArrowSelected", "RightArrow",this);

        leftArrow3 = new PushButton(
                spacingX * 1.60f, spacingY * 0.6f, 30.0f, 30.0f,
                "LeftArrowSelected","LeftArrow", this);

        rightArrow3 = new PushButton(
                spacingX * 3.40f, spacingY * 0.6f, 30.0f, 30.0f,
                "RightArrowSelected","RightArrow", this);

    }

    //40174176 - James Sims
    //Method for cycling through possible colours for the user's counter by pressing the next button
    public void nextCounter(ArrayList<GameObject> counters ){
        String currentCounterColour = gameManager.getCurrentUser().getColour();
        switch(currentCounterColour){
            case "red":
                counters.get(0).position.add(+5000f, 0);
                counters.get(1).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("blue");
                break;
            case "blue":
                counters.get(1).position.add(+5000f, 0);
                counters.get(2).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("yellow");
                break;
            case "yellow":
                counters.get(2).position.add(+5000f, 0);
                counters.get(3).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("green");
                break;
            case "green":
                counters.get(3).position.add(+5000f, 0);
                counters.get(0).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("red");
                break;
            default: break;
        }

    }

    //40174176 - James Sims
    //Method for cycling through possible colours for the user's counter by pressing the back button
    public void previousCounter(ArrayList<GameObject> counters ){
        String currentCounterColour = gameManager.getCurrentUser().getColour();
        switch(currentCounterColour){
            case "red":
                counters.get(0).position.add(+5000f, 0);
                counters.get(3).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("green");
                break;
            case "blue":
                counters.get(1).position.add(+5000f, 0);
                counters.get(0).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("red");
                break;
            case "yellow":
                counters.get(2).position.add(+5000f, 0);
                counters.get(1).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("blue");
                break;
            case "green":
                counters.get(3).position.add(+5000f, 0);
                counters.get(2).position.add(-5000f, 0);
                gameManager.getCurrentUser().setColour("yellow");
                break;
            default: break;
        }

    }
}