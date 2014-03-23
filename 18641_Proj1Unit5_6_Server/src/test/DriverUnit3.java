package test;

import scale.EditOptions;
import adapter.BuildAuto;
import adapter.EditThread;

public class DriverUnit3 {
    public static void testCase1(){
        BuildAuto buildAuto = new BuildAuto();

        System.out
                .printf("--------------\nRead files:testFile1 :\n---------------\n");

        // test create and print an Auto instance.
        buildAuto.buildAuto("testFile1.txt", 0);

        // Focus Wagon ZTW
        
        EditThread b1 = buildAuto;

        /* use option ID to control the option. */
        EditOptions t1 = new EditOptions("t1", b1, 1);
        EditOptions t2 = new EditOptions("t2", b1, 2);

        t1.start();
        t2.start();
        
    }
    
    public static void testCase2(){
        BuildAuto buildAuto = new BuildAuto();

        System.out
                .printf("--------------\nRead files:testFile1 :\n---------------\n");

        // test create and print an Auto instance.
        buildAuto.buildAuto("testFile1.txt", 0);
        
        EditThread b1 = buildAuto;

        /* use option ID to control the option. */
        EditOptions t3 = new EditOptions("t3", b1, 3);
        EditOptions t4 = new EditOptions("t4", b1, 4);

        t3.start();
        t4.start();
                
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        
        /* first use t1 to modify the Option Choice, then use t2 to read
         * it. In this case, because set option will sleep 10s, then t2
         * will wait 10s to get the value */
        
        
        //System.out.printf("\nTest case 2:\n");
        
        /* first use t3 to modify the Option Choice, 
         * also we use t4 to modify the Option Choice.
         * They can alter same property and does not cause data corruption. */
        testCase2();
        
        //System.out.printf("\nTest case 1:\n");
        //testCase1();
        

    }
}
