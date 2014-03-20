package euchre;

/**
 * @author Joey
 * Tests the Trick class
 */
public class TrickTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        int trump = Card.DIAMONDS;
        EuchreDeck d = new EuchreDeck();
        d.shuffle();
        Trick t = new Trick(trump);
        System.out.println(t);

        for(int i = 0; !d.isEmpty() ; i++)
        {
            t.addCard(d.pop());
            System.out.println(t);
        }

//        t.addCard(new Card(Card.ACE, Card.CLUBS));
//        System.out.println(t);
//        t.addCard(new Card(Card.JACK, Card.SPADES));
//        System.out.println(t);
//        t.addCard(new Card(Card.JACK, Card.CLUBS));
//        System.out.println(t);
//        t.addCard(new Card(Card.ACE, Card.CLUBS));
//        System.out.println(t);
    }

}