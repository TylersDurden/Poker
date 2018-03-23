import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Trainer implements Runnable{
    
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
    
    public Trainer(String fname){
        
        Vector<String> data = getContents(fname);
        Map<String,Double> odds = plainLook(data);
        int rank = 1;
        System.out.println(data.size()+" samples: ");
        for(String hand : classes){
            System.out.println(rank+") "+hand+" "+(100*(odds.get(hand)/data.size()))+"%");
            rank+=1;
        }   
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
        
    }
    
    public static void main(String[]args){
        if(args.length<1){System.out.println("Incorrect Usage. For Help Enter:\n$java Trainer -h");}
        else{new Trainer(args[0]);}
    }
}