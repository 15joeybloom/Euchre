package euchre;

/**
 * @author Joey
 *
 */
public class EuchreDeckTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        EuchreDeck d = new EuchreDeck();
        System.out.println(d);
        d.shuffle();
        System.out.println(d);
    }

}