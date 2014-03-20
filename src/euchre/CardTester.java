package euchre;

/**
 *
 * @author 151bloomj
 */
public class CardTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Card jack = new Card(Card.JACK, Card.DIAMONDS);
        Card ace = new Card(Card.KING, Card.CLUBS);
        
        info(jack);
        info(ace);
    }
    
    private static void info(Card c)
    {
        System.out.println(c);
        System.out.println("Spades: " + c.getEuchreRank(Card.SPADES));
        System.out.println("Hearts: " + c.getEuchreRank(Card.HEARTS));
        System.out.println("Clubs: " + c.getEuchreRank(Card.CLUBS));
        System.out.println("Diamonds: " + c.getEuchreRank(Card.DIAMONDS));
        
    }
}
