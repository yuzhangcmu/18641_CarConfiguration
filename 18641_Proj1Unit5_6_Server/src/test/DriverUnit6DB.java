package test;

import model.Automobile;
import adapter.BuildAuto;
import adapter.DeteleAutoDB;
import adapter.EditThread;
import db.MySqlCreateTable;
import db.MySqlDeleteAuto;
import db.MySqlEditTable;
import db.MySqlOption;

/**
 * *
 * @author Yu Zhang
 * 
 * For testing the unit6: database.
 *
 */

public class DriverUnit6DB {
    public static void main (String[] args) {
        // initialize the database.
        //MySqlCreateTable.initDatabase();
        
        addAuto();
        //delAuto();
        //updateAuto_optionsetName();
        //updateAuto_price();
        
    }
    
    public static void updateAuto_price() {
        MySqlEditTable mset = new MySqlEditTable();
        
        mset = new MySqlEditTable();
        
        System.out.println("Try to modify the Liquid Grey Clearcoat Metallic price of Audi A6 to be 2000.24 :");
        mset.updateOptionPrice("Audi A6", "Color of Audi MODIFIED", 
                "Liquid Grey Clearcoat Metallic", (float)2000.24);
        //System.out.println("After add a auto, display the new automobile table:");
        
        mset.displayAutoTable();
    }
    
    private static void updateAuto_optionsetName() {
        MySqlEditTable mset = new MySqlEditTable();
        
        mset = new MySqlEditTable();
        
        System.out.println("Try to modify the Optionset 'Color of Audi' to be 'Color of Audi MODIFIED':");
        mset.updateOptionSetName("Audi A6", "Color of Audi", "Color of Audi MODIFIED");
        //System.out.println("After add a auto, display the new automobile table:");
        
        mset.displayAutoTable();
    }

    static void addAuto() {
        // initialize the database.
        //MySqlOption.initDatabase();
        
        BuildAuto buildAuto = new BuildAuto();
        buildAuto.buildAuto("testFile1.txt", 0);
        buildAuto.buildAuto("testFile2.txt", 0);
        
        EditThread et = buildAuto;
        Automobile auto = et.getAutomobileByName("Benz S4");
        //auto.print();
        
        
        MySqlEditTable mset = new MySqlEditTable();
        
        mset.addAuto(auto);
        System.out.println("After add a auto, display the new automobile table:");
        
        mset.displayAutoTable();
        
        auto = et.getAutomobileByName("Audi A6");
        //auto.print();
        
        
        mset = new MySqlEditTable();
        
        mset.addAuto(auto);
        System.out.println("After add a auto, display the new automobile table:");
        
        mset.displayAutoTable();
    }
    
    static void delAuto() {
        // initialize the database.
       //MySqlOption.initDatabase();
       
       BuildAuto buildAuto = new BuildAuto();
       buildAuto.buildAuto("testFile1.txt", 0);
       
       EditThread et = new BuildAuto();
       Automobile auto = et.getAutomobileByName("Benz S4");
       
       DeteleAutoDB mset = new MySqlDeleteAuto();
       
       System.out.println("Delete the automobile from table..");
       mset.delAuto(auto);
       System.out.println("After delete a auto, display the new automobile table:");
       //mset.displayAutoTable();
    }
}
