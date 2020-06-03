package uk.ac.qub.eeecs.Babette;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.qub.eeecs.gage.Game;

public class DemoGame extends Game {
    private GameManager gameManager;

    public DemoGame() {
        super();
        gameManager = new GameManager();
        gameManager.setContext(this.getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go with a default 20 UPS/FPS
        setTargetFramesPerSecond(20);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Call the Game's onCreateView to get the view to be returned.
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create and add a stub game screen to the screen manager. We don't
        // want to do this within the onCreate method as the menu screen
        // will layout the buttons based on the size of the view.
        SplashScreen splashScreen = new SplashScreen(this, gameManager);
        mScreenManager.addScreen(splashScreen);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if (mScreenManager.getCurrentScreen().getName().equals("SplashScreen"))
            return false;

        // Stop any playing music
        if(mAudioManager.isMusicPlaying())
            mAudioManager.stopMusic();

        // Go back to the menu screen
        getScreenManager().removeAllScreens();
        SplashScreen splashScreen = new SplashScreen(this, gameManager);
        getScreenManager().addScreen(splashScreen);

        return true;
    }
}