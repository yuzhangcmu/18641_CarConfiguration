package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import model.Automobile;
import adapter.BuildAuto;

public class BuildCarModelOptions implements AutoServer, SocketClientConstants{
    
    @Override
    public void buildAutoFromPropertyFile(Object propertyObj) {
        AutoServer auto = new BuildAuto();
        auto.buildAutoFromPropertyFile(propertyObj);
    }
    
}
