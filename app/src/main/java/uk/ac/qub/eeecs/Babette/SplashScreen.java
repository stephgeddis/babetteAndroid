package uk.ac.qub.eeecs.Babette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;


/**
 * This screen is used in order to provide
 * a splash screen for the user.
 *
 * @author  Stephen Geddis 40210068
 * @version 6.0
 *
 */
public class SplashScreen  extends GameScreen {

    //Used to time display of images on screen
    private int secondsPassedCounter = 0;

    private GameObject gameObjectLogo,gameObjectCard,gameObjectCard2,gameObjectAce;
    private Bitmap bitmapLogo,bitmapAce,bitmapCard;

    private final Input touches = mGame.getInput();
    private List<TouchEvent> checkTouch ;

    //Creates the timer for the splash screen
    private final Timer splashCount = new Timer();
    private final TimerTask nextScreen = new TimerTask() {

        @Override
        public void run() {
            secondsPassedCounter++;
        }
    };

    private  AssetManager assetManager = mGame.getAssetManager();

    private Paint paintVersionText = new Paint();
    private GameManager gameManager;

    public SplashScreen(Game game, GameManager gm) {
        super("SplashScreen", game);
        gameManager = gm;


        //used to set the delay of image display to a second
        splashCount.scheduleAtFixedRate(nextScreen,1000,1000);

       loadAndAddBitmapToAssetManager();
       defineBitmaps();

        gameObjectLogo = new GameObject(game.getScreenWidth() * 0.5f, game.getScreenHeight()*0.38f, bitmapLogo, this);
        gameObjectAce = new GameObject(game.getScreenWidth() * 0.7f, game.getScreenHeight()*0.75f, bitmapAce, this);
        gameObjectCard = new GameObject(game.getScreenWidth() *0.34f, game.getScreenHeight()*0.75f, bitmapCard, this);
        gameObjectCard2 = new GameObject(game.getScreenWidth() *0.53f, game.getScreenHeight()*0.75f, bitmapCard, this);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        //Checks for touches allowing for skip to next screen instead of waiting for splash timer to finish
        checkTouch = touches.getTouchEvents();
        if(checkTouch.size() > 0)
            openMenuScreen();

        //Checks to see if 5 seconds has passed before opening the menu screen
        if(secondsPassedCounter == 5)
            openMenuScreen();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        gameObjectLogo.draw(elapsedTime, graphics2D);

        paintVersionText.setTypeface(Typeface.MONOSPACE);
        paintVersionText.setTextSize(25.0f);
        paintVersionText.setTextAlign(Paint.Align.CENTER);


        graphics2D.drawText("Version 1.6 ", mDefaultLayerViewport.x +440 , mDefaultLayerViewport.y+550, paintVersionText);

        //Displays the various images used to represent the loading bar
        // after a second interval has passed
        //Stephen Geddis 4020068
        if (secondsPassedCounter>1)gameObjectCard.draw(elapsedTime, graphics2D);
        if (secondsPassedCounter>2)gameObjectCard2.draw(elapsedTime, graphics2D);
        if (secondsPassedCounter>3)gameObjectAce.draw(elapsedTime, graphics2D);
    }

    //Allows the StartScreen for the game to be called once time has passed or touch occurs
    //Stephen Geddis 40210068
    public void openMenuScreen(){
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(new StartScreen(mGame, gameManager));
    }

    //Adds and loads the bitmap image file to the asset manager
    //Stephen Geddis 40210068
    public void loadAndAddBitmapToAssetManager(){
        assetManager.loadAndAddBitmap("LoadingAce", "img/LoadingAce.png");
        assetManager.loadAndAddBitmap("LoadingCard", "img/LoadingCard.png");
        assetManager.loadAndAddBitmap("LoadingLogo", "img/LoadingLogo.png");
    }

    //Define the properties of the bitmap objects
    //Stephen Geddis 40210068
    public void defineBitmaps(){
        bitmapLogo = Bitmap.createScaledBitmap(assetManager.getBitmap("LoadingLogo"), 600,
                600, false);
        bitmapAce = Bitmap.createScaledBitmap(assetManager.getBitmap("LoadingAce"), 150,
                150, false);
        bitmapCard = Bitmap.createScaledBitmap(assetManager.getBitmap("LoadingCard"), 150,
                150, false);
    }
}