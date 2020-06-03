package uk.ac.qub.eeecs.Babette;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 *
 *
 * This class is used for counters to be created from
 *
 * @author  James Sims
 * @version 6.0
 *
 */

public class Counter extends GameObject {
    String colour;
    int xCoordinate;
    int yCoordinate;
    String counterBitmap;

    public Counter(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen){
        super(x,  y, width, height, bitmap, gameScreen);
    }

    public void updateOwner(String newColour, String newCounterBitmap){
        this.colour = newColour;
        this.counterBitmap = newCounterBitmap;
    }

    public String getColour() {
        return colour;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public String getCounterBitmap() {
        return counterBitmap;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setCounterBitmap(String counterBitmap) {
        this.counterBitmap = counterBitmap;
    }
}
