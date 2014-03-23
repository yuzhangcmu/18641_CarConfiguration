package util;

// use this to get information from property file.
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;




//import client.FileIO;
import model.Automobile;

public class FileIO {
    static public void serializeAuto(Object inputObject, String fileName){
        try{
            
            ObjectOutputStream out = 
                    new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(inputObject);
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
            inMobile = (Object)in.readObject();
            in.close();
            return inMobile;
            
        }catch(Exception e){
            System.out.print(e);
            
        }
        
        return null; 
    }
    
 // accept a serilized property file and create a automobile through it.
    public static Automobile readProperty(Object propertyObj) {
        StringBuilder modelName = new StringBuilder();
        
        Properties props = (Properties) propertyObj;
        
        if (props == null) {
            return null;
        }
        
        Automobile auto = null;
        
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
        
        // print out the automobile.
        auto.print();
        
        return auto;
        
    }
    
    
    public static Properties readPropertyFileToObject(String File) {
        if (File == null) {
            return null;
        }
        
        Properties props = new Properties();
        FileInputStream in = null;
        
        try {
            in = new FileInputStream(File);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
        try {
            props.load(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return props;
    }
}