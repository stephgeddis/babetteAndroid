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
 * This class is used to a main menu for
 * the user to decide which screen they
 * want to move to
 *
 * @author  Stephen Irons 40204625
 * @version 6.0
 *
 */

public class BabetteMainMenu extends GameScreen {
    private PushButton playGame;
    private PushButton settings;
    private PushButton logOut;
    private PushButton bonusGame;
    private PushButton Keyboard;

    private Paint textPaint = new Paint();

    private String usernameString = "hello";

    private GameObject gBabetteBackground;
    private GameObject gBabetteLogo;
    private GameObject avatarGO;
    private GameObject avatarPlayCounter;
    private Bitmap bitmapBackground;
    private GameManager gameManager;

    int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
    int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

    private ArrayList<User> userList;

    public BabetteMainMenu(Game game, GameManager gm) {
        super("BabetteMainMenu", game);
        gameManager = gm;

        loadAndAddAllAssets();
        createGameObjects(game);
        createButtons();

        try {
            userList = User.readFromFile(game.getActivity());
        }catch(Exception e){
            userList = new ArrayList<>();
        }
        loadUserDetails(gameManager.getCurrentUser());
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update.
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            playGame.update(elapsedTime);
            bonusGame.update(elapsedTime);
            settings.update(elapsedTime);
            logOut.update(elapsedTime);
            Keyboard.update(elapsedTime);

            if (playGame.isPushTriggered())
                mGame.getScreenManager().addScreen(new GameSetUpScreen(mGame, gameManager));
            else if (bonusGame.isPushTriggered())
                mGame.getScreenManager().addScreen(new SlotMachine(mGame, gameManager));
            else if (settings.isPushTriggered())
                mGame.getScreenManager().addScreen(new SettingsScreen(mGame, gameManager));
            else if (logOut.isPushTriggered()) {
                mGame.getScreenManager().addScreen(new LoginScreen(mGame, gameManager));
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        gBabetteBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        gBabetteLogo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        avatarGO.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        avatarPlayCounter.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        playGame.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        settings.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        bonusGame.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        logOut.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Keyboard.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(40.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.WHITE);
        graphics2D.drawText("Username:", spacingX * 2.375f, spacingY * 5.75f, textPaint);
        textPaint.setColor(Color.GREEN);
        graphics2D.drawText(usernameString, spacingX * 4.625f, spacingY * 5.75f, textPaint);
        textPaint.setColor(Color.WHITE);
        graphics2D.drawText("Play Colour:", spacingX * 1.625f, spacingY * 6.375f, textPaint);
    }

    // Loads and adds assets for future use on this screen.
    // Stephen Irons 40204625
    private void loadAndAddAllAssets() {
        mGame.getAssetManager().loadAssets("txt/assets/BabetteAssets.JSON");

        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Play", "img/PLAYcurved.png");
        assetManager.loadAndAddBitmap("Settings", "img/MainMenuSettingsButton.png");
        assetManager.loadAndAddBitmap("Avatar1", "img/av1.png");
        assetManager.loadAndAddBitmap("Avatar2", "img/av2.png");
        assetManager.loadAndAddBitmap("Avatar3", "img/av3.png");
        assetManager.loadAndAddBitmap("Avatar4", "img/av4.png");
        assetManager.loadAndAddBitmap("Avatar5", "img/av5.png");
        assetManager.loadAndAddBitmap("AvatarSelected", "img/AvatarSelected.png");
        assetManager.loadAndAddBitmap("btnBonus", "img/btnBonus.png");
        assetManager.loadAndAddBitmap("btnBonusInvert", "img/btnBonusInvert.png");
        assetManager.loadAndAddBitmap("YellowCircle", "img/YellowCircle.png");
        assetManager.loadAndAddBitmap("BlueCircle", "img/BlueCircle.png");
        assetManager.loadAndAddBitmap("GreenCircle", "img/GreenCircle.png");
        assetManager.loadAndAddBitmap("RedCircle", "img/RedCircle.png");
        assetManager.loadAndAddBitmap("babetteLogoV2", "img/babetteLogoV2.png");
        //inverted buttons
        assetManager.loadAndAddBitmap("PLAYcurvedinvert", "img/PLAYcurvedinvert.png");
        assetManager.loadAndAddBitmap("Settingsinvert", "img/Settingsinvert.png");
        assetManager.loadAndAddBitmap("SignOutinvert", "img/SignOutinvert.png");

        assetManager.loadAndAddBitmap("Keyboard", "img/Keyboard.png");
    }

    // Creates the game objects that will be displayed on screen.
    // Stephen Irons 40204625
    private void createGameObjects(Game game) {
        AssetManager assetManager = mGame.getAssetManager();
        bitmapBackground = Bitmap.createScaledBitmap(assetManager.getBitmap("BabetteBgScreen"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        gBabetteBackground = new GameObject(mDefaultLayerViewport.x, mDefaultLayerViewport.y, mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("BabetteBgScreen"), this);
        gBabetteLogo = new GameObject(spacingX * 2.5f, spacingY * 2.4f, 500f, 312.5f,
                game.getAssetManager().getBitmap("babetteLogoV2"), this);
        avatarGO = new GameObject(spacingX * 1.625f, spacingY * 1.375f, spacingX, spacingX,
                game.getAssetManager().getBitmap("AvatarSelected"), this);

        // Jake Smyth 40204021
        avatarPlayCounter = new GameObject(spacingX * 1.775f, spacingY * 0.5125f, spacingX / 6f, spacingX / 6f,
                game.getAssetManager().getBitmap("YellowCircle"), this);
    }

    // Creates the buttons that will be displayed on screen.
    // Stephen Irons 40204625
    private void createButtons() {
        // Create the trigger buttons
        playGame = new PushButton(spacingX * 3.5f, spacingY * 1.8f, 80.0f, 50.0f,
                "PLAYcurved", "PLAYcurvedinvert", this);
        settings = new PushButton(spacingX * 3.5f, spacingY * 1.3f, 80.0f, 50.0f,
                "Settings", "Settingsinvert", this);
        bonusGame = new PushButton(spacingX * 3.5f, spacingY * 0.8f, 80.0f, 47.0f,
                "btnBonus", "btnBonusInvert", this);
        logOut = new PushButton(spacingX * 3.5f, spacingY * 0.3f, 80.0f, 50.0f,
                "SignOut", "SignOutinvert", this);

        // Keyboard by Chris Jennings
        Keyboard = new PushButton(spacingX * 4.50f, spacingY * 0.3f, 80.0f, 60.0f,
                "Keyboard", "Keyboard", this);
    }

    // Loads the details of the current user to be displayed on the screen.
    // Jake Smyth 40204021
    public void loadUserDetails(User u) {
        usernameString = u.getUsername();
        switch(u.getAvatar()){
            case "Avatar1": avatarGO.setBitmap(this.getGame().getAssetManager().getBitmap("Avatar1")); break;
            case "Avatar2": avatarGO.setBitmap(this.getGame().getAssetManager().getBitmap("Avatar2")); break;
            case "Avatar3": avatarGO.setBitmap(this.getGame().getAssetManager().getBitmap("Avatar3")); break;
            case "Avatar4": avatarGO.setBitmap(this.getGame().getAssetManager().getBitmap("Avatar4")); break;
            case "Avatar5": avatarGO.setBitmap(this.getGame().getAssetManager().getBitmap("Avatar5")); break;
            default: break;
        }
        switch(u.getColour()){
            case "yellow": avatarPlayCounter.setBitmap(this.getGame().getAssetManager().getBitmap("YellowCircle")); break;
            case "blue": avatarPlayCounter.setBitmap(this.getGame().getAssetManager().getBitmap("BlueCircle")); break;
            case "green": avatarPlayCounter.setBitmap(this.getGame().getAssetManager().getBitmap("GreenCircle")); break;
            case "red": avatarPlayCounter.setBitmap(this.getGame().getAssetManager().getBitmap("RedCircle")); break;
            default: break;
        }
    }
}

