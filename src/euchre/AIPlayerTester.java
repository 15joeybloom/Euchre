package euchre;

import java.util.*;

/**
 * @author Joey
 *
 */
public class AIPlayerTester {

    public static void main(String[] args)
    {
//        testOrderUp();
//        testCall();
        testPlay();
    }

    public static void testOrderUp()
    {
        System.out.println("orderUp");
        Card top = new Card(Card.KING, Card.DIAMONDS);
        System.out.println("Top: " + top);

        AIPlayer instance = new AIPlayer();
        EuchreDeck d = new EuchreDeck();
        d.shuffle();
        instance.dealCards(d.pop(), d.pop(), d.pop(), d.pop(), d.pop());
        System.out.println("Hand: " + instance.getHand());

        int result = instance.orderUp(top);
        System.out.println("Result: " + result);
    }

    public static void testCall()
    {
        System.out.println("call");

        AIPlayer instance = new AIPlayer();
        EuchreDeck d = new EuchreDeck();
        d.shuffle();
        instance.dealCards(d.pop(), d.pop(), d.pop(), d.pop(), d.pop());
        System.out.println("Hand: " + instance.getHand());

        int result = instance.call();
        System.out.println("Result: " + result);
    }

    public static void testPickItUp()
    {
        System.out.println("pickItUp");
        Card top = new Card(Card.KING, Card.DIAMONDS);
        System.out.println("Top: " + top);

        AIPlayer instance = new AIPlayer();
        EuchreDeck d = new EuchreDeck();
        d.shuffle();
        instance.dealCards(d.pop(), d.pop(), d.pop(), d.pop(), d.pop());
        System.out.println("Hand: " + instance.getHand());

        instance.pickItUp(top);
        System.out.println("Result: " + instance);
    }

    public static void testPlay()
    {
//        System.out.println("playCard");
//        Player player = new TextBasedPlayer();
//        Player player = new AIPlayer();
//        EuchreDeck deck = new EuchreDeck();
//        deck.shuffle();
//        player.dealCards(deck.pop(), deck.pop(), deck.pop(), deck.pop(), deck.pop());

//        Trick trick = new Trick(Card.SPADES);
//        int trickSize = new Random().nextInt(4);
//        for(int i = 0; i < trickSize; i++)
//        {
//            trick.addCard(deck.pop());
//        }

        System.out.println("playCard");
        Player player = new AIPlayer();
        player.dealCards(
            new Card(Card.JACK,     Card.DIAMONDS),
            new Card(Card.QUEEN,    Card.DIAMONDS),
//            new Card(10,            Card.HEARTS),
//            new Card(Card.JACK,     Card.CLUBS),
//            new Card(Card.JACK,     Card.HEARTS));
            new Card(Card.QUEEN,    Card.CLUBS),
            new Card(9,             Card.SPADES),
            new Card(10,            Card.SPADES)
            );

        Trick trick = new Trick(Card.HEARTS);
//        trick.addCard(new Card(Card.QUEEN,  Card.CLUBS));
//        trick.addCard(new Card(9,           Card.HEARTS));
        trick.addCard(new Card(Card.JACK,   Card.HEARTS));

        System.out.println("Trick: " + trick);
        System.out.println("Hand: " + player.getHand());
        System.out.println("Choice: " + player.playCard(trick));
    }
}