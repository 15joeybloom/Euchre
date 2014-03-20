package euchre;

import java.util.*;

/**
 * @author Joey Bloom
 *
 */
public class Hand
{
    private ArrayList<Card> cards;
    private boolean sorted;

    private static CardComparator[] comps = new CardComparator[4];
    static
    {
        for(int i = 0; i < 4; i++)
        {
            comps[i] = new CardComparator(i);
        }
    }

    /**
     * Constructs an empty Hand
     */
    public Hand()
    {
        cards = new ArrayList<>(5);
        sorted = false;
    }

    /**
     * Adds a Card to the Hand
     */
    public void add(Card c)
    {
        cards.add(c);
        sorted = false;
    }

    /**
     * @return a deep copy of the Cards in this Hand
     */
    public ArrayList<Card> getCards()
    {
        ArrayList<Card> returnMe = new ArrayList<>();
        for(Card c : cards)
        {
            returnMe.add(c);
        }
        return returnMe;
    }

    /**
     * Get a clone of a Card
     * @param n the index of the Card to get
     * @return a clone of the nth Card
     */
    public Card get(int n)
    {
        return new Card(cards.get(n));
    }

    /**
     * Removes a Card from the Hand
     * @param n the index of the Card to be removed
     * @return the removed Card
     */
    public Card remove(int n)
    {
        return cards.remove(n);
    }

    /**
     * Clears the Hand
     */
    public void clear()
    {
        cards.clear();
    }

    /**
     * Sorts the Cards in the Hand based on the given trump suit by suit and then decreasing rank
     * @param trump sorts the Hand placing Cards of this trump at the lowest indicies.
     */
    public void sort(int trump)
    {
        if(sorted)
        {
            return;
        }
        Collections.sort(cards,comps[trump]);
//        ArrayList<Card> temp = new ArrayList<>();
//        ArrayList<Integer> ranks = new ArrayList<>();
//        for(int i = 0; i < cards.size(); i++)
//        {
//            ranks.add(getSortRank(cards.get(i),trump));
//        }
//        while(!cards.isEmpty())
//        {
//            int minValue = 25;
//            int minIndex = 0;
//            for(int i = 0; i < ranks.size(); i++)
//            {
//                int r = ranks.get(i);
//                if(r < minValue)
//                {
//                    minIndex = i;
//                    minValue = r;
//                }
//            }
//            temp.add(cards.remove(minIndex));
//            ranks.remove(minIndex);
//        }
//        cards = temp;
        sorted = true;
    }

    /**
     * Compares Cards, taking trump into account.
     */
    private static class CardComparator implements Comparator<Card>
    {
        private int trump;

        /**
         * Constructs a CardComparator that compares according to the given trump
         * @param t trump
         */
        public CardComparator(int t)
        {
            trump = t;
        }

        /**
         * @return trump
         */
        public int getTrump()
        {
            return trump;
        }

        /**
         * Compares o1 to o2, given the trump suit of this comparator.
         * @param o1 first Card
         * @param o2 second Card
         * @return 0 if the cards are the same card, 1 if o1 is better than o2 in trump, and -1 otherwise.
         */
        @Override
        public int compare(Card o1, Card o2)
        {
            if(o1.equals(o2))
            {
                return 0;
            }
            return getSortRank(o1,trump) - getSortRank(o2,trump);
        }

        /**
         * Returns the rank of a Card, for the purpose of sorting
         * @param c Card analyze
         * @param trump trump suit
         * @return sorting rank
         */
        private int getSortRank(Card c, int trump)
        {
            int x = c.getEuchreRank(trump);
            if(x < 7)
            {
                return x;
            }
            int a = c.getSuit() - trump - 1;
            a += 4;
            a %= 4;
            a *= 6;
            a += x;
            return a;
        }
    }

    /**
     * Returns a String representing the Hand
     * @return a String of the Cards of the Hand
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
}
