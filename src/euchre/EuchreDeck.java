package euchre;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author 151bloomj
 * A EuchreDeck has Cards, starting with one of each of the 24 unique cards
 * ranks 9-A in each suit. One may get a Card from any position in the EuchreDeck,
 * remove the top Card from a EuchreDeck, get the size of the EuchreDeck, shuffle the
 * EuchreDeck, and get whether the EuchreDeck is empty.
 */
public class EuchreDeck
{
    private ArrayList<Card> cards = new ArrayList<Card>();

    public static final int CARDS_IN_A_DECK = 24;

    /**
     * Constructs a full EuchreDeck. Just like when you get a physical EuchreDeck of Cards,
     * it does not come pre-shuffled. You do it.
     */
    public EuchreDeck()
    {
        for(int s = 0; s < 4; s++)
        {
            cards.add(new Card(1,s));
            for(int r = 9; r <= 13; r++)
            {
                cards.add(new Card(r,s));
            }
        }
    }

    /**
     * Shuffles the EuchreDeck
     */
    public void shuffle()
    {
        Random rand = new Random();
        ArrayList<Card> shuffledDeck = new ArrayList<>();
        while(!cards.isEmpty())
        {
            shuffledDeck.add(cards.remove(rand.nextInt(cards.size())));
        }
        cards = shuffledDeck;
    }

    /**
     * Returns a String showing all the cards in the EuchreDeck, in order starting
     * at the bottom of the EuchreDeck. The last Card is the top Card.
     * @return a String of the EuchreDeck
     */
    @Override
    public String toString()
    {
        String returnMe = "";
        for(Card c : cards)
        {
            returnMe += c + " ";
        }
        return returnMe;
    }

    /**
     * Takes a Card off the top of the EuchreDeck. The Card is removed from the EuchreDeck.
     * @return Card at the top of the EuchreDeck, or null if <code> isEmpty() == true </code>.
     */
    public Card pop()
    {
        if(!isEmpty())
        {
            return cards.remove(cards.size()-1);
        }
        else
        {
            return null;
        }
    }

    /**
     * Get a Card from the EuchreDeck. The Card is not removed from the EuchreDeck.
     * @param n position in deck to get Card from. 0 is the bottom, this.size() - 1 is the top.
     * @return Card at position n
     */
    public Card get(int n)
    {
        return cards.get(n);
    }

    /**
     * returns the number of cards in the EuchreDeck
     * @return size
     */
    public int size()
    {
        return cards.size();
    }

    /**
     * @return true if the EuchreDeck is empty
     */
    public boolean isEmpty()
    {
        return cards.isEmpty();
    }
}
