package euchre;

/**
 * @author 151bloomj
 */
public class HandTester
{
    public static void main(String[] args)
    {
//        int trump = Card.HEARTS;
//
//        Card righty = new Card(Card.JACK, trump);
//        Card aceHearts = new Card(Card.ACE, trump);
//        System.out.println(Hand.getSortRank(righty, trump));
//        System.out.println(Hand.getSortRank(aceHearts, trump));

        long time = System.currentTimeMillis();
        Hand hand = new Hand();
        int trump = Card.DIAMONDS;
        hand.add(new Card(Card.JACK, Card.DIAMONDS));
        hand.add(new Card(Card.QUEEN, Card.CLUBS));
        hand.add(new Card(9,Card.HEARTS));
        hand.add(new Card(10,Card.HEARTS));
        hand.add(new Card(Card.ACE, Card.HEARTS));
        hand.add(new Card(Card.JACK, (trump + 2) % 4));
        hand.add(new Card(Card.ACE, trump));
        hand.sort(trump);
        System.out.println(hand);
        System.out.println(System.currentTimeMillis() - time);
    }
}
