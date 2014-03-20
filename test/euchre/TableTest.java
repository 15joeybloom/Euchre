package euchre;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joey
 */
public class TableTest
{

    public TableTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of toString method, of class Table.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        Table instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of beginJackOff method, of class Table.
     */
    @Test
    public void testBeginJackOff()
    {
        System.out.println("beginJackOff");
        Table instance = null;
        instance.beginJackOff();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of jackOff method, of class Table.
     */
    @Test
    public void testJackOff()
    {
        System.out.println("jackOff");
        Table instance = new Table(
            new AIPlayer("South"),
            new AIPlayer("West"),
            new AIPlayer("North"),
            new AIPlayer("East"));
        Card expResult = null;
        Card result = instance.jackOff();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasWinner method, of class Table.
     */
    @Test
    public void testHasWinner()
    {
        System.out.println("hasWinner");
        Table instance = new Table(
            new AIPlayer("South"),
            new AIPlayer("West"),
            new AIPlayer("North"),
            new AIPlayer("East"));
        boolean expResult = false;
        boolean result = instance.hasWinner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dealNewRound method, of class Table.
     */
    @Test
    public void testDealNewRound()
    {
        System.out.println("dealNewRound");
        Player s = new AIPlayer("South");
        Player w = new AIPlayer("West");
        Player n = new AIPlayer("North");
        Player e = new AIPlayer("East");
        Table instance = new Table(s,w,n,e);
        Card result = instance.dealNewRound();
        System.out.println(s.getHand());
        System.out.println(w.getHand());
        System.out.println(n.getHand());
        System.out.println(e.getHand());
        System.out.println(result);
    }

    /**
     * Test of setTrump method, of class Table.
     */
    @Test
    public void testSetTrump()
    {
        System.out.println("setTrump");
        int suit = 0;
        Table instance = null;
        instance.setTrump(suit);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of goAlone method, of class Table.
     */
    @Test
    public void testGoAlone()
    {
        System.out.println("goAlone");
        int p = 0;
        Table instance = null;
        instance.goAlone(p);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTrump method, of class Table.
     */
    @Test
    public void testGetTrump()
    {
        System.out.println("getTrump");
        Table instance = null;
        int expResult = 0;
        int result = instance.getTrump();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handOver method, of class Table.
     */
    @Test
    public void testHandOver()
    {
        System.out.println("handOver");
        Table instance = null;
        boolean expResult = false;
        boolean result = instance.handOver();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTrickPlayers method, of class Table.
     */
    @Test
    public void testGetTrickPlayers()
    {
        System.out.println("getTrickPlayers");
        Table instance = null;
        Player[] expResult = null;
        Player[] result = instance.getTrickPlayers();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of trick method, of class Table.
     */
    @Test
    public void testTrick()
    {
        System.out.println("trick");
        Trick trick = null;
        Table instance = null;
        Player expResult = null;
        Player result = instance.trick(trick);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handScore method, of class Table.
     */
    @Test
    public void testHandScore()
    {
        System.out.println("handScore");
        Table instance = null;
        int[] expResult = null;
        int[] result = instance.handScore();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handResultString method, of class Table.
     */
    @Test
    public void testHandResultString()
    {
        System.out.println("handResultString");
        Table instance = null;
        String expResult = "";
        String result = instance.handResultString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gameScore method, of class Table.
     */
    @Test
    public void testGameScore()
    {
        System.out.println("gameScore");
        Table instance = null;
        int[] expResult = null;
        int[] result = instance.gameScore();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gameResultString method, of class Table.
     */
    @Test
    public void testGameResultString()
    {
        System.out.println("gameResultString");
        Table instance = null;
        instance.gameResultString();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
