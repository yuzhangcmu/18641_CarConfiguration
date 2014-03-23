package util;

// use this to get information from property file.
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Automobile;

public class FileIO {
    static public void serializeAuto(Object autoMobile){
        try{
            
            ObjectOutputStream out = 
                    new ObjectOutputStream(new FileOutputStream("auto.ser"));
            out.writeObject(autoMobile);
            out.close();
            
        }catch(Exception e){
            System.out.print(e);
        }
    }
    
    static public Object DeserializeAuto(String fileName){
        try{
            Object inMobile;
            
            ObjectInputStream in = 
                    new ObjectInputStream(new FileInputStream(fileName));
            inMobile = in.readObject();
            in.close();
            return inMobile;
            
        }catch(Exception e){
            System.out.print(e);
            
        }
        
        return null; 
    }
    
    public static Automobile readPropertyFile(String fileName) {
        StringBuilder modelName = new StringBuilder();
        
        Automobile auto = null;
        
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        }
        
        if (in != null) {
            try {
                props.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            } // this loads the entire file in memory.
        }
        
        String carMake = props.getProperty("CarMake"); // this is how you read a property. It is like getting a value
                                                       // from HashTable
        
        if (!carMake.equals(null)) {
            String carModel = props.getProperty("CarModel");

            // create the automobile by the model name.
            modelName.append(carMake);
            modelName.append(" ");
            modelName.append(carModel);
            auto = new Automobile(modelName.toString(), 0);
            
            String basePrice = props.getProperty("BasePrice");
            if (basePrice != null) {
                auto.setBasePrice(Integer.parseInt(basePrice));
            }
            
            String optionl = null;
            optionl = props.getProperty("Option1");
            
            for (int i = 1; ;i++) {
                
                optionl = props.getProperty("Option"+i);
                
                if (optionl == null) {
                    // no more optionl.
                    break;
                }
                
                //System.out.println(optionl);
                
                // set the 1st optionSet.
                auto.setOptionSet(optionl, 0);
                // set the 1st option of optionSet 1.
                String OptionValue;
                String OptionPrice;
                for (int j = 0; ; j++) {
                    char seq = (char) ('a'+j);
                    
                    OptionValue = props.getProperty("OptionValue"+i+seq);
                    
                    if (OptionValue == null) {
                        break;
                    }
                    
                    OptionPrice = props.getProperty("OptionPrice"+i+seq);
                    
                    if (OptionPrice != null) {
                        auto.setOption(optionl, OptionValue,
                                Integer.parseInt(OptionPrice));
                    }
                }
               
            }
            
        }
        
        return auto;
        
    }
}
