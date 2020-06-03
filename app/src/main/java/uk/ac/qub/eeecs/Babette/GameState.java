package uk.ac.qub.eeecs.Babette;

import java.util.ArrayList;

// Created and written by Adam Higgins 40212255:  An encapsulated way of storing information about a game instance
public class GameState {

    private ArrayList<Player> players;
    private int currentPlayer;
    private int gameStage;

    // Constructor
    public GameState() {
        players = new ArrayList<>(0);
        currentPlayer = 0;
        gameStage = 0;
    }

    // Methods
    // Adam Higgins 40212255
    public int getGameStage() {
        return gameStage;
    }

    // Adam Higgins 40212255
    public void setStartingPlayers(ArrayList<Player> newList) {
        players = newList;
    }

    // Adam Higgins 40212255
    public ArrayList<Player> getPlayers() {
        return players;
    }

    // Adam Higgins 40212255
    public int numberOfPlayers() {
        return players.size();
    }

    // Adam Higgins 40212255
    public void addPlayerToGame(Player player) {
        players.add(player);
    }

    // Adam Higgins 40212255
    public void kickPlayerFromGame(String username) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(username)) {
                players.remove(i);
                if (currentPlayer == 0)
                    currentPlayer = players.size() - 1;
                else
                    currentPlayer--;
            }
        }
    }

    // Adam Higgins 40212255
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    // Adam Higgins 40212255: updates which players turn it is
    public void nextTurn() {
        if (currentPlayer < players.size() - 1)
            currentPlayer++;
        else
            currentPlayer = 0;
    }

    // Adam Higgins 40212255
    public void nextStage() {
        gameStage++;
    }
}
