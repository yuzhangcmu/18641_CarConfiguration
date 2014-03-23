package test;

import adapter.BuildAuto;
import adapter.CreateAuto;
import adapter.UpdateAuto;

public class DriverUnit4 {
    public static void main(String[] args) {
        testCase1();
    }
    
    public static void testCase1() {
        //BuildAuto buildAuto = new BuildAuto();
        
        
        CreateAuto a1 = new BuildAuto();
        //UpdateAuto a2 =  new BuildAuto();
        
        
        
        System.out
        .printf("--------------\nRead files: properties.txt :\n---------------\n");

        // test create and print an Auto instance.
        a1.buildAuto("Prius.properties", 1);
        a1.printAuto("Toyota Prius");
    }
}
