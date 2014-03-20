package euchre;

import java.util.*;
import java.util.Arrays;

/**
 * @author Joey Bloom
 *
 */
public class Table
{
    private Player[]    players;

    private int         dealer;     //absolute
    private int         leader;     //absolute
    private int         caller;     //absolute
    private int         leftOut;    //absolute

    private EuchreDeck  deck;

    private int[]       handScore;
    private int[]       gameScore;

    private byte        trump;

    private static final int SOUTH = 0;
    private static final int WEST = 1;
    private static final int NORTH = 2;
    private static final int EAST = 3;

    /**
     * Used to access the score of the north south team
     */
    public static final int NORTH_SOUTH = 0;
    /**
     * Used to access the score of the east west team
     */
    public static final int EAST_WEST = 1;


    /**
     * Constructs a player with 4 players, each at the four different positions
     * @param south north's partner
     * @param west east's partner
     * @param north south's partner
     * @param east west's partner
     */
    public Table(Player south, Player west, Player north, Player east)
    {
        players = new Player[4];
        players[SOUTH] = south;
        players[WEST] = west;
        players[NORTH] = north;
        players[EAST] = east;
        deck = new EuchreDeck();
        deck.shuffle();
        dealer = 0;
        handScore = new int[]{0,0};
        gameScore = new int[]{0,0};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "South: " + players[SOUTH] +
               "\nWest: " + players[WEST] +
               "\nNorth: " + players[NORTH] +
               "\nEast: " + players[EAST] +
               "\n";
    }

    private byte jackPlayer = -1;
    /**
     * Call this to begin jacking off.
     * @see jackOff()
     */
    public void beginJackOff()
    {
        jackPlayer = 0;
        deck = new EuchreDeck();
        deck.shuffle();
    }

    /**
     * Returns the next Card dealt in the
     * "Jack off" or null if the jack off is over
     * @return next Card in Jackoff or null
     */
    public Card jackOff()
    {
        if(jackPlayer == -1)
        {
            return null;
        }
        Card c = deck.pop();
        if(c.getRank() == Card.JACK)
        {
            dealer = jackPlayer;
            jackPlayer = -1;
        }
        else
        {
            jackPlayer++;
            jackPlayer %= 4;
        }
        return c;
    }

    /**
     * Returns true if either team's score
     * is 10 or greater, false otherwise
     * @return true if the game is over, false otherwise
     */
    public boolean gameOver()
    {
        int[] score = gameScore();
        for(int x : score)
        {
            if(x >= 10)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Deals a full 5 Card Hand to each player, and returns the top
     * Card of the kitty, and resets the Table.
     *
     * The Table's deck is reset, shuffled, and then Cards are
     * dealt using the standard 23233232 pattern. The Hand
     * score is reset. Going alone and leader are reset.
     * @return the top Card of the kitty
     */
    public Card dealNewRound()
    {
        handScore = new int[]{0,0};
        leftOut = -1;
        leader = (dealer + 1)%4;
        for(Player p : players)
        {
            p.getHand().clear();
        }

        deck = new EuchreDeck();
        deck.shuffle();
        int[] dealNums = {2,3,2,3,3,2,3,2};
        Player[] temp = getBidPlayers();
        for(int i = 0; i < 8; i++)
        {
            Player p = temp[(i+dealer)%4];
            int x = dealNums[i];
            for(int j = 0; j < x; j++)
            {
                p.dealCards(deck.pop());
            }
        }
        return deck.pop();
    }

    /**
     * Returns the Players in the order they bid
     * @return  the Players, starting with the Player to
     *          the left of the dealer and ending with
     *          the dealer.
     */
    public Player[] getBidPlayers()
    {
        Player[] temp = new Player[4];
        System.arraycopy(players, 0, temp, 0, 4);
        Collections.rotate(Arrays.asList(temp),3-dealer);
        return temp;
    }

    /**
     * Returns the dealer
     * @return  the dealer
     */
    public Player getDealer()
    {
        return players[dealer];
    }

    /**
     * Sets the trump for the hand
     * @param   suit    one of the suit constants in Card
     * @param   p       the Player that called trump
     * @see Card
     */
    public void setTrump(int suit, Player p, boolean alone)
    {
        trump = (byte)suit;
        caller = indexOfPlayer(p);
        if(alone)
        {
            goAlone(caller);
        }
    }

    /**
     * Tells the Table that the Player is going alone
     * @param p     the position of the Player that is going alone,
     *              expressed as number of seats to the left of dealer.
     */
    private void goAlone(int p)
    {
        //sets leftOut to the absolute position of the person left out
        leftOut = (p + 2) % 4;
        //if the leader is now leftOut, increment
        //leader to next Player to the left
        if(leader == leftOut)
        {
            leader = (leader+1)%4;
        }
    }

    /**
     * Returns the current trump suit
     * @return trump, one of the suit constants in Card
     */
    public int getTrump()
    {
        return trump;
    }

    /**
     * Returns true if the hand is over, or false otherwise
     * @return true if the hand is over
     */
    public boolean handOver()
    {
        return handScore[0] + handScore[1] >=5;
    }

    /**
     * Returns the Players participating in the next trick, in
     * the order in which they will participate.
     * @return  the Players in the next trick, taking order and any
     *          going alone into account. The size of the returned
     *          array will be 3 or 4.
     */
    public Player[] getTrickPlayers()
    {
        Player[] returnMe;
        if(leftOut == -1)
        {
            returnMe = new Player[4];
            for(int i = 0; i < 4; i++)
            {
                returnMe[i] = players[(leader+i)%4];
            }
        }
        else
        {
            returnMe = new Player[3];
            for(int i = 0, j = 0; i < 3; i++, j++)
            {
                if(leftOut == (j + leader) % 4)
                {
                    j++;
                }
                returnMe[i] = players[(j+leader)%4];
            }
        }
        return returnMe;
    }

    /**
     * Call this method to give the table a completed Trick, with
     * the Cards played in the order returned by getTrickPlayers().
     * getTrickPlayers(), after calling this method, will use
     * the winner of the trick passed to this method as the leader
     * of the trick. The hand score is also updated appropriately.
     *
     * @param   trick
     *          the Trick played by the players as returned by
     *          getTrickPlayers()
     *
     * @return  the Player that won the trick
     * @see     getTrickPlayers()
     */
    public Player trick(Trick trick)
    {
        int winI = trick.winner();
        Player p = getTrickPlayers()[winI];
        leader = indexOfPlayer(p);
        handScore[leader%2]++;
        return p;
    }

    /**
     * @param p Player to search for
     * @return the index of p or -1 if not found
     */
    private int indexOfPlayer(Player p)
    {
        for(int i = 0; i < 4; i++)
        {
            if(players[i] == p)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the score of the current hand, as an
     * array of size two, of the form {south-north, west-east}
     * @return  the score of the hand. One accesses
     *          scores using the NORTH_SOUTH and EAST-WEST
     *          constants.
     *
     * @see NORTH_SOUTH, EAST_WEST
     */
    public int[] handScore()
    {
        return handScore;
    }

    /**
     * Returns the result of the hand, including
     * who won, how they won, who won points, and
     * how many points they won. Only call this method
     * once per hand at the end of the hand; it increments
     * the score and dealer position as well.
     * @return result of the hand
     */
    public String handResultString()
    {
        if(!handOver())
        {
            return "";
        }
        dealer = (dealer + 1)%4;
        String returnMe = "";
        int i = caller%2;
        Player call = players[caller];
        String callerTeamName = call + " and " + players[(caller+2)%4];
        String defendingTeamName = players[(caller+1)%4] + " and " + players[(caller+3)%4];
        if(leftOut == -1)
        {
            switch(handScore[caller%2])
            {
                case 0:
                    gameScore[1-i] += 4;
                    returnMe = callerTeamName + " were swept. 4 points to " + defendingTeamName + ".";
                    break;
                case 1:
                case 2:
                    gameScore[1-i] += 2;
                    returnMe = callerTeamName + " were euched. 2 points to " + defendingTeamName + ".";
                    break;
                case 3:
                case 4:
                    gameScore[i] += 1;
                    returnMe = callerTeamName + " made the bid. 1 point to " + callerTeamName + ".";
                    break;
                case 5:
                    gameScore[i] += 2;
                    returnMe = callerTeamName + " swept. 2 points to " + callerTeamName + ".";
                    break;
                default:
                    Math.round(0/0);
            }
        }
        else //going alone
        {
            switch(handScore[caller%2])
            {
                case 0:
                    gameScore[1-i] += 8;
                    returnMe = call + " went alone and was swept. 8 points to " + defendingTeamName;
                    break;
                case 1:
                case 2:
                    gameScore[1-i] += 4;
                    returnMe = call + " went alone and was euched. 4 points to " + defendingTeamName;
                    break;
                case 3:
                case 4:
                    gameScore[i] += 1;
                    returnMe = call + " went alone and made the bid. 1 point to " + callerTeamName;
                    break;
                case 5:
                    gameScore[i] += 4;
                    returnMe = call + " went alone and swept. 4 points to " + callerTeamName;
                    break;
                default:
                    Math.round(0/0);
            }
        }
        return returnMe;
    }

    /**
     * Returns the score of the game, as an
     * array of size two, of the form
     * {south-north, west-east}
     * @return  the score of the game. One accesses
     *          scores using the NORTH_SOUTH and EAST-WEST
     *          constants.
     */
    public int[] gameScore()
    {
        return gameScore;
    }

    /**
     * Returns a String telling who won, iff gameOver() is true.
     * @return
     */
    public String gameResultString()
    {
        String returnMe = "";
        if(!gameOver()){}
        else if(gameScore[NORTH_SOUTH] >= 10)
        {
            returnMe = players[NORTH] + " and " + players[SOUTH] + " won!";
        }
        else
        {
            returnMe = players[WEST] + " and " + players[EAST] + " won!";
        }
        return returnMe;
    }

    public static void main(String[] args)
    {
        Player s = new AIPlayer("South");
        Player w = new AIPlayer("West");
        Player n = new AIPlayer("North");
        Player e = new AIPlayer("East");
        Table table = new Table(s,w,n,e);

        Card kitty = table.dealNewRound();

        table.setTrump(Card.SPADES, n, true);

        System.out.println(table.gameOver());
        System.out.println(Arrays.toString(table.getTrickPlayers()));
    }
}
