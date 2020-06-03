package uk.ac.qub.eeecs.Babette;

import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * An exceedingly basic menu screen with a couple of touch buttons
 *
 * @version 1.0
 */
public class StartScreen extends GameScreen {
    private GameObject mBackground;
    private PushButton mPlayButton;
    private GameManager gameManager;

    public StartScreen(Game game, GameManager gm) {
        super("StartScreen", game);
        gameManager = gm;
        gameManager.setContext(this.getGame().getActivity());

        // Load in the bitmaps used on the main menu screen
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("babetteLogoV2", "img/babetteLogoV2.png");
        assetManager.loadAndAddBitmap("PLAYinvert", "img/PLAYinvert.png");
        assetManager.loadAndAddBitmap("PLAY", "img/PLAY.png");

        // Define the spacing that will be used to position the buttons
        int spacingX = (int)mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int)mDefaultLayerViewport.getHeight() / 3;

        // Create the trigger buttons
        mBackground = new GameObject(spacingX * 2.5f, spacingY * 1.8f, 700f, 400f,
                game.getAssetManager().getBitmap("babetteLogoV2"), this);
        mPlayButton = new PushButton(spacingX * 2.50f, spacingY * 0.8f, 100.0f, 60.0f,
                "PLAY", "PLAYinvert", this);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            mPlayButton.update(elapsedTime);

            if (mPlayButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new LoginScreen(mGame, gameManager));
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        mBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mPlayButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}
