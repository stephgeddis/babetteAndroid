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
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;

/**
 *
 * This class is used to create a game screen
 * to enable the user and AI to play the game
 * and provide the user with a visual representation
 * of how the game is progressing
 *
 * @author  Stephen Irons 40204625
 * @version 6.0
 *
 */

public class BabetteGameScreen extends GameScreen {

    private GameObject gBabetteBackground;
    private GameObject gBabetteMap;
    private GameObject gBabetteRegionLabels;
    private GameObject gGameObjectiveBar;
    private GameObject babetteCard1;
    private GameObject babetteCard2;
    private GameObject babetteCard3;
    private GameObject babetteCard4;
    private GameObject babetteCard5;


    int spacingX;
    int spacingY;

    private Bitmap bitmapBackground;
    private Bitmap bitmapWorldMap;
    private Bitmap bitmapText;

    private Bitmap bitmapFramedAvatar;
    private Bitmap bitmapAttack;
    private Bitmap bitmapDraft;
    private Bitmap bitmapFortify;

    private Bitmap bitmapAttackState;
    private Bitmap bitmapDraftState;
    private Bitmap bitmapFortifyState;

    private Bitmap bitmapRedCounter;
    private Bitmap bitmapBlueCounter;
    private Bitmap bitmapYellowCounter;
    private Bitmap bitmapGreenCounter;
    private Bitmap bitmapGreyCounter;
    private Bitmap bitmapGameObjectiveBar;

    private String attackMessage = "Click on the map to start";
    private String regionOne = "";
    private String regionTwo = "";
    private String regionSelected = "";
    private int waitCounter;
    private String playerMessage;

    private Paint textPaint = new Paint();

    private PushButton showAndHideRegionLabels;
    private Boolean regionLabelsShownOnMap = false;

    //private PushButton ButtonBabetteCard;
    private PushButton ButtonTrade;
    private PushButton NextButton;
    private GameObject GameObjectDraft;
    private GameObject GameObjectAttack;
    private GameObject GameObjectFortify;
    private GameObject GameObjectFramedAvatar;
    private Counter playerCounter;

    private GameObject gameObjectDraftState;
    private GameObject gameObjectAttackState;
    private GameObject gameObjectFortifyState;

    private AssetManager assetManager = mGame.getAssetManager();
    private GameManager gameManager;

    int resourceCounter;
    boolean playerInput;
    int userResourcesToPlaceThisTurn;
    boolean hasResetUserResources;
    public int playerTurnPhase;
    int aiAttackCounter;
    boolean playerFortified;
    boolean draftBool;
    boolean attackBool;
    boolean fortifyBool;
    boolean draftVisible;
    boolean attackVisible;
    boolean fortifyVisible;
    private int touchDelay;
    private boolean regionEnlarged;
    private boolean enlargementFlag;

    ArrayList<GameObject> babetteCards = new ArrayList<>();
    ArrayList<BabetteCard> bCardList = new ArrayList<>();

    public BabetteGameScreen(Game game, GameManager gm) {
        super("BabetteGameScreen", game);

        spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        gameManager = gm;
        waitCounter = 1;
        resourceCounter = 0;
        playerInput = false;
        playerMessage = "";
        playerTurnPhase = 0;
        aiAttackCounter = 9;
        hasResetUserResources = false;
        playerFortified = false;
        draftBool = false;
        attackBool = false;
        fortifyBool = false;
        draftVisible = false;
        attackVisible = false;
        fortifyVisible = false;
        touchDelay = 0;
        regionEnlarged = false;
        enlargementFlag = false;

        babetteCards.add(babetteCard1);
        babetteCards.add(babetteCard2);
        babetteCards.add(babetteCard3);
        babetteCards.add(babetteCard4);
        babetteCards.add(babetteCard5);

        for (int i = 0; i < gameManager.getCurrentPlayers().size(); i++){
            if(gameManager.getCurrentPlayers().get(i) instanceof User)
                gameManager.getCurrentPlayers().get(i).cards = bCardList;
        }

        //Stephen Irons 40204625
        createCounterBitmaps();

        //Jake Smyth 40204021
        addAssets();

        createBitmaps();
        createGameObjects(game);
        createUserNavButtons();

        assignCountersToRegions(gameManager.getCurrentPlayers());
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        Input input = mGame.getInput();

        // Method used to "show"/"hide" the region labels on the map, alternating with the player counters.
        // Jake Smyth 40204021
        showAndHideRegionLabels.update(elapsedTime);
        if (showAndHideRegionLabels.isPushTriggered()) {
            showHideMapLabels();
        }

        //Christopher Jennings 40200418
        //GameScreen Nav Buttons
        NextButton.update(elapsedTime);
        if (NextButton.isPushTriggered()) {
            if(playerTurnPhase == 1)
                userAttack(gameManager.getCurrentUser());
            else if(playerTurnPhase == 2)
                userFortify(gameManager.getCurrentUser());
            regionEnlarged = false;
        }

        ButtonTrade.update(elapsedTime);
        babetteCardHandler();

        // Adam Higgins 40212255 and Stephen Irons 40204625
        // Slows the game down to an appropriate playing speed
        if (waitCounter > -1)
            waitCounter--;
        if(waitCounter == 0 || (playerInput && gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User))
            proceedWithGame();

        if (!(gameManager.getGameStage() == 2 &&
                gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof  User &&
                (playerTurnPhase == 1 || playerTurnPhase == 2)))
            regionEnlarged = false;
        regionEnlargementHandler();

        playerInput = false;
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchDelay > 0)
            touchDelay--;
        if (touchEvents.size() > 0 && touchDelay == 0) {
            touchDelay = 4;
            outerLoop:
            //Code to create a bounding box for the areas on the map
            //Based on the lecture by Philip
            //Created by Stephen Geddis 40210068
            for (TouchEvent touchEvent : touchEvents) {
                Vector2 layerPosition = new Vector2();
                ViewportHelper.convertScreenPosIntoLayer(
                        mDefaultScreenViewport, touchEvent.x, touchEvent.y,
                        mDefaultLayerViewport, layerPosition);

                BoundingBox bound = gBabetteMap.getBound();
                if (bound.contains(layerPosition.x, layerPosition.y)) {
                    float xLoc = (layerPosition.x - bound.getLeft()) / bound.getWidth();
                    float yLoc = (bound.getTop() - layerPosition.y) / bound.getHeight();

                    Bitmap bitmap = gBabetteMap.getBitmap();
                    int colour = bitmap.getPixel(
                            (int) (xLoc * bitmap.getWidth()),
                            (int) (yLoc * bitmap.getHeight()));

                    for (int i = 0; i < gameManager.getRegions().size(); i++) {
                        if (gameManager.getRegions().get(i).getRegionName().equals(gameManager.regionSelectedByUser(colour))) {
                            regionSelected = gameManager.regionSelectedByUser(colour);
                            playerInput = true;
                            break;
                        }
                    }

                    if (playerInput) {
                        //Stephen Irons 40204625
                        //Used to select regions to attack from and to
                        boolean isValid = false;
                        for (Region region : gameManager.getRegions()) {
                            if (gameManager.regionNameToHexCode(regionSelected) == region.getHexCode() && region.getNumResources() > 1) {
                                isValid = true;
                            }
                        }

                        if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User && playerTurnPhase == 1) {
                            if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).regionsHeld.contains(gameManager.regionNameToHexCode(regionSelected))) {


                                if (regionOne == "" && isValid) {
                                    regionTwo = "";
                                    regionOne = gameManager.regionNameToHexCode(regionSelected);
                                    regionEnlarged = true;
                                    greyOutRegionCounters(gameManager.regionNameToHexCode(regionSelected));
                                }

                                else if (regionOne == gameManager.regionNameToHexCode(regionSelected)) {
                                    regionOne = "";
                                    regionTwo = "";
                                    regionEnlarged = false;
                                    assignCountersToRegions(gameManager.getCurrentPlayers());
                                }
                            }

                            if (!gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).regionsHeld.contains(gameManager.regionNameToHexCode(regionSelected)) && regionOne != "") {
                                for (Region region : gameManager.getRegions()) {
                                    if (region.getHexCode() == regionOne) {
                                        if (region.getAjacentRegions().contains(gameManager.regionNameToHexCode(regionSelected))) {
                                            regionTwo = gameManager.regionNameToHexCode(regionSelected);
                                        }
                                    }
                                }
                            }
                        }

                        //40174176 - James Sims
                        //Select Regions to Fortify with
                        else if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User &&
                                playerTurnPhase == 2) {
                            if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).regionsHeld.contains(gameManager.regionNameToHexCode(regionSelected))) {
                                if (regionOne == "" & isValid) {
                                    regionTwo = "";
                                    regionOne = gameManager.regionNameToHexCode(regionSelected);
                                    regionEnlarged = true;
                                    greyOutForFortify((User) gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()));
                                }

                                else if (regionOne == gameManager.regionNameToHexCode(regionSelected) ||
                                        regionOne == "") {
                                    regionOne = "";
                                    regionTwo = "";
                                    regionEnlarged = false;
                                    assignCountersToRegions(gameManager.getCurrentPlayers());
                                }
                            }

                            if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).regionsHeld.contains(gameManager.regionNameToHexCode(regionSelected)) && regionOne != "")
                                regionTwo = gameManager.regionNameToHexCode(regionSelected);
                        }
                    }
                }

                if (playerInput)
                    break outerLoop;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.BLACK);
        gBabetteBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        gBabetteMap.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        gBabetteRegionLabels.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        showAndHideRegionLabels.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        gGameObjectiveBar.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        GameObjectDraft.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        GameObjectAttack.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        GameObjectFortify.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        gameObjectDraftState.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        gameObjectAttackState.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        gameObjectFortifyState.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        if(BabetteCard.checkTradePossible(bCardList) &&
                gameManager.getGameStage() == 2 &&
                playerTurnPhase == 0 &&
                gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User)
            ButtonTrade.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        /*else
            ButtonBabetteCard.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);*/

        NextButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        GameObjectFramedAvatar.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        playerCounter.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextAlign(Paint.Align.LEFT);

        //Stephen Irons 40204625 and Jake Smyth 40204021
        //This displays the counters and number of resources for each region. Blanks resources when region labels are shown.
        for (int i = 0; i < gameManager.getRegions().size(); i++) {
            gameManager.getRegions().get(i).getRegionCounter().draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
            textPaint.setTextAlign(Paint.Align.CENTER);
            if(!regionLabelsShownOnMap) {
                textPaint.setTextSize(35.0f);textPaint.setColor(Color.WHITE);
                graphics2D.drawText(Integer.toString(gameManager.getRegions().get(i).getNumResources()), gameManager.getRegions().get(i).xCoOrdinate / 1.0f, gameManager.getRegions().get(i).yCoOrdinate / 1.0f + 10.0f, textPaint);
                textPaint.setTextSize(25.0f);textPaint.setColor(Color.BLACK);
                graphics2D.drawText("Area Names", mDefaultLayerViewport.halfWidth * 0.625f, mDefaultLayerViewport.halfHeight * 4.5f, textPaint);
            }
            else {
                textPaint.setTextSize(35.0f);textPaint.setColor(Color.WHITE);
                graphics2D.drawText("", gameManager.getRegions().get(i).xCoOrdinate / 1.0f, gameManager.getRegions().get(i).yCoOrdinate / 1.0f + 10.0f, textPaint);
                textPaint.setTextSize(25.0f);textPaint.setColor(Color.BLACK);
                graphics2D.drawText("Resources", mDefaultLayerViewport.halfWidth * 0.625f, mDefaultLayerViewport.halfHeight * 4.5f, textPaint);
            }
        }

        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(20.0f);
        graphics2D.drawText(attackMessage, 642.0f, 25.0f, textPaint);

        for (int i = 0; i < gameManager.getCurrentPlayers().size(); i++){
            if(gameManager.getCurrentPlayers().get(i) instanceof User)
                bCardList = gameManager.getCurrentPlayers().get(i).cards;
        }
        if(bCardList.size() > 0)
        babetteCard1.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        if(bCardList.size() > 1)
        babetteCard2.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        if(bCardList.size() > 2)
        babetteCard3.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        if(bCardList.size() > 3)
        babetteCard4.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        if(bCardList.size() > 4)
        babetteCard5.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }


    // Methods for altering the information on screen ----------------------------------------------

    // Adam Higgins 40212255 and Stephen Irons 40204625
    // Lets the AI or User continue with the game and decides the appropriate task to be performed
    public void proceedWithGame() {

        // Lets the players fill up the empty regions at the beginning of the game
        if (gameManager.checkEmptyRegionsExist() && gameManager.getGameStage() == 0) {
            fillEmptyRegions();

            if (!gameManager.checkEmptyRegionsExist())
                gameManager.nextStage();
        }

        // Lets the players place the remainder of their resources in the regions they've selected at the beginning of the game
        if (gameManager.getGameStage() == 1) {
            if (resourceCounter <= 120)
                fillPopulatedRegions();
            else
                gameManager.nextStage();
        }

        // Once all regions have been selected and the players have put their resources in, each player can
        // take their turns of drafting, attacking, and fortifying
        if (gameManager.getGameStage() == 2)
            playerGamePlay();

        // Every time a user takes their turn of drafting attacking or fortifying the game checks to see if there
        // are any players with no regions so they can be removed from the game. If there is only one player left
        // the game ends and there is a display to show which player has won the game.
        if (gameManager.getGameStage() == 2)
            checkWinner();

        // Changes the message to show the user what is currently happening in the game
        outputMessageToScreen();
        updateGameBarInterface();

        if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User)
            waitCounter = 1;
        else
            waitCounter = 8;
    }

    // Adam Higgins 40212255
    public void outputMessageToScreen() {
        switch (gameManager.getGameStage()) {
            case 0:
                attackMessage = "Place initial resources";
                break;
            case 1:
                attackMessage = "Place remaining resources (" + resourceCounter + " / 120)";
                break;
            case 2:
                attackMessage = "" + playerMessage;
                break;
            case 3:
                attackMessage = gameManager.getCurrentPlayers().get(0).getUsername().toUpperCase() + " has won the game";
                break;
            default:
                attackMessage = "Game Stage Unknown: Stage " + gameManager.getGameStage();
        }
    }

    // Adam Higgins 40212255
    // Highlights the game objective bar depending on what user input the game is expecting
    public void updateGameBarInterface() {

        if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User) {
            if (playerTurnPhase == 0) {
                draftBool = true;
                attackBool = false;
                fortifyBool = false;
            } else if (playerTurnPhase == 1) {
                draftBool = false;
                attackBool = true;
                fortifyBool = false;
            } else if (playerTurnPhase == 2) {
                draftBool = false;
                attackBool = false;
                fortifyBool = true;
            }
        }
        else {
            draftBool = false;
            attackBool = false;
            fortifyBool = false;
        }

        if (draftBool != draftVisible)
            toggleDraftView();
        if (attackBool != attackVisible)
            toggleAttackView();
        if (fortifyBool != fortifyVisible)
            toggleFortifyView();
    }

    public void toggleDraftView() {
        if (!draftVisible) {
            gameObjectDraftState.position.y = gameObjectDraftState.position.y - 1000;
            draftVisible = true;
        }
        else {
            gameObjectDraftState.position.y = gameObjectDraftState.position.y + 1000;
            draftVisible = false;
        }
    }

    public void toggleAttackView() {
        if (!attackVisible) {
            gameObjectAttackState.position.y = gameObjectAttackState.position.y - 1000;
            attackVisible = true;
        }
        else {
            gameObjectAttackState.position.y = gameObjectAttackState.position.y + 1000;
            attackVisible = false;
        }
    }

    public void toggleFortifyView() {
        if (!fortifyVisible) {
            gameObjectFortifyState.position.y = gameObjectFortifyState.position.y - 1000;
            fortifyVisible = true;
        }
        else {
            gameObjectFortifyState.position.y = gameObjectFortifyState.position.y + 1000;
            fortifyVisible = false;
        }
    }

    //Stephen Irons 40204625
    //Moves he player to the next turn phase
    public void nextTurnPhase() {
        if(playerTurnPhase < 2)
            playerTurnPhase++;
        else
            playerTurnPhase = 0;
        assignCountersToRegions(gameManager.getCurrentPlayers());

        // Highlights the correct section of the game bar interface in the game
       // updateGameBarInterface();
    }

    // Chris Jennings
    public void babetteCardHandler() {
        if(bCardList.size() > 0)
            babetteCard1 = new GameObject(spacingX * 4.3f, spacingY * 0.75f, 20f, 40f,
                bCardList.get(0).bCardBitmap, this);
        if(bCardList.size() > 1)
            babetteCard2 = new GameObject(spacingX * 4.4f, spacingY * 0.95f, 20f, 40f,
                bCardList.get(1).bCardBitmap, this);
        if(bCardList.size() > 2)
            babetteCard3 = new GameObject(spacingX * 4.5f, spacingY * 1.15f, 20f, 40f,
                bCardList.get(2).bCardBitmap, this);
        if(bCardList.size() > 3)
            babetteCard4 = new GameObject(spacingX * 4.6f, spacingY * 1.35f, 20f, 40f,
                bCardList.get(3).bCardBitmap, this);
        if(bCardList.size() > 4)
            babetteCard5 = new GameObject(spacingX * 4.7f, spacingY * 1.55f, 20f, 40f,
                bCardList.get(4).bCardBitmap, this);

        if (ButtonTrade.isPushTriggered() &&
                gameManager.getGameStage() == 2 &&
                playerTurnPhase == 0 &&
                BabetteCard.checkTradePossible(bCardList)) {
            userResourcesToPlaceThisTurn += BabetteCard.resourcesToAssignToUser(gameManager.getTradeCounter());
            gameManager.incrementTradeCounter();
            bCardList = BabetteCard.tradeCardsForResources(bCardList);

            for (int i = 0; i < gameManager.getCurrentPlayers().size(); i++){
                if(gameManager.getCurrentPlayers().get(i) instanceof User)
                    gameManager.getCurrentPlayers().get(i).cards=bCardList;
            }
        }
    }

    // Adam Higgins
    private void regionEnlargementHandler() {
        if (enlargementFlag != regionEnlarged) {
            enlargementFlag = regionEnlarged;
            int sizeIncrement;

            if (regionEnlarged)
                sizeIncrement = 20;
            else
                sizeIncrement = -20;

            for (Region r : gameManager.getRegions()) {
                if (r.getHexCode().equals(regionOne)) {
                    r.regionCounter.setWidth(r.regionCounter.getWidth() + sizeIncrement);
                    r.regionCounter.setHeight(r.regionCounter.getHeight() + sizeIncrement);
                }
            }
        }
    }

    // Jake Smyth
    public void showHideMapLabels() {
        if (!regionLabelsShownOnMap) {
            // Move labels onto the map.
            gBabetteRegionLabels.position.add(-1000f, -1000f);
            regionLabelsShownOnMap = true;
            //Stephen Irons 40204625
            //Move the counters off the map
            for(Region region: gameManager.getRegions()){
                region.getRegionCounter().position.add(-1000f,-1000f);
            }
        }

        else {
            //Move labels off the map.
            gBabetteRegionLabels.position.add(1000f, 1000f);
            regionLabelsShownOnMap = false;
            //Stephen Irons 40204625
            //Move the counters onto the map
            for(Region region: gameManager.getRegions()){
                region.getRegionCounter().position.add(1000f,1000f);
            }
        }
    }

    // Adam Higgins 40212255: a method for turn based region selection until the map is filled
    public void fillEmptyRegions() {
        if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User) {
            if (assignRegionUser((User) gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer())))
                gameManager.nextTurn();
        }

        else if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof AI) {
            assignRegionAI((AI) gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()));
            gameManager.nextTurn();
        }
    }

    // Adam Higgins 40212255
    // Methods to assign regions at the beginning of a game
    public void assignRegionAI(AI ai) {
        ai.placeResourcesInEmptyRegions(gameManager.getRegions());
        assignCountersToRegions(gameManager.getCurrentPlayers());
        resourceCounter++;
    }

    // Adam Higgins 40212255
    public boolean assignRegionUser(User user) {
        for (Region r : gameManager.getRegions()) {
            if (r.getRegionName() == regionSelected) {
                if (r.getNumResources() == 0) {
                    user.placeInitialResources(gameManager.getRegions(), r.hexCode);
                    assignCountersToRegions(gameManager.getCurrentPlayers());
                    resourceCounter++;
                    return true;
                }

                else
                    break;
            }
        }

        return false;
    }

    //Stephen Irons 40204625
    //A method to allow players to place the remaining resources they have onto regions they already own
    public void fillPopulatedRegions() {
        if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User) {
            if (playerInput) {
                for (String str : gameManager.getCurrentUser().regionsHeld) {
                    if (str.equals(gameManager.regionNameToHexCode(regionSelected))) {
                        if (populateRegionUser((User) gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()))) {
                            resourceCounter++;
                            gameManager.nextTurn();
                        }
                    }
                }
            }
        }

        else if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof AI) {
            populateRegionAI((AI) gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()));
            gameManager.nextTurn();
            resourceCounter++;
        }
    }

    //Stephen Irons 40204625
    //Allows the user to place their remaining resources onto the map
    public boolean populateRegionUser(User user){
        for (Region r : gameManager.getRegions()) {
            if (r.getRegionName() == regionSelected) {
                if (r.getNumResources() != 0) {
                    user.placeRemainingResources(gameManager.getRegions(),r.hexCode);
                    return true;
                }

                else
                    break;
            }
        }

        return false;
    }

    //Stephen Irons 40204625
    //Allows the AI to place their remaining resources onto the map
    public void populateRegionAI(AI ai) {
        ai.placeRemainingResources(gameManager.getRegions(), gameManager.getTerritories(), gameManager);
    }

    //Stephen Irons 40204625 and Adam Higgins 40212255
    //Allows the players to carry out the 3 phases of their turn
    public void playerGamePlay() {

        if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User) {
            if (!hasResetUserResources) {
                hasResetUserResources = true;
                userResourcesToPlaceThisTurn = gameManager.getCurrentUser().placeTurnResources(gameManager.getTerritories());
                gameManager.setCardThisTurn(false);
            }

            if (!playerTurnUser()) { // Adam Higgins
                gameManager.nextTurn();
                assignCountersToRegions(gameManager.getCurrentPlayers());
            }
        }

        else if (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof AI)
            playerTurnAI((AI) gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()));
    }

    //Stephen Irons 40204625
    //Allows the AI to carry out the phases of their turn
    public void playerTurnAI(AI ai){
        if(playerTurnPhase == 0) {

            playerMessage = gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).getColour() +" AI draft";
            ai.placeTurnResources(gameManager.getTerritories(), gameManager.getRegions());
            nextTurnPhase();
        }

        else if(playerTurnPhase == 1) {
            playerMessage = gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).getColour() +" AI Attack, attacks remaining: " + aiAttackCounter;
            ai.callForMerger(gameManager.getRegions(), gameManager.getCurrentPlayers(), gameManager.getTerritories(), this.getGame(), gameManager);

            assignCountersToRegions(gameManager.getCurrentPlayers());
            aiAttackCounter--;
            if(aiAttackCounter <= 0) {
                nextTurnPhase();
                aiAttackCounter = gameManager.generateNumber(9,15);
            }
        }

        else if(playerTurnPhase == 2) {
            playerMessage = gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).getColour() +"AI fortify";
            ai.selectFortifyTerritories(gameManager.getRegions());
            nextTurnPhase();
            gameManager.nextTurn();
        }
    }

    //Stephen Irons 40204625 and Adam Higgins 40212255
    //Allows the player to carry out the phases of their turn
    public boolean playerTurnUser(){

        if (playerTurnPhase == 0) {
            playerMessage = "Resources: " + userResourcesToPlaceThisTurn;

            if (BabetteCard.checkTradePossible(bCardList))
                playerMessage += " :=: Trade 3 matching cards for resources";

            if ((userResourcesToPlaceThisTurn > 0) &&
                    (gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()) instanceof User) &&
                    (playerInput)) {
                for (String str : gameManager.getCurrentUser().regionsHeld) {
                    if (str.equals(gameManager.regionNameToHexCode(regionSelected))) {
                        if (populateRegionUser((User) gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer())))
                            userResourcesToPlaceThisTurn--;
                    }
                }
            }

            else if (userResourcesToPlaceThisTurn <= 0){
                nextTurnPhase();
                hasResetUserResources = false;
            }
        }

        else if (playerTurnPhase == 1) {
            playerMessage = "ATTACKING-- From: " + gameManager.hexCodeToRegionName(regionOne) + " To: " + gameManager.hexCodeToRegionName(regionTwo);
            if (regionOne.equals(""))
                assignCountersToRegions(gameManager.getCurrentPlayers());
        }

        else if (playerTurnPhase == 2) {
            playerMessage = "FORTIFYING-- From: " + gameManager.hexCodeToRegionName(regionOne) + " To: " + gameManager.hexCodeToRegionName(regionTwo);

            if (regionOne.equals(""))
                    assignCountersToRegions(gameManager.getCurrentPlayers());

            if (playerFortified) {
                playerFortified = false;
                regionOne = "";
                regionTwo = "";
                return false;
            }
        }

        return true;
    }

    public void userAttack(User user){

        if(regionOne == "" || regionTwo == "") {
            nextTurnPhase();
            regionTwo = "";
            regionOne = "";

            assignCountersToRegions(gameManager.getCurrentPlayers());
        }

        else if(validAttack(user)) {
            if (user.callForMerger(regionOne, regionTwo, gameManager.getRegions(), gameManager.getCurrentPlayers(), this.getGame(), gameManager)) {
                if (!gameManager.getCardThisTurn()) {
                    gameManager.setCardThisTurn(true);
                    gameManager.incrementTradeCounter();
                }
            }
            regionTwo = "";
            regionOne = "";
            assignCountersToRegions(gameManager.getCurrentPlayers());
        }
    }

    // James Sims 40174176
    // Code to call player fortify
    public void userFortify(User user){

        if(regionOne == "" && regionTwo == "") {
            gameManager.nextTurn();
            nextTurnPhase();
            assignCountersToRegions(gameManager.getCurrentPlayers());
            playerFortified = true;
        }

        else if(validFortify(user)) {
            int resourcesToMove = 0;
            for(Region region: gameManager.getRegions()){
                if(region.hexCode == regionOne){
                    resourcesToMove = region.numResources / 2;
                }
            }
            user.fortify(regionOne, regionTwo, gameManager.getRegions(), resourcesToMove);
            assignCountersToRegions(gameManager.getCurrentPlayers());
            playerFortified = true;
        }
    }


    public boolean validAttack(User user){
        if(user.regionsHeld.contains(regionOne) && !user.regionsHeld.contains(regionTwo))
            return true;
        return  false;
    }

    // James Sims 40174176
    // Similar to the above valid attack method, this will check that the selected regions are both the user's
    public boolean validFortify(User user){
        if(user.regionsHeld.contains(regionOne) && user.regionsHeld.contains(regionTwo))
            return true;
        else
            return false;
    }

    // Christopher Jennings 40200418
    // Minor expansions done by Adam Higgins 40212255
    public void checkWinner() {

        /*boolean remove = false;
        String username = "";*/

        if (gameManager.getGameStage() > 1) {
            /*for (Player player : gameManager.getCurrentPlayers()) {
                if (player.regionsHeld.size() < 1) {
                    remove = true;
                    username = player.getUsername();
                    break;
                }
            }*/

            for (int i = 0; i < gameManager.getCurrentPlayers().size(); i++) {
                if (gameManager.getCurrentPlayers().get(i).regionsHeld.size() < 1) {
                    gameManager.playerLoses(gameManager.getCurrentPlayers().get(i).getUsername());
                    break;
                }
            }
        }

        /*if (remove && gameManager.getCurrentPlayers().size() >= 2)
            gameManager.playerLoses(username);*/

        if(gameManager.getCurrentPlayers().size() < 2) {
            gameManager.endGame(gameManager.getCurrentPlayers().get(0));
            gameManager.nextStage();
            waitCounter = -1;
        }
    }

    //Stephen Irons 40204625
    //This code assigns the correct colour of counter to a region
    public void assignCountersToRegions(ArrayList<Player> playerArrayList){
        for(Region region: gameManager.getRegions()){
            if(region.getNumResources()==0)
                region.setRegionCounter(greyCounter(region.xCoOrdinate,region.yCoOrdinate));

            else {
                for(Player player:playerArrayList) {
                    for(String playerRegion:player.regionsHeld) {
                        if(playerRegion == region.getHexCode()) {
                            if (player.getColour() == "green")
                                region.setRegionCounter(greenCounter(region.xCoOrdinate, region.yCoOrdinate));
                            else if (player.getColour() == "red")
                                region.setRegionCounter(redCounter(region.xCoOrdinate, region.yCoOrdinate));
                            else if (player.getColour() == "yellow")
                                region.setRegionCounter(yellowCounter(region.xCoOrdinate, region.yCoOrdinate));
                            else if (player.getColour() == "blue")
                                region.setRegionCounter(blueCounter(region.xCoOrdinate, region.yCoOrdinate));
                            else
                                region.setRegionCounter(greyCounter(region.xCoOrdinate, region.yCoOrdinate));
                        }
                    }
                }
            }
        }
    }

    //Stephen Irons 40204625
    //Method allows a user to see what regions they can attack from a region they have selected
    public void greyOutRegionCounters(String centreRegionHexCode) {
        ArrayList<String> surroundingRegionHexCodes = new ArrayList<>();
        for(Region region: gameManager.getRegions()) {
            if(region.getHexCode()==centreRegionHexCode)
                surroundingRegionHexCodes=region.getAjacentRegions();
        }

        ArrayList<String> attackableRegionHexCodes = new ArrayList<>();
        for(String surroundingRegion: surroundingRegionHexCodes){
            if(!gameManager.getCurrentPlayers().get(gameManager.getCurrentPlayer()).regionsHeld.contains(surroundingRegion))
                attackableRegionHexCodes.add(surroundingRegion);
        }

        attackableRegionHexCodes.add(centreRegionHexCode);

        for(Region setRegionCounter: gameManager.getRegions()){
            if(!attackableRegionHexCodes.contains(setRegionCounter.getHexCode()))
                setRegionCounter.setRegionCounter(greyCounter(setRegionCounter.xCoOrdinate,setRegionCounter.yCoOrdinate));
        }
    }

    //40174176 - James Sims
    //Method to grey out appropriate counters
    public void greyOutForFortify(User user){
        for(Region region: gameManager.getRegions()){
            if(!user.regionsHeld.contains(region.getHexCode())){
                region.setRegionCounter(greyCounter(region.xCoOrdinate,region.yCoOrdinate));
            }
        }
    }


    // Methods for setting up the screen -----------------------------------------------------------
    //Method for loading Assets to the Assets Manager
    //Jake Smyth 40204021
    private void addAssets() {

        //Add the World Map/Dice assets
        //Stephen Geddis40210068
        assetManager.loadAndAddBitmap("WorldMapBackground", "img/WorldMapColouredBackgroundOnly.png");
        assetManager.loadAndAddBitmap("WorldMapText", "img/WorldMapColouredTextOnly.png");
        assetManager.loadAndAddBitmap("WorldMap", "img/WorldMapColouredMapOnly.png");
        assetManager.loadAndAddBitmap("GameObjectiveBar", "img/GameObjectiveBar.png");

        //Add counter assets
        //Stephen Irons 40204625
        assetManager.loadAndAddBitmap("BlueCircle1","img/BlueCircle1.png");
        assetManager.loadAndAddBitmap("GreenCircle1","img/GreenCircle1.png");
        assetManager.loadAndAddBitmap("RedCircle1","img/RedCircle1.png");
        assetManager.loadAndAddBitmap("YellowCircle1", "img/YellowCircle1.png");
        assetManager.loadAndAddBitmap("GreyCircle1", "img/GreyCircle1.png");

        assetManager.loadAndAddBitmap("Attack","img/Attack.png");
        assetManager.loadAndAddBitmap("Draft","img/Draft.png");
        assetManager.loadAndAddBitmap("Fortify","img/Fortify.png");
        assetManager.loadAndAddBitmap("FramedCircle","img/FramedCircle.png");

        assetManager.loadAndAddBitmap("AttackInvert","img/AttackInvert.png");
        assetManager.loadAndAddBitmap("DraftInvert","img/DraftInvert.png");
        assetManager.loadAndAddBitmap("FortifyInvert","img/FortifyInvert.png");

        assetManager.loadAndAddBitmap("Next", "img/Next.png");
        assetManager.loadAndAddBitmap("WildCard", "img/WildCard.png");

        mGame.getAssetManager().loadAssets("txt/assets/BabetteAssets.JSON");
    }

    // Jake Smyth 40204021
    // Creates bitmaps for game screen.
    private void createBitmaps() {
        bitmapBackground = Bitmap.createScaledBitmap(assetManager.getBitmap("BabetteBgScreen"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapText = Bitmap.createScaledBitmap(assetManager.getBitmap("WorldMapText"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapWorldMap = Bitmap.createScaledBitmap(assetManager.getBitmap("WorldMap"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapGameObjectiveBar = Bitmap.createScaledBitmap(assetManager.getBitmap("GameObjectiveBar"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapAttack = Bitmap.createScaledBitmap(assetManager.getBitmap("Attack"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapDraft = Bitmap.createScaledBitmap(assetManager.getBitmap("Draft"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapFortify = Bitmap.createScaledBitmap(assetManager.getBitmap("Fortify"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapFramedAvatar = Bitmap.createScaledBitmap(assetManager.getBitmap("FramedCircle"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapFortifyState = Bitmap.createScaledBitmap(assetManager.getBitmap("FortifyInvert"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapDraftState = Bitmap.createScaledBitmap(assetManager.getBitmap("DraftInvert"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapAttackState = Bitmap.createScaledBitmap(assetManager.getBitmap("AttackInvert"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
    }

    // Jake Smyth 40204021
    // Creates game objects for game screen.
    private void createGameObjects(Game game) {
        gBabetteBackground = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y,
                mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("BabetteBgScreen"), this);
        gBabetteRegionLabels = new GameObject(
                mDefaultLayerViewport.x + 1000f, mDefaultLayerViewport.y + 1000f,
                mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("WorldMapText"), this);
        gBabetteMap = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y,
                mDefaultLayerViewport.getWidth(), mDefaultLayerViewport.getHeight(),
                game.getAssetManager().getBitmap("WorldMap"), this);
        gGameObjectiveBar = new GameObject(
                mDefaultLayerViewport.x, mDefaultLayerViewport.y + 155,
                mDefaultLayerViewport.getWidth()/2 + 50, mDefaultLayerViewport.getHeight()/10 + 10,
                bitmapGameObjectiveBar, this);
    }

    private void createUserNavButtons(){
        GameObjectDraft = new GameObject(spacingX * 2f, spacingY * 0.2f, 50f, 30f,
                bitmapDraft, this);
        GameObjectFortify = new GameObject(spacingX * 3f, spacingY * 0.2f, 50f, 30f,
                bitmapFortify, this);
        GameObjectAttack = new GameObject(spacingX * 2.5f, spacingY * 0.2f, 50f, 30f,
                bitmapAttack, this);

        babetteCard1 = new GameObject(spacingX * 4.75f, spacingY * 0.5f, 20f, 40f,
                bitmapAttack, this);
        babetteCard2 = new GameObject(spacingX * 4.75f, spacingY * 0.8f, 20f, 40f,
                bitmapAttack, this);
        babetteCard3 = new GameObject(spacingX * 4.75f, spacingY * 1.1f, 20f, 40f,
                bitmapAttack, this);
        babetteCard4 = new GameObject(spacingX * 4.75f, spacingY * 1.4f, 20f, 40f,
                bitmapAttack, this);
        babetteCard5 = new GameObject(spacingX * 4.75f, spacingY * 1.7f, 20f, 40f,
                bitmapAttack, this);

        gameObjectDraftState = new GameObject(spacingX * 2f, spacingY * 0.2f + 1000f, 50f, 30f,
                bitmapDraftState, this);
        gameObjectFortifyState= new GameObject(spacingX * 3f, spacingY * 0.2f + 1000f, 50f, 30f,
                bitmapFortifyState, this);
        gameObjectAttackState = new GameObject(spacingX * 2.5f, spacingY * 0.2f + 1000f, 50f, 30f,
                bitmapAttackState, this);

        NextButton = new PushButton(spacingX * 3.35f, spacingY * 0.2f, 35f, 35f,
                "Next", "Next", this);
        GameObjectFramedAvatar= new GameObject(spacingX * 1.65f, spacingY * 0.2f, 35f, 35f,
                bitmapFramedAvatar, this);

        Bitmap playerCounterBitmap = bitmapGreyCounter;
        if(gameManager.getCurrentUser().getColour() == "green"){
            playerCounterBitmap = bitmapGreenCounter;
        }else if(gameManager.getCurrentUser().getColour() == "yellow"){
            playerCounterBitmap = bitmapYellowCounter;
        }else if(gameManager.getCurrentUser().getColour() == "red"){
            playerCounterBitmap = bitmapRedCounter;
        }else if(gameManager.getCurrentUser().getColour() == "blue"){
            playerCounterBitmap = bitmapBlueCounter;
        }
        playerCounter = new Counter(spacingX * 1.65f, spacingY * 0.21f, 37f, 37f, playerCounterBitmap,  this);



        // Jake Smyth 40204021
        // Button for showing/hiding region labels, and a button for trading User Babette cards.
        showAndHideRegionLabels = new PushButton(mDefaultLayerViewport.halfWidth / 5, mDefaultLayerViewport.halfHeight / 5, 75f, 50f,
                "WorldMap", "WorldMap", this);
        /*ButtonBabetteCard = new PushButton(spacingX * 4.5f, mDefaultLayerViewport.halfHeight / 5, 65f, 40f,
                "BabetteCardButton", "BabetteCardButton", this);*/
        ButtonTrade = new PushButton(spacingX * 4.5f, mDefaultLayerViewport.halfHeight / 5, 65f, 40f,
                "TradeButton", "TradeButton", this);
    }

    //Stephen Irons 40204625
    //Method to add the counter bitmaps
    private void createCounterBitmaps() {
        bitmapBlueCounter = Bitmap.createScaledBitmap(assetManager.getBitmap("BlueCircle1"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapRedCounter = Bitmap.createScaledBitmap(assetManager.getBitmap("RedCircle1"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapYellowCounter = Bitmap.createScaledBitmap(assetManager.getBitmap("YellowCircle1"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapGreenCounter = Bitmap.createScaledBitmap(assetManager.getBitmap("GreenCircle1"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
        bitmapGreyCounter = Bitmap.createScaledBitmap(assetManager.getBitmap("GreyCircle1"), (int) mDefaultLayerViewport.getWidth(),
                (int) mDefaultLayerViewport.getHeight(), false);
    }

    //Stephen Irons 40204625
    //Methods to create instances of a new counter button
    private Counter greenCounter(int x, int y){
        if (regionLabelsShownOnMap) {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f + 1000f;
            float xValue = x / 2.5f - 1015.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapGreenCounter, this);
        }
        else {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f;
            float xValue = x / 2.5f - 15.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapGreenCounter, this);
        }
    }

    private Counter blueCounter(int x, int y){
        if (regionLabelsShownOnMap) {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f - 1000f;
            float xValue = x / 2.5f - 1015.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapBlueCounter, this);
        }
        else {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f;
            float xValue = x / 2.5f - 15.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapBlueCounter, this);
        }
    }

    private Counter redCounter(int x, int y){
        if (regionLabelsShownOnMap) {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f - 1000f;
            float xValue = x / 2.5f - 1015.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapRedCounter, this);
        }
        else {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f;
            float xValue = x / 2.5f - 15.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapRedCounter, this);
        }
    }

    private Counter yellowCounter(int x, int y){
        if (regionLabelsShownOnMap) {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f - 1000f;
            float xValue = x / 2.5f - 1015.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapYellowCounter, this);
        }
        else {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f;
            float xValue = x / 2.5f - 15.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapYellowCounter, this);
        }
    }

    private Counter greyCounter(int x, int y){
        if (regionLabelsShownOnMap) {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f - 1000f;
        float xValue = x / 2.5f - 1015.5f;
        return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapGreyCounter, this);
    }
        else {
            float yValue = mDefaultLayerViewport.getHeight() - y / 2.5f;
            float xValue = x / 2.5f - 15.5f;
            return new Counter(xValue, yValue, 25.0f, 25.0f, bitmapGreyCounter, this);
        }
    }
}