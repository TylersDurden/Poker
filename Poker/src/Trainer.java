import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Trainer implements Runnable{
    
    public BST probableDecisions; 
    
    public static final String [] classes = {"HighCard",
                                             "Pair",
                                             "TwoPair",
                                             "ThreeKind",
                                             "Flush",
                                             "Straight",
                                             "FullHouse",
                                             "FourKind",
                                             "StraightFlush",
                                             "RoyalFlush"};
    public Map<String,Double> ODDS = new HashMap<>();
    public static Map<String,Integer> SCORES = new HashMap<>();
    public Vector<String> DATA = new Vector<>();
    public Trainer(String fname){
        
        Vector<String> data = getContents(fname);
        this.ODDS = plainLook(data);
        this.DATA = data;
        
        int rank = 1;
        System.out.println(data.size()+" lines of PokerData Analyzed: ");
        for(String hand : classes){
            if(rank==1){Trainer.SCORES.put(hand,10);}
            else{Trainer.SCORES.put(hand,(int)(100*data.size()/(this.ODDS.get(hand)+0.001*10)));}
            rank+=1;
        }
        //Straight is sometimes getting ranked higher than full house
        if(this.ODDS.get("Straight")>this.ODDS.get("FullHouse")){}
        for(String c : classes){
            System.out.println(
            c+" ["+(100*(this.ODDS.get(c)/data.size()))+"%] = "+
            Trainer.SCORES.get(c)+" points");
            //Sigmoid these to a good range bc rylflush is HUGE!  
        }
        
        /** <RUN> */
       run();
    }
    
    Vector<String> getContents(String fname){
        File f = Paths.get(System.getProperty("user.dir"),fname).toFile();
        BufferedReader br = null;
        Vector<String>contents = new Vector<>();
      try{
            br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine()) != null){contents.add(line);}
      }catch(FileNotFoundException e){System.out.println("File not found");}
       catch(IOException e){e.printStackTrace();}
       return contents;
    }
    
    public Map<String,Double> plainLook(Vector<String>data){
        Map<String,Double> frequency = new HashMap<>();
        // Initialize with zeroes
        for(String t : classes){frequency.put(t,0.0);}
        int [] counts = new int[10];
        for(String row : data ){
           for(String e : row.split(" ")){
               int i = 0;
               for(String type:classes){if(e.compareTo(type)==0){counts[i]+=1.0;}i++;}
           }
        }
        int index = 0;
        for(int count : counts){
            double percent = count;
            frequency.put(classes[index],percent);
            index++;
        }
        return frequency;
    }
    
/** <:RUN_IT:>
 * <1> The program is given two cards and it considers potential
 * hands that are most likely. Then it chooses to play or fold.
 * <2> Then the three flop cards are added. Options are considered, 
 * and the program has to decide whether to play or fold. 
 * <3> The Turn is added, same process. 
 * <4> The River is added and finally the program has to classify if
 * the hand was made. If it was correct, reward those decisions. 
 * If not, make corrections based on errors in scores. 
 * The goal here is to maximize output score my selecting the highest 
 * ranked hand with the lowest probability of occuring. (Best possible)
 */
    public void run(){
        //Start Creating the network structure
       this.probableDecisions = new BST(this.ODDS,this.DATA);
       //create an organized set of card inputs from training data 
       this.probableDecisions.createInputNodesFromTree(this.DATA);
       // Now feed the nodes through an organized decision tree 
       new BST.CardShark(this.probableDecisions.S0,
                         this.probableDecisions.S1,
                         this.probableDecisions.S2,
                         this.probableDecisions.S3,
                         this.probableDecisions.S4,
                         this.probableDecisions.S5,
                         this.probableDecisions.S6,
                         this.ODDS,
                         Trainer.SCORES);
       
    }
    
    /** <MAIN> */
    public static void main(String[]args){
        if(args.length<1){System.out.println("Incorrect Usage. For Help Enter:\n$java Trainer -h");}
        else if(args[0].contains(".txt")){Trainer t = new Trainer(args[0]);}
        if(args[0].compareTo("unsupervised")==0){
            new Trainer.VirtualPoker(1000000,"trickypoker.txt").run();
            System.out.println("100000 hands dealt.");
            }
        
    }
    
    public static class VirtualPoker implements Runnable{
        
        Map<Integer,Vector<Card>> HANDS = new HashMap<>();
        Map<Integer,Vector<Card>> FLOPS = new HashMap<>();
        Map<Integer,Vector<Card>> TURNS = new HashMap<>();
        public int SEQ=0;
        private int DEPTH;
        
        public VirtualPoker(int nHands,String outputfname){
            this.DEPTH = nHands;
        }
        
        public void run(){
            while(SEQ<this.DEPTH){
                Deck d = new Deck();
                d.shuffle();
                HANDS.put(SEQ,d.deal(2));
                FLOPS.put(SEQ,d.deal(1));
                TURNS.put(SEQ,d.deal(1));
                SEQ++;
            }
        }
    }
    
}