import java.util.*;
import java.nio.file.*;
import java.io.File;
import java.io.*;

/** POKER_LOGIC
 * @author SRobbins
 * @date 5/25/2018
 */
public class PokerLogic {

    static Deck DECK;
    static Hand HAND;
    static Map<Integer,Vector<Card>> simulatedCards = new HashMap<>();

    /** <PokerLogic>
     *  POKERLOGIC - run a number of simulated poker hands. Classify the results of each trial.
     *  Use this information for calculating the odds of each possible hands, the particular outs for
     *  different table configurations/layouts, and use the probabilities of these hyperparameters to
     *  assign scores to the possible hands.
     * </PokerLogic>
     *
     * @param data
     */
    public PokerLogic(Map<Integer,Vector<Card>> data){
        simulatedCards = data;
    }

    /** MAIN
     * Defines the iterations for developing deeper insight for
     * the artificial poker intuitions about the number of outs
     * and strength of hands.
     * @param args Number of simulated poker rounds to deal
     */
    public static void main(String[]args){
        Map<Integer,Vector<Card>> cardsMap =new HashMap<>();
        int nTries = Integer.parseInt(args[0]);
        for (int i = 0; i <nTries ; i++) {
            Deck d = new Deck();
            d.shuffle();
            Hand h = new Hand(d.deal(7));
            cardsMap.put(i,h.CARDS);
        }

        PokerLogic trainingData = new PokerLogic(cardsMap);
        System.out.println("------------------------------------------------");
        PokerInterpreter PI = new PokerInterpreter("C:\\Users\\srobbins\\Desktop\\Poker-ML\\src","outputdata.txt");
        PI.run();
    }

    /**
     *
     */
    public static class Card {

        public int RANK;
        public String SUIT;
        public static String [] TYPES = {"H","D","C","S"};


        /** */
        public Card(int rank, String suit){
            this.RANK = rank;
            this.SUIT = suit;
        }

        public static Map<String,Boolean> compareCards(Card A, Card B){
            boolean suited = false;
            boolean ranked = false;
            boolean near = false;
            if(A.SUIT.compareTo(B.SUIT)==0){suited=true;}
            if(A.RANK==B.RANK){ranked=true;}
            if(Math.abs(A.RANK-B.RANK)<=5){near=true;}
            Map<String,Boolean> result = new HashMap<>();
            result.put("suits",suited);
            result.put("ranks",ranked);
            result.put("stryt",near);
            return result;
        }
    }

    public static class Deck {

        static Vector<Card> self;
        static boolean SHUFFLED;
        public Deck(){
            self = initialize();
            shuffle();
        }

        /** Initialize A Deck of Cards
         * @return Vector<Card>Initial Deck Configuration</Card>
         */
        public Vector<Card> initialize(){
            SHUFFLED = false;
            Vector<Card> self = new Vector<>();
            for(int i=2;i<15;i++){
                for(String suit : PokerLogic.Card.TYPES){
                    self.add(new Card(i,suit));
                }
            }
            return self;
        }

        /** show the deck */
        public void show(){for(Card c : self){System.out.println(c.RANK+" "+c.SUIT);}}

        /** shuffle the deck */
        public static void shuffle(){
             Deck.SHUFFLED = true;
            // first create a numeric mapping of un-shuffled generic deck
            HashMap<Integer,Card> numdeck = new HashMap<>();
            int index =0;
            for(Card c : self){
                numdeck.put(index,c);
                index++;
            }
            // Shuffle the deck
            Vector<Card> shuffledDeck = new Vector<>();
            List<Integer> IV = new ArrayList<>(numdeck.keySet());
            Collections.shuffle(IV);
            for(Integer i:IV){shuffledDeck.add(numdeck.get(i));}
            self = shuffledDeck;
        }

        /** */
        public Vector<Card> deal(int nCards){
            Vector<Card>cards = new Vector<>();
            for(int i=0;i<nCards;i++){
                cards.add(self.get(i));
            }
            return cards;


    }
    }
    public static class Hand {

        private Vector<Card> CARDS;

        public Hand(Vector<Card> self){
            CARDS = self;

              for(Card c : CARDS){
                if(c.RANK==11){System.out.print("J"+c.SUIT+" ");}
                if(c.RANK==12){System.out.print("Q"+c.SUIT+" ");}
                if(c.RANK==13){System.out.print("K"+c.SUIT+" ");}
                if(c.RANK==14){System.out.print("A"+c.SUIT+" ");}
                if(c.RANK<11) {System.out.print(c.RANK+c.SUIT+" ");}
            }
            System.out.println("");

        }



        public void show() {for(Card c: CARDS){System.out.print(c.RANK+c.SUIT+" ");}}

    }

    public static class PokerInterpreter implements Runnable {
        private String path;
        private String fname;
        private Vector<String> content = new Vector<>();
        private Map<Integer,Vector<String>> handsDealt = new HashMap<>();
        private Map<Integer,Vector<String>> flopsDealt = new HashMap<>();
        private Map<Integer,Vector<String>> turnsDealt = new HashMap<>();
        private Map<Integer,Vector<String>> rivrsDealt = new HashMap<>();

        PokerInterpreter(String p, String f) {
            path = p;
            fname = f;
        }

        public void run() {
            int lenDat = readTrainingData();
            System.out.println(lenDat+" lines of PokerData Read.");
            if(lenDat!=0){parseHandsFromTrainingData();}
            System.out.println(handsDealt.size()+" hands parsed.");
            System.out.println(flopsDealt.size()+" flops parsed.");
            System.out.println(turnsDealt.size()+" turns parsed. ");
            System.out.println(rivrsDealt.size()+" rivers parsed.");
            System.out.println("------------------------------------------------");
            HandClassifier hc = new HandClassifier(handsDealt,
                                                   flopsDealt,
                                                   turnsDealt,
                                                   rivrsDealt);
        }

        int readTrainingData() {
            Vector<String> lines = new Vector<>();
            BufferedReader br = null;
            FileReader fr = null;
            File training = Paths.get(path, fname).toFile();
            try {
                fr = new FileReader(training);
                String ln = null;
                try {
                   br = new BufferedReader(fr);
                    while ((ln = br.readLine()) != null) {
                        lines.add(ln);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFound!!");
            }
            content = lines;
            return lines.size();

        }

        int parseHandsFromTrainingData(){
            int nhands = 0; int index = 0;
            Vector<Integer> malformedHand = new Vector<>();
            Map<Integer,Vector<String>> cardsdealt = new HashMap<>();
            Map<Integer,Vector<String>> flopdata = new HashMap<>();
            for(String tables : content){
                String [] cards = tables.split(" ");
                Vector<String> hand = new Vector<>();
                Vector<String> flop = new Vector<>();
                Vector<String> turn = new Vector<>();
                Vector<String> rivs = new Vector<>();
                if(cards.length!=5){malformedHand.add(index);}
                else{
                    hand.add(cards[0]); hand.add(cards[1]);
                    flop.add(cards[2]); flop.add(cards[3]); flop.add(cards[4]);
                    turn.add(cards[5]); rivs.add(cards[6]);
                }
                handsDealt.put(index,hand);
                flopsDealt.put(index,flop);
                turnsDealt.put(index,turn);
                rivrsDealt.put(index,rivs);
                index++;
            }
            return nhands = cardsdealt.size();
        }
    }

    public static class HandClassifier implements Runnable {

        public Map<Integer,Vector<String>> HANDS = new HashMap<>();
        public Map<Integer,Vector<String>> FLOPS = new HashMap<>();
        public Map<Integer,Vector<String>> TURNS = new HashMap<>();
        public Map<Integer,Vector<String>> RIVRS = new HashMap<>();

        public final String [] possibleHands = {"High Card", "Pair", "Two Pair", "Three of a Kind",
                                                "Flush", "Straight", ""};


        HandClassifier(Map<Integer,Vector<String>> hands,
                       Map<Integer,Vector<String>> flops,
                       Map<Integer,Vector<String>> turns,
                       Map<Integer,Vector<String>> rivrs){

            this.HANDS = hands;
            this.FLOPS = flops;
            this.TURNS = turns;
            this.RIVRS = rivrs;
            if(this.HANDS.size()==this.FLOPS.size() && this.TURNS.size()==this.FLOPS.size() &&
                    this.RIVRS.size()==this.TURNS.size()){
                run();
            }
        }

        public void run(){
            int index = 0;
            for(Vector<String>hands : this.HANDS.values()){
                Vector<Card> han = strVecToCard(hands);
                Vector<Card> flops = strVecToCard(this.FLOPS.get(index));
                Vector<Card> turns = strVecToCard(this.TURNS.get(index));
                Vector<Card> river = strVecToCard(this.RIVRS.get(index));
                new Classification(han,flops,turns,river);
                index +=1;
            }
        }

        public Vector<Card> strVecToCard(Vector<String>strings){
            Vector<Card> ans = new Vector<>();

            for(String c : strings){
                int rank= 0; String suit = null;
                if(c.split(" ")[0].compareTo("J")==0){rank = 11;}
                if(c.split(" ")[0].compareTo("Q")==0){rank = 12;}
                if(c.split(" ")[0].compareTo("K")==0){rank = 13;}
                if(c.split(" ")[0].compareTo("A")==0){rank = 14;}
                else{
                    rank = Integer.parseInt(c.split(" ")[0]);
                    suit = c.split(" ")[1];
                }
                ans.add(new Card(rank,suit));
            }
            return ans;
        }

        public static class Classification  {

            public static Map<String,Vector<Card>> handclass = new HashMap<>();
            public Vector<Card> pairs = new Vector<>();
            public Vector<Card> suits = new Vector<>();
            public Vector<Card> strait = new Vector<>();

            /** <Classification>
             * @param hand Card vector representing a hand
             * @param flop Card vector representing a flop
             * @param turn Card vector representing a turn
             * @param river Card vector representing a river
             * </Classification>
             */
            public Classification(Vector<Card>hand,
                                  Vector<Card>flop,
                                  Vector<Card>turn,
                                  Vector<Card>river){

                boolean pp = pocketPairChecker(hand);
                boolean pairs = checkForPairs(hand,flop,turn,river);
                int nsuits = checkForFlush(hand,flop,turn,river);
                int nearby = checkForStraights(hand,flop,turn,river);

                if(pp){ this.pairs.add(hand.get(0)); this.pairs.add(hand.get(1)); }


            }

            /** <pocketPairChecker>
             * Quick check to classify whether pocket cards make a pair.
             * @param hands
             * @return boolean hasPocketPair
             */
            static boolean pocketPairChecker(Vector<Card>hands){
                boolean pocketpair = false;
                pocketpair = PokerLogic.Card.compareCards(hands.get(0),hands.get(1)).get("ranks");
                return pocketpair;
            }

            /** <checkForPairs>
             * quick check for whether there are any paired cards on the table
             * @param hand
             * @param flop
             * @param turn
             * @param river
             * @return
             */
            static boolean checkForPairs(Vector<Card>hand,
                                      Vector<Card>flop,
                                      Vector<Card>turn,
                                      Vector<Card>river){
                boolean pair = false;
                for(Card c : hand){
                    for(Card f : flop){
                        if(PokerLogic.Card.compareCards(c,f).get("ranks")){ pair = true; }
                        if(PokerLogic.Card.compareCards(turn.get(0),c).get("ranks")){pair = true;}
                        if(PokerLogic.Card.compareCards(turn.get(0),f).get("ranks")){pair = true;}
                        if(PokerLogic.Card.compareCards(turn.get(0),river.get(0)).get("ranks")){pair=true;}
                        if(PokerLogic.Card.compareCards(river.get(0),c).get("ranks")){pair=true;}
                        if(PokerLogic.Card.compareCards(river.get(0),f).get("ranks")){pair = true;}
                    }
                }
                return pair;
            }

            /** <checkForFlush><
             * Make a cursory pass through the cards on the table to
             * count the number of cards on the table with matching suits.
             * @param hand Card vector representing a hand
             * @param flop Card vector representing a flop
             * @param turn Card Vector representing a turn
             * @param river Card Vector representing a river
             * @return int nCards with suits match.
             * /checkForFlush>
             **/
            static int checkForFlush(Vector<Card>hand,
                                      Vector<Card>flop,
                                      Vector<Card>turn,
                                      Vector<Card>river){
                int suits = 0;
                if(PokerLogic.Card.compareCards(turn.get(0),river.get(0)).get("ranks")){suits+=1;}
                for(Card c : hand){
                    if(PokerLogic.Card.compareCards(turn.get(0),c).get("ranks")){suits += 1;}
                    if(PokerLogic.Card.compareCards(river.get(0),c).get("ranks")){suits+=1;}
                    for(Card f : flop){
                        if(PokerLogic.Card.compareCards(c,f).get("ranks")){ suits +=1; }
                        if(PokerLogic.Card.compareCards(turn.get(0),f).get("ranks")){suits += 1;}
                        if(PokerLogic.Card.compareCards(river.get(0),f).get("ranks")){suits +=1;}
                    }
                }
                return suits;
            }

            /** <checkForStraights>
             * Check how many cards on table have ranks within 5 of eachother
             * @param hand Card vector representing a Hand.
             * @param flop Card vector representing a Flop
             * @param turn Card vector representing a Turn.
             * @param river Card vector representing a River.
             * @return int nCards with ranks within 5 of each other.
             */
            static int checkForStraights(Vector<Card>hand,
                                      Vector<Card>flop,
                                      Vector<Card>turn,
                                      Vector<Card>river){
                int ncards =0;
                if(PokerLogic.Card.compareCards(turn.get(0),river.get(0)).get("stryt")){ncards+=1;}
                for(Card c : hand){
                    if(PokerLogic.Card.compareCards(turn.get(0),c).get("stryt")){ncards  +=1;}
                    if(PokerLogic.Card.compareCards(river.get(0),c).get("stryt")){ncards +=1;}
                    for(Card f : flop){
                        if(PokerLogic.Card.compareCards(c,f).get("stryt")){ ncards +=1; }
                        if(PokerLogic.Card.compareCards(turn.get(0),f).get("stryt")){ncards += 1;}
                        if(PokerLogic.Card.compareCards(river.get(0),f).get("stryt")){ncards +=1;}
                    }
                }

                return ncards;
            }



        }

    }

}

