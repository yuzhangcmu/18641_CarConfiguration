package adapter;

import java.util.LinkedHashMap;

import model.Automobile;

public interface EditThread {
    public Automobile getAutomobileByName(String name);
    public LinkedHashMap<String, Automobile> getAutoHashMap(); 
    public String getAvailableAutos();
}
