package uk.ac.qub.eeecs.Babette;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

//Christopher Jennings 40200418
//Class that is needed for the keyboard that is used in the create account screen
//References Philip Hannahs code from a lecture on building a keyboard.

public class Key extends PushButton {
    // Properties:
    private String mKeyChar;
    private Paint mKeyPaint;

    private StringBuffer mLinkedStringBuffer = null;
    // Constructors

    public Key(float x, float y,
               float width, float height, char keyChar, GameScreen gameScreen) {
        super(x, y, width, height, "Key11", gameScreen);

        processInLayerSpace(true);

        float fontSize =
                ViewportHelper.convertXDistanceFromLayerToScreen(
                        height, gameScreen.getDefaultLayerViewport(),
                        gameScreen.getDefaultScreenViewport());

        mKeyChar = String.valueOf(keyChar);

        mKeyPaint = new Paint();
        mKeyPaint.setTextSize(40.0f);
        mKeyPaint.setTextAlign(Paint.Align.CENTER);
        mKeyPaint.setColor(Color.BLACK);
        mKeyPaint.setTypeface(Typeface.SANS_SERIF);
    }

    //Christopher Jennings 40200418
    //Implemented method for keyboard

    public void setLinkedStringBuffer(StringBuffer linkedStringBuffer) {
        mLinkedStringBuffer = linkedStringBuffer;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);

        if(mLinkedStringBuffer != null && isPushTriggered()) {
            mLinkedStringBuffer.append(mKeyChar);
        }
    }

    Rect textBounds = new Rect();
    Vector2 screenPos = new Vector2();

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        ViewportHelper.convertLayerPosIntoScreen(layerViewport,
                mBound.x, mBound.y, screenViewport, screenPos);

        // Align in the middle of the y-axis
        mKeyPaint.getTextBounds(mKeyChar, 0, mKeyChar.length(), textBounds);
        screenPos.y -= textBounds.exactCenterY();

        graphics2D.drawText(mKeyChar, screenPos.x, screenPos.y, mKeyPaint);
    }

}
