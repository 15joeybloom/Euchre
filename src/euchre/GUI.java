package euchre;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import static euchre.Player.PASS;
import static euchre.Player.ORDER_UP;
import static euchre.Player.GO_ALONE;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Joey
 *
 */
public class GUI extends JFrame
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run()
            {
                new GUI();
            }
        });
    }


    private JMenuBar menuBar;
        private JMenu fileMenu;
            private JMenuItem newGame;
        private JMenu editMenu;

    private JPanel gamePanel;
        private JLabel[][] cardLabels;
        private JLabel[] speechLabels;
        private JLabel[] trickLabels;
        private JLabel trumpLabel;
    private JSlider speedSlider;
    private JPanel infoPanel;

    private Table table;
    private Player[] players;

    private static final ImageIcon[] CARD_BACKS;
    static
    {
        BufferedImage old = Card.CARD_BACK;
        CARD_BACKS = new ImageIcon[4];
        for(int i = 0; i < 4; i++)
        {
            CARD_BACKS[i] = new ImageIcon(rotate(old, i));
        }
    }
    private static final ImageIcon[] SPEECH_BUBBLES;
    static
    {
        SPEECH_BUBBLES = new ImageIcon[4];
        for(int i = 0; i < 4; i++)
        {

            SPEECH_BUBBLES[i] = new ImageIcon(GUI.class.getResource("imgs/speechBubbles/" + i + ".png"));

            if(SPEECH_BUBBLES[i] == null)
            {
                System.err.println("Error loading speech bubble image.");
                System.exit(1);
            }
        }
    }
    private static final Color FELT_COLOR = new Color(0x006600);
    private static final BufferedImage SUITS;
    static
    {
        BufferedImage img;
        try
        {
            img = ImageIO.read(Card.class.getResource("imgs/suits/suits.png"));
        }
        catch(IOException e)
        {
            img = null;
        }
        SUITS = img;
    }

    /**
     * Constructs a GUI
     */
    public GUI()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        setTitle("Euchre");
        setResizable(false);
        getContentPane().setBackground(FELT_COLOR);

        setUpMenuBar();

        setUpGamePanel();
            setUpCardLabels();
            setUpSpeechLabels();
            setUpTrickLabels();
            setUpTrumpLabel();
        setUpSpeedSlider();

        setSize(713,651); //this makes the contentPane exactly 600x600
        setVisible(true);
//        System.out.println(gamePanel.getHeight());
//        System.out.println(gamePanel.getWidth());
        //these are for checking the width and height of the contentPane
        newGame.doClick();
    }

    private void setUpMenuBar()
    {
        menuBar = new JMenuBar();
            fileMenu = new JMenu("File");
                newGame = new JMenuItem("New Game");
                newGame.addActionListener(new NewGameListener());
                fileMenu.add(newGame);
            editMenu = new JMenu("Edit");
            menuBar.add(fileMenu);
            menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    private void setUpGamePanel()
    {
        gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(600,600));
        gamePanel.setLayout(null);
        gamePanel.setBackground(FELT_COLOR);

        add(gamePanel,BorderLayout.CENTER);
    }

    private void setUpCardLabels()
    {
        int[] xs = {213,    0,      213,    477};
        int[] ys = {477,    213,    0,      213};
        cardLabels = new JLabel[4][];
        for(int i = 0; i < 4; i++)
        {
            cardLabels[i] = new JLabel[6];
            for(int j = 0; j < 6; j++)
            {
                cardLabels[i][j] = new JLabel();
                cardLabels[i][j].setSize(
                    CARD_BACKS[i].getIconWidth(),
                    CARD_BACKS[i].getIconHeight());
                int gap = Card.CARD_W / 4;
                cardLabels[i][j].setLocation(
                    xs[i] + (1 - i % 2) * gap * j,
                    ys[i] + (i % 2) * gap * j);
                cardLabels[i][j].addMouseListener(new CardListener(i,j));

                cardLabels[i][j].setIcon(CARD_BACKS[i]);
                gamePanel.add(cardLabels[i][j]);
            }
        }
    }

    private void setUpSpeechLabels()
    {
        int[] xs = {213 - SPEECH_BUBBLES[0].getIconWidth(),     0,                                          387,    600 - SPEECH_BUBBLES[3].getIconWidth()};
        int[] ys = {600 - SPEECH_BUBBLES[0].getIconHeight(),    213 - SPEECH_BUBBLES[1].getIconHeight(),    0,      387};
        speechLabels = new JLabel[4];
        for(int i = 0; i < 4; i++)
        {
            ImageIcon img = SPEECH_BUBBLES[i];
            speechLabels[i] = new JLabel(img);
            speechLabels[i].setLocation(xs[i],ys[i]);
            speechLabels[i].setSize(img.getIconWidth(),img.getIconHeight());
            speechLabels[i].setHorizontalTextPosition(SwingConstants.CENTER);
            speechLabels[i].setVerticalTextPosition(SwingConstants.CENTER);
            speechLabels[i].setIconTextGap(0);
            speechLabels[i].setForeground(FELT_COLOR);

            gamePanel.add(speechLabels[i]);
        }
    }

    private void setUpTrickLabels()
    {
        int[] xs = {261, 140, 261, 337};
        int[] ys = {337, 261, 140, 261};
        trickLabels = new JLabel[4];
        for(int i = 0; i < 4; i++)
        {
            trickLabels[i] = new JLabel();
            trickLabels[i].setSize(
                CARD_BACKS[i].getIconWidth(),
                CARD_BACKS[i].getIconHeight());
            trickLabels[i].setLocation(
                xs[i], ys[i]);

            gamePanel.add(trickLabels[i]);
        }
    }

    private void setUpTrumpLabel()
    {
        trumpLabel = new JLabel();
        trumpLabel.setSize(SUITS.getWidth() / 4, SUITS.getHeight());
        trumpLabel.setLocation(260,260);

        gamePanel.add(trumpLabel);
    }

    private void setUpSpeedSlider()
    {
        speedSlider = new JSlider(SwingConstants.VERTICAL, 0, 3000, 1000);
        speedSlider.setOrientation(SwingConstants.VERTICAL);
        speedSlider.setSnapToTicks(false);
        speedSlider.setPaintLabels(true);
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Hashtable<Integer,JComponent> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Instantaneous"));
        labelTable.put(250, new JLabel("Very Fast"));
        labelTable.put(500, new JLabel("Fast"));
        labelTable.put(1000, new JLabel("Medium"));
        labelTable.put(2000, new JLabel("Slow"));
        labelTable.put(3000, new JLabel("Very Slow"));
        speedSlider.setLabelTable(labelTable);
        speedSlider.addChangeListener(new SpeedSliderListener());

        add(speedSlider, BorderLayout.EAST);
        waitTime = speedSlider.getValue();
    }

    private boolean hasClick = false;
    private boolean wantsClick = false;
    private int whoWantsClick = -1;
    private int choice = -1;

    private class GUIPlayer extends Player
    {
        private int position;
        public GUIPlayer(int pos)
        {
            super();
            position = pos;
        }
        public GUIPlayer(int pos, String name)
        {
            super(name);
            position = pos;
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
                "What would you like to do?",
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

        @Override
        public synchronized void pickItUp(Card c)
        {
            Hand hand = getHand();
            hand.add(c);
            hand.sort(c.getSuit());
            updateHand(position);
            wantsClick = true;
            whoWantsClick = position;
            while(!hasClick)
            {
                try
                {
                    wait();
                }
                catch(InterruptedException ex){}
            }
            hasClick = false;
            hand.remove(choice);
        }

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
                "What would you like to call?",
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
                "You are stuck. What do you call?",
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

        @Override
        public synchronized Card playCard(Trick t)
        {
            wantsClick = true;
            whoWantsClick = position;
            while(!hasClick)
            {
                try
                {
                    wait();
                }
                catch(InterruptedException e){}
            }
            hasClick = false;
            Hand hand = getHand();
            ArrayList<Card> trickCards = t.getCards();
            if(trickCards.isEmpty())
            {
                return hand.remove(choice);
            }
            int trump = t.getTrump();
            int leadSuit =
                //if lefty
                trickCards.get(0).getEuchreRank(trump) == 1 ? trump : trickCards.get(0).getSuit();
            Card selection = hand.get(choice);
            //if following suit
            if(selection.getEuchreRank(trump) == 1 ? leadSuit == trump : leadSuit == selection.getSuit())
            {
                return hand.remove(choice);
            }
            //if not following suit, check for if could have
            for(Card c : hand.getCards())
            {
                //if could have followed suit
                if(c.getEuchreRank(trump) == 1 ? leadSuit == trump : c.getSuit() == leadSuit)
                {
                    JOptionPane.showMessageDialog(null, "Need to follow suit.", "Renege", JOptionPane.ERROR_MESSAGE);
                    return playCard(t);
                }
            }
            //if not following suit and couldn't have,
            return hand.remove(choice);
        }

    }

    private class CardListener implements MouseListener
    {
        private byte dat; // 00iiipp
        private static final byte P =   0b0000011;
        private static final byte I =   0b0011100;

        /**
         * Constructs a CardListener for the Card at index i
         * of the Hand of the Player at index p
         * @param p     the index of the Player
         * @param i     the index of the Card
         */
        public CardListener(int p, int i)
        {
            super();
            dat = (byte) i;
            dat <<= 2;
            dat += (byte) p;
        }

        /**
         * returns the index of the Player
         * @return the index of the Player
         */
        public int getPlayer()
        {
            return dat & P;
        }

        /**
         * returns the index of the Card
         * @return the index of the Card
         */
        public int getCard()
        {
            return (dat & I) >> 2;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            int p = getPlayer();
            if(wantsClick && whoWantsClick == p && getCard() < players[p].getHand().getCards().size())
            {
                synchronized(players[p])
                {
                    choice = getCard();
                    wantsClick = false;
                    hasClick = true;
                    players[p].notifyAll();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e){}
        @Override
        public void mouseReleased(MouseEvent e){}
        @Override
        public void mouseEntered(MouseEvent e){}
        @Override
        public void mouseExited(MouseEvent e){}
    }

    private class NewGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ev)
        {
            SwingWorker task = new GameTask();
            task.execute();
        }
    }

    public int waitTime;
    private class GameTask extends SwingWorker<Void,Void>
    {
        @Override
        @SuppressWarnings("UseSpecificCatch")
        protected Void doInBackground() throws InterruptedException
        {
            try
            {
                Player partner = new AIPlayer("Art Nuhr");
                Player left = new AIPlayer("Ima Tyourleft");
                Player right = new AIPlayer("R. Iteside");
                String str = JOptionPane.showInputDialog(null,
                        "Hello. Welcome to Euchre." +
                        "\nYou are playing with " + partner.getName() +
                        "\nagainst " + left.getName() + " at West and " + right.getName() + " at East." +
                        "\nYou are South." +
                        "\n\nWhat is your name?");
                if(str == null || str.isEmpty())
                {
                    return null;
                }
                Player thisGuy = new GUIPlayer(0,str);
                players = new Player[]{thisGuy,left,partner,right};
                table = new Table(thisGuy,left,partner,right);

                //clears the cardLabels
                for(int i = 0; i < 4; i++)
                {
                    updateHand(i);
                }

                Card jackCard;
                int jackI;
                table.beginJackOff();
                for(jackI = 0;(jackCard = table.jackOff()) != null; jackI++)
                {
                    players[jackI%4].dealCards(jackCard);
                    Thread.sleep(waitTime);
                    displayHand(jackI%4);
                }
                JOptionPane.showMessageDialog(
                    null,
                    /*players[(--jackI)%4]*/table.getDealer() + " dealt first Jack and gets first deal.",
                    "Jack Off Result",
                    JOptionPane.PLAIN_MESSAGE);

                game:
                while(!table.gameOver())
                {
                    //TODO dealer chip code here
                    Card kitty = table.dealNewRound();
                    Player[] bidPlayers = table.getBidPlayers();
                    trickLabels[0].setIcon(kitty.getImageIcon()); //show kitty
                    //update hands
                    for(int i = 0; i < 4; i++)
                    {
                        updateHand(i);
                    }
                    trumpLabel.setIcon(null);//remove previous trump from middle of table
                    bidding:
                    {
                        firstRoundOfBidding:
                        for(int i = 0; i < 4; i++)
                        {
                            Player p = bidPlayers[i];
                            int whatchaGonnaDo = p.orderUp(kitty);
                            if(whatchaGonnaDo == PASS)
                            {
                                say(p, "Pass.");
                            }
                            else if(whatchaGonnaDo == ORDER_UP)
                            {
                                say(p, "Pick it up.");
                                Player dealer = table.getDealer();
                                table.setTrump(kitty.getSuit(), p, false);
                                say(dealer, "Picking it up...");
                                trickLabels[0].setIcon(null);
                                dealer.pickItUp(kitty);
                                updateHand(dealer);
                                say(dealer, "Picked it up.");
                                break bidding;
                            }
                            else if(whatchaGonnaDo == GO_ALONE)
                            {
                                say(p, "Pick it up. I'm going alone.");
                                table.setTrump(kitty.getSuit(), p, true);
                                Player dealer = table.getDealer();
                                say(dealer, "Picking it up...", 1000);
                                trickLabels[0].setIcon(null);
                                dealer.pickItUp(kitty);
                                say(dealer, "Picked it up.");
                                break bidding;
                            }
                            else
                            {
                                throw new RuntimeException("Uh oh. OrderItUp returned " + whatchaGonnaDo);
                            }
                            Thread.sleep(waitTime);
                        }//end firstRoundOfBidding

                        trickLabels[0].setIcon(null); //remove kitty after first round
                        secondRoundOfBidding:
                        for(int i = 0; i < 3; i++)
                        {
                            Player p = bidPlayers[i];
                            int whatchaGonnaDo = p.call();
                            if(whatchaGonnaDo == PASS)
                            {
                                say(p, "Pass.");
                            }
                            else if(whatchaGonnaDo < GO_ALONE + 4 && whatchaGonnaDo >= 0)
                            {
                                int suit = whatchaGonnaDo % 4;
                                String sayString = "I call " + Card.SUIT_SYMBOLS[suit]; //TODO suit image?
                                boolean alone = false;
                                if(whatchaGonnaDo >= GO_ALONE)
                                {
                                    sayString += " alone";
                                    alone = true;
                                }
                                say(p, sayString);
                                table.setTrump(suit,p,alone);
                                break bidding;
                            }
                            else
                            {
                                throw new RuntimeException("Uh oh. Call returned " + whatchaGonnaDo);
                            }
                            Thread.sleep(waitTime);
                        }//end secondRoundOfBidding
                        Player dealer = table.getDealer();
                        int choice = dealer.stick();
                        assert choice >= 0 && choice <= 7;
                        int suit = choice % 4;
                        boolean alone = choice >= GO_ALONE;
                        table.setTrump(suit, dealer, alone);
                        say(dealer, "I call " + Card.SUIT_SYMBOLS[suit] + (alone ? " alone" : ""));
                    }//end bidding

                    trumpLabel.setIcon(getSuitIcon(table.getTrump())); //display the trump suit in the middle of the table
                    play:
                    {
                        int trump = table.getTrump();
                        while(!table.handOver())
                        {
                            Trick trick = new Trick(trump);
                            Player[] ps = table.getTrickPlayers();
                            List list = Arrays.asList(players);
                            say(ps[0], "Leading...");
                            for(int i = 0; i < ps.length; i++)
                            {
                                Thread.sleep(waitTime);
                                Player p = ps[i];
                                Card c = p.playCard(trick);
                                trick.addCard(c);
                                int x = list.indexOf(p);
                                trickLabels[x].setIcon(new ImageIcon(rotate(c.getBufferedImage(),x)));
                                updateHand(x);
                            }
                            int winner = list.indexOf(table.trick(trick));
                            say(winner, "I took the trick!");
                            //wait, then clear the trickLabels
                            Thread.sleep(2000);
                            for(int i = 0; i < trickLabels.length; i++)
                            {
                                trickLabels[i].setIcon(null);
                            }
                        }
                    }//end play

                    String handOverMsg = "";
                    handOverMsg += "Hand over.";
                    int[] handScore = table.handScore();
                    handOverMsg += "\nScore: " +
                        handScore[0] + "-" + handScore[1];
                    handOverMsg += '\n' + table.handResultString();
                    int[] gameScore = table.gameScore();
                    handOverMsg += ("\nGame Score: " +
                        gameScore[0] + "-" + gameScore[1]);
                    JOptionPane.showMessageDialog(null, handOverMsg, "Hand Over", JOptionPane.PLAIN_MESSAGE);
                }//end game

                String gameOverMsg = "";
                gameOverMsg += "Game over.\n";
                gameOverMsg += table.gameResultString();
                int[] gameScore = table.gameScore();
                gameOverMsg += "\nGame Score: " +
                    gameScore[0] + "-" + gameScore[1];
                JOptionPane.showMessageDialog(null, gameOverMsg, "Game Over", JOptionPane.PLAIN_MESSAGE);
            }
            catch(Throwable e)
            {
                e.printStackTrace();
                throw e;
            }
            return null;
        }
    }

    private class SpeedSliderListener implements ChangeListener
    {

        @Override
        public void stateChanged(ChangeEvent e)
        {
            int selection = speedSlider.getValue();
            waitTime = selection;
        }

    }

    /**
     * returns a BufferedImage rotated clockwise the number of quadrants
     * specified
     * @param old           the BufferedImage to rotate
     * @param quadrants     number of quadrants to rotate, clockwise.
     *                      Example: 1 quadrant would be 90 degrees
     *                      clockwise
     * @return
     */
    private static BufferedImage rotate(BufferedImage old, int quadrants)
    {
        if(quadrants % 4 == 0)
        {
            return old;
        }
        BufferedImage newImage;
        if(quadrants % 2 == 0)
        {
            newImage = new BufferedImage(old.getWidth(), old.getHeight(), old.getType());
        }
        else
        {
            newImage = new BufferedImage(old.getHeight(), old.getWidth(), old.getType());
        }
        Graphics2D g2 = (Graphics2D) newImage.getGraphics();
//        g2.setColor(new Color(0x006600));
        if(quadrants == 1)
        {
            g2.translate(
                -(newImage.getWidth() - old.getWidth()) / 2,
                (newImage.getHeight() - old.getHeight()) / 2);
        }
        else if(quadrants == 3)
        {
            g2.translate(
                (newImage.getWidth() - old.getWidth()) / 2,
                -(newImage.getHeight() - old.getHeight()) / 2);
        }
        g2.rotate(-Math.toRadians(quadrants * 90), newImage.getWidth() / 2, newImage.getHeight() / 2);
        g2.drawImage(old, 0, 0, old.getWidth(), old.getHeight(), null);
        return newImage;
    }

    /**
     * Updates the display of the hand of the specified Player
     * @param p Player whose Hand will be updated. If it is an AIPlayer,
     *          the Cards will be upside down.
     */
    private void updateHand(int p)
    {
        Player play = players[p];
        ArrayList<Card> cards = play.getHand().getCards();
        if(play instanceof AIPlayer)
        {
            //display the cards upsidedown
            for(int i = 0; i < cards.size(); i++)
            {
                cardLabels[p][i].setIcon(CARD_BACKS[p]);
//                System.out.println("Card updated Player " + p + " Card " + i);
            }
        }
        else
        {
            //display the cards right side up
            for(int i = 0; i < cards.size(); i++)
            {
                cardLabels[p][i].setIcon(cards.get(i).getImageIcon());
//                System.out.println("Card updated Player " + p + " Card " + i);
            }
        }
        //undisplays the rest of the Hand
        for(int i = cards.size(); i < cardLabels[p].length; i++)
        {
            cardLabels[p][i].setIcon(null);
//            System.out.println("Card updated Player " + p + " Card " + i);
        }
    }

    private void updateHand(Player p)
    {
        updateHand(Arrays.asList(players).indexOf(p));
    }

    /**
     * Displays the Hand of the specified Player
     * @param p Player whose Hand will be displayed. The Cards will
     *          be displayed regardless of which subclass of Player.
     */
    private void displayHand(int p)
    {
        ArrayList<Card> cards = players[p].getHand().getCards();
        //display the cards right side up
        for(int i = 0; i < cards.size(); i++)
        {
            cardLabels[p][i].setIcon(new ImageIcon(rotate(cards.get(i).getBufferedImage(),p)));
        }
        //undisplays the rest of the Hand
        for(int i = cards.size(); i < cardLabels[p].length; i++)
        {
            cardLabels[p][i].setIcon(null);
        }
    }

    private Object[] sayLocks = {
        new Object(),
        new Object(),
        new Object(),
        new Object()
    };

    /**
     * Queues the specified text to appear in the specified
     * Player's speech bubble
     * @param p index of the Player who is saying. null to say nothing.
     * @param text Player's words to say
     * @param millis how many milliseconds to display text
     */
    private void say(final int p, final String text, final int millis)
    {
        new SwingWorker<Void,Void>(){
            @Override
            @SuppressWarnings("SleepWhileHoldingLock")
            public Void doInBackground() throws InterruptedException
            {
                //using this synchronization, each saying is said for
                //millis milliseconds, and will not be interrupted
                //by another invokation of say
                synchronized(sayLocks[p])
                {
                    speechLabels[p].setText(text);
                    Thread.sleep(millis);
                    speechLabels[p].setText(null);
                    return null;
                }
            }
        }.execute();
    }

    /**
     * Overloads say, with default of 4000 millis
     * @param p     index of Player to say
     * @param text  text to say
     */
    private void say(int p, String text)
    {
        say(p,text,2000);
    }

    private void say(Player p, String text, int millis)
    {
        say(Arrays.asList(players).indexOf(p), text, millis);
    }

    private void say(Player p, String text)
    {
        say(p,text,1000);
    }

    private ImageIcon getSuitIcon(int s)
    {
        int w = SUITS.getWidth() / 4;
        int h = SUITS.getHeight();
        int x = s * w;
        int y = 0;
        return new ImageIcon(SUITS.getSubimage(x,y,w,h));
    }
}