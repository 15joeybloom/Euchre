package euchre;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author 151bloomj
 * Assignment #2
 * A Card has suit and rank.
 */
public class Card
{
    private byte dat;

    private static final byte SUIT = 3;
    private static final byte RANK = 60;
    public static final String[] SUIT_SYMBOLS =
        { "\u2660", //spade
          "\u2665", //heart
          "\u2663", //club
          "\u2666"  //diamond
        };
    private static final String[] RANK_STRINGS =
        { "A", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

    public static final int SPADES = 0;
    public static final int CLUBS = 2;
    public static final int HEARTS = 1;
    public static final int DIAMONDS = 3;
    //to get the other suit of same color, add 2 mod 4.

    public static final int ACE = 1;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;

    public static final int CARD_W = 79;
    public static final int CARD_H = 123;
    private static final BufferedImage PACK;
    static
    {
        BufferedImage img;
        try
        {
            img = ImageIO.read(Card.class.getResource("imgs/cards.png"));

        }
        catch(IOException e)
        {
            img = null;
        }
        PACK = img;
    }
    public static final BufferedImage CARD_BACK = PACK.getSubimage(3 * CARD_W, 4 * CARD_H, CARD_W, CARD_H);

    /**
     * Constructs a Card.
     * @param r rank precondition: 0 <= r <= 13
     * @param s suit precondition: 0 <= s <= 4
     */
    public Card(int r, int s)
    {
        dat |= r;
        dat <<= 2;
        dat |= s;
    }
    /**
     * Constructs a Card with same rank and suit as another Card
     * @param c other Card
     */
    public Card(Card c)
    {
        this(c.getRank(),c.getSuit());
    }

    /**
     * returns the suit as an integer
     * 0 = spades
     * 1 = clubs
     * 2 = hearts
     * 3 = diamonds
     * @return suit
     */
    public int getSuit()
    {
        return dat & SUIT;
    }

    /**
     * returns the suit as a symbol
     * @return suit symbol
     */
    public String getSuitSymbol()
    {
        return SUIT_SYMBOLS[getSuit()];
    }

    /**
     * returns the rank as an int
     * 0 = soft A
     * 1 = hard A
     * 2-10 = 2-10
     * 11 = J
     * 12 = Q
     * 13 = K
     * @return rank int
     */
    public int getRank()
    {
        return (dat & RANK) >> 2;
    }

    /**
     * returns the rank as a String
     * 0 = A
     * 1 = A
     * 2-10 = 2-10
     * 11 = J
     * 12 = Q
     * 13 = K
     * @return rank int
     */
    public String getRankString()
    {
        return RANK_STRINGS[getRank()];
    }

    /**
     * @return the Card represented by a String, like A\u2660 for Ace of Spades
     */
    @Override
    public String toString()
    {
        return getRankString() + getSuitSymbol();
    }

    /**
     * Returns a BufferedImage of this Card
     * @return BufferedImage
     */
    public BufferedImage getBufferedImage()
    {
        return PACK.getSubimage(getRank() * CARD_W, getSuit() * CARD_H, CARD_W, CARD_H);
    }

    /**
     * Returns an ImageIcon of this Card
     * @return ImageIcon
     */
    public ImageIcon getImageIcon()
    {
        return new ImageIcon(getBufferedImage());
    }

    /**
     * returns the rank of this Card in the game of euchre
     * 0 = righty
     * 1 = lefty
     * 2 = ace of trump
     * 3 = king of trump
     * 4 = queen of trump
     * 5 = ten of trump
     * 6 = nine of trump
     * 7-12 = ace-nine nontrump
     * @param trump the trump suit
     * @return euchre rank. -1 if Card is not used in euchre.
     */
    int getEuchreRank(int trump)
    {
        int r = getRank();
        int s = getSuit();
        if(r == Card.JACK)
        {
            if(s == trump)
            {
                return 0;
            }
            if(s == (trump + 2) % 4)
            {
                return 1;
            }
            return 10;
        }
        if(s == trump)
        {
            switch(r)
            {
                case Card.ACE:
                    return 2;
                case Card.KING:
                    return 3;
                case Card.QUEEN:
                    return 4;
                case 10:
                    return 5;
                case 9:
                    return 6;
                default:
                    return -1;
            }
        }
        switch(r)
        {
            case Card.ACE:
                return 7;
            case Card.KING:
                return 8;
            case Card.QUEEN:
                return 9;
            case 10:
                return 11;
            case 9:
                return 12;
            default:
                return -1;
        }
    }

    /**
     * Returns true if these are the same Card
     * @return true if same card
     */
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Card))
        {
            return false;
        }
        return o.hashCode() == hashCode();
    }

    /**
     * @return the byte used to store this Card's data
     */
    @Override
    public int hashCode()
    {
        return dat;
    }
}
