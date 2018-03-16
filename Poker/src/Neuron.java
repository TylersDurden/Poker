/** NEURON.java */
import java.util.*;
import java.io.*;

import java.nio.file.*;
import java.util.regex.*;

/**<NEURON>
 * Classify and label the training set, by teaching program
 * to classify Poker Hands. 
 * @author ScottRobbins
 */
public class Neuron {
    
    public final int S0_size = 2;
    public final int S1_size = 3;
    public final int S2_size = 4;
    public final int S3_size = 5;
    
    public int SIZE;
    public Vector<Card> SELF = new Vector<>();
    public Map<Card,Vector<Card>> OUTS = new HashMap<>();
    
    
    public Neuron(String cards){
        int length_classifier = cards.split(" ").length;
        switch(length_classifier){
            case(S0_size):
                prepSTATE0Neuron(cards.split(" "));
                this.SIZE = S0_size;
                break;
            case(S1_size):
                prepSTATE1Neuron(cards.split(" "));
                this.SIZE = S1_size;
                break;
            case(S2_size):
                prepSTATE2Neuron(cards.split(" "));
                this.SIZE = S2_size;
                break;
            case(S3_size):
                prepSTATE3Neuron(cards.split(" "));
                this.SIZE = S3_size;
                break;
            default:
                break;
        }
        /**Once all the Neurons are Prepped...<ACTIVATE>!*/ 
    }
    /** <Instantiate_PreFlop_Neurons> */
        void prepSTATE0Neuron(String [] cards){
            // What Matters for pre-flop conditions?
            createSelfInstance(cards);
            new Perceptron(this.SELF);
        }
        /** <Instantiate_Flop_Neurons> */
        void prepSTATE1Neuron(String [] cards){
            //Whats important about flop?
            createSelfInstance(cards);
            new Perceptron(this.SELF);
        }
        /** <Instantiate_Turn_Neurons> */
        void prepSTATE2Neuron(String [] cards){
            //What happens at the turn?
            createSelfInstance(cards);
            new Perceptron(this.SELF);
        }
        /** <Instantiate_Table_Neurons> */
        void prepSTATE3Neuron(String [] cards){
            //What are the results at STATE3?
            createSelfInstance(cards);
            new Perceptron(this.SELF);
        }
        
        /** <Instantiate_the_SELF_Vector> */
        void createSelfInstance(String [] cards){
            for(String card : cards){
                int rank = (int)(extract(card));
                String suit = card.split(String.valueOf(rank))[1];
                this.SELF.add(new Card(rank,suit));
            }
        }
        
        /** <Extract> double from input String */
        double extract(String str) {
            Double dig = 0.0;
            Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(str);
            while (m.find()){return Double.parseDouble(m.group(1));}
            return dig;
        }

    

    public static void main(String []args){
        if(args.length<1){System.out.println("Incorrect Usage.");}
        else{
            TrainingDataPipeLine tdpl = new TrainingDataPipeLine(args[0]);
             if(tdpl.goodDims){System.out.println("Correct Dimensions");}
            // Create Neurons By STATE 
            Vector<Neuron> NEURONS0 = new Vector<>();
            Vector<Neuron> NEURONS1 = new Vector<>();
            Vector<Neuron> NEURONS2 = new Vector<>();
            Vector<Neuron> NEURONS3 = new Vector<>();
            for(String hand : tdpl.HANDS){NEURONS0.add(new Neuron(hand));}
            for(String flop : tdpl.FLOPS){NEURONS1.add(new Neuron(flop));}
            for(String turn : tdpl.TURNS){NEURONS2.add(new Neuron(turn));}
            for(String cards: tdpl.TABLES){NEURONS3.add(new Neuron(cards));}
            /** Debug PrintOut */
            System.out.println(NEURONS0.size()+" STATE0 Neurons Instantiated");
            System.out.println(NEURONS1.size()+" STATE1 Neurons Instantiated");
            System.out.println(NEURONS2.size()+" STATE2 Neurons Instantiated");
            System.out.println(NEURONS3.size()+" STATE3 Neurons Instantiated");
            //Take a peek at first set of states for debugging 
            //for(Card c : NEURONS3.get(0).SELF){c.showMe();}
            
            
            
           }
        }
    
    public static class TrainingDataPipeLine implements Runnable{
        // Raw text file data picked up by the FileReader
        public String fname;
        public Vector<String> DATAIN = new Vector<>();
        // Parse out the relevant content : 
        public Vector<String> HANDS  = new Vector<>();
        public Vector<String> FLOPS  = new Vector<>();
        public Vector<String> TURNS = new Vector<>();
        public Vector<String> TABLES = new Vector<>();
        //Checks for states of completion
        public boolean doneReading = false;
        public boolean goodDims = false;
        
        public TrainingDataPipeLine(String trainfile){
            this.fname  = trainfile;            
            this.DATAIN = fetchTrainingData();    
            //System.out.println(this.DATAIN.get(0));;}
            run();
            if(doneReading){//DEBUG - check variables
                if((this.HANDS.size()==this.FLOPS.size()) &&
            (this.TURNS.size()==this.TABLES.size())){this.goodDims=true;}
                        
                }
        }
        
        /** Read <training.txt> file */
        Vector<String> fetchTrainingData(){
            Vector<String> content = new Vector<>();
            BufferedReader br = null;
            try{
                File f = Paths.get(System.getProperty("user.dir"),this.fname).toFile();
                br = new BufferedReader(new FileReader(f));
                String ln = null; 
                while((ln = br.readLine()) != null){content.add(ln);}
            }catch(FileNotFoundException e){System.out.println("File Not Found!");}
            catch(IOException e){e.printStackTrace();}
            // Finally Return the content read 
            return content;
            
        }
        /** <RUN_IT> */
        public void run(){
            /** Every element is actually 1ROUND */ 
           for(String ROUND : this.DATAIN){
               this.HANDS.add(ROUND.split(",")[0]);
               this.FLOPS.add(ROUND.split(",")[1]);
               this.TURNS.add(ROUND.split(",")[2]);
               this.TABLES.add(ROUND.split(",")[3]);
           }
           this.doneReading = true; 
        }
        
        
  }/**<EndOf_TrainingDataPipeLine>*/
 
  /** <PERCEPTRON:> 
   * 
   */
  public static class Perceptron {
      
      //For each Rank, how many diff cards with this rank are present?
      public Map<Integer,Integer> rankedBinCounts = new HashMap<>(); 
      //For each Suit, how many diff cards with same suit are present?
      public Map<String, Integer> suitedBinCounts = new HashMap<>();
      //For each Rank, how many diff cards present build a straight? 
      public Map<Integer,Integer> straitBinCounts = new HashMap<>();
      
      //Contruct Perceptron
      public Perceptron(Vector <Card> cardsIn){
          /** Keep this as Generic as possible */
          //For following: If Cards aren't same record distinctions
          //Compare Ranks
          this.rankedBinCounts = rankCheck(cardsIn);
          //Compare Suits
          this.suitedBinCounts = suitCheck(cardsIn);
          //Proximity check
          this.straitBinCounts = nearCheck(cardsIn);
      }
      
      Map<Integer,Integer> rankCheck(Vector<Card>cards){
          Map<Integer,Integer> rankoutfound = new HashMap<>();
          return rankoutfound;
      }
      
      Map<String,Integer> suitCheck(Vector<Card>cards){
          Map<String,Integer> suitoutfound = new HashMap<>();
          return suitoutfound;
      }
      
      Map<Integer,Integer> nearCheck(Vector<Card>cards){
          Map<Integer,Integer> strtoutfound = new HashMap<>();
          return strtoutfound;
      }
      
      
  }/** <EndOf_PERCEPTRON>*/ 
 
}/**<EndOf_NEURON>*/