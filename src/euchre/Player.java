package euchre;

/**
 * @author Joey Bloom
 *
 */
public abstract class Player
{
    private Hand hand;
    private String name;

    public static int PASS = -1;
    public static int ORDER_UP = 0;
    public static int GO_ALONE = 4;

    /**
     * Constructs a Player with name "Johnny Default" and empty Hand
     */
    public Player()
    {
        this("Johnny Default");
    }

    /**
     * Constructs a Player with a name and empty Hand
     */
    public Player(String name)
    {
        this.name = name;
        hand = new Hand();
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Adds Cards to the Hand
     * @param c Cards to add
     */
    public void dealCards(Card... c)
    {
        for(Card x : c)
        {
            hand.add(x);
        }
    }

    /**
     * @return hand
     */
    public Hand getHand()
    {
        return hand;
    }

    /**
     * Returns one of the Player constants based on whether the player will
     * order up trump with the given top Card of the kitty
     * @param top the top card of the kitty
     * @return true if will orderUp
     */
    public abstract int orderUp(Card top);

    /**
     * Discards a Card when the Player
     * is the dealer and is told to pick it up. Adds c to the
     * Player's Hand and discards some Card.
     * @param c Card picked up. The suit of this Card is trump.
     */
    public abstract void pickItUp(Card c);

    /**
     * Returns the suit this Player will call
     * given the Hand, or Player.PASS if pass
     * @return  Player.PASS if will pass, or one of the suit constants
     *          defined in Card (Card.SPADES, Card.CLUBS, etc)
     *          returns one of the constants + Player.GO_ALONE
     *          if going alone.
     */
    public abstract int call();

    /**
     * Returns the suit this Player will call
     * when the Dealer is stuck. Cannot PASS.
     * @return  one of the suit constants defined in Card,
     *          or returns one of the constants + Player.GO_ALONE
     *          if going alone.
     */
    public abstract int stick();

    /**
     * Returns the Card this Player will play
     * given the Hand and the Trick
     * @param t the Trick
     * @return the best Card to play
     */
    public abstract Card playCard(Trick t);

    /**
     * Returns a String representation of the Player
     * @return
     */
    @Override
    public String toString()
    {
        return name;
    }
}