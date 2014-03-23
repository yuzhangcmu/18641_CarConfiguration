package server;

import model.Automobile;

// this is implemented by BuildAuto.
public interface AutoServer {
    
    // build a Automible and added it to the LHM from the property file.
    public void buildAutoFromPropertyFile(Object propertyObj);

}
