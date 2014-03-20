package euchre;

import java.util.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author 151bloomj
 */
public class TextBasedPlayer extends Player
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        Player partner = new AIPlayer("Art Nuhr");
        Player left = new AIPlayer("Ima Tyourleft");
        Player right = new AIPlayer("R. Iteside");
        System.out.println(
                "Hello. Welcome to Euchre. You are playing with " + partner.getName() +
                " against " + left.getName() + " at West and " + right.getName() + " at East. You are South.");
        System.out.println("What is your name?");
        Player thisGuy = new AIPlayer(in.nextLine());
        Player[] players = {thisGuy,left,partner,right};
        Table table = new Table(thisGuy,left,partner,right);
        System.out.println(table);

        System.out.println("Jacking off: ");
        Card jackCard;
        int jackI;
        table.beginJackOff();
        for(jackI = 0;(jackCard = table.jackOff()) != null; jackI++)
        {
            System.out.println(players[jackI%4] + " was dealt " + jackCard);
        }
        System.out.println(players[(--jackI)%4] + " gets first deal.");
        System.out.println();
        
        game:
        while(!table.gameOver())
        {
            System.out.println("Dealer: " + table.getDealer());
            System.out.print("Press Enter to deal:");
            in.nextLine();
            System.out.println("Dealing...");
            Card kitty = table.dealNewRound();
            players = table.getBidPlayers();
            System.out.println("Top of kitty: " + kitty);
            bidding:
            {
                firstRoundOfBidding:
                for(int i = 0; i < 4; i++)
                {
                    Player p = players[i];
                    int whatchaGonnaDo = p.orderUp(kitty);
                    if(whatchaGonnaDo == PASS)
                    {
                        System.out.println(p + " says \"Pass.\"");
                    }
                    else if(whatchaGonnaDo == ORDER_UP)
                    {
                        System.out.println(p + " says \"Pick it up.\"");
                        Player dealer = table.getDealer();
                        table.setTrump(kitty.getSuit(), p, false);
                        System.out.println(dealer + " is picking it up...");
                        dealer.pickItUp(kitty);
                        System.out.println(dealer + " picked it up.");
                        break bidding;
                    }
                    else if(whatchaGonnaDo == GO_ALONE)
                    {
                        System.out.println(p + " says \"Pick it up. I'm going alone.\"");
                        table.setTrump(kitty.getSuit(), p, true);
                        Player dealer = table.getDealer();
                        System.out.println(dealer + " is picking it up...");
                        dealer.pickItUp(kitty);
                        System.out.println(dealer + " picked it up.");
                        break bidding;
                    }
                    else
                    {
                        throw new RuntimeException("Uh oh. OrderItUp returned " + whatchaGonnaDo);
                    }
                }//end firstRoundOfBidding
                secondRoundOfBidding:
                for(int i = 0; i < 3; i++)
                {
                    Player p = players[i];
                    int whatchaGonnaDo = p.call();
                    if(whatchaGonnaDo == PASS)
                    {
                        System.out.println(p + " says \"Pass.\"");
                    }
                    else if(whatchaGonnaDo < GO_ALONE + 4 && whatchaGonnaDo >= 0)
                    {
                        int suit = whatchaGonnaDo % 4;
                        System.out.println(p + " calls " + Card.SUIT_SYMBOLS[suit]);
                        boolean alone = false;
                        if(whatchaGonnaDo >= GO_ALONE)
                        {
                            System.out.println(p + " is going alone.");
                            alone = true;
                        }
                        table.setTrump(suit,p,alone);
                        break bidding;
                    }
                    else
                    {
                        throw new RuntimeException("Uh oh. Call returned " + whatchaGonnaDo);
                    }
                }//end secondRoundOfBidding
                Player dealer = table.getDealer();
                System.out.println(dealer + " is stuck.");
                int choice = dealer.stick();
                assert choice >= 0 && choice <= 7;
                int suit = choice % 4;
                boolean alone = choice >= GO_ALONE;
                table.setTrump(suit, dealer, alone);
                System.out.println(dealer + " calls " + Card.SUIT_SYMBOLS[suit] + (alone ? " alone." : "."));
            }//end bidding

            play:
            {
                System.out.println();
                int trump = table.getTrump();
                while(!table.handOver())
                {
                    Trick trick = new Trick(trump);
                    for(Player p : table.getTrickPlayers())
                    {
                        Card c = p.playCard(trick);
                        trick.addCard(c);
                        System.out.println(p + " played the " + c);
                    }
                    Player winner = table.trick(trick);
                    System.out.println(winner + " took the trick.");
                    System.out.println();
                }
            }//end play

            System.out.println("Hand over.");
            int[] handScore = table.handScore();
            System.out.println("Score: " +
                handScore[0] + "-" + handScore[1]);
            System.out.println(table.handResultString());
            int[] gameScore = table.gameScore();
            System.out.println("Game Score: " +
                gameScore[0] + "-" + gameScore[1]);
        }//end game

        System.out.println("Game over.");
        System.out.println(table.gameResultString());
        int[] gameScore = table.gameScore();
        System.out.println("Game Score: " +
            gameScore[0] + "-" + gameScore[1]);
        System.out.println();
        System.out.println();
    }

    /**
     * Constructs a TextBasedPlayer with default name
     */
    public TextBasedPlayer()
    {
        super();
    }

    /**
     * Constructs a TextBasedPlayer with a name
     * @param n name
     */
    public TextBasedPlayer(String n)
    {
        super(n);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int orderUp(Card top)
    {
        Object[] options = { "Pass", "Order Up Trump", "Go Alone" };
        int result = JOptionPane.showOptionDialog(
            null,
            "Up Card: " + top +
            "\nYour Hand: " + getHand() +
            "\nWhat would you like to do?",
            "Order Up",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
        switch(result)
        {
            case 0:
                return PASS;
            case 1:
                return ORDER_UP;
            case 2:
                return GO_ALONE;
            default:
                return orderUp(top);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void pickItUp(Card c)
    {
        Hand hand = getHand();
        hand.add(c);
        Object[] options = hand.getCards().toArray();
        int discard = JOptionPane.showOptionDialog(
            null,
            "You have picked up " + c +
            ". Which\n Card would you like to discard?",
            "Pick It Up",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            null);
        if(discard == JOptionPane.CLOSED_OPTION)
        {
            pickItUp(c);
            return;
        }
        hand.remove(discard);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int call()
    {
        Object[] options =
        {
            Card.SUIT_SYMBOLS   [Card.SPADES],
            Card.SUIT_SYMBOLS   [Card.HEARTS],
            Card.SUIT_SYMBOLS   [Card.CLUBS],
            Card.SUIT_SYMBOLS   [Card.DIAMONDS],
            Card.SUIT_SYMBOLS   [Card.SPADES]   + " alone",
            Card.SUIT_SYMBOLS   [Card.HEARTS]   + " alone",
            Card.SUIT_SYMBOLS   [Card.CLUBS]    + " alone",
            Card.SUIT_SYMBOLS   [Card.DIAMONDS] + " alone",
            "Pass"//////////////////////////////////////////,
        };
        int call = JOptionPane.showOptionDialog(
            null,
            getHand() + "\nWhat would you like to call?",
            "Call",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            null);
        if(call == JOptionPane.CLOSED_OPTION)
        {
            return call();
        }
        if(call == 8)
        {
            return PASS;
        }
        return call;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int stick()
    {
        Object[] options =
        {
            Card.SUIT_SYMBOLS   [Card.SPADES],
            Card.SUIT_SYMBOLS   [Card.HEARTS],
            Card.SUIT_SYMBOLS   [Card.CLUBS],
            Card.SUIT_SYMBOLS   [Card.DIAMONDS],
            Card.SUIT_SYMBOLS   [Card.SPADES]   + " alone",
            Card.SUIT_SYMBOLS   [Card.HEARTS]   + " alone",
            Card.SUIT_SYMBOLS   [Card.CLUBS]    + " alone",
            Card.SUIT_SYMBOLS   [Card.DIAMONDS] + " alone",
        };
        int call = JOptionPane.showOptionDialog(
            null,
            getHand() + "\nYou are stuck. What do you call?",
            "Stuck",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            null);
        if(call == JOptionPane.CLOSED_OPTION)
        {
            return stick();
        }
        return call;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Card playCard(Trick t)
    {
        Hand hand = getHand();
        Card[] options = hand.getCards().toArray(new Card[0]);
        int result = JOptionPane.showOptionDialog(
            null,
            "Trick on the table: " + t,
            "Play",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            null);
        if(result == JOptionPane.CLOSED_OPTION)
        {
            return playCard(t);
        }
        ArrayList<Card> trickCards = t.getCards();
        if(trickCards.isEmpty())
        {
            return hand.remove(result);
        }
        int trump = t.getTrump();
        int leadSuit =
            //if lefty
            trickCards.get(0).getEuchreRank(trump) == 1 ? trump : trickCards.get(0).getSuit();
        Card selection = hand.get(result);
        //if following suit
        if(selection.getEuchreRank(trump) == 1 ? leadSuit == trump : leadSuit == selection.getSuit())
        {
            return hand.remove(result);
        }
        //if not following suit, check for if could have
        for(Card c : options)
        {
            //if could have followed suit
            if(c.getEuchreRank(trump) == 1 ? leadSuit == trump : c.getSuit() == leadSuit)
            {
                JOptionPane.showMessageDialog(null, "Need to follow suit.", "Renege", JOptionPane.ERROR_MESSAGE);
                return playCard(t);
            }
        }
        //if not following suit and couldn't have,
        return hand.remove(result);
    }
}