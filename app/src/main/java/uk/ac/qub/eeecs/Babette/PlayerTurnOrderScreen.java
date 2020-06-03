package uk.ac.qub.eeecs.Babette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * This class  is used to create a player turn order
 * screen in order to configure the turn of play before
 * the game starts
 *
 * @author  Stephen Geddis 40210068 && Stephen Irons 40204625
 * @version 6.0
 *
 */
public class PlayerTurnOrderScreen extends GameScreen {



    //Bitmaps for displaying the appropriate dice image
    //Stephen Geddis 4010068
    private Bitmap bitmapSpinningWheel1;
    private Bitmap bitmapSpinningWheel2;
    private Bitmap bitmapSpinningWheel3;
    private Bitmap bitmapSpinningWheel4;
    private Bitmap bitmapSpinningWheel5;
    private Bitmap bitmapSpinningWheel6;
    private Bitmap bitmapSpinningWheelHide;

    //Boolean variables for stopping the dice at the right panel
    //Stephen Geddis 40210068
    private boolean isFirstPanel = false;
    private boolean isSecondPanel = false;
    private boolean isThirdPanel = false;
    private boolean isFourthPanel = false;
    private boolean isFifthPanel = false;
    private boolean isSixthPanel = false;
    private boolean diceDisplayed = false;
    private boolean numDisplay = false;
    private boolean diceFirstFullSpin = false;

    //Used to display the AI and user's numbers for roll on screen
    //Stephen Geddis 40210068
    private boolean displayAINumbersTextOnScreen = false;
    private boolean displayUserNumberTextOnScreen = false;

    private boolean aiDiceRoll = false;
    private boolean playRollSound = false;


    //Variables used when triggering the dice to be displayed
    //Stephen Geddis 40210068
    private boolean triggerSecondsCounter = false;
    private int diceButtonPressCount = 0;

    private GameObject gameObjectBackground;
    private GameObject gameObjectUserAvatar;
    private GameObject gameObjectAIAvatar;
    private GameObject gameObjectAIAvatar2;
    private GameObject gameObjectAIAvatar3;

    private GameObject gameObjectDice;

    //Variables used to store dice numbers
    //Stephen Geddis 40210068
    private static int userPlayerDiceNumber;
    private static int aiPlayerDiceNumber;
    private static int aiPlayerDiceNumber2;
    private static int aiPlayerDiceNumber3;

    private int numberToDisplayUserDice = 0;

    ArrayList<Player> playerArrayList = new ArrayList<Player>();

    private Paint paintToDisplayNumbersOnDiceObject = new Paint();

    private Paint paintToDisplayUserDiceNumber = new Paint();
    private Paint paintToDisplayAIDiceNumber = new Paint();

    private PushButton btnSpinDice;
    private PushButton btnPlayButton;


    private AudioManager diceRollAudio = getGame().getAudioManager();
    private AssetManager assetManager = mGame.getAssetManager();


    private int secondsPassedCounter;
    private int hideDiceCounter;

    //Timer used to count the seconds passed in order to time animation
    //For the dice displayed on the screen
    //Stephen Geddis
    private final Timer diceStartCount = new Timer();
    private final TimerTask diceStart = new TimerTask() {
        @Override
        public void run() {
            secondsPassedCounter++;

            //Hide the dice objects including the text for the numbers
            //A second after the hide dice method has been triggered
            //Stephen Geddis 40210068
            if(hideDiceCounter == 1){
                gameObjectDice.setBitmap(bitmapSpinningWheelHide);
                hideDiceCounter=0;
                //Makes the text invisible on the dice
                paintToDisplayNumbersOnDiceObject.setAlpha(0);
                displayUserNumberTextOnScreen= true;

            }
        }
    };

    private GameManager gameManager;
    private String stringUsername;
    private String stringAvatar;

    public PlayerTurnOrderScreen(Game game, GameManager gm) {
        super("PlayerTurnOrderScreen", game);
        gameManager = gm;

        //Stephen Irons 40204625
        //Created AI objects to enable testing of game play
        // Adam Higgins 40212255 && Stephen Irons 40204625: Editing the original code so that it creates 3 AI players
        // that don't have the same colour as each other or the current user
        playerArrayList = gameManager.createEnemyAIList();
        playerArrayList.add(gameManager.getCurrentUser());

        aiPlayerDiceNumber = createPlayerDiceNumber();
        aiPlayerDiceNumber2 = createPlayerDiceNumber();
        aiPlayerDiceNumber3 = createPlayerDiceNumber();

        //----------------------------------------------

        addAssetsToAssetManager();

        createBackgroundObject(game);
        createAvatarObjects(game);
        createDiceObject(game);

        createDiceButton();
        createPlayButton();
        createDiceBitmaps();

        loadUserInformation(gameManager.getCurrentUser());

        //Sets the rate of the dice timer to a second
        //Stephen Geddis 40210068
        diceStartCount.scheduleAtFixedRate(diceStart, 1000, 1000);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            diceButtonPressed(elapsedTime);
            playGameButtonPressed(elapsedTime);
        }
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    graphics2D.clear(Color.BLACK);

    gameObjectBackground.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

    gameObjectUserAvatar.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
    gameObjectAIAvatar.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
    gameObjectAIAvatar2.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
    gameObjectAIAvatar3.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);


    paintToDisplayAIDiceNumber.setColor(Color.WHITE);
    paintToDisplayAIDiceNumber.setTypeface(Typeface.MONOSPACE);
    paintToDisplayAIDiceNumber.setTextSize(30.0f);
    paintToDisplayAIDiceNumber.setTextAlign(Paint.Align.CENTER);


    btnSpinDice.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

    btnPlayButton.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);


        graphics2D.drawText("Please press the dice button in order to decide the turn order ", mDefaultLayerViewport.x + 400 , mDefaultLayerViewport.y -100, paintToDisplayAIDiceNumber);

        //displays AI roll number on screen
        //Stephen Geddis 40210068
        if(displayAINumbersTextOnScreen) {
            graphics2D.drawText("Rolled a " + aiPlayerDiceNumber, mDefaultLayerViewport.x + 400, mDefaultLayerViewport.y + 350, paintToDisplayAIDiceNumber);
            graphics2D.drawText("Rolled a " + aiPlayerDiceNumber2, mDefaultLayerViewport.x + 400, mDefaultLayerViewport.y + 450, paintToDisplayAIDiceNumber);
            graphics2D.drawText("Rolled a " + aiPlayerDiceNumber3, mDefaultLayerViewport.x + 400, mDefaultLayerViewport.y + 570, paintToDisplayAIDiceNumber);
        }

        //displays AI roll number on screen
        //Stephen Geddis 40210068
        if(displayUserNumberTextOnScreen){
            graphics2D.drawText(stringUsername+" rolled  a " + numberToDisplayUserDice, mDefaultLayerViewport.x + 550, mDefaultLayerViewport.y +40, paintToDisplayAIDiceNumber);
        }


    gameObjectDice.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

        paintToDisplayNumbersOnDiceObject.setTypeface(Typeface.MONOSPACE);
        paintToDisplayNumbersOnDiceObject.setTextSize(50.0f);
        paintToDisplayNumbersOnDiceObject.setTextAlign(Paint.Align.CENTER);



        //keeps the counter for timing animation at  0 until button is pressed
        //Stephen Geddis 40210068
        if(triggerSecondsCounter == false){secondsPassedCounter = 0;}

        //starts the counter for timing the animation count
        if(btnSpinDice.isPushed()){triggerSecondsCounter = true;}

        //Code to display dice numbers on screen
        //Stephen Geddis 40210068
        if(btnSpinDice.isPushed()){numDisplay = true; triggerSecondsCounter = true;diceButtonPressCount=1; paintToDisplayNumbersOnDiceObject.reset();}
        if((!diceDisplayed) && (numDisplay)) {

            if (diceButtonPressCount == 2) {

                if(playRollSound){
                    playDiceRolling();
                    playRollSound=false;
                }

                graphics2D.drawText("1", gameObjectDice.getWidth()+270 , gameObjectDice.getHeight()-80, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("2", gameObjectDice.getWidth() + 500, gameObjectDice.getHeight() + 60, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("3", gameObjectDice.getWidth() + 400, gameObjectDice.getHeight() + 280, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("4", gameObjectDice.getWidth() + 100, gameObjectDice.getHeight() + 320, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("5", gameObjectDice.getWidth() -150, gameObjectDice.getHeight() + 180, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("6", gameObjectDice.getWidth()-70, gameObjectDice.getHeight() -20, paintToDisplayNumbersOnDiceObject);
                displayAINumbersTextOnScreen = true;

                //Moves the dice button off screen
                //Stephen Geddis 40210068
                btnSpinDice.position.x = btnSpinDice.position.x+1000;

                //Move the play button on screen
                //Stephen Geddis 40210068
                btnPlayButton.position.x = mDefaultLayerViewport.x+180;
                btnPlayButton.position.y = mDefaultLayerViewport.y -120;



            }

            if (diceButtonPressCount == 1) {

                graphics2D.drawText("", gameObjectDice.getWidth() + 440, gameObjectDice.getHeight() + 140, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("", gameObjectDice.getWidth() + 580, gameObjectDice.getHeight() + 220, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("", gameObjectDice.getWidth() + 520, gameObjectDice.getHeight() + 340, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("", gameObjectDice.getWidth() + 350, gameObjectDice.getHeight() + 370, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("", gameObjectDice.getWidth() + 230, gameObjectDice.getHeight() + 290, paintToDisplayNumbersOnDiceObject);
                graphics2D.drawText("", gameObjectDice.getWidth() + 280, gameObjectDice.getHeight() + 180, paintToDisplayNumbersOnDiceObject);
                diceButtonPressCount++;

                playRollSound = true;


            }

        }


        //Statements for spinning dice animation which is used at the start once the dice button
        // has been pressed in order to spinning the dice fully before it stops at
        // the appropriate dice panel
        //Checks to see how many seconds have passed and changes to appropriate dice image
        //Stephen Gedddis 40210068
        if ((secondsPassedCounter > 1) && (secondsPassedCounter < 3))
            gameObjectDice.setBitmap(bitmapSpinningWheel1);

        if ((secondsPassedCounter > 2) && (secondsPassedCounter < 4))
            gameObjectDice.setBitmap(bitmapSpinningWheel2);

        if ((secondsPassedCounter > 3) && (secondsPassedCounter < 5))
            gameObjectDice.setBitmap(bitmapSpinningWheel3);

        if ((secondsPassedCounter > 4) && (secondsPassedCounter < 6))
            gameObjectDice.setBitmap(bitmapSpinningWheel4);
        if ((secondsPassedCounter > 5) && (secondsPassedCounter < 7))
            gameObjectDice.setBitmap(bitmapSpinningWheel5);

        if ((secondsPassedCounter > 6) && (secondsPassedCounter < 8)) {
            gameObjectDice.setBitmap(bitmapSpinningWheel6);

            diceFirstFullSpin = true;
        }

        //Statements to decide which dice will be displayed based on the appropriate boolean
        // being set to true via the createPlayerDice number method
        //Stephen Geddis 40210068
        if (secondsPassedCounter > 7) {
            if (isFirstPanel)
                diceSpinToFirst();
            if (isSecondPanel)
                diceSpinToSecond();
            if (isThirdPanel)
                diceSpinToThird();
            if (isFourthPanel)
                diceSpinToFourth();
            if (isFifthPanel)
                diceSpinToFifth();
            if (isSixthPanel)
                diceSpinToSixth();
        }
    }

    //Method to load assets
    // Stephen Geddis 40204021
    private void addAssetsToAssetManager(){

        assetManager.loadAndAddBitmap("WorldMapBackground", "img/WorldMapColouredBackgroundOnly.png");

        assetManager.loadAndAddBitmap("Avatar1", "img/av1.png");
        assetManager.loadAndAddBitmap("Avatar2", "img/av2.png");
        assetManager.loadAndAddBitmap("Avatar3", "img/av3.png");
        assetManager.loadAndAddBitmap("Avatar4", "img/av4.png");

        assetManager.loadAndAddBitmap("SpinningWheelBeforeActive", "img/SpinningWheelBeforeActive.png");
        assetManager.loadAndAddBitmap("SpinningWheel1", "img/SpinningWheel1.png");
        assetManager.loadAndAddBitmap("SpinningWheel2", "img/SpinningWheel2.png");
        assetManager.loadAndAddBitmap("SpinningWheel3", "img/SpinningWheel3.png");
        assetManager.loadAndAddBitmap("SpinningWheel4", "img/SpinningWheel4.png");
        assetManager.loadAndAddBitmap("SpinningWheel5", "img/SpinningWheel5.png");
        assetManager.loadAndAddBitmap("SpinningWheel6", "img/SpinningWheel6.png");
        assetManager.loadAndAddBitmap("InitalInvisible", "img/InitalInvisible.png");
    }

    //Method to create a background object
    // Stephen Geddis 40204021
    private void createBackgroundObject(Game game){
        gameObjectBackground = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y,
                mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("BabetteBgScreen"), this);
    }

    //Method to create avatar objects
    // Stephen Geddis 40204021
    private void createAvatarObjects(Game game){
        gameObjectUserAvatar = new GameObject(
                mDefaultLayerViewport.x -50, mDefaultLayerViewport.y+80,
                mDefaultLayerViewport.getWidth()/6, mDefaultLayerViewport.getHeight()/4,
                game.getAssetManager().getBitmap(gameManager.getCurrentUser().getAvatar()), this);

        gameObjectAIAvatar = new GameObject(
                mDefaultLayerViewport.x -73, mDefaultLayerViewport.y-30,
                mDefaultLayerViewport.getWidth()/12, mDefaultLayerViewport.getHeight()/8,
                game.getAssetManager().getBitmap(playerArrayList.get(0).getAvatar()), this);

        gameObjectAIAvatar2 = new GameObject(
                mDefaultLayerViewport.x -73, mDefaultLayerViewport.y-80,
                mDefaultLayerViewport.getWidth()/12, mDefaultLayerViewport.getHeight()/8,
                game.getAssetManager().getBitmap(playerArrayList.get(1).getAvatar()), this);

        gameObjectAIAvatar3 = new GameObject(
                mDefaultLayerViewport.x -73, mDefaultLayerViewport.y-130,
                mDefaultLayerViewport.getWidth()/12, mDefaultLayerViewport.getHeight()/8,
                game.getAssetManager().getBitmap(playerArrayList.get(2).getAvatar()), this);

    }

    // Made by Stephen Geddis 40210068 to load user's information
    //Refactored by Adam Higgins 40212255
    private void loadUserInformation(User user) {
        stringUsername = user.getUsername();
        stringAvatar = gameManager.getCurrentUser().getAvatar();
        gameObjectUserAvatar.setBitmap(this.getGame().getAssetManager().getBitmap(user.getAvatar()));
    }

    //Method to create a dice button
    // Stephen Geddis 40204021
    private void createDiceButton() {
        btnSpinDice = new PushButton(mDefaultLayerViewport.x + 50, mDefaultLayerViewport.y +80,
                mDefaultLayerViewport.getWidth() / 6, mDefaultLayerViewport.getHeight() / 4,
                "SpinningWheelBeforeActive", "SpinningWheelBeforeActive", this);
    }


    //Method to create a dice button
    // Stephen Geddis 40204021
    private void createPlayButton() {
        btnPlayButton = new PushButton(mDefaultLayerViewport.x-1000, mDefaultLayerViewport.y ,
                mDefaultLayerViewport.getWidth() / 6, mDefaultLayerViewport.getHeight() / 8,
                "PLAY", "PLAYinvert", this);
    }


    private void diceButtonPressed(ElapsedTime elapsedTime){

        btnSpinDice.update(elapsedTime);

        if (btnSpinDice.isPushTriggered()) {


            //Method call to create Dice Number
            createUserPlayerDiceNumber();

            //Method to sort player turn
            //sortPlayerTurn(playerArrayList);

            //Sets up dice off screen
            //Stephen Geddis 40210068
            gameObjectDice.setBitmap(bitmapSpinningWheel1);
            diceFirstFullSpin = false;
            gameObjectDice.position.add(-1000f, -1000f);
            diceDisplayed = true;

            //Sets players for game passing the sorted player array list
            gameManager.setStartingPlayers(sortPlayerTurn(playerArrayList));

            if (diceDisplayed) {
                //positions dice on screen
                gameObjectDice.position.add(1000f, 1000f);
                diceDisplayed = false;
                diceFirstFullSpin = false;

                //resets dice
                secondsPassedCounter =1;
                gameObjectDice.setBitmap(bitmapSpinningWheel1);
            }
        }
    }

    //Method to trigger the opening of main babette game screen
    //once the play button is pressed
    // Stephen Geddis 40204021
    private void playGameButtonPressed(ElapsedTime elapsedTime){

        btnPlayButton.update(elapsedTime);
        if (btnPlayButton.isPushTriggered()) {
            mGame.getScreenManager().addScreen(new BabetteGameScreen(mGame, gameManager));
        }

    }

    //Method to create a dice object
    // Stephen Geddis 40204021
    private void createDiceObject(Game game) {
        gameObjectDice = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y,
                mDefaultLayerViewport.getWidth()-40 , mDefaultLayerViewport.getHeight()-40,
                game.getAssetManager().getBitmap("InitalInvisible"), this);
    }


    //Method to create dice bitmaps
    // Stephen Geddis 40204021
    private void createDiceBitmaps() {

        bitmapSpinningWheel1 = Bitmap.createScaledBitmap(assetManager.getBitmap("SpinningWheel1"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapSpinningWheel2 = Bitmap.createScaledBitmap(assetManager.getBitmap("SpinningWheel2"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapSpinningWheel3 = Bitmap.createScaledBitmap(assetManager.getBitmap("SpinningWheel3"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapSpinningWheel4 = Bitmap.createScaledBitmap(assetManager.getBitmap("SpinningWheel4"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapSpinningWheel5 = Bitmap.createScaledBitmap(assetManager.getBitmap("SpinningWheel5"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapSpinningWheel6 = Bitmap.createScaledBitmap(assetManager.getBitmap("SpinningWheel6"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);

        bitmapSpinningWheelHide = Bitmap.createScaledBitmap(assetManager.getBitmap("InitalInvisible"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
    }

    //Method to generate a dice number
    // Stephen Geddis 40204021
    private int createPlayerDiceNumber() {

        Random rand = new Random();
        return rand.nextInt(6) + 1;

    }


    //Method to create dice number
    // Stephen Geddis 40204021
    private void createUserPlayerDiceNumber(){

        userPlayerDiceNumber = createPlayerDiceNumber();

        //sets the number to be displayed on screen to the user
        //sets appropiate boolean value to true in order to change
        //the dice panel to that said panel after the first spin animation
        //Stephen Geddis 40210068
        switch (userPlayerDiceNumber){
            case 1: isFirstPanel= true;
                numberToDisplayUserDice = 1;
                break;
            case 2: isSecondPanel= true;
                numberToDisplayUserDice = 2;
                break;
            case 3: isThirdPanel= true;
                numberToDisplayUserDice = 3;
                break;
            case 4: isFourthPanel= true;
                numberToDisplayUserDice = 4;
                break;
            case 5: isFifthPanel= true;
                numberToDisplayUserDice = 5;
                break;
            case 6: isSixthPanel= true;
                numberToDisplayUserDice = 6;
                break;
        }
    }


    //Methods to set the dice panel to the appropriate
    //panel after the dice animation has occured and trigger the steps
    //to play sound and then hide the dice after
    //Stephen Geddis 40210068
    private void diceSpinToFirst(){
        gameObjectDice.setBitmap(bitmapSpinningWheel1);
        isFirstPanel = false;
        playDiceRollSelected();
        hideDice();
        numberToDisplayUserDice =  1;
    }

    private void diceSpinToSecond(){
        gameObjectDice.setBitmap(bitmapSpinningWheel2);
        isSecondPanel = false;
        playDiceRollSelected();
        hideDice();
        numberToDisplayUserDice =  2;
    }

    private void diceSpinToThird(){
        gameObjectDice.setBitmap(bitmapSpinningWheel3);
        isThirdPanel= false;
        playDiceRollSelected();
       hideDice();
        numberToDisplayUserDice =  3;
    }

    private void diceSpinToFourth(){
        gameObjectDice.setBitmap(bitmapSpinningWheel4);
        isFourthPanel= false;
        playDiceRollSelected();
        hideDice();
        numberToDisplayUserDice =  4;
    }

    private void diceSpinToFifth(){
        gameObjectDice.setBitmap(bitmapSpinningWheel5);
        isFifthPanel= false;
        playDiceRollSelected();
        hideDice();
        numberToDisplayUserDice =  5;
    }


    private void diceSpinToSixth(){
        gameObjectDice.setBitmap(bitmapSpinningWheel6);
        isSixthPanel= false;
        playDiceRollSelected();
        hideDice();
        numberToDisplayUserDice =  6;
    }

    //Method used to hide the dice after the panel is display after the animation
    //Stephen Geddis 40210068
    private void hideDice(){
        //set to one will trigger the if statement in run method
       hideDiceCounter = 1;
    }

    //Method to Play Dice Sounds
    //Stephen Geddis 40210068
    private void playDiceRollSelected(){ diceRollAudio.play(getGame().getAssetManager().getSound("SpinningWheelSelected")); }
    private void playDiceRolling(){ diceRollAudio.play(getGame().getAssetManager().getSound("SpinningWheelSpinning")); }

    //Stephen Irons 40204625
    //Method to put the players into order for the game
    public static ArrayList<Player> sortPlayerTurn(ArrayList<Player> passedPlayerList){
        String[][] player2DArray = new String[passedPlayerList.size()][2];

        for(int i = 0; i< passedPlayerList.size();i++){
            player2DArray[i][0]=passedPlayerList.get(i).getUsername();
        }

        player2DArray[0][1]= Integer.toString(aiPlayerDiceNumber);
        player2DArray[1][1]= Integer.toString(aiPlayerDiceNumber2);
        player2DArray[2][1]= Integer.toString(aiPlayerDiceNumber3);
        player2DArray[3][1]= Integer.toString(userPlayerDiceNumber);

        player2DArray = sortPlayer2DArray(player2DArray);

        ArrayList<Player> orderedPlayerList = new ArrayList<Player>(0);
        for(int i = 0; i<player2DArray.length;i++){
            for(int j = 0; j<passedPlayerList.size();j++){
                if(passedPlayerList.get(j).getUsername()==player2DArray[i][0]){
                    orderedPlayerList.add(passedPlayerList.get(j));
                }
            }
        }
        return orderedPlayerList;
    }


    //Stephen Irons 40204625
    //Method to bubble sort the 2D Array of players
    public static String[][] sortPlayer2DArray(String[][] player2DArray){
        boolean sorted = false;
        do{
            sorted = true;
            for(int i = 0; i<player2DArray.length-1;i++){
                if(Integer.parseInt(player2DArray[i][1])<Integer.parseInt(player2DArray[i+1][1])){
                    String temp0 = player2DArray[i][0];
                    String temp1 = player2DArray[i][1];
                    player2DArray[i][0] = player2DArray[i+1][0];
                    player2DArray[i][1] = player2DArray[i+1][1];
                    player2DArray[i+1][0] = temp0;
                    player2DArray[i+1][1] = temp1;
                    sorted = false;
                }
            }
        }
        while(sorted == false);
        return player2DArray;
    }
}
