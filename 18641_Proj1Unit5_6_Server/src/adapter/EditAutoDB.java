package adapter;

import model.Automobile;

// implement the methods to modify the database
public interface EditAutoDB {
    public void displayAutoTable();
    
    public void addAuto(Automobile auto);
        
    public void updateOptionSetName(String Modelname, 
            String OptionSetname, String newName);
    
    public void updateOptionPrice(String Modelname, String optionSetName,
            String Option, float newprice);
}
