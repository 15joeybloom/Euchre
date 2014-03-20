package euchre;

import java.util.*;

/**
 * @author Joey Bloom
 *
 */
public class Trick
{
    private ArrayList<Card> cards;
    private byte trump;
    private boolean trumped;
    private int winner;

    /**
     * Constructs an empty Trick
     * @param trump trump suit
     */
    public Trick(int trump)
    {
        cards = new ArrayList<>();
        this.trump = (byte) trump;
        trumped = false;
    }

    /**
     * Returns one of the suit constants from Card
     * @return the trump suit
     */
    public byte getTrump()
    {
        return trump;
    }

    /**
     * returns the Cards in this Trick
     * @return cards
     */
    public ArrayList<Card> getCards()
    {
        return cards;
    }

    /**
     * Adds a Card to the Trick. Updates the winner if necessary
     * @param Card to add
     */
    public void addCard(Card c)
    {
        cards.add(c);
        int winnerRank = cards.get(winner).getEuchreRank(getTrump());
        int cRank = c.getEuchreRank(getTrump());
        if( trumped && cRank < winnerRank)
        {
            winner = cards.size() - 1;
        }
        //if the card is trump
        else if(!trumped && cRank < 7)
        {
            winner = cards.size() - 1;
            trumped = true;
        }
        //if the card follows suit
        else if(c.getSuit() == cards.get(0).getSuit() && cRank < winnerRank)
        {
            winner = cards.size() - 1;
        }
    }

    /**
     * returns the stored winner of the Trick
     * @return index of the winner Card
     */
    public int winner()
    {
        return winner;
    }

    /**
     * returns a String representation of the Trick
     * @return string
     */
    @Override
    public String toString()
    {
        String returnMe = "";
        for(Card c : cards)
        {
            returnMe += c + " ";
        }
        returnMe += "\nTrump: " + Card.SUIT_SYMBOLS[getTrump()];
        return returnMe;
    }
}
