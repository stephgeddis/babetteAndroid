package uk.ac.qub.eeecs.Babette;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

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

public class CreateAccountScreen extends GameScreen {
    private GameObject gBabetteBackground;
    private GameObject avatarOneGO,avatarTwoGO,avatarThreeGO,avatarFourGO,avatarFiveGO;
    private GameObject username,password,confirmPassword;
    private ArrayList<GameObject> avatarList = new ArrayList<>();
    private ArrayList<GameObject> avatarsToHide = new ArrayList<>();

    private PushButton backToLogin,createAccount;
    private PushButton leftArrow,rightArrow;
    private PushButton tUsername,tPassword,tConfirm;
    private PushButton cUsername,cPassword,cConfirm;

    private Paint textPaint = new Paint();

    private int avatarCounter = 1, textboxCounter = 1;
    private int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
    private int spacingY = (int) mDefaultLayerViewport.getHeight() / 5;

    private String mKeyLabels = "1234567890QWERTYUIOPASDFGHJKLZ !XCVBNM_ ";
    private ArrayList<Key> mKeys = new ArrayList<>();
    private StringBuffer mText = new StringBuffer();

    private String mUsername = "", mPassword = "", mConfirm = "";
    private String userIsNew = "", allFilled = "", passSame = "", passStrong = "";
    private String avatarSelected = avatarStringForUser(avatarCounter);
    private ArrayList<String> requirementList = new ArrayList<>();
    private ArrayList<User> userList;
    private GameManager gameManager;

    public CreateAccountScreen(Game game, GameManager gm) {
        super("CreateAccountScreen", game);
        gameManager = gm;
        loadAndAddAllAssets();
        createGameObjects(game);

        avatarsToHide.add(avatarTwoGO);
        avatarsToHide.add(avatarThreeGO);
        avatarsToHide.add(avatarFourGO);
        avatarsToHide.add(avatarFiveGO);
        requirementList.add(userIsNew);
        requirementList.add(allFilled);
        requirementList.add(passSame);
        requirementList.add(passStrong);

        try {
            userList = User.readFromFile(game.getActivity());
        }catch(Exception e){
            userList = new ArrayList<>();
        }

        hideAvatarsOffScreen(avatarsToHide);
        createButtons();
        createAndPositionKeys();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        userIsNew = Boolean.toString(userDoesNotAlreadyExist(mText.toString(), userList));
        allFilled = Boolean.toString(allTextboxesFilled(mUsername,mPassword,mConfirm));
        passSame = Boolean.toString(passwordEntriesIdentical(mPassword, mConfirm));
        passStrong = Boolean.toString(strongPasswordSubmitted(mPassword));

        switch(textboxCounter) {
            case 1: mUsername = mText.toString(); break;
            case 2: mPassword = mText.toString(); break;
            case 3: mConfirm = mText.toString();  break;
        }

        // Greys out Create Account button if the requirements have not been met.
        if(userDoesNotAlreadyExist(mText.toString(), userList) && allTextboxesFilled(mUsername,mPassword,mConfirm) && passwordEntriesIdentical(mPassword, mConfirm) && strongPasswordSubmitted(mPassword)) {
            createAccount.setBitmap(this.getGame().getAssetManager().getBitmap("CreateAccount"));
        } else {
            createAccount.setBitmap(this.getGame().getAssetManager().getBitmap("CreateAccountinvert"));
        }

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            // Update each button and transition if needed
            createAccount.update(elapsedTime);
            backToLogin.update(elapsedTime);
            leftArrow.update(elapsedTime);
            rightArrow.update(elapsedTime);
            tUsername.update(elapsedTime);
            tPassword.update(elapsedTime);
            tConfirm.update(elapsedTime);
            cUsername.update(elapsedTime);
            cPassword.update(elapsedTime);
            cConfirm.update(elapsedTime);

            //Sets a maximum String length of 16.
            if(mUsername.length() < 16)
                for (Key key : mKeys)
                    key.update(elapsedTime);

            if (createAccount.isPushTriggered()) {
                // Ensures all requirements have been met.
                if(userDoesNotAlreadyExist(mText.toString(), userList) &&
                        allTextboxesFilled(mUsername,mPassword,mConfirm) &&
                        passwordEntriesIdentical(mPassword, mConfirm) &&
                        strongPasswordSubmitted(mPassword)) {
                    // Writes new user to file, sets new user as the current user.
                    User newUser = new User(mUsername,"yellow", avatarSelected, mPassword);
                    gameManager.setCurrentUser(newUser);
                    userList.add(newUser);
                    User.writeToFile(userList, this.getGame().getActivity());
                    mGame.getScreenManager().addScreen(new BabetteMainMenu(mGame, gameManager));
                }
            }
            else if (backToLogin.isPushTriggered())
                mGame.getScreenManager().addScreen(new LoginScreen(mGame, gameManager));
            else if (leftArrow.isPushTriggered()) {
                previousAvatarInSequence(avatarList, avatarCounter);
                if (avatarCounter > 1) {
                    avatarCounter--;
                } else {
                    avatarCounter = 5;
                }
                avatarSelected = avatarStringForUser(avatarCounter);
            }
            else if (rightArrow.isPushTriggered()) {
                nextAvatarInSequence(avatarList, avatarCounter);
                if (avatarCounter < 5) {
                    avatarCounter++;
                } else {
                    avatarCounter = 1;
                }
                avatarSelected = avatarStringForUser(avatarCounter);
            }
            else if (tUsername.isPushTriggered() && textboxCounter != 1)
                switchToTextbox(textboxCounter, 1);
            else if (tPassword.isPushTriggered() && textboxCounter != 2)
                switchToTextbox(textboxCounter, 2);
            else if (tConfirm.isPushTriggered() && textboxCounter != 3)
                switchToTextbox(textboxCounter, 3);
            else if (cUsername.isPushTriggered())
                clearTextbox(textboxCounter, 1);
            else if (cPassword.isPushTriggered())
                clearTextbox(textboxCounter, 2);
            else if (cConfirm.isPushTriggered())
                clearTextbox(textboxCounter, 3);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //  Drawing the various elements on screen.
        graphics2D.clear(Color.BLACK);
        gBabetteBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        createAccount.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        backToLogin.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        username.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        password.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        confirmPassword.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        avatarOneGO.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        avatarTwoGO.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        avatarThreeGO.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        avatarFourGO.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        avatarFiveGO.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        leftArrow.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        rightArrow.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        tUsername.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        tPassword.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        tConfirm.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        cUsername.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        cPassword.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        cConfirm.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(25.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.WHITE);

        graphics2D.drawText("Please create your account by filling out the form below.", spacingX * 2.25f, spacingY * 0.75f, textPaint);
        graphics2D.drawText("Your default game colour is ", spacingX * 0.75f, spacingY * 1.25f, textPaint);
        textPaint.setColor(Color.YELLOW);
        graphics2D.drawText("yellow", spacingX * 5.25f, spacingY * 1.25f, textPaint);
        textPaint.setColor(Color.WHITE);
        graphics2D.drawText(". You can change this in Settings later.", spacingX * 6.25f, spacingY * 1.25f, textPaint);
        graphics2D.drawText("Avatar", spacingX * 0.75f, spacingY * 3.875f, textPaint);
        graphics2D.drawText("Username", spacingX * 5f, spacingY * 2.375f, textPaint);
        graphics2D.drawText("Password", spacingX * 5f, spacingY * 3.625f, textPaint);
        graphics2D.drawText("Confirm", spacingX * 5.05f, spacingY * 4.725f, textPaint);
        graphics2D.drawText("Password", spacingX * 5f, spacingY * 5.025f, textPaint);

        for(Key key : mKeys)
            key.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        switch(textboxCounter) {
            case 1: graphics2D.drawText(mText.toString(), spacingX * 6.75f, spacingY * 2.375f, textPaint); break;
            case 2: graphics2D.drawText(mText.toString(), spacingX * 6.75f, spacingY * 3.625f, textPaint); break;
            case 3: graphics2D.drawText(mText.toString(), spacingX * 6.75f, spacingY * 4.875f, textPaint); break;
        }

        graphics2D.drawText(mUsername, spacingX * 6.75f, spacingY * 2.375f, textPaint);
        graphics2D.drawText(mPassword, spacingX * 6.75f, spacingY * 3.625f, textPaint);
        graphics2D.drawText(mConfirm, spacingX * 6.75f, spacingY * 4.875f, textPaint);

        graphics2D.drawText("Requirements", spacingX * 8.5f, spacingY * 6f, textPaint);
        graphics2D.drawText("Username not already taken:", spacingX * 6.75f, spacingY * 7f, textPaint);
        graphics2D.drawText("All textboxes are filled:", spacingX * 6.75f, spacingY * 8f, textPaint);
        graphics2D.drawText("Password entries identical:", spacingX * 6.75f, spacingY * 9f, textPaint);
        graphics2D.drawText("Password contains at least", spacingX * 6.75f, spacingY * 9.75f, textPaint);
        graphics2D.drawText("8 characters, and 1 number:", spacingX * 6.75f, spacingY * 10.25f, textPaint);

        // Changes text colour depending on whether requirements are met.
        if(userIsNew.equals("true")) { textPaint.setColor(Color.GREEN); } else { textPaint.setColor(Color.RED); }
        graphics2D.drawText(userIsNew, spacingX * 11.5f, spacingY * 7f, textPaint);
        if(allFilled.equals("true")) { textPaint.setColor(Color.GREEN); } else { textPaint.setColor(Color.RED); }
        graphics2D.drawText(allFilled, spacingX * 11.5f, spacingY * 8f, textPaint);
        if(passSame.equals("true")) { textPaint.setColor(Color.GREEN); } else { textPaint.setColor(Color.RED); }
        graphics2D.drawText(passSame, spacingX * 11.5f, spacingY * 9f, textPaint);
        if(passStrong.equals("true")) { textPaint.setColor(Color.GREEN); } else { textPaint.setColor(Color.RED); }
        graphics2D.drawText(passStrong, spacingX * 11.5f, spacingY * 10.25f, textPaint);
    }

    // Loads and adds assets for future use on this screen.
    // Jake Smyth 40204021
    private void loadAndAddAllAssets() {
        AssetManager assetManager = mGame.getAssetManager();
        mGame.getAssetManager().loadAssets("txt/assets/BabetteCreateAccountAssets.JSON");
        assetManager.loadAndAddBitmap("Textbox", "img/TextBox.png");

        mGame.getAssetManager().loadAssets("txt/assets/BabetteAssets.JSON");

        assetManager.loadAndAddBitmap("BabetteBgScreen", "img/BabetteBgScreen.png");
        assetManager.loadAndAddBitmap("CreateAccount", "img/CreateAccountButton.png");
        assetManager.loadAndAddBitmap("BackButtonBabette", "img/BackButton.png");
        assetManager.loadAndAddBitmap("CreateAccountinvert", "img/CreateAccountinvert.png");
        assetManager.loadAndAddBitmap("Backinvert", "img/Backinvert.png");
        assetManager.loadAndAddBitmap("Avatar1", "img/av1.png");
        assetManager.loadAndAddBitmap("Avatar2", "img/av2.png");
        assetManager.loadAndAddBitmap("Avatar3", "img/av3.png");
        assetManager.loadAndAddBitmap("Avatar4", "img/av4.png");
        assetManager.loadAndAddBitmap("Avatar5", "img/av5.png");
        assetManager.loadAndAddBitmap("Key11","img/Key11.png");

        mGame.getAssetManager().loadAssets("txt/assets/PlatformDemoScreenAssets.JSON");
        assetManager.loadAndAddBitmap("RightArrow", "img/RightArrow.png");
        assetManager.loadAndAddBitmap("LeftArrow", "img/LeftArrow.png");
    }

    // Creates the game objects that will be displayed on screen.
    // Jake Smyth 40204021
    private void createGameObjects(Game game) {
        gBabetteBackground = new GameObject(mDefaultLayerViewport.x, mDefaultLayerViewport.y, mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("BabetteBgScreen"), this);
        avatarOneGO = new GameObject(spacingX * 1.15f, spacingY * 3.5f, spacingX, spacingX,
                game.getAssetManager().getBitmap("Avatar1"), this);
        avatarTwoGO = new GameObject(spacingX * 1.15f, spacingY * 3.5f, spacingX, spacingX,
                game.getAssetManager().getBitmap("Avatar2"), this);
        avatarThreeGO = new GameObject(spacingX * 1.15f, spacingY * 3.5f, spacingX, spacingX,
                game.getAssetManager().getBitmap("Avatar3"), this);
        avatarFourGO = new GameObject(spacingX * 1.15f, spacingY * 3.5f, spacingX, spacingX,
                game.getAssetManager().getBitmap("Avatar4"), this);
        avatarFiveGO = new GameObject(spacingX * 1.15f, spacingY * 3.5f, spacingX, spacingX,
                game.getAssetManager().getBitmap("Avatar5"), this);

        username = new GameObject(spacingX * 3.1f, spacingY * 4.1f, spacingX * 1.25f, spacingY * 0.3f,
                game.getAssetManager().getBitmap("Textbox"), this);
        password = new GameObject(spacingX * 3.1f, spacingY * 3.6f, spacingX * 1.25f, spacingY * 0.3f,
                game.getAssetManager().getBitmap("Textbox"), this);
        confirmPassword = new GameObject(spacingX * 3.1f, spacingY * 3.1f, spacingX * 1.25f, spacingY * 0.3f,
                game.getAssetManager().getBitmap("Textbox"), this);

        avatarList.add(avatarOneGO);
        avatarList.add(avatarTwoGO);
        avatarList.add(avatarThreeGO);
        avatarList.add(avatarFourGO);
        avatarList.add(avatarFiveGO);
    }

    // Relocating Avatars 2-5 off screen in order to "hide" them for future use.
    // Jake Smyth 40204021
    public static void hideAvatarsOffScreen(ArrayList<GameObject> avatarsToHide) {
        for(int i = 0; i < avatarsToHide.size(); i++)
        {
            avatarsToHide.get(i).position.add(5000f, 5000f);
        }
    }

    // Creates the buttons that will be displayed on screen.
    // Jake Smyth 40204021
    private void createButtons() {
        //  Navigation buttons.
        createAccount = new PushButton(spacingX * 4.5f, spacingY * 0.25f, spacingX * 0.75f, spacingY * 0.5f,
                "CreateAccount", "CreateAccountinvert", this);
        backToLogin = new PushButton(spacingX * 0.4f, spacingY * 0.25f, spacingX * 0.75f, spacingY * 0.5f,
                "BackButtonBabette", "Backinvert", this);

        //  Arrows used to cycle through avatars in the ArrayList.
        leftArrow = new PushButton(spacingX * 0.875f, spacingY * 2.45f, spacingX / 3f, spacingX / 3f,
                "LeftArrow", "LeftArrowSelected", this);
        rightArrow = new PushButton(spacingX * 1.425f, spacingY * 2.45f, spacingX / 3f, spacingX / 3f,
                "RightArrow", "RightArrowSelected", this);

        //  Edit and clear buttons to edit user input.
        tUsername = new PushButton(spacingX * 4f, spacingY * 4.1f, spacingX * 0.45f, spacingY * 0.375f,
                "editbutton", "editinvert", this);
        tPassword = new PushButton(spacingX * 4f, spacingY * 3.6f, spacingX * 0.45f, spacingY * 0.375f,
                "editbutton", "editinvert", this);
        tConfirm = new PushButton(spacingX * 4f, spacingY * 3.1f, spacingX * 0.45f, spacingY * 0.375f,
                "editbutton", "editinvert", this);
        cUsername = new PushButton(spacingX * 4.5f, spacingY * 4.1f, spacingX * 0.45f, spacingY * 0.375f,
                "clearbutton", "clearinvert", this);
        cPassword = new PushButton(spacingX * 4.5f, spacingY * 3.6f, spacingX * 0.45f, spacingY * 0.375f,
                "clearbutton", "clearinvert", this);
        cConfirm = new PushButton(spacingX * 4.5f, spacingY * 3.1f, spacingX * 0.45f, spacingY * 0.375f,
                "clearbutton", "clearinvert", this);
    }

    // Switch statement to rotate to the next of the five avatar options.
    // Counter integer used to determine which avatar to show.
    // Jake Smyth 40204021
    public static void nextAvatarInSequence(ArrayList<GameObject> avatarArray, int avCounter) {
        switch(avCounter)
        {
            case 1:  avatarArray.get(0).position.add(5000f, 5000f);
                avatarArray.get(1).position.add(-5000f, -5000f);
                break;
            case 2:  avatarArray.get(1).position.add(5000f, 5000f);
                avatarArray.get(2).position.add(-5000f, -5000f);
                break;
            case 3:  avatarArray.get(2).position.add(5000f, 5000f);
                avatarArray.get(3).position.add(-5000f, -5000f);
                break;
            case 4:  avatarArray.get(3).position.add(5000f, 5000f);
                avatarArray.get(4).position.add(-5000f, -5000f);
                break;
            case 5:  avatarArray.get(4).position.add(5000f, 5000f);
                avatarArray.get(0).position.add(-5000f, -5000f);
                break;
            default: break;
        }
    }

    // A reverse of the above switch statement.
    // Jake Smyth 40204021
    public static void previousAvatarInSequence(ArrayList<GameObject> avatarArray, int avCounter) {
        switch(avCounter)
        {
            case 1:  avatarArray.get(4).position.add(-5000f, -5000f);
                avatarArray.get(0).position.add(5000f, 5000f);
                break;
            case 2:  avatarArray.get(0).position.add(-5000f, -5000f);
                avatarArray.get(1).position.add(5000f, 5000f);
                break;
            case 3:  avatarArray.get(1).position.add(-5000f, -5000f);
                avatarArray.get(2).position.add(5000f, 5000f);
                break;
            case 4:  avatarArray.get(2).position.add(-5000f, -5000f);
                avatarArray.get(3).position.add(5000f, 5000f);
                break;
            case 5:  avatarArray.get(3).position.add(-5000f, -5000f);
                avatarArray.get(4).position.add(5000f, 5000f);
                break;
            default: break;
        }
    }

    // Converts the avatar counter to the String used to access the avatar bitmap.
    // Jake Smyth 40204021
    public static String avatarStringForUser(int avCounter) {
        String avString = "";
        switch (avCounter) {
            case 1: avString = "Avatar1"; break;
            case 2: avString = "Avatar2"; break;
            case 3: avString = "Avatar3"; break;
            case 4: avString = "Avatar4"; break;
            case 5: avString = "Avatar5"; break;
        }
        return avString;
    }

    // Method used in androidTests to ensure that hideAvatarsOffScreen(), nextAvatarInSequence() and
    // previousAvatarInSequence are all working correctly.
    // Jake Smyth 40204021
    public static boolean checkForOverlappingAvatars(GameObject avatarToCheck, ArrayList<GameObject> avatarList) {
        boolean isOverlapping = false;
        for (GameObject avatarFromList : avatarList)
            if(avatarFromList.getBitmap() != avatarToCheck.getBitmap())
            {
                float avatarToCheckX = avatarToCheck.position.x;
                float avatarToCheckY = avatarToCheck.position.y;
                float avatarFromListX = avatarFromList.position.x;
                float avatarFromListY = avatarFromList.position.y;

                if(avatarToCheckX == avatarFromListX && avatarToCheckY == avatarFromListY)
                    isOverlapping = true;
            }
        return isOverlapping;
    }

    // Use of Chris Jennings' keyboard to allow user input.
    private void createAndPositionKeys() {
        final int rowLength = 10;
        final float topLeftKeyX = spacingX * 0.25f, topLeftKeyY = spacingY * 2f;
        final float keyWidth = 17.5f, keyHeight = 17.5f;
        final float keyXSpacing = 3.0f, keyYSpacing = 3.0f;

        float keyX = topLeftKeyX, keyY = topLeftKeyY;
        for(int keyIdx = 0; keyIdx < mKeyLabels.length(); keyIdx++) {
            Key key = new Key(keyX, keyY, keyWidth, keyHeight, mKeyLabels.charAt(keyIdx), this);
            key.setLinkedStringBuffer(mText);
            mKeys.add(key);

            if(keyIdx > 0 && (keyIdx+1) % rowLength == 0) {
                keyY -= keyHeight + keyYSpacing;
                keyX = topLeftKeyX;
            }
            else keyX += keyWidth + keyXSpacing;
        }
    }

    // Method used to change the variable the StringBuffer is updating.
    // Jake Smyth 40204021
    private void switchToTextbox(int previousTextbox, int nextTextbox){
        switch(previousTextbox) {
            case 1: mUsername = mText.toString(); break;
            case 2: mPassword = mText.toString(); break;
            case 3: mConfirm = mText.toString(); break;
        }
        mText.delete(0, mText.length());
        switch(nextTextbox) {
            case 1: mText.append(mUsername); break;
            case 2: mText.append(mPassword); break;
            case 3: mText.append(mConfirm); break;
        }
        textboxCounter = nextTextbox;
    }

    // Method used to clear the relevant textbox.
    // Jake Smyth 40204021
    private void clearTextbox(int boxCurrentlySelected, int boxToClear) {
        mText.delete(0, mText.length());
        switch(boxToClear) {
            case 1: mUsername = ""; break;
            case 2: mPassword = ""; break;
            case 3: mConfirm = ""; break;
        }
        switch(boxCurrentlySelected) {
            case 1: mText.append(mUsername); break;
            case 2: mText.append(mPassword); break;
            case 3: mText.append(mConfirm); break;
        }
    }

    // Method to check that both password entries are identical.
    // Jake Smyth 40204021
    public static boolean passwordEntriesIdentical(String entryOne, String entryTwo) {
        Boolean entriesIdentical = false;
        if(entryOne.equals(entryTwo))
            entriesIdentical = true;
        return entriesIdentical;
    }

    // Method to check that all textboxes are filled.
    // Jake Smyth 40204021
    public static boolean allTextboxesFilled(String tb1, String tb2, String tb3) {
        Boolean allTextboxesFilled = true;
        if (tb1 == ""  || tb2 == "" || tb3 == "")
            allTextboxesFilled = false;
        return  allTextboxesFilled;
    }

    // Method to check that the password entered contains at least 8 characters and 1 number.
    // Jake Smyth 40204021
    public static boolean strongPasswordSubmitted(String pwSubmitted) {
        char ch;
        boolean hasEightCharacters = false;
        boolean hasNumber = false;
        if(pwSubmitted.length() >= 8)
            hasEightCharacters = true;
        for(int i=0;i < pwSubmitted.length();i++) {
            ch = pwSubmitted.charAt(i);
            if( Character.isDigit(ch))
                hasNumber = true;
            if(hasEightCharacters && hasNumber)
                return true;
        }
        return false;
    }

    // Method to check that the username entered is not already taken.
    // Jake Smyth 40204021
    public static boolean userDoesNotAlreadyExist(String uName, ArrayList<User> uList) {
        boolean exists = false;
        if(uList.size() != 0) {
            for (User u : uList) {
               if (u.getUsername().equals(uName))
                   exists = true;
            }
        }
        return !exists;
    }
}

