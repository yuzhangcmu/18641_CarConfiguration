package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import exceptions.UserExceptions;
import model.Automobile;

public class ReadData {
    //private 

    public Automobile buildAutoObject(String fileName) throws UserExceptions{
        String lastString = null; /* save the last string. */
        String currString = null; /* save the current string. */
        
        String modelName = null;
        
        Automobile autoMobileLab = null;
        
        String optionName = null;
        
        try{
            FileReader file = new 
                    FileReader(fileName);
            
            BufferedReader buff = new BufferedReader(file);
            
            boolean eof = false;
            
            int lineNumber = 0;
            
            while(!eof){
                //System.out.printf("line number: %d\n", lineNumber);
                //lineNumber++;
                
                String line = buff.readLine();
                if(line == null){
                    eof = true;
                    break;
                }
                else{
                    //System.out.println(line);
                }
                
                // tokenize a line to words.
                StringTokenizer st = new StringTokenizer(line, ",");
                
                // record the token number.
                int tokenNumber = 0;
                
                //lastString = null;
                //currString = null;
                
                while(st.hasMoreTokens()){
                    //int tokenInt = Integer.parseInt(st.nextToken());
                    
                    lastString = currString;
                    currString = st.nextToken();
                    
                    if (!st.hasMoreTokens()) {
                        if (currString.equals("Model Name")) {
                            buff.close();
                            throw new UserExceptions(1, "Miss Model Name!");
                        } else if (currString.equals("Base Price")) {
                            buff.close();
                            throw new UserExceptions(2, "Miss Base Price!");
                        } else if (currString.equals("OptionSet number")) {
                            buff.close();
                            throw new UserExceptions(3, "Miss OptionSet number!");
                        } else if (tokenNumber > 1 && (tokenNumber%2 == 1)){
                            //buff.close();
                            //throw new UserExceptions(4, "Miss Price!");
                        }
                    }
                    //System.out.printf("LastString:%s currString:%s \n", lastString, currString);
                    
                    if (lastString == null || currString == null){
                        continue;
                    }
                    
                    // skip the first word. also the current string should not be null. 
                    //if (lastString == null){
                    //    continue;
                    //}
                    
                    //System.out.printf("LastString:%s currString:%s \n", lastString, currString);
                    
                   
                    
                    if (lastString.equals("Model Name")){
                        modelName = currString;
                    }else if (lastString.equals("OptionSet number")){
                        if (modelName != null){
                            /*
                            System.out.printf("modelname:%s, options number:%d\n",
                                    modelName, Integer.parseInt(currString));
                            */
                            autoMobileLab = new Automobile(modelName, Integer.parseInt(currString));
                        }
                        else{
                            buff.close();
                            throw new IllegalArgumentException(
                                    "File format is wrong!");
                        }
                    }else if (lastString.equals("Base Price")){
                        /*
                        System.out.printf("Base Price:%d\n",
                               Integer.parseInt(currString));
                        */       
                        float basePrice = Integer.parseInt(currString);
                        
                        if(basePrice < 0){
                            buff.close();
                            throw new UserExceptions(4, "The base Price is wrong!");
                        }
                        
                        if (autoMobileLab != null){                            
                            autoMobileLab.setBasePrice(Integer.parseInt(currString));
                        }
                    }else if (1 == tokenNumber){
                        // store the option name.
                        optionName = lastString;
                        
                        // this is the options. set the options number.
                        /*
                        System.out.printf("SET OPTIONS: last:%s curr:%s\n",
                                lastString, currString);
                                */
                         if (autoMobileLab != null){
                             // initialize the optionSet.
                             autoMobileLab.setOptionSet(lastString, Integer.parseInt(currString));
                         }
                    }else if (tokenNumber > 1 && (tokenNumber%2 == 1)){
                        // this is the options. set the options number.
                        /*
                         System.out.printf("SET OPTION: last:%s curr:%s\n",
                                lastString, currString);*/
                        
                         
                         if (autoMobileLab != null){
                             // initialize the optionSet.
                             autoMobileLab.setOption(optionName, lastString, Integer.parseInt(currString));
                         }
                    }
                    
                    tokenNumber++;
                }
            }
            
            buff.close();
        }catch(IOException e){
            System.out.println("Error --" + e.toString());
            throw new UserExceptions(0, "File IO error, wrong file name.");
        }
        
        return autoMobileLab;
    }
}
