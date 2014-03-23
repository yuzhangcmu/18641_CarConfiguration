package model;

import java.io.Serializable;
import java.util.ArrayList;

public class OptionSet implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8144521042536148215L;
    private String name;
    private ArrayList<Option> opt;

    private Option choice;

    // private int optionCapcity = 0;

    protected OptionSet(String name_input, int size) {
        opt = new ArrayList<Option>();
        name = name_input;
    }
    
    public int getOptionNumber() {
        return opt.size();
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setOption(String optionName, float value) {
        // just add new Option to the ArrayList.
        opt.add(new Option(optionName, value));
    }

    protected void deleteOption(String optionName) {
        if (opt.size() == 0) {
            throw new IllegalArgumentException("The optionset is empty!");
        }

        int index = findOptionIndexWithName(optionName);

        // remove the Option whose index is "index"
        opt.remove(index);
    }
    
    public Option getOptionWithIndex(int index) {
        return opt.get(index);
    }

    protected Option findOptionWithName(String name) {
        for (int i = 0; i < opt.size(); i++) {
            if (opt.get(i).getName().equals(name)) {
                return opt.get(i);
            }
        }

        return null;
    }

    private int findOptionIndexWithName(String name) {
        // find the OptionSet by name.
        for (int i = 0; i < opt.size(); i++) {
            // if (opt[i].getName().equals(name)){
            if (opt.get(i).getName().equals(name)) {
                return i;
            }
        }

        // throws exception! Because the capacity is full.
        throw new IllegalArgumentException("Did not find the option name!");
    }

    protected void print() {
        System.out.printf("OptionSet name: %s\n", name);
        System.out.printf("Option number: %d\n", opt.size());
        // System.out.printf("optionCapcity: %d\n", optionCapcity);
        System.out.printf("==============================\n");

        for (int i = 0; i < opt.size(); i++) {
            opt.get(i).print();
            // opt[i].print();
        }
    }
    
    protected void printChoice() {
        
        System.out.printf("OptionSet name: %s\n", name);
        
        if (choice == null) {
            System.out.printf("User choice: User didn't choose.\n");
        }else {
            System.out.printf("User choice: %s\n", choice.getName());
        }
        
        System.out.printf("==============================\n");
    }

    /*
     * new methods to get the option set.
     */
    protected Option getOptionChoice() {
        return choice;
    }

    protected String getOptionChoiceName() {
        if (choice != null) {
            return choice.getName();
        }

        return null;
    }

    // set the choice of the user.
    protected void setOptionChoice(String optionName) {
        choice = findOptionWithName(optionName);
    }

    protected void setOptionName(String optionName, String newName) {
        Option optTmp = findOptionWithName(optionName);
        if (optTmp != null) {
            optTmp.setName(newName);
            return;
        }

        throw new IllegalArgumentException("Did not find the option!");
    }

    protected void setOptionPrice(String optionName, float newPrice) {
        Option optTmp = findOptionWithName(optionName);
        if (optTmp != null) {
            optTmp.setValue(newPrice);
            return;
        }

        throw new IllegalArgumentException("Did not find the option!");
    }

    // set the user choice's price.
    protected void setOptionChoicePrice(float newPrice) {
        if (choice != null) {
            choice.setValue(newPrice);
        }

        throw new IllegalArgumentException("No choice set!");
    }

    protected float getOptionChoicePrice() {
        if (choice != null) {
            return choice.getValue();
        }

        throw new IllegalArgumentException("No choice set!");
    }

    public class Option implements Serializable {

        private static final long serialVersionUID = 5584942606633921380L;
        private String optName;
        private float value;

        protected Option(String name, float value) {
            optName = name;
            this.value = value;
        }

        public synchronized String getName() {
            return optName;
        }

        private float getValue() {
            return value;
        }

        private void setName(String name) {
            optName = name;
        }

        private void setValue(float newValue) {
            value = newValue;
        }

        private void print() {
            System.out.printf("%s:%f\n", optName, value);
        }
    }
}
