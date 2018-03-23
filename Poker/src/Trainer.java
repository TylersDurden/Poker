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
        System.out.println(data.size()+" samples: ");
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
    
    public void run(){
       this.probableDecisions = new BST(this.ODDS,this.DATA);
       this.probableDecisions.generateStateMap(this.DATA);
       //System.out.println("N = "+N);
    }
    
    public static void main(String[]args){
        if(args.length<1){System.out.println("Incorrect Usage. For Help Enter:\n$java Trainer -h");}
        else{
            Trainer t = new Trainer(args[0]);
            }
        
    }
}