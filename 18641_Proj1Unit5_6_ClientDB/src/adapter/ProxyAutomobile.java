package adapter;

import java.util.LinkedHashMap;
import java.util.Set;

import model.Automobile;
import util.FileIO;
import util.ReadData;
import exceptions.UserExceptions;

public abstract class ProxyAutomobile {
    // private static Automobile a1;
    private static LinkedHashMap<String, Automobile> mobileHash = new LinkedHashMap<String, Automobile>();

    // build a new Auto.
    public void buildAuto(String filename, int fileType) {
        
        if (fileType == 0) { // read the configruation file.
            String tmpfileName = filename;
            Automobile newAuto = null;
            
            ReadData readData = new ReadData();

            // fix the problem by inputting new file name from the console.
            do {
                try {
                    newAuto = readData.buildAutoObject(tmpfileName);
                } catch (UserExceptions e) {
                    e.printmyproblem();

                    if (e.getErrorNumber() == 0) {

                        // get a new file name from the console
                        tmpfileName = e.fixProblemReadFromConsole();
                    } else {
                        break;
                    }
                }
            } while (newAuto == null);
            // add the new automobile model to the hash table.
            mobileHash.put(newAuto.getName(), newAuto);
        } else if (fileType == 1) { // read the properties file. 
            Automobile newAuto = null;
            newAuto = FileIO.readPropertyFile(filename);
            
            if (newAuto != null) {
                // add the new automobile model to the hash table.
                mobileHash.put(newAuto.getName(), newAuto);
            }
        }
    }

    public void updateOptionSetName(String Modelname, String OptionSetname,
            String newName) {
        // find the automobile from the hash table.
        Automobile autoTmp = mobileHash.get(Modelname);

        // set the option name to be a new one.
        autoTmp.updateOptionSet(OptionSetname, newName);
    }

    public void updateOptionPrice(String Modelname, String optionname,
            String Option, float newprice) {

        // find the automobile from the hash table.
        Automobile autoTmp = mobileHash.get(Modelname);

        // don't set new name, just set a new price.
        autoTmp.updateOption(optionname, Option, null, newprice);
    }

    public void printAuto(String Modelname) {
        // find the automobile from the hash table.
        Automobile autoTmp = mobileHash.get(Modelname);
        if (autoTmp != null) {
            autoTmp.print();
        }
    }

    // Unit 3:
    // get the Automobile class through the module name.
    public Automobile getAutomobileByName(String name) {
        return mobileHash.get(name);
    }
    
    // return the mobileHash
    public LinkedHashMap<String, Automobile> getAutoHashMap() {
        return mobileHash;
    }
    
    // build a Automible and added it to the LHM from the property file.
    public void buildAutoFromPropertyFile(String filename) {
        // 1: read a property file.
        buildAuto(filename, 1);
    }
    
    // return all the avaliable automobile's name
    public String getAvailableAutos(){
        Set<String> keyset = mobileHash.keySet();
        return keyset.toString();
    }
}
