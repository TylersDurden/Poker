 import java.util.*;

/**PLAYER 
 * A poker player object
 * @documentation  For more information:
 * {@link https://github/com/TylersDurden/Poker}
 * @author root
 */
public class Player {

    public Vector<Card> hand;
    public Vector<Card> tableCards;
    public String       player;
    public int          chipcount;
    
    public boolean folded;
    
    public Player(String name, int buyIn) {
        this.player = name;
        this.chipcount = buyIn;
        this.folded = false;
    }

    void getHand(Vector<Card> dealtCards) {this.hand = dealtCards;}

    void showHand() {for (Card c : hand) {c.showMe();}}

    void placeBet(int amt) {if (amt < chipcount) {chipcount -= amt;}}

    void setTable(Vector<Card> table) {tableCards = table;}
    
   
    
    ////////////////// AI //////////////////////
    int anteUp(int amt){
        int answer = 0; //return -1 for a fold 
        answer = new Hand("preflop",this.hand).evaluate();
        return answer;
    }
    
    static class Hand {
        
        public String context;
        public Vector <Card> hand = new Vector<>();
        
        Hand(String mode, Vector<Card> cards){
                this.context = mode;
                this.hand = cards;
                switch(this.context){
                    case("preflop"):
                        AIpreFlop();
                    case("flop"):
                        AIflop();
                    case("turn"):
                        AIturn();
                    case("river"):
                        AIriver();
                    case("raise"):
                        AIraise();
                    default:
                        break;
                }
        }
        
        void AIpreFlop(){
            
        }
    
        void AIflop(){}
        
        void AIriver(){}
    
        void AIturn(){}
        
        void AIraise(){
            
        }
        
        int evaluate(){
            int choice = -1;
            
            return choice;
        }
    }
    

}
/** <EndOf:_PLAYER.java_> */
