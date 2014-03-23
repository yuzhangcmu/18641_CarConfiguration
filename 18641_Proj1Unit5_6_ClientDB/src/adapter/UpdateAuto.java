package adapter;

public interface UpdateAuto {
    public void updateOptionSetName(String Modelname, 
            String OptionSetname, String newName);

    public void updateOptionPrice(String Modelname, String optionSetName,
            String Option, float newprice);
}
