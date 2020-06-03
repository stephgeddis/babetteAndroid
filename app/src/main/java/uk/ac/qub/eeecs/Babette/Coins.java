package uk.ac.qub.eeecs.Babette;

/**
 * This class is used in order to create a coin object for
 * the slot machine game screen
 *
 * @author  Stephen Geddis 40210068
 * @version 6.0
 *
 */
public class Coins {

    private int coinAmount;

    public Coins(int coinAmount){
        this.coinAmount = coinAmount;
    }

    public int getCoinAmount(){ return this.coinAmount; }

    public void setCoinAmount(){ this.coinAmount = coinAmount; }

    public void reduceCoins(){ this.coinAmount-=1; }

    public void increaseCoins(){ this.coinAmount+=2; }

}

