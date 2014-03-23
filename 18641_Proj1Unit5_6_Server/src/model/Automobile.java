package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Automobile implements Serializable {
    /**
     * generate a ID.
     */
    private static final long serialVersionUID = 4143516018295268216L;

    private String name;

    private String model;
    private float basePrise;

    // use ArrayList to solve the problem.
    private ArrayList<OptionSet> opset;

    // new variables in UNIT2
    private String make;

    // private String model;

    public Automobile(String modelName, int size) {
        opset = new ArrayList<OptionSet>();
        name = modelName;
        model = "";
        make = "";
    }

    public String getName() {
        return name;
    }

    // update.
    public synchronized void setName(String newName) {
        name = newName;
    }

    // read
    public synchronized float getBasePrice() {
        return basePrise;
    }

    // update
    public synchronized void setBasePrice(float price) {
        basePrise = price;
    }

    // get OptionSet by index.
    public synchronized OptionSet getOptionSet(int index) {
        if (index < opset.size()) {
            // return opset[index];
            return opset.get(index);
        }
        return null;
    }
    
    public synchronized String getOptionSetNameByIndex(int index) {
        if (index < opset.size()) {
            // return opset[index];
            return opset.get(index).getName();
        }
        return null;
    }
    
    public synchronized int getOptionNumberByIndex(int index) {
        if (index < opset.size()) {
            return opset.get(index).getOptionNumber();
        }
        
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public synchronized String getOptionNameByIndex(int optionSetIndex, int optionIndex) {
        if (optionSetIndex < opset.size()) {
            return opset.get(optionSetIndex).getOptionByIndex(optionIndex).getName();
        }
        
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public synchronized float getOptionPriceByIndex(int optionSetIndex, int optionIndex) {
        if (optionSetIndex < opset.size()) {
            return opset.get(optionSetIndex).getOptionByIndex(optionIndex).getValue();
        }
        
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public synchronized int getOptionSetNumber() {
        return opset.size();
    }

    public synchronized OptionSet findOpSetWithName(String opSetName) {

        // find the OptionSet by name.
        for (int i = 0; i < opset.size(); i++) {
            if (opset.get(i).getName().equals(opSetName)) {
                return opset.get(i);
            }
        }

        return null;
    }

    private synchronized int findOpSetIndexWithName(String opSetName) {
        // find the OptionSet by name.
        for (int i = 0; i < opset.size(); i++) {
            if (opset.get(i).getName().equals(opSetName)) {
                return i;
            }
        }

        // throws exception! Because the capacity is full.
        throw new IllegalArgumentException("Did not find the option name!");
    }

    public synchronized OptionSet.Option findOptionWithName(String opSetName,
            String optionName) {
        OptionSet optsetTemp = findOpSetWithName(opSetName);

        if (optsetTemp != null) {
            return optsetTemp.findOptionWithName(optionName);
        }

        return null;
    }

    // insert a new Option Set to the model
    /**
     * add a option with name and option value to a option set.
     * 
     * @param opSetname
     *            the name of the option set which user want to add
     * 
     * @param optionNumber
     *            how many options will the option set include.
     * 
     * @return void
     */
    public synchronized void setOptionSet(String opSetname, int optionNumber) {
        opset.add(new OptionSet(opSetname, optionNumber));
    }

    /**
     * add a option with name and option value to a option set.
     * 
     * @param opSetname
     *            the name of the optionset which user want to add option to.
     * @param optionName
     *            the name of the option
     * @param optionName
     *            the value of the option
     * @return void
     */
    public synchronized void setOption(String opSetname, String optionName,
            int optionValue) {
        if (opSetname == null || optionName == null) {
            throw new IllegalArgumentException("input null string!");
        }

        OptionSet optsetTemp = findOpSetWithName(opSetname);
        if (optsetTemp != null) {
            optsetTemp.setOption(optionName, optionValue);
        } else {
            throw new IllegalArgumentException("Did not find the optionset!");
        }
    }

    /**
     * delete a option from a option set.
     * 
     * @param opSetname
     *            the name of the optionset which user want to delete option.
     * @param optionName
     *            the name of the option
     * @return void
     */
    public synchronized void deleteOption(String opSetname, String optionName) {
        OptionSet optsetTemp = findOpSetWithName(opSetname);
        if (optsetTemp != null) {
            optsetTemp.deleteOption(optionName);
        }
    }

    /**
     * delete a option set
     * 
     * @param opSetname
     *            the name of the optionset which user want to delete
     * @return void
     */
    public synchronized void deleteOptionSet(String opSetname) {
        int optionSetIndex = 0;

        if (opset.size() == 0) {
            return;
        }

        optionSetIndex = findOpSetIndexWithName(opSetname);

        if (optionSetIndex >= opset.size()) {
            // throws exception! Because the index is full.
            throw new IllegalArgumentException("Did not find the option name!");
        }

        // remove the index.
        opset.remove(optionSetIndex);

    }

    /**
     * update a option set
     * 
     * @param opSetName
     *            the name of the optionset which user want to update
     * @param opSetNewName
     *            the new name of the optionset
     * @return void
     */
    public synchronized void updateOptionSet(String opSetName,
            String opSetNewName) {
        OptionSet optsetTemp = findOpSetWithName(opSetName);
        if (optsetTemp != null) {
            optsetTemp.setName(opSetNewName);
        } else {
            throw new IllegalArgumentException("Did not find the optionset!");
        }
    }

    /**
     * update a option with new name and new price
     * 
     * @param opSetname
     *            the name of the optionset which user want to update
     * @param optionName
     *            the name of the option
     * @param newOptionName
     *            the new name of the option
     * @param newOptionPrice
     *            the new price of the option.
     * @return void
     */
    public synchronized void updateOption(String opSetname, String optionName,
            String newOptionName, float newOptionPrice) {

        OptionSet optSetTmp = findOpSetWithName(opSetname);

        if (optSetTmp != null) {
            // when newOptionName is null, just skip setting new name.
            if (newOptionName != null) {
                optSetTmp.setOptionName(optionName, newOptionName);
            }
            optSetTmp.setOptionPrice(optionName, newOptionPrice);
        }
    }

    /*
     * methods of UNIT 2
     */
    public synchronized String getMake() {
        return make;
    }

    public synchronized void setMake(String set_make) {
        this.make = set_make;
    }

    public synchronized String getModel() {
        return model;
    }

    public synchronized void setModel(String newName) {
        model = newName;
    }

    public synchronized String getOptionChoice(String optionSetName) {
        OptionSet optsetTemp = findOpSetWithName(optionSetName);

        System.out.printf("Enter getOptionChoice\n");

        if (optsetTemp != null) {
            return optsetTemp.getOptionChoiceName();
        }

        return null;
    }

    public synchronized float getOptionChoicePrice(String optionSetName) {
        OptionSet optsetTemp = findOpSetWithName(optionSetName);

        if (optsetTemp != null) {
            return optsetTemp.getOptionChoicePrice();
        }

        throw new IllegalArgumentException("No option set!");
    }

    // set the choice of option in the optionSet
    public synchronized void setOptionChoice(String optionSetName,
            String optionName) {
        OptionSet optsetTemp = findOpSetWithName(optionSetName);

        System.out.printf("Enter setOptionChoice\n");

        if (optsetTemp != null) {
            optsetTemp.setOptionChoice(optionName);
        } else {
            throw new IllegalArgumentException("No option set!");
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.printf("Set operation sleep over(after 10s)!\n");
    }

    public synchronized float getTotalPrice() {
        float totalPrice = basePrise;
        for (int i = 0; i < opset.size(); i++) {
            totalPrice += opset.get(i).getOptionChoicePrice();
        }

        return totalPrice;
    }

    /*
     * methods of UNIT 2
     */

    public synchronized void print() {
        System.out.printf("===========================\n");
        System.out.printf("Print out the model:\n");
        System.out.printf("Model name:%s\n", name);
        System.out.printf("Make:%s\n", make);
        System.out.printf("Model:%s\n", model);
        System.out.printf("Base Price:%f\n", basePrise);
        System.out.printf("OptionSet number:%d\n", opset.size());
        // System.out.printf("Option capacity:%d\n\n", opsetCapcity);

        for (int i = 0; i < opset.size(); i++) {
            opset.get(i).print();
            System.out.printf("\n");
        }

    }
}
