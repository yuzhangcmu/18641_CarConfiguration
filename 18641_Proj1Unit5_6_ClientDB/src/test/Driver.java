package test;

//import Adapter.*;

import model.Automobile;
import util.ReadData;
import adapter.BuildAuto;
import adapter.CreateAuto;
import adapter.UpdateAuto;
import exceptions.UserExceptions;

public class Driver {
    public static void main(String[] args) {

         test_Unit2_changePrice_updateOptName();

        //test_Unit2_user_selection();

        // TEST THE FIVE EXCEPTIONS.

        // exception 0 Test the auto fix exception.
         test_Unit1_AutoFixFileName();

        // exception 1
        // test_MissModelName();

        // exception 2
        // test_MissBasePrice();

        // exception 3
        // test_MissOptionNumber();

        // exception 4: base price is wrong, like: -10000
        // test_BasePriceWrong();
    }

    private static void test_Unit1_AutoFixFileName() {
        ReadData readData = new ReadData();
        Automobile FordZTW = null;

        String fileName = "ErrorFileName.txt";

        // fix the problem by inputting new file name from the console.
        do {
            try {
                FordZTW = readData.buildAutoObject(fileName);
            } catch (UserExceptions e) {
                // get a new file name from the console
                // System.out.printf(e.toString());
                e.printmyproblem();
                fileName = e.fixProblemReadFromConsole();
            }
        } while (FordZTW == null);

        // print attributes before serialization
        FordZTW.print();

        // serialize the object
        util.FileIO.serializeAuto(FordZTW);

        // deserialize and read it into memory
        Automobile newFordZTW = (Automobile) util.FileIO.DeserializeAuto("auto.ser");

        // print new attributes
        if (newFordZTW != null) {
            newFordZTW.print();
        }
    }

    private static void test_Unit2_changePrice_updateOptName() {
        CreateAuto a1 = new BuildAuto();
        UpdateAuto a2 =  new BuildAuto();
        

        System.out
                .printf("--------------\nRead 2 files:testFile1 and testFile2:\n---------------\n");

        // test create and print an Auto instance.
        a1.buildAuto("testFile1.txt", 0);
        a1.buildAuto("testFile2.txt", 0);

        System.out
                .printf("--------------\nPrint the first model:\n---------------\n");

        a1.printAuto("Focus Wagon ZTW");

        System.out
                .printf("--------------\nPrint the second model:\n---------------\n");

        a1.printAuto("Audi A6");

        // modify the name of the optionset "Color" to be "ColorOfAuto"
        a2.updateOptionSetName("Focus Wagon ZTW", "Color", "ColorOfAuto");

        // modify the price of color "Liquid Grey Clearcoat Metallic"
        // to be 100.45.
        a2.updateOptionPrice("Focus Wagon ZTW", "ColorOfAuto",
                "Liquid Grey Clearcoat Metallic", (float) 100.45);

        // modify the name of the optionset "Brakes/Traction Control" to be
        // "Brakes/Traction Control"
        a2.updateOptionSetName("Audi A6", "Brakes/Traction Control",
                "Brakes or Traction Control");

        // modify the price of color "Liquid Grey Clearcoat Metallic"
        // to be 100.45.
        a2.updateOptionPrice("Audi A6", "Brakes or Traction Control",
                "ABS", (float) 450);

        System.out.printf("--------------\nAfter modify:\n---------------\n");

        System.out
                .printf("--------------\nPrint the first model:\n---------------\n");
        a1.printAuto("Focus Wagon ZTW");
        System.out
                .printf("--------------\nPrint the second model:\n---------------\n");
        a1.printAuto("Audi A6");

    }

    /*
     * Automobile class has way to track user selection (through creation of
     * Option choice variable and related functionality).
     */

    /*
    private static void test_Unit2_user_selection() {
        System.out
                .printf("--------------\nTest user selection:\n---------------\n");

        ReadData readData = new ReadData();
        Automobile FordZTW = null;

        try {
            FordZTW = readData.buildAutoObject("testFile1.txt");
        } catch (UserExceptions e) {
            // get a new file name from the console
            // System.out.printf(e.toString());
            e.printmyproblem();
        }

        if (FordZTW == null) {
            return;
        }

        // print attributes before serialization
        FordZTW.print();

        FordZTW.setOptionChoice("Color", "Liquid Grey Clearcoat Metallic");
        FordZTW.setOptionChoice("Transmission", "manual");
        FordZTW.setOptionChoice("Brakes/Traction Control",
                "ABS with Advance Trac");
        FordZTW.setOptionChoice("Side Impact Air Bags", "not present");
        FordZTW.setOptionChoice("Power Moonroof", "present");

        // serialize the object
        util.FileIO.serializeAuto(FordZTW);

        print_choice(FordZTW);

        System.out
                .printf("--------------\nAfter serialization:\n---------------\n");

        // deserialize and read it into memory
        Automobile newFordZTW = util.FileIO.DeserializeAuto("auto.ser");

        // print new attributes
        if (newFordZTW != null) {
            newFordZTW.print();

            print_choice(newFordZTW);
        }

    }
    */
    
    private static void print_choice(Automobile mobile) {
        if (mobile == null) {
            return;
        }

        System.out.printf("Print the user choices:\n");

        System.out.printf("The user choice of \"Color\" is:%s Price is: %f\n",
                mobile.getOptionChoice("Color"),
                mobile.getOptionChoicePrice("Color"));
        System.out.printf(
                "The user choice of \"Transmission\" is:%s. Price is: %f\n",
                mobile.getOptionChoice("Transmission"),
                mobile.getOptionChoicePrice("Transmission"));
        System.out
                .printf("The user choice of \"Brakes/Traction Control\" is:%s. Price is: %f\n",
                        mobile.getOptionChoice("Brakes/Traction Control"),
                        mobile.getOptionChoicePrice("Brakes/Traction Control"));
        System.out
                .printf("The user choice of \"Side Impact Air Bags\" is:%s. Price is: %f\n",
                        mobile.getOptionChoice("Side Impact Air Bags"),
                        mobile.getOptionChoicePrice("Side Impact Air Bags"));
        System.out.printf(
                "The user choice of \"Power Moonroof\" is:%s. Price is: %f\n",
                mobile.getOptionChoice("Power Moonroof"),
                mobile.getOptionChoicePrice("Power Moonroof"));

        System.out.printf("Total price is: %f\n", mobile.getTotalPrice());

        return;

    }

    private static void test_MissBasePrice() {
        BuildAuto buildAuto = new BuildAuto();

        System.out
                .printf("--------------\n Test a file that miss BasePrice:\n---------------\n");

        // test create and print an Auto instance.
        buildAuto.buildAuto("test_MissBasePrice.txt", 0);

        buildAuto.printAuto("Audi A6");
    }

    private static void test_MissModelName() {
        BuildAuto buildAuto = new BuildAuto();

        System.out
                .printf("--------------\n Test a file that miss ModelName:\n---------------\n");

        // test create and print an Auto instance.
        buildAuto.buildAuto("test_MissModelName.txt", 0);

        buildAuto.printAuto("Audi A6");
    }

    private static void test_MissOptionNumber() {
        BuildAuto buildAuto = new BuildAuto();

        System.out
                .printf("--------------\n Test a file that miss OptionSet number:\n---------------\n");

        // test create and print an Auto instance.
        buildAuto.buildAuto("test_MissOptionSetNumber.txt", 0);

        buildAuto.printAuto("Audi A6");
    }

    private static void test_BasePriceWrong() {
        BuildAuto buildAuto = new BuildAuto();

        System.out
                .printf("--------------\n Test a file that Base Price is negtive(Wrong):\n---------------\n");

        // test create and print an Auto instance.
        buildAuto.buildAuto("test_BasePriceIsWrong.txt", 0);

        buildAuto.printAuto("Audi A6");
    }

}
