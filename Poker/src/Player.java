import java.util.*;

/**<PLAYER >
 * A poker player object
 * @documentation  For more information:
 * {@link https://github/com/TylersDurden/Poker}
 * @author root
 */
public class Player {

    public Vector<Card> hand;
    public Vector<Card> tableCards;
    public String       player;
    public int          chips;
    
    public Vector<Card> highest = new Vector<>();
    public Vector<Card> pair    = new Vector<>();//
    public Vector<Card> twoPair = new Vector<>();//
    public Vector<Card> tres    = new Vector<>();// Three of a Kind
    public Vector<Card> flush   = new Vector<>();//
    public Vector<Card> strt    = new Vector<>();// Straight
    public Vector<Card> house   = new Vector<>();// Full House 
    public Vector<Card>strFlush = new Vector<>();// Straight Flush 
    public Vector<Card> quads   = new Vector<>();// Four of a Kind  
    public Vector<Card> rylFlush = new Vector<>();// Royal Flush

    public Player(String name, int buyIn) {
        this.player = name;
        this.chips = buyIn;
    }

    void getHand(Vector<Card> dealtCards) {this.hand = dealtCards;}

    void showHand() {for (Card c : hand) {c.showMe();}}

    void placeBet(int amt) {if (amt < chips) {chips -= amt;}}

    void setTable(Vector<Card> table) {tableCards = table;}
    
    void clearProbabilities(){
        pair.clear();
        twoPair.clear();
        tres.clear();
        flush.clear();
        strt.clear();
        house.clear();
    }

}
/** <EndOf:_PLAYER.java_> */
