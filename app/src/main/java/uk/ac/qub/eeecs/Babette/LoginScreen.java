package uk.ac.qub.eeecs.Babette;

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
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.ui.PushButton;

/**
 *
 * This class is to create a login screen for the user
 * to select their account or navigate to create a new user
 *
 * @author  James Sims 40174176
 * @version 6.0
 *
 */

//  Login screen for the user to select their account or navigate to create a new one.
//  James Sims 40174176
public class LoginScreen extends GameScreen {
    private PushButton account1;
    private PushButton account2;
    private PushButton account3;
    private PushButton account4;
    private PushButton CreateAccount;
    private PushButton leftArrow;
    private PushButton rightArrow;
    private PushButton cPassword;
    ArrayList<PushButton> accountList = new ArrayList<>();

    private GameObject gBabetteLogo;
    private GameObject password;
    private Bitmap bitmapBackground;

    String mKeyLabels = "1234567890QWERTYUIOPASDFGHJKLZ !XCVBNM_ ";
    ArrayList<Key> mKeys = new ArrayList<>();
    StringBuffer mText = new StringBuffer();

    private Paint textPaint = new Paint();

    private int avatarCounter = 1;

    int spacingX = (int)mDefaultLayerViewport.getWidth() / 5;
    int spacingY = (int)mDefaultLayerViewport.getHeight() / 3;

    private ArrayList<User> userList;

    private GameManager gameManager;

    public LoginScreen(Game game, GameManager gm) {
        super("CardScreen", game);
        gameManager = gm;

        try {
            userList = User.readFromFile(game.getActivity());
        }catch(Exception e){
            userList = new ArrayList<>();
        }

        // James Sims 40174176
        loadAndAddAllAssets();
        createGameObjects(game);
        createButtons();
        createAndPositionKeys();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        // adams temporary code-------------------------------------------------------
        //gameManager.setCurrentUser(userList.get(0));
        //mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
        //------------------------------------------------------------------------------

        // Jake Smyth 40204021
        for(int i = 0; i < userList.size(); i++) {
            String userAvatar = userList.get(i).getAvatar();
            switch(userAvatar){
                case "Avatar1": accountList.get(i).setBitmap(this.getGame().getAssetManager().getBitmap("Avatar1")); break;
                case "Avatar2": accountList.get(i).setBitmap(this.getGame().getAssetManager().getBitmap("Avatar2")); break;
                case "Avatar3": accountList.get(i).setBitmap(this.getGame().getAssetManager().getBitmap("Avatar3")); break;
                case "Avatar4": accountList.get(i).setBitmap(this.getGame().getAssetManager().getBitmap("Avatar4")); break;
                case "Avatar5": accountList.get(i).setBitmap(this.getGame().getAssetManager().getBitmap("Avatar5")); break;
                default: break;
            }
        }

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        boolean buttonWithoutAccount = false;
        for(int i = 0; i < accountList.size(); i++)
        {
            // Jake Smyth 40204021
            if(accountList.get(i).getBitmap() != this.getGame().getAssetManager().getBitmap("AvatarSelected")) {
                // Checks that correct password and avatar number is entered before allowing access.
                if(userList.get(i).correctPassword(mText.toString()) && avatarCounter == i + 1)
                    accountList.get(i).update(elapsedTime);
            } else {
                buttonWithoutAccount = true;
            }
        }

        //Denies access to CreateAccount if maximum number of accounts has been reached.
        if(buttonWithoutAccount) {
            CreateAccount.update(elapsedTime);
        } else {
            CreateAccount.setBitmap(this.getGame().getAssetManager().getBitmap("CreateAccountinvert"));
        }

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            leftArrow.update(elapsedTime);
            rightArrow.update(elapsedTime);
            cPassword.update(elapsedTime);

            //Sets a maximum String length of 16.
            if(mText.length() < 16) {
                for (Key key : mKeys)
                    key.update(elapsedTime);
            }

            //Sets the current user before navigating to the next screen.
            if (account1.isPushTriggered()) {
                gameManager.setCurrentUser(userList.get(0));
                mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
            }
            else if (account2.isPushTriggered()) {
                gameManager.setCurrentUser(userList.get(1));
                mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
            }
            else if (account3.isPushTriggered()) {
                gameManager.setCurrentUser(userList.get(2));
                mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
            }
            else if (account4.isPushTriggered()) {
                gameManager.setCurrentUser(userList.get(3));
                mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
            }
            else if (CreateAccount.isPushTriggered())
                mGame.getScreenManager().addScreen(new CreateAccountScreen(mGame, gameManager));
            else if (leftArrow.isPushTriggered()) {
                if (avatarCounter > 1) {
                    avatarCounter--;
                } else {
                    avatarCounter = 4;
                }
            }
            else if (rightArrow.isPushTriggered()) {
                if (avatarCounter < 4) {
                    avatarCounter++;
                } else {
                    avatarCounter = 1;
                }
            }
            else if (cPassword.isPushTriggered()) {
                mText.delete(0, mText.length());
            }
        }
    }

    //James Sims
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen and draw the buttons
        graphics2D.clear(Color.BLACK);

        gBabetteLogo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        password.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        leftArrow.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        rightArrow.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        account1.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        account2.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        account3.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        account4.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        CreateAccount.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        cPassword.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        for(Key key : mKeys)
            key.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(25.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.WHITE);

        graphics2D.drawText("Choose the number of the account you wish to log in to,", spacingX * 2.25f, spacingY * 0.625f, textPaint);
        graphics2D.drawText("enter the password, and click on your account's avatar.", spacingX * 2.25f, spacingY * 0.875f, textPaint);
        textPaint.setColor(Color.GREEN);

        drawUserNames(graphics2D);

        textPaint.setColor(Color.WHITE);
        graphics2D.drawText("Account Number:", spacingX * 7f, spacingY * 4.8f, textPaint);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(40.0f);
        graphics2D.drawText(Integer.toString(avatarCounter), spacingX * 10.55f, spacingY * 4.88f, textPaint);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25.0f);
        graphics2D.drawText("Password:", spacingX * 7f, spacingY * 5.55f, textPaint);
        graphics2D.drawText(mText.toString(), spacingX * 8.75f, spacingY * 5.55f, textPaint);
    }

    public void drawUserNames(IGraphics2D graphics2D) {

        if (userList.size() > 0)
            graphics2D.drawText(userList.get(0).getUsername(), spacingX * 0.65f, spacingY * 1.7f, textPaint);
        else
            graphics2D.drawText("1", spacingX * 0.65f, spacingY * 1.7f, textPaint);
        if (userList.size() > 1)
            graphics2D.drawText(userList.get(1).getUsername(), spacingX * 4f, spacingY * 1.7f, textPaint);
        else
            graphics2D.drawText("2", spacingX * 4f, spacingY * 1.7f, textPaint);
        if (userList.size() > 2)
            graphics2D.drawText(userList.get(2).getUsername(), spacingX * 7.35f, spacingY * 1.7f, textPaint);
        else
            graphics2D.drawText("3", spacingX * 7.35f, spacingY * 1.7f, textPaint);
        if (userList.size() > 3)
            graphics2D.drawText(userList.get(3).getUsername(), spacingX * 10.7f, spacingY * 1.7f, textPaint);
        else
            graphics2D.drawText("4", spacingX * 10.7f, spacingY * 1.7f, textPaint);
    }

    // Loads and adds assets for future use on this screen.
    // James Sims 40174176
    private void loadAndAddAllAssets() {
        //Load in Bitmaps for accounts.
        mGame.getAssetManager().loadAssets("txt/assets/BabetteAssets.JSON");

        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Avatar1", "img/av1.png");
        assetManager.loadAndAddBitmap("Avatar2", "img/av2.png");
        assetManager.loadAndAddBitmap("Avatar3", "img/av3.png");
        assetManager.loadAndAddBitmap("Avatar4", "img/av4.png");
        assetManager.loadAndAddBitmap("Avatar5", "img/av5.png");
        assetManager.loadAndAddBitmap("AvatarSelected", "img/AvatarSelected.png");
        assetManager.loadAndAddBitmap("CreateAccount", "img/CreateAccount.png");
        assetManager.loadAndAddBitmap("CreateAccountinvert", "img/CreateAccountinvert.png");
        assetManager.loadAndAddBitmap("Key11","img/Key11.png");

        // Jake Smyth 40204021
        mGame.getAssetManager().loadAssets("txt/assets/PlatformDemoScreenAssets.JSON");
        assetManager.loadAndAddBitmap("RightArrow", "img/RightArrow.png");
        assetManager.loadAndAddBitmap("LeftArrow", "img/LeftArrow.png");

        mGame.getAssetManager().loadAssets("txt/assets/BabetteCreateAccountAssets.JSON");
        assetManager.loadAndAddBitmap("Textbox", "img/TextBox.png");
    }

    // Creates the game objects that will be displayed on screen.
    // James Sims 40174176
    private void createGameObjects(Game game) {
        AssetManager assetManager = mGame.getAssetManager();
        bitmapBackground = Bitmap.createScaledBitmap(assetManager.getBitmap("BabetteBgScreen"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        gBabetteLogo = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y,
                mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("BabetteBgScreen"), this);

        // Jake Smyth 40204021
        password = new GameObject(
                spacingX * 3.9f, spacingY * 0.825f, spacingX * 1.25f, spacingY * 0.15f,
                game.getAssetManager().getBitmap("Textbox"), this);
    }

    // Creates the buttons that will be displayed on screen.
    // James Sims 40174176
    private void createButtons() {
        // Create the trigger buttons
        account1 = new PushButton(spacingX * 0.50f, spacingY * 2f, spacingX, spacingY,
                "AvatarSelected", "AvatarSelected",this);
        account1.setPlaySounds(true, true);
        account2 = new PushButton(spacingX * 1.83f, spacingY * 2f, spacingX, spacingY,
                "AvatarSelected", "AvatarSelected", this);
        account2.setPlaySounds(true, true);
        account3 = new PushButton(spacingX * 3.17f, spacingY * 2f, spacingX, spacingY,
                "AvatarSelected", "AvatarSelected", this);
        account3.setPlaySounds(true, true);
        account4 = new PushButton(spacingX * 4.50f, spacingY * 2f, spacingX, spacingY,
                "AvatarSelected", "AvatarSelected", this);
        account4.setPlaySounds(true, true);

        accountList.add(account1);
        accountList.add(account2);
        accountList.add(account3);
        accountList.add(account4);

        CreateAccount = new PushButton(spacingX * 4.5f, spacingY * 0.15f, spacingX * 0.75f, spacingY * 0.3f,
                "CreateAccount", "CreateAccountinvert", this);
        CreateAccount.setPlaySounds(true, true);

        //  Arrows used to cycle through avatars in the ArrayList.
        //  Jake Smyth 40204021
        leftArrow = new PushButton(spacingX * 3.8f, spacingY * 1.12f, spacingX / 4f, spacingX / 4f,
                "LeftArrow", "LeftArrowSelected", this);
        rightArrow = new PushButton(spacingX * 4.4f, spacingY * 1.12f, spacingX / 4f, spacingX / 4f,
                "RightArrow", "RightArrowSelected", this);

        cPassword = new PushButton(spacingX * 3.9f, spacingY * 0.6f, spacingX * 0.5f, spacingY * 0.2f,
                "clearbutton", "clearinvert", this);
    }

    // Use of Chris Jennings' keyboard to allow user input.
    private void createAndPositionKeys() {
        final int rowLength = 10;
        final float topLeftKeyX = spacingX * 0.25f, topLeftKeyY = spacingY * 1f;
        final float keyWidth = 17.5f, keyHeight = 17.5f;
        final float keyXSpacing = 3.0f, keyYSpacing = 3.0f;

        float keyX = topLeftKeyX, keyY = topLeftKeyY;
        for(int keyIdx = 0; keyIdx < mKeyLabels.length(); keyIdx++) {
            Key key = new Key(keyX, keyY, keyWidth, keyHeight,
                    mKeyLabels.charAt(keyIdx), this);
            key.setLinkedStringBuffer(mText);
            mKeys.add(key);

            if(keyIdx > 0 && (keyIdx+1) % rowLength == 0) {
                keyY -= keyHeight + keyYSpacing;
                keyX = topLeftKeyX;
            }
            else keyX += keyWidth + keyXSpacing;
        }
    }
}
