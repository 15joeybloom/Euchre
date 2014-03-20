package euchre;

import java.util.*;

/**
 *
 * @author 151bloomj
 */
public class AIPlayer extends Player
{
    /**
     * Constructs an AIPlayer with name "Arty Fishal"
     */
    public AIPlayer()
    {
        this("Arty Fishal");
    }

    /**
     * Constructs an AIPlayer with a name
     */
    public AIPlayer(String name)
    {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int orderUp(Card top)
    {
        Hand hand = getHand();
        ArrayList<Card> cards = hand.getCards();
        int trump = top.getSuit();
        boolean righty = false;
        boolean lefty = false;
        boolean ace = false;
        int trumps = 0;
        int offSuitAces = 0;
        for(int i = 0; i < cards.size(); i++)
        {
            int cRank = cards.get(i).getEuchreRank(trump);
            switch(cRank)
            {
                case 0:
                    righty = true;
                    break;
                case 1:
                    lefty = true;
                    break;
                case 2:
                    ace = true;
                    break;
                case 7:
                    offSuitAces++;
                    break;
            }
            if(cRank < 7)
            {
                trumps++;
            }
        }
        if(!righty && !lefty)
        {
            return PASS;
        }
        int highTrumps = (righty ? 1 : 0) + (lefty ? 1 : 0) + (ace ? 1 : 0);
        if( highTrumps >= 2)
        {
            if(highTrumps == 3 || (offSuitAces >= 2 && righty))
            {
                return GO_ALONE;
            }
            if(righty)
            {
                return ORDER_UP;
            }
        }
        if(righty && ((offSuitAces >= 1 && trumps >= 3) || offSuitAces >= 2))
        {
            return ORDER_UP;
        }
        return PASS;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void pickItUp(Card c)
    {
        Hand hand = getHand();
        hand.add(c);
        ArrayList<Card> cards = hand.getCards();
        int trump = c.getSuit();
        int indexToToss = 0;
        int rank = 14;
        for(int i = 0; i < cards.size(); i++)
        {
            int x = cards.get(i).getEuchreRank(trump);
            if(x < rank)
            {
                indexToToss = i;
                rank = x;
            }
        }
        hand.remove(indexToToss);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int call()
    {
        if(!hasJack())
        {
            return PASS;
        }
        //ideas: count # of trumps in each suit
        //       pick trump based on lowest sum of euchreRanks
        int bestSuit = -1;
        int result = PASS;
        for(int i = 0; i < 4; i++)
        {
            int x = orderUp(new Card(9,i));
            if(x > result)
            {
                bestSuit = i;
                result = x;
            }
        }
        return result == PASS ? PASS : result + bestSuit;
    }

    /**
     * returns true if the player has a jack in his hand
     * @return if has a jack
     */
    private boolean hasJack()
    {
        ArrayList<Card> cards = getHand().getCards();
        for(Card c : cards)
        {
            if(c.getRank() == Card.JACK)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int stick()
    {
        //if would call anyway (if not stuck), then do so.
        int call = call();
        if(call != PASS)
        {
            return call;
        }
        Hand hand = getHand();
        ArrayList<Card> cards = hand.getCards();

        //idea here is to collect info
        //which jacks one has, how many jacks,
        //and how many of each trump one has
        boolean[] jacks = {false,false,false,false};
        byte numJacks = 0;
        byte[] trumps = new byte[4];
        for(Card c : cards)
        {
            int suit = c.getSuit();
            trumps[suit]++;
            if(c.getRank() == Card.JACK)
            {
                jacks[suit] = true;
                trumps[(suit+2)%4]++;
                numJacks++;
            }
        }

        //if no jacks or all jacks, pick most trump
        if(numJacks == 0 || numJacks == 4)
        {
            int maxIndex = 0;
            int max = 0;
            for(int i = 0; i < trumps.length; i++)
            {
                int x = trumps[i];
                if(x > max)
                {
                    max = x;
                    maxIndex = i;
                }
            }
            return maxIndex;
        }
        //if one jack, return the suit of that jack
        else if(numJacks == 1)
        {
            for(int i = 0; i < jacks.length; i++)
            {
                if(jacks[i])
                {
                    return i;
                }
            }
            throw new RuntimeException("Error in numJacks == 1");
        }
        //if two or three jacks, pick the one with righty and
        //most trump
        else
        {
            int maxIndex = 0;
            int max = 0;
            for(int i = 0; i < 4; i++)
            {
                if(jacks[i] && trumps[i] > max)
                {
                    maxIndex = i;
                    max = trumps[i];
                }
            }
            return maxIndex;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Card playCard(Trick t)
    {
        Hand hand = getHand();
        int trump = t.getTrump();
        hand.sort(trump);

        ArrayList<Card> cards = hand.getCards();
        ArrayList<Card> trickCards = t.getCards();
        if(trickCards.isEmpty())
        {
            int bestIndex = 0;
            int bestValue = 14;
            for(int i = 0; i < cards.size(); i++)
            {
                int r = cards.get(i).getEuchreRank(trump);
                if(r < bestValue)
                {
                    bestValue = r;
                    bestIndex = i;
                }
            }
            return hand.remove(bestIndex);
        }
        Card lead = trickCards.get(0);
        int leadSuit = lead.getEuchreRank(trump) == 1 ? trump : lead.getSuit();
        int winnerRank = trickCards.get(t.winner()).getEuchreRank(trump);

        int[] euchreRanks = new int[cards.size()];
        boolean suited = false;
        for(int i = 0; i < euchreRanks.length; i++)
        {
            Card c = cards.get(i);
            euchreRanks[i] = c.getEuchreRank(trump);
            if(euchreRanks[i] == 1 ? leadSuit == trump : leadSuit == c.getSuit())
            {
                suited = true;
            }
        }
        if(suited)
        {
            //iterate through the hand backwards so as to pick the lowest possible Card
            for(int i = euchreRanks.length - 1; i >= 0; i--)
            {
                Card c = cards.get(i);
                //if can beat winner
                if(leadSuit == c.getSuit() && euchreRanks[i] < winnerRank)
                {
                    return hand.remove(i);
                }
            }
            for(int i = euchreRanks.length - 1; i >= 0; i--)
            {
                //dump worst card while still following suit
                int suit = euchreRanks[i] == 1 ? trump : cards.get(i).getSuit(); //the suit of lefty is trump
                if(leadSuit == suit)
                {
                    return hand.remove(i);
                }
            }
            System.err.println("Joey. This is bad. Help me. AIPlayer.playCard(Trick) returned null.");
            return null;
        }
        else
        {
            //win if possible
            for(int i = euchreRanks.length - 1; i >= 0; i--)
            {
                int s = cards.get(i).getSuit();
                int r = euchreRanks[i];
                if((s == leadSuit || r < 7) && r < winnerRank)
                {
                    return hand.remove(i);
                }
            }
            //dump worst Card
            int worstIndex = 0;
            int worstValue = -1;
            for(int i = 0; i < euchreRanks.length; i++)
            {
                int v = euchreRanks[i];
                if(v > worstValue)
                {
                    worstIndex = i;
                    worstValue = v;
                }
            }
            return hand.remove(worstIndex);
        }
    }

}
