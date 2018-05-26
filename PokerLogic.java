import java.io.FileWriter;
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

    public PokerLogic(Map<Integer,Vector<Card>> data){
        simulatedCards = data;
        dumpToTextFile();
    }

    void dumpToTextFile(){
        TextFileWriter txtfw = new TextFileWriter(simulatedCards);
        txtfw.run();
    }

    public static void main(String[]args){
        Map<Integer,Vector<Card>> cardsMap =new HashMap<>();
        int nTries = Integer.parseInt(args[0]);
        for (int i = 0; i <nTries ; i++) {
            Deck d = new Deck();
            d.shuffle();
            Hand h = new Hand(d.deal(5));
            cardsMap.put(i,h.CARDS);
        }

        PokerLogic trainingData = new PokerLogic(cardsMap);

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

    private static class TextFileWriter implements Runnable{

        Map<Integer,Vector<Card>> DATA = new HashMap<>();

        TextFileWriter(Map<Integer,Vector<Card>>data){
            DATA = data;
        }

        public void run(){
            String path = "";
            File fout = Paths.get(path,"outputfile.txt").toFile();
            try{
                FileWriter fw = new FileWriter(fout);
                String content =null;
                for(Vector<Card> cards : DATA.values()){
                    StringBuffer ln = null;
                    for(Card c : cards){
                         ln = new StringBuffer(c.RANK+c.SUIT+" ");
                    }
                    ln.append("\n");
                    content+= ln.toString();
                }
                fw.write(content);
            }catch(IOException e){e.printStackTrace();}

        }
    }


    }

